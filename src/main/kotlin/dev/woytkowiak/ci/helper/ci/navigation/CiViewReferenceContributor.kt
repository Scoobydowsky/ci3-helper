package dev.woytkowiak.ci.helper.ci.navigation

import com.intellij.openapi.util.TextRange
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.patterns.PlatformPatterns
import com.intellij.psi.*
import com.intellij.util.ProcessingContext
import dev.woytkowiak.ci.helper.ci.CiViewUtils

/**
 * "Go to Declaration" z load->view('ścieżka') na plik application/views/ścieżka.php.
 * Obsługuje ścieżki z "/" (users/list) i pojedyncze nazwy (header).
 */
class CiViewReferenceContributor : PsiReferenceContributor() {

    override fun registerReferenceProviders(registrar: PsiReferenceRegistrar) {
        registrar.registerReferenceProvider(
            PlatformPatterns.psiElement(PsiElement::class.java),
            ViewReferenceProvider()
        )
    }

    private class ViewReferenceProvider : PsiReferenceProvider() {

        override fun getReferencesByElement(
            element: PsiElement,
            context: ProcessingContext
        ): Array<PsiReference> {
            val file = element.containingFile ?: return PsiReference.EMPTY_ARRAY
            if (!file.name.endsWith(".php")) return PsiReference.EMPTY_ARRAY
            if (file.name == "routes.php") return PsiReference.EMPTY_ARRAY

            val text = element.text ?: return PsiReference.EMPTY_ARRAY
            val cleanText = text.trim().trim('\'', '"')
            if (cleanText.isEmpty()) return PsiReference.EMPTY_ARRAY

            if (!isInsideLoadView(element, cleanText)) return PsiReference.EMPTY_ARRAY

            val project = element.project
            val target = CiViewUtils.resolveViewFile(project, cleanText)
                ?: return PsiReference.EMPTY_ARRAY

            val start = if (text.startsWith("'") || text.startsWith("\"")) 1 else 0
            val end = element.textLength - if (text.endsWith("'") || text.endsWith("\"")) 1 else 0
            val range = TextRange(start, end.coerceAtLeast(start))

            return arrayOf(ViewReference(element, target, range))
        }

        private fun isInsideLoadView(element: PsiElement, viewPath: String): Boolean {
            val doc = com.intellij.openapi.fileEditor.FileDocumentManager.getInstance()
                .getDocument(element.containingFile?.virtualFile ?: return false) ?: return false
            val offset = element.textOffset
            val lineNum = doc.getLineNumber(offset)
            val lineStart = doc.getLineStartOffset(lineNum)
            val lineEnd = doc.getLineEndOffset(lineNum)
            val line = doc.getText(com.intellij.openapi.util.TextRange(lineStart, lineEnd))
            if (!line.contains("load->view(")) return false
            return line.contains("'$viewPath'") || line.contains("\"$viewPath\"")
        }
    }

    private class ViewReference(
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
