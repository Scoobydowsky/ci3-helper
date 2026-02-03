package dev.woytkowiak.ci.helper.ci.statusbar

import com.intellij.openapi.project.Project
import com.intellij.openapi.wm.StatusBarWidget
import com.intellij.openapi.wm.StatusBarWidgetFactory

/**
 * Rejestruje widget CI3 na pasku statusu (ikona + tooltip z wersją, klik = wsparcie autora).
 */
class Ci3StatusBarWidgetFactory : StatusBarWidgetFactory {

    override fun getId(): String = "CodeIgniter3Helper"

    override fun getDisplayName(): String = "CodeIgniter 3 Helper"

    override fun createWidget(project: Project): StatusBarWidget = Ci3StatusBarWidget(project)
}
