package dev.woytkowiak.ci.helper.ci.actions

import com.intellij.openapi.actionSystem.ActionUpdateThread
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.application.ReadAction
import com.intellij.openapi.command.WriteCommandAction
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.project.DumbAwareAction
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VfsUtil
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.psi.PsiDocumentManager
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.openapi.project.DumbService
import com.jetbrains.php.lang.psi.PhpFile
import com.jetbrains.php.lang.psi.elements.Method
import dev.woytkowiak.ci.helper.MyBundle
import dev.woytkowiak.ci.helper.ci.Ci3Icons
import dev.woytkowiak.ci.helper.ci.Ci3PluginState
import dev.woytkowiak.ci.helper.ci.guessProjectBaseDir

/**
 * Code → Generate: insert `$sections = array(...);` and `$this->output->set_profiler_sections($sections);`
 * inside a controller method (CI3).
 */
class GenerateCi3ProfilerSectionsAction : DumbAwareAction(
    MyBundle.messagePointer("action.ci3.generate.profiler.text"),
    MyBundle.messagePointer("action.ci3.generate.profiler.description"),
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
        val file = editor?.virtualFile
        if (project == null || editor == null || file == null) {
            presentation.isEnabledAndVisible = false
            return
        }
        if (DumbService.isDumb(project) || !isCi3ControllerPhp(project, file)) {
            presentation.isEnabledAndVisible = false
            return
        }
        val inMethod = isCaretInControllerMethod(project, editor)
        presentation.isEnabledAndVisible = inMethod
    }

    override fun actionPerformed(e: com.intellij.openapi.actionSystem.AnActionEvent) {
        val project = e.project ?: return
        val editor = e.getData(CommonDataKeys.EDITOR) ?: return
        val file = editor.virtualFile
        if (!isCi3ControllerPhp(project, file) || !isCaretInControllerMethod(project, editor)) return

        val dialog = Ci3ProfilerSectionsDialog(project)
        if (!dialog.showAndGet()) return
        val result = dialog.getResult() ?: return

        val block = buildProfilerBlock(result)
        val document = editor.document
        val offset = editor.caretModel.offset
        val prefix = buildInsertPrefix(document, offset)

        WriteCommandAction.runWriteCommandAction(
            project,
            MyBundle.message("action.ci3.generate.profiler.undo"),
            MyBundle.message("action.ci3.generate.profiler.undo.context"),
            Runnable {
                document.insertString(offset, prefix + block)
                PsiDocumentManager.getInstance(project).commitDocument(document)
                editor.caretModel.moveToOffset(offset + prefix.length + block.length)
            }
        )
    }

    private fun buildProfilerBlock(result: Ci3ProfilerSectionResult): String = buildString {
        append("\$sections = array(\n")
        for (key in CI3_PROFILER_BOOLEAN_KEYS) {
            val on = result.booleanSections[key] == true
            append("\t'").append(key).append("' => ").append(if (on) "TRUE" else "FALSE").append(",\n")
        }
        append("\t'query_toggle_count' => ").append(result.queryToggleCount).append("\n")
        append(");\n")
        append("\$this->output->set_profiler_sections(\$sections);\n")
    }

    companion object {
        fun isCi3ControllerPhp(project: Project, file: VirtualFile): Boolean {
            if (!file.name.endsWith(".php")) return false
            val base = project.guessProjectBaseDir() ?: return false
            val rel = VfsUtil.getRelativePath(file, base, '/') ?: return false
            return rel.startsWith("application/controllers/")
        }

        fun isCaretInControllerMethod(project: Project, editor: Editor): Boolean {
            return ReadAction.compute<Boolean, RuntimeException> {
                val psiFile = PsiDocumentManager.getInstance(project).getPsiFile(editor.document) as? PhpFile
                    ?: return@compute false
                if (psiFile.textLength == 0) return@compute false
                val off = editor.caretModel.offset.coerceIn(0, psiFile.textLength - 1)
                val element = psiFile.findElementAt(off) ?: return@compute false
                PsiTreeUtil.getParentOfType(element, Method::class.java) != null
            }
        }

        fun buildInsertPrefix(document: com.intellij.openapi.editor.Document, offset: Int): String {
            if (offset == 0) return ""
            val before = document.charsSequence.subSequence(0, offset).toString()
            return when {
                before.endsWith("\n\n") -> ""
                before.endsWith("\n") -> "\n"
                else -> "\n\n"
            }
        }
    }
}
