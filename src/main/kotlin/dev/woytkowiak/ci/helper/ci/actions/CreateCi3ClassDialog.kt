package dev.woytkowiak.ci.helper.ci.actions

import com.intellij.openapi.fileChooser.FileChooserDescriptorFactory
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.DialogWrapper
import com.intellij.openapi.ui.TextFieldWithBrowseButton
import com.intellij.openapi.vfs.LocalFileSystem
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.psi.PsiDirectory
import com.intellij.psi.PsiManager
import dev.woytkowiak.ci.helper.MyBundle
import java.awt.GridBagConstraints
import java.awt.GridBagLayout
import java.awt.Insets
import java.io.File
import javax.swing.DefaultComboBoxModel
import javax.swing.JComboBox
import javax.swing.JComponent
import javax.swing.JLabel
import javax.swing.JPanel
import javax.swing.JTextField
import javax.swing.event.DocumentEvent
import javax.swing.event.DocumentListener

/**
 * CI3 wizard: Name, File name (auto from Name), Directory, Extends (dropdown).
 * No namespace or template (CI3 does not use them in this context).
 */
class CreateCi3ClassDialog(
    project: Project,
    private val initialDirectory: PsiDirectory,
    private val defaultExtends: String?,
    private val fileNameSuffix: String?,
    private val extendsOptions: List<String>,
    private val dialogTitleKey: String
) : DialogWrapper(project) {

    private val nameField = JTextField(20)
    private val fileNameField = JTextField(20)
    private val directoryField = TextFieldWithBrowseButton()
    private val extendsCombo = JComboBox<String>().apply {
        isEditable = true
        model = DefaultComboBoxModel(extendsOptions.toTypedArray())
        defaultExtends?.let { selectedItem = it }
    }

    var className: String
        get() = nameField.text.trim()
        set(value) {
            nameField.text = value
            updateFileNameFromName()
        }

    var fileName: String
        get() = fileNameField.text.trim()
        set(value) {
            fileNameField.text = value
        }

    var directoryPath: String
        get() = directoryField.text.trim()
        set(value) {
            directoryField.text = value
        }

    var extendsClass: String
        get() = (extendsCombo.editor?.item ?: extendsCombo.selectedItem)?.toString()?.trim() ?: ""
        set(value) {
            extendsCombo.selectedItem = value
        }

    init {
        title = MyBundle.message(dialogTitleKey)
        setOKButtonText(MyBundle.message("action.create.ci3.wizard.ok"))
        directoryPath = initialDirectory.virtualFile.path
        defaultExtends?.let { extendsCombo.selectedItem = it }
        nameField.document.addDocumentListener(object : DocumentListener {
            override fun insertUpdate(e: DocumentEvent) = updateFileNameFromName()
            override fun removeUpdate(e: DocumentEvent) = updateFileNameFromName()
            override fun changedUpdate(e: DocumentEvent) = updateFileNameFromName()
        })
        directoryField.addBrowseFolderListener(
            MyBundle.message("action.create.ci3.wizard.choose.directory"),
            null,
            project,
            FileChooserDescriptorFactory.createSingleFolderDescriptor()
        )
        init()
    }

    private fun updateFileNameFromName() {
        val name = nameField.text.trim()
        fileNameField.text = when {
            name.isEmpty() -> ""
            fileNameSuffix != null -> name + fileNameSuffix
            else -> name
        }
    }

    override fun createCenterPanel(): JComponent {
        val panel = JPanel(GridBagLayout())
        val c = GridBagConstraints().apply {
            insets = Insets(2, 2, 2, 2)
            anchor = GridBagConstraints.WEST
            fill = GridBagConstraints.HORIZONTAL
            weightx = 0.0
        }

        fun addRow(label: JLabel, field: JComponent, row: Int) {
            c.gridy = row
            c.gridx = 0
            c.weightx = 0.0
            panel.add(label, c)
            c.gridx = 1
            c.weightx = 1.0
            panel.add(field, c)
        }

        var row = 0
        addRow(JLabel(MyBundle.message("action.create.ci3.wizard.name")), nameField, row++)
        addRow(JLabel(MyBundle.message("action.create.ci3.wizard.filename")), fileNameField, row++)
        addRow(JLabel(MyBundle.message("action.create.ci3.wizard.directory")), directoryField, row++)
        if (extendsOptions.isNotEmpty()) {
            addRow(JLabel(MyBundle.message("action.create.ci3.wizard.extends")), extendsCombo, row++)
        }

        return panel
    }

    fun getTargetDirectory(): PsiDirectory? {
        val path = directoryPath.trim().trimEnd('/')
        val vf: VirtualFile? = LocalFileSystem.getInstance().findFileByIoFile(File(path))
        return vf?.let { PsiManager.getInstance(initialDirectory.project).findDirectory(it) }
    }
}
