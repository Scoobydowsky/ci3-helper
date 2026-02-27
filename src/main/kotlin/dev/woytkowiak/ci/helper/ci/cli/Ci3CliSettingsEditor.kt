package dev.woytkowiak.ci.helper.ci.cli

import com.intellij.openapi.fileChooser.FileChooserDescriptorFactory
import com.intellij.openapi.options.SettingsEditor
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.TextFieldWithBrowseButton
import com.intellij.ui.dsl.builder.AlignX
import com.intellij.ui.dsl.builder.panel
import com.intellij.util.ui.JBFont
import javax.swing.JComponent
import javax.swing.JTextField

class Ci3CliSettingsEditor(private val project: Project) : SettingsEditor<Ci3CliRunConfiguration>() {

    private val phpPath = TextFieldWithBrowseButton()
    private val workingDir = TextFieldWithBrowseButton()
    private val indexPhp = TextFieldWithBrowseButton()
    private val controller = JTextField()
    private val method = JTextField()
    private val args = JTextField()

    init {
        phpPath.addBrowseFolderListener(
            project,
            FileChooserDescriptorFactory.createSingleFileDescriptor()
                .withTitle("Select PHP executable")
                .withDescription("Choose your php binary (leave empty to use `php` from PATH).")
        )
        workingDir.addBrowseFolderListener(
            project,
            FileChooserDescriptorFactory.createSingleFolderDescriptor()
                .withTitle("Select working directory")
                .withDescription("Project root (where index.php is located).")
        )
        indexPhp.addBrowseFolderListener(
            project,
            FileChooserDescriptorFactory.createSingleFileDescriptor("php")
                .withTitle("Select index.php")
                .withDescription("CodeIgniter front controller (usually index.php).")
        )
    }

    override fun resetEditorFrom(s: Ci3CliRunConfiguration) {
        phpPath.textField.text = s.phpPath
        workingDir.textField.text = s.workingDirectory
        indexPhp.textField.text = s.indexPhpPath
        controller.text = s.controller
        method.text = s.method
        args.text = s.args
    }

    override fun applyEditorTo(s: Ci3CliRunConfiguration) {
        s.phpPath = phpPath.textField.text
        s.workingDirectory = workingDir.textField.text
        s.indexPhpPath = indexPhp.textField.text
        s.controller = controller.text
        s.method = method.text
        s.args = args.text
    }

    override fun createEditor(): JComponent {
        fun JTextField.mono() = apply { font = JBFont.create(font, true) }

        controller.mono()
        method.mono()
        args.mono()

        return panel {
            row("PHP:") {
                cell(phpPath).align(AlignX.FILL)
            }
            row("Working directory:") {
                cell(workingDir).align(AlignX.FILL)
            }
            row("index.php:") {
                cell(indexPhp).align(AlignX.FILL)
            }
            row("Controller:") {
                cell(controller).align(AlignX.FILL)
            }
            row("Method:") {
                cell(method).align(AlignX.FILL)
            }
            row("Args:") {
                cell(args).align(AlignX.FILL)
                    .comment("Optional CLI args, e.g. \"John Smith\" --dry-run")
            }
        }
    }
}

