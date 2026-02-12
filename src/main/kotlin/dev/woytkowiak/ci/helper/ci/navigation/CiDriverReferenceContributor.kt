package dev.woytkowiak.ci.helper.ci.navigation

import com.intellij.openapi.fileEditor.FileDocumentManager
import com.intellij.openapi.util.TextRange
import com.intellij.openapi.vfs.LocalFileSystem
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.patterns.PlatformPatterns
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiManager
import com.intellij.psi.PsiReference
import com.intellij.psi.PsiReferenceBase
import com.intellij.psi.PsiReferenceContributor
import com.intellij.psi.PsiReferenceProvider
import com.intellij.psi.PsiReferenceRegistrar
import com.intellij.util.ProcessingContext
import dev.woytkowiak.ci.helper.ci.Ci3PluginState
import dev.woytkowiak.ci.helper.ci.guessProjectBaseDir

/**
 * "Go to Declaration" from load->driver('cache') to stub CI_Cache.php
 * (and other native drivers when stubs exist).
 */
class CiDriverReferenceContributor : PsiReferenceContributor() {

    /** Native driver name (first argument to load->driver) -> stub file name in .ci3-helper/stubs/ */
    private val nativeDriverStubs = mapOf(
        "cache" to "CI_Cache.php"
    )

    override fun registerReferenceProviders(registrar: PsiReferenceRegistrar) {
        registrar.registerReferenceProvider(
            PlatformPatterns.psiElement(PsiElement::class.java),
            DriverReferenceProvider()
        )
    }

    private inner class DriverReferenceProvider : PsiReferenceProvider() {
        override fun getReferencesByElement(
            element: PsiElement,
            context: ProcessingContext
        ): Array<PsiReference> {
            if (!Ci3PluginState.getInstance().isEnabled) return PsiReference.EMPTY_ARRAY
            val file = element.containingFile ?: return PsiReference.EMPTY_ARRAY
            if (!file.name.endsWith(".php")) return PsiReference.EMPTY_ARRAY

            val text = element.text?.trim('\'', '"')?.trim()?.lowercase() ?: return PsiReference.EMPTY_ARRAY
            if (text.isEmpty() || text.contains("/") || text.contains(" ")) return PsiReference.EMPTY_ARRAY

            val stubFileName = nativeDriverStubs[text] ?: return PsiReference.EMPTY_ARRAY
            if (!isInsideLoadDriver(element, text)) return PsiReference.EMPTY_ARRAY

            val project = element.project
            val baseDir = project.guessProjectBaseDir() ?: return PsiReference.EMPTY_ARRAY
            val stubPath = "${baseDir.path}/.ci3-helper/stubs/$stubFileName"
            val target = LocalFileSystem.getInstance().refreshAndFindFileByPath(stubPath)
                ?: return PsiReference.EMPTY_ARRAY

            val start = if (element.text.startsWith("'") || element.text.startsWith("\"")) 1 else 0
            val end = element.textLength - if (element.text.endsWith("'") || element.text.endsWith("\"")) 1 else 0
            val range = TextRange(start, end.coerceAtLeast(start))

            return arrayOf(DriverReference(element, target, range))
        }

        private fun isInsideLoadDriver(element: PsiElement, driverName: String): Boolean {
            val doc = FileDocumentManager.getInstance()
                .getDocument(element.containingFile?.virtualFile ?: return false) ?: return false
            val offset = element.textOffset
            val lineNum = doc.getLineNumber(offset)
            val lineStart = doc.getLineStartOffset(lineNum)
            val lineEnd = doc.getLineEndOffset(lineNum)
            val line = doc.getText(TextRange(lineStart, lineEnd))
            if (!line.contains("load->driver(")) return false
            return line.contains("'$driverName'") || line.contains("\"$driverName\"")
        }
    }

    private class DriverReference(
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
