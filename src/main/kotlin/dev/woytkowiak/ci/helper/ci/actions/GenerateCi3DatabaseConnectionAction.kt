package dev.woytkowiak.ci.helper.ci.actions

import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.actionSystem.ActionUpdateThread
import com.intellij.openapi.project.DumbAwareAction
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.InputValidator
import com.intellij.openapi.ui.Messages
import com.intellij.openapi.vfs.VfsUtil
import com.intellij.psi.PsiDocumentManager
import com.intellij.openapi.command.WriteCommandAction
import dev.woytkowiak.ci.helper.MyBundle
import dev.woytkowiak.ci.helper.ci.Ci3Icons
import dev.woytkowiak.ci.helper.ci.Ci3PluginState
import dev.woytkowiak.ci.helper.ci.guessProjectBaseDir

/**
 * Code → Generate: insert a new `$db['name'] = array(...)` block in CI3 `application/config/database.php`.
 */
class GenerateCi3DatabaseConnectionAction : DumbAwareAction(
    MyBundle.messagePointer("action.ci3.generate.db.connection.text"),
    MyBundle.messagePointer("action.ci3.generate.db.connection.description"),
    Ci3Icons.Ci3Menu
) {

    override fun getActionUpdateThread(): ActionUpdateThread = ActionUpdateThread.BGT

    override fun update(e: com.intellij.openapi.actionSystem.AnActionEvent) {
        val presentation = e.presentation
        if (!Ci3PluginState.getInstance().isEnabled) {
            presentation.isEnabledAndVisible = false
            return
        }
        val project = e.project
        val editor = e.getData(CommonDataKeys.EDITOR)
        val file = editor?.virtualFile ?: e.getData(CommonDataKeys.VIRTUAL_FILE)
        if (project == null || file == null || !isCi3DatabasePhp(project, file)) {
            presentation.isEnabledAndVisible = false
            return
        }
        presentation.isEnabledAndVisible = true
    }

    override fun actionPerformed(e: com.intellij.openapi.actionSystem.AnActionEvent) {
        val project = e.project ?: return
        val editor = e.getData(CommonDataKeys.EDITOR) ?: return
        val file = editor.virtualFile

        if (!isCi3DatabasePhp(project, file)) return

        val name = Messages.showInputDialog(
            project,
            MyBundle.message("action.ci3.generate.db.connection.prompt"),
            MyBundle.message("action.ci3.generate.db.connection.title"),
            Messages.getQuestionIcon(),
            "secondary",
            object : InputValidator {
                private val valid = Regex("^[a-zA-Z_][a-zA-Z0-9_]*$")
                override fun checkInput(inputString: String): Boolean =
                    inputString.isNotEmpty() && valid.matches(inputString)
                override fun canClose(inputString: String): Boolean = checkInput(inputString)
            }
        ) ?: return

        if (connectionKeyExists(editor, name)) {
            Messages.showWarningDialog(
                project,
                MyBundle.message("action.ci3.generate.db.connection.duplicate", name),
                MyBundle.message("action.ci3.generate.db.connection.title")
            )
            return
        }

        val block = buildConnectionBlock(name)
        val document = editor.document
        val offset = editor.caretModel.offset
        val prefix = buildPrefix(document, offset)

        WriteCommandAction.runWriteCommandAction(
            project,
            MyBundle.message("action.ci3.generate.db.connection.undo"),
            MyBundle.message("action.ci3.generate.db.connection.undo.context"),
            Runnable {
                document.insertString(offset, prefix + block)
                PsiDocumentManager.getInstance(project).commitDocument(document)
                editor.caretModel.moveToOffset(offset + prefix.length + block.length)
            }
        )
    }

    private fun buildPrefix(document: com.intellij.openapi.editor.Document, offset: Int): String {
        if (offset == 0) return ""
        val before = document.charsSequence.subSequence(0, offset).toString()
        return when {
            before.endsWith("\n\n") -> ""
            before.endsWith("\n") -> "\n"
            else -> "\n\n"
        }
    }

    private fun connectionKeyExists(editor: Editor, name: String): Boolean {
        val text = editor.document.text
        val re = Regex("\\\$db\\s*\\[\\s*['\"]" + Regex.escape(name) + "['\"]\\s*\\]")
        return re.containsMatchIn(text)
    }

    companion object {
        fun isCi3DatabasePhp(project: Project, file: com.intellij.openapi.vfs.VirtualFile): Boolean {
            if (file.name != "database.php") return false
            val base = project.guessProjectBaseDir() ?: return false
            val rel = VfsUtil.getRelativePath(file, base, '/') ?: return false
            return rel == "application/config/database.php"
        }

        /** CI3 default `database.php` array shape (mysqli). */
        fun buildConnectionBlock(name: String): String = buildString {
            append("\$db['").append(name).append("'] = array(\n")
            append("\t'dsn'\t=> '',\n")
            append("\t'hostname' => 'localhost',\n")
            append("\t'username' => '',\n")
            append("\t'password' => '',\n")
            append("\t'database' => '',\n")
            append("\t'dbdriver' => 'mysqli',\n")
            append("\t'dbprefix' => '',\n")
            append("\t'pconnect' => FALSE,\n")
            append("\t'db_debug' => (ENVIRONMENT !== 'production'),\n")
            append("\t'cache_on' => FALSE,\n")
            append("\t'cachedir' => '',\n")
            append("\t'char_set' => 'utf8',\n")
            append("\t'dbcollat' => 'utf8_general_ci',\n")
            append("\t'swap_pre' => '',\n")
            append("\t'encrypt' => FALSE,\n")
            append("\t'compress' => FALSE,\n")
            append("\t'stricton' => FALSE,\n")
            append("\t'failover' => array(),\n")
            append("\t'save_queries' => TRUE\n")
            append(");\n")
        }
    }
}
