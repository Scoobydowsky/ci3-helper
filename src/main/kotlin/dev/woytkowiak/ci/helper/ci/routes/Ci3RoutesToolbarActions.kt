package dev.woytkowiak.ci.helper.ci.routes

import com.intellij.icons.AllIcons
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.DefaultActionGroup
import com.intellij.openapi.project.DumbAwareAction
import com.intellij.openapi.project.Project

/**
 * Toolbar for the CI3 Routes tool window: Refresh.
 */
class Ci3RoutesToolbarActions(project: Project, private val panel: Ci3RoutesPanel) :
    DefaultActionGroup() {

    init {
        add(RefreshRoutesAction(panel))
    }
}

private class RefreshRoutesAction(private val panel: Ci3RoutesPanel) :
    DumbAwareAction("Refresh", "Rescan controllers", AllIcons.Actions.Refresh) {

    override fun actionPerformed(e: AnActionEvent) {
        panel.loadRoutes()
    }
}
