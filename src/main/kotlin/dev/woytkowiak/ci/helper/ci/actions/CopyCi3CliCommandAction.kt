package dev.woytkowiak.ci.helper.ci.actions

import com.intellij.openapi.actionSystem.ActionUpdateThread
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.ide.CopyPasteManager
import dev.woytkowiak.ci.helper.MyBundle
import dev.woytkowiak.ci.helper.ci.Ci3Icons
import java.awt.datatransfer.StringSelection

class CopyCi3CliCommandAction : AnAction(
    MyBundle.messagePointer("action.ci3.cli.copy.text"),
    MyBundle.messagePointer("action.ci3.cli.copy.description"),
    Ci3Icons.Ci3Menu
) {
    override fun getActionUpdateThread(): ActionUpdateThread = ActionUpdateThread.BGT

    override fun update(e: AnActionEvent) {
        e.presentation.isEnabledAndVisible = Ci3CliControllerContextResolver.resolve(e) != null
    }

    override fun actionPerformed(e: AnActionEvent) {
        val ctx = Ci3CliControllerContextResolver.resolve(e) ?: return
        val cmd = buildString {
            append("php index.php ")
            append(ctx.controller)
            append(' ')
            append(ctx.method)
        }
        CopyPasteManager.getInstance().setContents(StringSelection(cmd))
    }
}

