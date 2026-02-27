package dev.woytkowiak.ci.helper.ci.navigation

import com.intellij.openapi.fileEditor.FileDocumentManager
import com.intellij.openapi.util.TextRange
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.patterns.PlatformPatterns
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiManager
import com.intellij.psi.PsiReference
import com.intellij.psi.PsiReferenceBase
import com.intellij.psi.PsiReferenceProvider
import com.intellij.util.ProcessingContext
import dev.woytkowiak.ci.helper.ci.Ci3PluginState
import dev.woytkowiak.ci.helper.ci.CiHookUtils
import dev.woytkowiak.ci.helper.ci.guessProjectBaseDir

/**
 * "Go to Declaration" in application/config/hooks.php from 'filename' / 'filepath' values
 * to the hook file (application/{filepath}/{filename}) or directory.
 */
class CiHookReferenceContributor : com.intellij.psi.PsiReferenceContributor() {

    override fun registerReferenceProviders(registrar: com.intellij.psi.PsiReferenceRegistrar) {
        registrar.registerReferenceProvider(
            PlatformPatterns.psiElement(PsiElement::class.java),
            HookReferenceProvider()
        )
    }

    private class HookReferenceProvider : PsiReferenceProvider() {

        override fun getReferencesByElement(
            element: PsiElement,
            context: ProcessingContext
        ): Array<PsiReference> {
            if (!Ci3PluginState.getInstance().isEnabled) return PsiReference.EMPTY_ARRAY
            val file = element.containingFile ?: return PsiReference.EMPTY_ARRAY
            if (file.name != "hooks.php") return PsiReference.EMPTY_ARRAY

            val doc = FileDocumentManager.getInstance().getDocument(file.virtualFile ?: return PsiReference.EMPTY_ARRAY)
                ?: return PsiReference.EMPTY_ARRAY
            val offset = element.textOffset
            val lineNum = doc.getLineNumber(offset)
            val lineStart = doc.getLineStartOffset(lineNum)
            val lineEnd = doc.getLineEndOffset(lineNum)
            val line = doc.getText(TextRange(lineStart, lineEnd))

            val valueText = element.text?.trim()?.trim('\'', '"') ?: return PsiReference.EMPTY_ARRAY
            val key = CiHookUtils.getHookKeyFromLine(line, valueText) ?: return PsiReference.EMPTY_ARRAY
            if (key != "filename" && key != "filepath") return PsiReference.EMPTY_ARRAY

            val hookDef = CiHookUtils.getContainingHookDefinition(doc, offset) ?: return PsiReference.EMPTY_ARRAY
            val filepath = (hookDef["filepath"] ?: "").trim().trimEnd('/').ifEmpty { "hooks" }
            val filename = (hookDef["filename"] ?: "").trim()

            val project = element.project

            if (key == "filename" && filename.isNotEmpty()) {
                val target = CiHookUtils.resolveHookFile(project, filepath, filename) ?: return PsiReference.EMPTY_ARRAY
                val start = if (element.text.startsWith("'") || element.text.startsWith("\"")) 1 else 0
                val end = element.textLength - if (element.text.endsWith("'") || element.text.endsWith("\"")) 1 else 0
                val range = TextRange(start, end.coerceAtLeast(start))
                return arrayOf(HookFileReference(element, target, range))
            }
            if (key == "filepath" && filepath.isNotEmpty()) {
                val baseDir = project.guessProjectBaseDir() ?: return PsiReference.EMPTY_ARRAY
                val appDir = baseDir.findChild("application") ?: return PsiReference.EMPTY_ARRAY
                val dir = appDir.findFileByRelativePath(filepath) ?: return PsiReference.EMPTY_ARRAY
                val start = if (element.text.startsWith("'") || element.text.startsWith("\"")) 1 else 0
                val end = element.textLength - if (element.text.endsWith("'") || element.text.endsWith("\"")) 1 else 0
                val range = TextRange(start, end.coerceAtLeast(start))
                return arrayOf(HookDirReference(element, dir, range))
            }
            return PsiReference.EMPTY_ARRAY
        }
    }

    private class HookFileReference(
        element: PsiElement,
        private val target: VirtualFile,
        range: TextRange
    ) : PsiReferenceBase<PsiElement>(element, range) {
        override fun resolve(): PsiElement? = PsiManager.getInstance(element.project).findFile(target)
        override fun getVariants(): Array<Any> = emptyArray()
    }

    private class HookDirReference(
        element: PsiElement,
        private val target: VirtualFile,
        range: TextRange
    ) : PsiReferenceBase<PsiElement>(element, range) {
        override fun resolve(): PsiElement? = PsiManager.getInstance(element.project).findDirectory(target)
        override fun getVariants(): Array<Any> = emptyArray()
    }
}
