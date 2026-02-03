package dev.woytkowiak.ci.helper.ci.statusbar

import com.intellij.openapi.project.Project
import com.intellij.openapi.wm.StatusBarWidget
import com.intellij.openapi.wm.StatusBarWidgetFactory

/**
 * Registers the CI3 widget on the status bar (icon + version tooltip, click = support author).
 */
class Ci3StatusBarWidgetFactory : StatusBarWidgetFactory {

    override fun getId(): String = "CodeIgniter3Helper"

    override fun getDisplayName(): String = "CodeIgniter 3 Helper"

    override fun createWidget(project: Project): StatusBarWidget = Ci3StatusBarWidget(project)
}
