package dev.woytkowiak.ci.helper.ci.navigation

import com.intellij.lang.findUsages.FindUsagesProvider
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import dev.woytkowiak.ci.helper.ci.CiViewUtils

/**
 * Find Usages for CI3 view files (application/views/...).
 * Finds all places where the view is loaded (load->view('...')).
 */
class CiViewFindUsagesProvider : FindUsagesProvider {

    override fun canFindUsagesFor(element: PsiElement): Boolean {
        if (element !is PsiFile) return false
        val vFile = element.virtualFile ?: return false
        val viewsDir = CiViewUtils.getViewsDir(element.project) ?: return false
        return vFile.path.startsWith(viewsDir.path)
    }

    override fun getWordsScanner() = null

    override fun getHelpId(psiElement: PsiElement) = null

    override fun getType(element: PsiElement): String =
        if (canFindUsagesFor(element)) "CI3 View" else ""

    override fun getDescriptiveName(element: PsiElement): String {
        if (element !is PsiFile) return ""
        val viewsDir = CiViewUtils.getViewsDir(element.project) ?: return element.name
        val vFile = element.virtualFile ?: return element.name
        val basePath = viewsDir.path ?: return element.name
        if (!vFile.path.startsWith(basePath)) return element.name
        val relative = vFile.path.removePrefix(basePath).trimStart('/').removeSuffix(".php")
        return relative.ifEmpty { element.name }
    }

    override fun getNodeText(element: PsiElement, useFullName: Boolean): String =
        getDescriptiveName(element)
}
