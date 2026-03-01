package dev.woytkowiak.ci.helper.ci.navigation

import com.intellij.codeInsight.daemon.LineMarkerInfo
import com.intellij.codeInsight.daemon.LineMarkerProvider
import com.intellij.codeInsight.navigation.NavigationGutterIconBuilder
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.jetbrains.php.lang.PhpLanguage
import com.jetbrains.php.lang.psi.elements.MethodReference
import com.jetbrains.php.lang.psi.elements.StringLiteralExpression
import dev.woytkowiak.ci.helper.ci.Ci3Icons
import dev.woytkowiak.ci.helper.ci.Ci3PluginState
import dev.woytkowiak.ci.helper.ci.CiViewUtils

/**
 * Gutter markers for CI3 views:
 * - From `$this->load->view('path')` to the target view file.
 * - From a view file (application/views/...) back to controllers that load it.
 */
class CiViewLineMarkerProvider : LineMarkerProvider {

    override fun getLineMarkerInfo(element: PsiElement): LineMarkerInfo<*>? = null

    override fun collectSlowLineMarkers(
        elements: MutableList<out PsiElement>,
        result: MutableCollection<in LineMarkerInfo<*>>
    ) {
        if (!Ci3PluginState.getInstance().isEnabled) return
        if (elements.isEmpty()) return

        val firstFile = elements.first().containingFile ?: return
        if (firstFile.language != PhpLanguage.INSTANCE) return

        for (element in elements) {
            if (element is StringLiteralExpression) {
                addControllerToViewMarker(element)?.let { result.add(it) }
            }
        }

        val file = firstFile
        if (file is PsiFile) {
            addViewToControllersMarkers(file, result)
        }
    }

    private fun addControllerToViewMarker(literal: StringLiteralExpression): LineMarkerInfo<*>? {
        val methodRef = literal.parent as? com.jetbrains.php.lang.psi.elements.ParameterList
            ?: return null
        val call = methodRef.parent as? MethodReference ?: return null
        val methodName = call.name ?: return null
        if (methodName != "view") return null

        val classRef = call.classReference as? MethodReference ?: return null
        if (classRef.name != "load") return null
        val root = classRef.classReference as? com.jetbrains.php.lang.psi.elements.Variable ?: return null
        if (root.name != "this") return null

        val viewName = literal.contents.trim()
        if (viewName.isEmpty()) return null

        val project = literal.project
        val target = CiViewUtils.resolveViewFile(project, viewName) ?: return null
        val psiTarget = com.intellij.psi.PsiManager.getInstance(project).findFile(target) ?: return null

        val builder = NavigationGutterIconBuilder.create(Ci3Icons.Ci3)
            .setTooltipText("Go to CI3 view '$viewName'")
            .setTargets(psiTarget)
        return builder.createLineMarkerInfo(literal)
    }

    private fun addViewToControllersMarkers(
        file: PsiFile,
        result: MutableCollection<in LineMarkerInfo<*>>
    ) {
        val project = file.project
        val viewsDir = CiViewUtils.getViewsDir(project) ?: return
        val vFile = file.virtualFile ?: return
        if (!vFile.path.startsWith(viewsDir.path)) return

        val viewName = CiViewUtils.getViewNameForFile(project, vFile) ?: return

        val usages = CiViewUtils.findViewUsages(project, viewName)
        if (usages.isEmpty()) return

        val anchor = file.firstChild ?: return

        val builder = NavigationGutterIconBuilder.create(Ci3Icons.Ci3)
            .setTooltipText("Loaded as CI3 view '$viewName'")
            .setTargets(usages)

        result.add(builder.createLineMarkerInfo(anchor))
    }
}

