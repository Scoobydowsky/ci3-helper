package dev.woytkowiak.ci.helper.ci.navigation

import com.intellij.codeInsight.daemon.LineMarkerInfo
import com.intellij.codeInsight.daemon.LineMarkerProvider
import com.intellij.codeInsight.navigation.NavigationGutterIconBuilder
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.jetbrains.php.lang.PhpLanguage
import dev.woytkowiak.ci.helper.ci.Ci3Icons
import dev.woytkowiak.ci.helper.ci.Ci3PluginState
import dev.woytkowiak.ci.helper.ci.CiHookUtils

/**
 * Line marker on CI3 hook files under application/hooks: shows icon and navigates to hooks.php usages.
 */
class CiHookLineMarkerProvider : LineMarkerProvider {

    override fun getLineMarkerInfo(element: PsiElement): LineMarkerInfo<*>? = null

    override fun collectSlowLineMarkers(
        elements: MutableList<out PsiElement>,
        result: MutableCollection<in LineMarkerInfo<*>>
    ) {
        if (!Ci3PluginState.getInstance().isEnabled) return
        if (elements.isEmpty()) return

        val firstFile = elements.first().containingFile ?: return
        if (firstFile.language != PhpLanguage.INSTANCE) return

        val file = firstFile as? PsiFile ?: return
        val vFile = file.virtualFile ?: return
        val project = file.project
        if (!CiHookUtils.isHookFile(project, vFile)) return

        val (filepath, filename) = CiHookUtils.getHookFilepathAndFilename(project, vFile) ?: return
        val usages = CiHookUtils.findHookUsages(project, filepath, filename)
        if (usages.isEmpty()) return

        val anchor = file.firstChild ?: return
        val builder = NavigationGutterIconBuilder.create(Ci3Icons.Ci3)
            .setTooltipText("Registered in config/hooks.php")
            .setTargets(usages)
        result.add(builder.createLineMarkerInfo(anchor))
    }
}
