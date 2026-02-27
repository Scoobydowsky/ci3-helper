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
import dev.woytkowiak.ci.helper.ci.completion.getModelNameForFile
import dev.woytkowiak.ci.helper.ci.completion.findModelUsages
import dev.woytkowiak.ci.helper.ci.completion.resolveModelFile

/**
 * Gutter markers for CI3 models:
 * - From `$this->load->model('path')` to the target model file.
 * - From a model file (application/models/...) back to controllers that load it.
 */
class CiModelLineMarkerProvider : LineMarkerProvider {

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
                addControllerToModelMarker(element)?.let { result.add(it) }
            }
        }

        val file = firstFile
        if (file is PsiFile) {
            addModelToControllersMarkers(file, result)
        }
    }

    private fun addControllerToModelMarker(literal: StringLiteralExpression): LineMarkerInfo<*>? {
        val methodRef = literal.parent as? com.jetbrains.php.lang.psi.elements.ParameterList
            ?: return null
        val call = methodRef.parent as? MethodReference ?: return null
        if (call.name != "model") return null

        val classRef = call.classReference as? MethodReference ?: return null
        if (classRef.name != "load") return null
        val root = classRef.classReference as? com.jetbrains.php.lang.psi.elements.Variable ?: return null
        if (root.name != "this") return null

        val modelName = literal.contents.trim().trim('\'', '"')
        if (modelName.isEmpty()) return null

        val project = literal.project
        val target = resolveModelFile(project, modelName) ?: return null
        val psiTarget = com.intellij.psi.PsiManager.getInstance(project).findFile(target) ?: return null

        val builder = NavigationGutterIconBuilder.create(Ci3Icons.Ci3)
            .setTooltipText("Go to CI3 model '$modelName'")
            .setTargets(psiTarget)
        return builder.createLineMarkerInfo(literal)
    }

    private fun addModelToControllersMarkers(
        file: PsiFile,
        result: MutableCollection<in LineMarkerInfo<*>>
    ) {
        val project = file.project
        val modelsDir = dev.woytkowiak.ci.helper.ci.completion.getModelsDir(project) ?: return
        val vFile = file.virtualFile ?: return
        if (!vFile.path.startsWith(modelsDir.path)) return

        val modelName = getModelNameForFile(project, vFile) ?: return

        val usages = findModelUsages(project, modelName)
        if (usages.isEmpty()) return

        val anchor = file.firstChild ?: return

        val builder = NavigationGutterIconBuilder.create(Ci3Icons.Ci3)
            .setTooltipText("Loaded as CI3 model '$modelName'")
            .setTargets(usages)

        result.add(builder.createLineMarkerInfo(anchor))
    }
}
