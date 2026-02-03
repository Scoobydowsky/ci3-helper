package dev.woytkowiak.ci.helper.ci.actions

import com.intellij.ide.actions.CreateFileFromTemplateAction
import com.intellij.ide.fileTemplates.FileTemplateManager
import java.util.Collections
import com.intellij.openapi.actionSystem.ActionUpdateThread
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.actionSystem.LangDataKeys
import com.intellij.openapi.fileEditor.FileEditorManager
import com.intellij.psi.PsiFile
import dev.woytkowiak.ci.helper.ci.Ci3Icons
import dev.woytkowiak.ci.helper.MyBundle

/**
 * Bazowa akcja tworzenia pojedynczego pliku CI3 z szablonu.
 * Kreator: Name, File name (auto), Directory (domyślnie application/controllers itd.), Extends (lista).
 *
 * @param defaultFolderName "controllers", "models" lub "views" – domyślny katalog CI3
 */
abstract class CreateCi3FileAction(
    private val templateName: String,
    private val textKey: String,
    @Suppress("UNUSED_PARAMETER") private val actionNameKey: String,
    private val fileNameSuffix: String? = null,
    private val defaultExtends: String? = null,
    private val extendsOptions: List<String>,
    private val defaultFolderName: String,
    private val dialogTitleKey: String = "action.create.ci3.wizard.title"
) : AnAction(
    MyBundle.messagePointer(textKey),
    MyBundle.messagePointer(textKey),
    Ci3Icons.Ci3Menu
) {

    override fun actionPerformed(e: AnActionEvent) {
        val project = e.project ?: return
        val view = e.getData(LangDataKeys.IDE_VIEW) ?: return
        val dir = view.getOrChooseDirectory() ?: return
        val defaultDir = Ci3DirectoryFinder.findDefaultDirectory(project, dir, defaultFolderName)

        val dialog = CreateCi3ClassDialog(
            project = project,
            initialDirectory = defaultDir,
            defaultExtends = defaultExtends,
            fileNameSuffix = fileNameSuffix,
            extendsOptions = extendsOptions,
            dialogTitleKey = dialogTitleKey
        )
        if (!dialog.showAndGet()) return

        val targetDir = dialog.getTargetDirectory() ?: return
        val fileName = dialog.fileName.ifBlank { dialog.className }.let { name ->
            when {
                fileNameSuffix == null -> name
                name.endsWith(fileNameSuffix) -> name
                else -> name + fileNameSuffix
            }
        }.removeSuffix(".php").ifBlank { return@actionPerformed }
        val template = FileTemplateManager.getInstance(project).getInternalTemplate(templateName)
        val extendsClass = dialog.extendsClass.ifBlank { defaultExtends }.orEmpty()
        val extraProps = if (extendsClass.isNotEmpty()) mapOf("EXTENDS" to extendsClass) else emptyMap()
        val psiFile = CreateFileFromTemplateAction.createFileFromTemplate(
            fileName, template, targetDir, null, true,
            Collections.emptyMap(), extraProps
        ) as? PsiFile
        psiFile?.virtualFile?.let { FileEditorManager.getInstance(project).openFile(it, true) }
        psiFile?.let { view.selectElement(it) }
    }

    override fun getActionUpdateThread(): ActionUpdateThread = ActionUpdateThread.BGT

    override fun update(e: AnActionEvent) {
        val project = CommonDataKeys.PROJECT.getData(e.dataContext)
        val view = LangDataKeys.IDE_VIEW.getData(e.dataContext)
        e.presentation.isEnabledAndVisible = project != null && view != null && view.directories.isNotEmpty()
    }
}
