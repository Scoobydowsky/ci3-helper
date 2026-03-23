package dev.woytkowiak.ci.helper.ci.actions

import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.DialogWrapper
import com.intellij.openapi.ui.ValidationInfo
import dev.woytkowiak.ci.helper.MyBundle
import java.awt.GridBagConstraints
import java.awt.GridBagLayout
import java.awt.Insets
import javax.swing.DefaultComboBoxModel
import javax.swing.JComponent
import javax.swing.JComboBox
import javax.swing.JLabel
import javax.swing.JTextField

internal enum class Ci3MigrationType {
    TIMESTAMP,
    SEQUENTIAL;

    override fun toString(): String = when (this) {
        TIMESTAMP -> "timestamp (YYYYMMDDHHmmss)"
        SEQUENTIAL -> "sequential (001, 002, ...)"
    }
}

internal data class Ci3MigrationDialogResult(
    val migrationName: String,
    val migrationType: Ci3MigrationType
)

internal class Ci3MigrationDialog(
    project: Project,
    initialName: String,
    initialType: Ci3MigrationType
) : DialogWrapper(project) {

    private val nameField = JTextField(initialName, 28)
    private val typeCombo = JComboBox<Ci3MigrationType>().apply {
        model = DefaultComboBoxModel(Ci3MigrationType.values())
        selectedItem = initialType
    }

    private var lastResult: Ci3MigrationDialogResult? = null

    init {
        title = MyBundle.message("action.ci3.generate.migration.title")
        init()
    }

    fun getResult(): Ci3MigrationDialogResult? = lastResult

    override fun doValidate(): ValidationInfo? {
        val name = nameField.text.trim()
        if (name.isEmpty()) {
            return ValidationInfo(MyBundle.message("action.ci3.generate.migration.error.empty"), nameField)
        }
        if (!Regex("^[a-z0-9_]+$").matches(name)) {
            return ValidationInfo(MyBundle.message("action.ci3.generate.migration.error.invalid"), nameField)
        }
        return null
    }

    override fun doOKAction() {
        val migrationName = nameField.text.trim()
        val migrationType = (typeCombo.selectedItem as? Ci3MigrationType) ?: Ci3MigrationType.TIMESTAMP
        lastResult = Ci3MigrationDialogResult(migrationName = migrationName, migrationType = migrationType)
        super.doOKAction()
    }

    override fun createCenterPanel(): JComponent {
        val panel = javax.swing.JPanel(GridBagLayout())
        val c = GridBagConstraints().apply {
            insets = Insets(4, 4, 4, 4)
            fill = GridBagConstraints.HORIZONTAL
            anchor = GridBagConstraints.WEST
        }

        c.gridx = 0
        c.gridy = 0
        c.weightx = 0.0
        panel.add(JLabel(MyBundle.message("action.ci3.generate.migration.name")), c)

        c.gridx = 1
        c.weightx = 1.0
        panel.add(nameField, c)

        c.gridx = 0
        c.gridy = 1
        c.weightx = 0.0
        panel.add(JLabel(MyBundle.message("action.ci3.generate.migration.type")), c)

        c.gridx = 1
        c.weightx = 1.0
        panel.add(typeCombo, c)

        return panel
    }
}
