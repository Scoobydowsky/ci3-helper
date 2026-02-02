package dev.woytkowiak.ci.helper.ci.navigation

import com.intellij.openapi.util.TextRange
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.patterns.PlatformPatterns
import com.intellij.psi.*
import com.intellij.util.ProcessingContext

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

            if (element.containingFile?.name == "routes.php") return PsiReference.EMPTY_ARRAY
            val text = element.text ?: return PsiReference.EMPTY_ARRAY
            if (!text.contains("/")) return PsiReference.EMPTY_ARRAY

            val project = element.project
            val baseDir = project.baseDir ?: return PsiReference.EMPTY_ARRAY

            val viewsDir = baseDir
                .findChild("application")
                ?.findChild("views")
                ?: return PsiReference.EMPTY_ARRAY

            val cleanText = text.trim('\'', '"')
            val target = viewsDir.findFileByRelativePath("$cleanText.php")
                ?: return PsiReference.EMPTY_ARRAY

            return arrayOf(ViewReference(element, target))
        }
    }

    private class ViewReference(
        element: PsiElement,
        private val target: VirtualFile
    ) : PsiReferenceBase<PsiElement>(element, TextRange(1, element.textLength - 1)) {

        override fun resolve(): PsiElement? {
            return PsiManager.getInstance(myElement.project)
                .findFile(target)
        }

        override fun getVariants(): Array<Any> = emptyArray()
    }
}
