package dev.woytkowiak.ci.helper.ci.navigation

import dev.woytkowiak.ci.helper.ci.guessProjectBaseDir
import com.intellij.openapi.util.TextRange
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.patterns.PlatformPatterns
import com.intellij.psi.*
import com.intellij.util.ProcessingContext
import dev.woytkowiak.ci.helper.ci.Ci3PluginState

/**
 * In routes.php enables "Go to Declaration" from route value (controller/method)
 * to the controller file in application/controllers/.
 */
class CiRouteReferenceContributor : PsiReferenceContributor() {

    override fun registerReferenceProviders(registrar: PsiReferenceRegistrar) {
        registrar.registerReferenceProvider(
            PlatformPatterns.psiElement(PsiElement::class.java),
            RouteReferenceProvider()
        )
    }

    private class RouteReferenceProvider : PsiReferenceProvider() {

        override fun getReferencesByElement(
            element: PsiElement,
            context: ProcessingContext
        ): Array<PsiReference> {
            if (!Ci3PluginState.getInstance().isEnabled) return PsiReference.EMPTY_ARRAY
            val file = element.containingFile ?: return PsiReference.EMPTY_ARRAY
            if (file.name != "routes.php") return PsiReference.EMPTY_ARRAY

            val text = element.text ?: return PsiReference.EMPTY_ARRAY
            val cleanText = text.trim('\'', '"').trim()
            if (cleanText.isEmpty()) return PsiReference.EMPTY_ARRAY

            val controllerPart = cleanText.substringBefore("/").substringBefore("$").trim()
            if (controllerPart.isEmpty()) return PsiReference.EMPTY_ARRAY

            val project = element.project
            val baseDir = project.guessProjectBaseDir() ?: return PsiReference.EMPTY_ARRAY
            val controllersDir = baseDir.findChild("application")
                ?.findChild("controllers")
                ?: return PsiReference.EMPTY_ARRAY

            val controllerFile = controllersDir.findChild("$controllerPart.php")
                ?: return PsiReference.EMPTY_ARRAY

            val start = if (text.startsWith("'") || text.startsWith("\"")) 1 else 0
            val end = text.length - if (text.endsWith("'") || text.endsWith("\"")) 1 else 0
            val range = TextRange(start, end.coerceAtLeast(start))

            return arrayOf(RouteReference(element, controllerFile, range))
        }
    }

    private class RouteReference(
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
