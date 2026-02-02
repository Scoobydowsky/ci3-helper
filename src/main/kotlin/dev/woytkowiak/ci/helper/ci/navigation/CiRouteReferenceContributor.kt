package dev.woytkowiak.ci.helper.ci.navigation

import com.intellij.openapi.util.TextRange
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.patterns.PlatformPatterns
import com.intellij.psi.*
import com.intellij.util.ProcessingContext

/**
 * W pliku routes.php umożliwia "Go to Declaration" z wartości route (controller/method)
 * na plik kontrolera w application/controllers/.
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
            val file = element.containingFile ?: return PsiReference.EMPTY_ARRAY
            if (file.name != "routes.php") return PsiReference.EMPTY_ARRAY

            val text = element.text ?: return PsiReference.EMPTY_ARRAY
            val cleanText = text.trim('\'', '"').trim()
            if (cleanText.isEmpty()) return PsiReference.EMPTY_ARRAY

            val controllerPart = cleanText.substringBefore("/").substringBefore("$").trim()
            if (controllerPart.isEmpty()) return PsiReference.EMPTY_ARRAY

            val project = element.project
            val baseDir = project.baseDir ?: return PsiReference.EMPTY_ARRAY
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
