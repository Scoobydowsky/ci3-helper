package dev.woytkowiak.ci.helper.ci.navigation

import com.intellij.openapi.util.TextRange
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.patterns.PlatformPatterns
import com.intellij.psi.*
import com.intellij.util.ProcessingContext
import dev.woytkowiak.ci.helper.ci.Ci3PluginState
import dev.woytkowiak.ci.helper.ci.completion.resolveHelperFile

/**
 * "Go to Declaration" from load->helper('form') to file application/helpers/form_helper.php.
 */
class CiHelperReferenceContributor : PsiReferenceContributor() {

    override fun registerReferenceProviders(registrar: PsiReferenceRegistrar) {
        registrar.registerReferenceProvider(
            PlatformPatterns.psiElement(PsiElement::class.java),
            HelperReferenceProvider()
        )
    }

    private class HelperReferenceProvider : PsiReferenceProvider() {

        override fun getReferencesByElement(
            element: PsiElement,
            context: ProcessingContext
        ): Array<PsiReference> {
            if (!Ci3PluginState.getInstance().isEnabled) return PsiReference.EMPTY_ARRAY
            val file = element.containingFile ?: return PsiReference.EMPTY_ARRAY
            if (!file.name.endsWith(".php")) return PsiReference.EMPTY_ARRAY

            val text = element.text?.trim()?.trim('\'', '"') ?: return PsiReference.EMPTY_ARRAY
            if (text.isEmpty() || text.contains("/") || text.contains(" ")) return PsiReference.EMPTY_ARRAY

            if (!isInsideLoadHelper(element, text)) return PsiReference.EMPTY_ARRAY

            val project = element.project
            val target = resolveHelperFile(project, text) ?: return PsiReference.EMPTY_ARRAY

            val start = if (element.text.startsWith("'") || element.text.startsWith("\"")) 1 else 0
            val end = element.textLength - if (element.text.endsWith("'") || element.text.endsWith("\"")) 1 else 0
            val range = TextRange(start, end.coerceAtLeast(start))

            return arrayOf(HelperReference(element, target, range))
        }

        private fun isInsideLoadHelper(element: PsiElement, helperName: String): Boolean {
            val doc = com.intellij.openapi.fileEditor.FileDocumentManager.getInstance()
                .getDocument(element.containingFile?.virtualFile ?: return false) ?: return false
            val offset = element.textOffset
            val lineNum = doc.getLineNumber(offset)
            val lineStart = doc.getLineStartOffset(lineNum)
            val lineEnd = doc.getLineEndOffset(lineNum)
            val line = doc.getText(com.intellij.openapi.util.TextRange(lineStart, lineEnd))
            if (!line.contains("load->helper(")) return false
            return line.contains("'$helperName'") || line.contains("\"$helperName\"")
        }
    }

    private class HelperReference(
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
