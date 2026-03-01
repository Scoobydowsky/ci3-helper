package dev.woytkowiak.ci.helper.ci.navigation

import com.intellij.lang.findUsages.FindUsagesProvider
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import dev.woytkowiak.ci.helper.ci.Ci3PluginState
import dev.woytkowiak.ci.helper.ci.completion.getModelNameForFile
import dev.woytkowiak.ci.helper.ci.completion.getModelsDir

/**
 * Find Usages for CI3 model files (application/models/...).
 * Finds all places where the model is loaded (load->model('...')).
 */
class CiModelFindUsagesProvider : FindUsagesProvider {

    override fun canFindUsagesFor(element: PsiElement): Boolean {
        if (!Ci3PluginState.getInstance().isEnabled) return false
        if (element !is PsiFile) return false
        val vFile = element.virtualFile ?: return false
        val modelsDir = getModelsDir(element.project) ?: return false
        return vFile.path.startsWith(modelsDir.path)
    }

    override fun getWordsScanner() = null

    override fun getHelpId(psiElement: PsiElement) = null

    override fun getType(element: PsiElement): String =
        if (canFindUsagesFor(element)) "CI3 Model" else ""

    override fun getDescriptiveName(element: PsiElement): String {
        if (element !is PsiFile) return ""
        val project = element.project
        val vFile = element.virtualFile ?: return element.name
        return getModelNameForFile(project, vFile) ?: element.name
    }

    override fun getNodeText(element: PsiElement, useFullName: Boolean): String =
        getDescriptiveName(element)
}
