package dev.woytkowiak.ci.helper.ci.actions

import com.intellij.openapi.fileChooser.FileChooserDescriptorFactory
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.DialogWrapper
import com.intellij.openapi.ui.TextFieldWithBrowseButton
import com.intellij.openapi.vfs.LocalFileSystem
import com.intellij.psi.PsiDirectory
import com.intellij.psi.PsiManager
import dev.woytkowiak.ci.helper.MyBundle
import dev.woytkowiak.ci.helper.ci.CiHookUtils
import java.awt.GridBagConstraints
import java.awt.GridBagLayout
import java.awt.Insets
import java.io.File
import javax.swing.JCheckBox
import javax.swing.JComboBox
import javax.swing.JComponent
import javax.swing.JLabel
import javax.swing.JPanel
import javax.swing.JTextField
import javax.swing.event.DocumentEvent
import javax.swing.event.DocumentListener

/**
 * CI3 Hook wizard: Name, File name, Directory, Hook point, "Add to hooks.php" checkbox.
 */
class CreateCi3HookDialog(
    project: Project,
    private val initialDirectory: PsiDirectory
) : DialogWrapper(project) {

    private val nameField = JTextField(20)
    private val fileNameField = JTextField(20)
    private val directoryField = TextFieldWithBrowseButton()
    private val hookPointCombo = JComboBox<String>().apply {
        model = javax.swing.DefaultComboBoxModel(CiHookUtils.HOOK_POINT_NAMES.toTypedArray())
        selectedItem = "pre_controller"
    }
    private val addToHooksCheckbox = JCheckBox(MyBundle.message("action.create.ci3.hook.add.to.hooks"), true)

    var className: String
        get() = nameField.text.trim()
        set(value) {
            nameField.text = value
            updateFileNameFromName()
        }

    var fileName: String
        get() = fileNameField.text.trim()
        set(value) { fileNameField.text = value }

    var directoryPath: String
        get() = directoryField.text.trim()
        set(value) { directoryField.text = value }

    var hookPoint: String
        get() = (hookPointCombo.selectedItem as? String)?.trim() ?: "pre_controller"
        set(value) { hookPointCombo.selectedItem = value }

    var addToHooks: Boolean
        get() = addToHooksCheckbox.isSelected
        set(value) { addToHooksCheckbox.isSelected = value }

    init {
        title = MyBundle.message("action.create.ci3.wizard.title.hook")
        setOKButtonText(MyBundle.message("action.create.ci3.wizard.ok"))
        directoryPath = initialDirectory.virtualFile.path
        nameField.document.addDocumentListener(object : DocumentListener {
            override fun insertUpdate(e: DocumentEvent) = updateFileNameFromName()
            override fun removeUpdate(e: DocumentEvent) = updateFileNameFromName()
            override fun changedUpdate(e: DocumentEvent) = updateFileNameFromName()
        })
        directoryField.addBrowseFolderListener(
            project,
            FileChooserDescriptorFactory.createSingleFolderDescriptor()
                .withTitle(MyBundle.message("action.create.ci3.wizard.choose.directory"))
        )
        init()
    }

    private fun updateFileNameFromName() {
        val name = nameField.text.trim()
        fileNameField.text = when {
            name.isEmpty() -> ""
            name.endsWith(".php") -> name
            else -> "$name.php"
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
        addRow(JLabel(MyBundle.message("action.create.ci3.wizard.hook.point")), hookPointCombo, row++)
        c.gridy = row
        c.gridx = 0
        c.gridwidth = 2
        panel.add(addToHooksCheckbox, c)
        return panel
    }

    fun getTargetDirectory(): PsiDirectory? {
        val path = directoryPath.trim().trimEnd('/')
        val vf = LocalFileSystem.getInstance().findFileByIoFile(File(path))
        return vf?.let { PsiManager.getInstance(initialDirectory.project).findDirectory(it) }
    }
}
