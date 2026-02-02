package dev.woytkowiak.ci.helper.ci.navigation

import com.intellij.openapi.util.TextRange
import com.intellij.openapi.vfs.LocalFileSystem
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.patterns.PlatformPatterns
import com.intellij.psi.*
import com.intellij.util.ProcessingContext

/**
 * "Go to Declaration" z load->library('name') na plik application/libraries/Name.php.
 */
class CiLibraryReferenceContributor : PsiReferenceContributor() {

    override fun registerReferenceProviders(registrar: PsiReferenceRegistrar) {
        registrar.registerReferenceProvider(
            PlatformPatterns.psiElement(PsiElement::class.java),
            LibraryReferenceProvider()
        )
    }

    private class LibraryReferenceProvider : PsiReferenceProvider() {

        override fun getReferencesByElement(
            element: PsiElement,
            context: ProcessingContext
        ): Array<PsiReference> {
            val file = element.containingFile ?: return PsiReference.EMPTY_ARRAY
            if (!file.name.endsWith(".php")) return PsiReference.EMPTY_ARRAY

            val text = element.text?.trim('\'', '"')?.trim() ?: return PsiReference.EMPTY_ARRAY
            if (text.isEmpty() || text.contains("/") || text.contains(" ")) return PsiReference.EMPTY_ARRAY

            if (!isInsideLoadLibrary(element, text)) return PsiReference.EMPTY_ARRAY

            val project = element.project
            val basePath = project.basePath ?: return PsiReference.EMPTY_ARRAY
            val libsDir = LocalFileSystem.getInstance().findFileByPath("$basePath/application/libraries")
                ?: return PsiReference.EMPTY_ARRAY

            val target = findLibraryFile(libsDir, text) ?: return PsiReference.EMPTY_ARRAY

            val start = if (element.text.startsWith("'") || element.text.startsWith("\"")) 1 else 0
            val end = element.textLength - if (element.text.endsWith("'") || element.text.endsWith("\"")) 1 else 0
            val range = TextRange(start, end.coerceAtLeast(start))

            return arrayOf(LibraryReference(element, target, range))
        }

        private fun isInsideLoadLibrary(element: PsiElement, libraryName: String): Boolean {
            val doc = com.intellij.openapi.fileEditor.FileDocumentManager.getInstance()
                .getDocument(element.containingFile?.virtualFile ?: return false) ?: return false
            val offset = element.textOffset
            val lineNum = doc.getLineNumber(offset)
            val lineStart = doc.getLineStartOffset(lineNum)
            val lineEnd = doc.getLineEndOffset(lineNum)
            val line = doc.getText(com.intellij.openapi.util.TextRange(lineStart, lineEnd))
            if (!line.contains("load->library(")) return false
            return line.contains("'$libraryName'") || line.contains("\"$libraryName\"")
        }

        private fun findLibraryFile(libsDir: VirtualFile, name: String): VirtualFile? {
            val withUcfirst = name.split("_").joinToString("_") { it.replaceFirstChar { c -> c.uppercaseChar() } }
            fun findRecursive(dir: VirtualFile, fileName: String): VirtualFile? {
                val direct = dir.findChild(fileName)
                if (direct != null) return direct
                for (child in dir.children) {
                    if (child.isDirectory) {
                        findRecursive(child, fileName)?.let { return it }
                    }
                }
                return null
            }
            val fileName = "$withUcfirst.php"
            return libsDir.findFileByRelativePath("$name.php")
                ?: libsDir.findFileByRelativePath(fileName)
                ?: findRecursive(libsDir, fileName)
                ?: findRecursive(libsDir, "${name.lowercase()}.php")
        }
    }

    private class LibraryReference(
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
