package dev.woytkowiak.ci.helper.ci.actions

import com.intellij.ide.BrowserUtil
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import dev.woytkowiak.ci.helper.ci.Ci3Icons
import dev.woytkowiak.ci.helper.ci.Ci3Support
import dev.woytkowiak.ci.helper.MyBundle

/**
 * Otwiera w przeglądarce stronę z informacją o wsparciu autora pluginu.
 * Link możesz zmienić na np. Ko-fi, Buy Me a Coffee, GitHub Sponsors.
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
