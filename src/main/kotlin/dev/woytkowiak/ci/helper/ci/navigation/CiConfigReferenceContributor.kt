package dev.woytkowiak.ci.helper.ci.navigation

import com.intellij.openapi.util.TextRange
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.patterns.PlatformPatterns
import com.intellij.psi.*
import com.intellij.util.ProcessingContext
import dev.woytkowiak.ci.helper.ci.Ci3PluginState
import dev.woytkowiak.ci.helper.ci.completion.resolveConfigFile

/**
 * "Go to Declaration" from load->config('keycloak') to file application/config/keycloak.php.
 */
class CiConfigReferenceContributor : PsiReferenceContributor() {

    override fun registerReferenceProviders(registrar: PsiReferenceRegistrar) {
        registrar.registerReferenceProvider(
            PlatformPatterns.psiElement(PsiElement::class.java),
            ConfigReferenceProvider()
        )
    }

    private class ConfigReferenceProvider : PsiReferenceProvider() {

        override fun getReferencesByElement(
            element: PsiElement,
            context: ProcessingContext
        ): Array<PsiReference> {
            if (!Ci3PluginState.getInstance().isEnabled) return PsiReference.EMPTY_ARRAY
            val file = element.containingFile ?: return PsiReference.EMPTY_ARRAY
            if (!file.name.endsWith(".php")) return PsiReference.EMPTY_ARRAY

            val text = element.text?.trim()?.trim('\'', '"') ?: return PsiReference.EMPTY_ARRAY
            if (text.isEmpty() || text.contains("/") || text.contains(" ")) return PsiReference.EMPTY_ARRAY

            if (!isInsideLoadConfig(element, text)) return PsiReference.EMPTY_ARRAY

            val project = element.project
            val target = resolveConfigFile(project, text) ?: return PsiReference.EMPTY_ARRAY

            val start = if (element.text.startsWith("'") || element.text.startsWith("\"")) 1 else 0
            val end = element.textLength - if (element.text.endsWith("'") || element.text.endsWith("\"")) 1 else 0
            val range = TextRange(start, end.coerceAtLeast(start))

            return arrayOf(ConfigReference(element, target, range))
        }

        private fun isInsideLoadConfig(element: PsiElement, configName: String): Boolean {
            val doc = com.intellij.openapi.fileEditor.FileDocumentManager.getInstance()
                .getDocument(element.containingFile?.virtualFile ?: return false) ?: return false
            val offset = element.textOffset
            val lineNum = doc.getLineNumber(offset)
            val lineStart = doc.getLineStartOffset(lineNum)
            val lineEnd = doc.getLineEndOffset(lineNum)
            val line = doc.getText(com.intellij.openapi.util.TextRange(lineStart, lineEnd))
            if (!line.contains("load->config(")) return false
            return line.contains("'$configName'") || line.contains("\"$configName\"")
        }
    }

    private class ConfigReference(
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
