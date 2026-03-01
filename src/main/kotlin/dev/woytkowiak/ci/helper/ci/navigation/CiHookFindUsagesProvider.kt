package dev.woytkowiak.ci.helper.ci.navigation

import com.intellij.lang.findUsages.FindUsagesProvider
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import dev.woytkowiak.ci.helper.ci.Ci3PluginState
import dev.woytkowiak.ci.helper.ci.CiHookUtils

/**
 * Find Usages for CI3 hook files under application/hooks.
 * Finds all entries in config/hooks.php that reference this hook file.
 */
class CiHookFindUsagesProvider : FindUsagesProvider {

    override fun canFindUsagesFor(element: PsiElement): Boolean {
        if (!Ci3PluginState.getInstance().isEnabled) return false
        if (element !is PsiFile) return false
        val vFile = element.virtualFile ?: return false
        return CiHookUtils.isHookFile(element.project, vFile)
    }

    override fun getWordsScanner() = null

    override fun getHelpId(psiElement: PsiElement) = null

    override fun getType(element: PsiElement): String =
        if (canFindUsagesFor(element)) "CI3 Hook" else ""

    override fun getDescriptiveName(element: PsiElement): String {
        if (element !is PsiFile) return ""
        val vFile = element.virtualFile ?: return element.name
        val project = element.project
        val (filepath, filename) = CiHookUtils.getHookFilepathAndFilename(project, vFile) ?: return element.name
        return "$filepath/$filename"
    }

    override fun getNodeText(element: PsiElement, useFullName: Boolean): String =
        getDescriptiveName(element)
}
