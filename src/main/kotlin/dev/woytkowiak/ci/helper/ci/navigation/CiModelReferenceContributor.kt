package dev.woytkowiak.ci.helper.ci.navigation

import com.intellij.openapi.util.TextRange
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.patterns.PlatformPatterns
import com.intellij.psi.*
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.util.ProcessingContext
import com.jetbrains.php.lang.psi.elements.FieldReference
import com.jetbrains.php.lang.psi.elements.Variable
import dev.woytkowiak.ci.helper.ci.completion.findLoadedModelClasses
import dev.woytkowiak.ci.helper.ci.completion.resolveModelFile

/**
 * "Go to Declaration":
 * – from $this->Order_model to file application/models/Order_model.php,
 * – from literal in load->model('Order_model') to the same file.
 */
class CiModelReferenceContributor : PsiReferenceContributor() {

    override fun registerReferenceProviders(registrar: PsiReferenceRegistrar) {
        registrar.registerReferenceProvider(
            PlatformPatterns.psiElement(PsiElement::class.java),
            ModelReferenceProvider()
        )
    }

    private class ModelReferenceProvider : PsiReferenceProvider() {

        override fun getReferencesByElement(
            element: PsiElement,
            context: ProcessingContext
        ): Array<PsiReference> {
            val file = element.containingFile ?: return PsiReference.EMPTY_ARRAY
            if (!file.name.endsWith(".php")) return PsiReference.EMPTY_ARRAY

            val project = element.project

            // 1) $this->Order_model → model file
            val fieldRef = PsiTreeUtil.getParentOfType(element, FieldReference::class.java)
            if (fieldRef != null && fieldRef.classReference is Variable && (fieldRef.classReference as Variable).name == "this") {
                val propertyName = fieldRef.name ?: element.text ?: return PsiReference.EMPTY_ARRAY
                val loadedModels = findLoadedModelClasses(file.text)
                val modelClassName = loadedModels[propertyName] ?: return PsiReference.EMPTY_ARRAY
                val target = resolveModelFile(project, modelClassName) ?: return PsiReference.EMPTY_ARRAY
                return arrayOf(ModelReference(element, target, TextRange(0, element.textLength)))
            }

            // 2) literal in load->model('Order_model') → model file
            val text = element.text?.trim()?.trim('\'', '"') ?: return PsiReference.EMPTY_ARRAY
            if (text.isNotEmpty() && !text.contains("/") && !text.contains(" ") && isInsideLoadModel(element, text)) {
                val target = resolveModelFile(project, text) ?: return PsiReference.EMPTY_ARRAY
                val start = if (element.text.startsWith("'") || element.text.startsWith("\"")) 1 else 0
                val end = element.textLength - if (element.text.endsWith("'") || element.text.endsWith("\"")) 1 else 0
                val range = TextRange(start, end.coerceAtLeast(start))
                return arrayOf(ModelReference(element, target, range))
            }

            return PsiReference.EMPTY_ARRAY
        }

        private fun isInsideLoadModel(element: PsiElement, modelName: String): Boolean {
            val doc = com.intellij.openapi.fileEditor.FileDocumentManager.getInstance()
                .getDocument(element.containingFile?.virtualFile ?: return false) ?: return false
            val offset = element.textOffset
            val lineNum = doc.getLineNumber(offset)
            val lineStart = doc.getLineStartOffset(lineNum)
            val lineEnd = doc.getLineEndOffset(lineNum)
            val line = doc.getText(com.intellij.openapi.util.TextRange(lineStart, lineEnd))
            if (!line.contains("load->model(")) return false
            return line.contains("'$modelName'") || line.contains("\"$modelName\"")
        }
    }

    private class ModelReference(
        element: PsiElement,
        private val target: VirtualFile,
        range: TextRange
    ) : PsiReferenceBase<PsiElement>(element, range) {

        override fun resolve(): PsiElement? {
            return PsiManager.getInstance(myElement.project).findFile(target)
        }

        override fun getVariants(): Array<Any> = emptyArray()
    }
}
