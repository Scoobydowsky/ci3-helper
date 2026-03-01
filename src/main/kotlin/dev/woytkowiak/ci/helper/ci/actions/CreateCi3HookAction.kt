package dev.woytkowiak.ci.helper.ci.actions

import com.intellij.ide.actions.CreateFileFromTemplateAction
import com.intellij.ide.fileTemplates.FileTemplateManager
import com.intellij.openapi.actionSystem.ActionUpdateThread
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.actionSystem.LangDataKeys
import com.intellij.openapi.command.WriteCommandAction
import com.intellij.openapi.fileEditor.FileEditorManager
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.psi.PsiFile
import com.intellij.psi.PsiManager
import dev.woytkowiak.ci.helper.ci.Ci3Icons
import dev.woytkowiak.ci.helper.ci.guessProjectBaseDir
import dev.woytkowiak.ci.helper.MyBundle
import java.util.Collections

/**
 * New → CodeIgniter 3 → Hook. Creates a hook class file and optionally adds an entry to config/hooks.php.
 */
class CreateCi3HookAction : AnAction(
    MyBundle.messagePointer("action.create.ci3.hook.text"),
    MyBundle.messagePointer("action.create.ci3.hook.text"),
    Ci3Icons.Ci3Menu
) {

    override fun actionPerformed(e: AnActionEvent) {
        val project = e.project ?: return
        val view = e.getData(LangDataKeys.IDE_VIEW) ?: return
        val dir = view.getOrChooseDirectory() ?: return
        val defaultDir = Ci3DirectoryFinder.findDefaultDirectory(project, dir, "hooks")

        val dialog = CreateCi3HookDialog(project, defaultDir)
        if (!dialog.showAndGet()) return

        val targetDir = dialog.getTargetDirectory() ?: return
        val className = dialog.className.ifBlank { return }
        var fileName = dialog.fileName.trim().removeSuffix(".php")
        if (fileName.isBlank()) fileName = className
        val hookPoint = dialog.hookPoint
        val addToHooks = dialog.addToHooks

        val template = FileTemplateManager.getInstance(project).getInternalTemplate("CI3 Hook.php")
        val extraProps = mapOf(
            "NAME" to className,
            "HOOK_POINT" to hookPoint
        )
        val psiFile = CreateFileFromTemplateAction.createFileFromTemplate(
            fileName, template, targetDir, null, true,
            Collections.emptyMap(), extraProps
        ) as? PsiFile ?: return

        psiFile.virtualFile?.let { FileEditorManager.getInstance(project).openFile(it, true) }
        view.selectElement(psiFile)

        if (addToHooks) {
            val baseDir = project.guessProjectBaseDir() ?: return
            val appDir = baseDir.findChild("application") ?: return
            val configDir = appDir.findChild("config") ?: return
            val hooksFile = configDir.findChild("hooks.php") ?: return
            val targetPath = targetDir.virtualFile.path
            val appPath = appDir.path
            val filepath = if (targetPath.startsWith(appPath)) {
                targetPath.removePrefix(appPath).trimStart('/', '\\').replace('\\', '/')
            } else "hooks"
            val filename = "${fileName}.php"
            WriteCommandAction.runWriteCommandAction(project) {
                appendHookEntry(project, hooksFile, hookPoint, className, "run", filename, filepath)
            }
        }
    }

    private fun appendHookEntry(
        project: com.intellij.openapi.project.Project,
        hooksFile: VirtualFile,
        hookPoint: String,
        className: String,
        functionName: String,
        filename: String,
        filepath: String
    ) {
        val content = hooksFile.contentsToByteArray().toString(Charsets.UTF_8)
        val entry = """
            |$${'$'}hook['$hookPoint'][] = array(
            |        'class'    => '$className',
            |        'function' => '$functionName',
            |        'filename' => '$filename',
            |        'filepath' => '$filepath'
            |);
            |
        """.trimMargin()
        val newContent = content.trimEnd() + "\n" + entry
        hooksFile.setBinaryContent(newContent.toByteArray(Charsets.UTF_8))
    }

    override fun getActionUpdateThread(): ActionUpdateThread = ActionUpdateThread.BGT

    override fun update(e: AnActionEvent) {
        val project = CommonDataKeys.PROJECT.getData(e.dataContext)
        val view = LangDataKeys.IDE_VIEW.getData(e.dataContext)
        e.presentation.isEnabledAndVisible = project != null && view != null && view.directories.isNotEmpty()
    }
}
