package dev.woytkowiak.ci.helper.ci.routes

import com.intellij.icons.AllIcons
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.command.WriteCommandAction
import com.intellij.openapi.project.DumbAwareAction
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.Messages
import dev.woytkowiak.ci.helper.MyBundle

/**
 * Opens dialog to add `$route['URI'] = 'target';` to application/config/routes.php.
 */
class Ci3AddRouteAction(
    private val project: Project,
    private val panel: Ci3RoutesPanel
) : DumbAwareAction(
    MyBundle.message("ci3.routes.add.action"),
    MyBundle.message("ci3.routes.add.action.description"),
    AllIcons.General.Add
) {

    override fun actionPerformed(e: AnActionEvent) {
        val routesFile = Ci3RoutesFileUtil.findRoutesFile(project)
        if (routesFile == null) {
            Messages.showErrorDialog(
                project,
                MyBundle.message("ci3.routes.add.error.no.routes.file"),
                MyBundle.message("ci3.routes.add.title")
            )
            return
        }
        val suggestions = panel.getSuggestionsForNewRoute()
        val dialog = Ci3AddRouteDialog(project, suggestions)
        if (!dialog.showAndGet()) return
        val uri = dialog.uriPattern
        val target = dialog.target
        val line = Ci3RoutesFileUtil.formatRouteLine(uri, target)
        WriteCommandAction.runWriteCommandAction(
            project,
            MyBundle.message("ci3.routes.add.undo.text"),
            MyBundle.message("ci3.routes.add.undo.group"),
            Runnable {
                val content = String(routesFile.contentsToByteArray(), Charsets.UTF_8)
                val newContent = Ci3RoutesFileUtil.insertRouteLine(content, line)
                routesFile.setBinaryContent(newContent.toByteArray(Charsets.UTF_8))
            }
        )
        routesFile.refresh(false, false)
        panel.loadRoutes()
    }
}
