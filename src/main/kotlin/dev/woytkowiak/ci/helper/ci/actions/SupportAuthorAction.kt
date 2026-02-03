package dev.woytkowiak.ci.helper.ci.actions

import com.intellij.ide.BrowserUtil
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import dev.woytkowiak.ci.helper.ci.Ci3Icons
import dev.woytkowiak.ci.helper.ci.Ci3Support
import dev.woytkowiak.ci.helper.MyBundle

/**
 * Opens in the browser the page with information on supporting the plugin author.
 * You can change the link to e.g. Ko-fi, Buy Me a Coffee, GitHub Sponsors.
 */
class SupportAuthorAction : AnAction(
    MyBundle.messagePointer("action.support.author.text"),
    MyBundle.messagePointer("action.support.author.description"),
    Ci3Icons.Ci3Menu
) {
    override fun actionPerformed(e: AnActionEvent) {
        BrowserUtil.browse(Ci3Support.URL)
    }
}
