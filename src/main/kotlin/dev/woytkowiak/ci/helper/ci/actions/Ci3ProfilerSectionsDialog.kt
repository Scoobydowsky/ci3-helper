package dev.woytkowiak.ci.helper.ci.actions

import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.DialogWrapper
import com.intellij.ui.components.JBScrollPane
import com.intellij.util.ui.FormBuilder
import com.intellij.util.ui.JBUI
import dev.woytkowiak.ci.helper.MyBundle
import java.awt.BorderLayout
import java.awt.Dimension
import javax.swing.JCheckBox
import javax.swing.JComponent
import javax.swing.JLabel
import javax.swing.JPanel
import javax.swing.JSpinner
import javax.swing.SpinnerNumberModel
import javax.swing.BoxLayout

/** Boolean profiler sections (CI3 docs order); `query_toggle_count` is numeric, separate in UI. */
internal val CI3_PROFILER_BOOLEAN_KEYS: List<String> = listOf(
    "benchmarks",
    "config",
    "controller_info",
    "get",
    "http_headers",
    "memory_usage",
    "post",
    "queries",
    "uri_string",
    "session_data"
)

internal data class Ci3ProfilerSectionResult(
    val booleanSections: Map<String, Boolean>,
    val queryToggleCount: Int
)

/**
 * Checkboxes for each profiler section + numeric query_toggle_count.
 */
internal class Ci3ProfilerSectionsDialog(project: Project) : DialogWrapper(project) {

    private val checkboxes = linkedMapOf<String, JCheckBox>()
    private val spinner = JSpinner(SpinnerNumberModel(25, 0, 9999, 1))

    private var lastResult: Ci3ProfilerSectionResult? = null

    init {
        title = MyBundle.message("action.ci3.generate.profiler.title")
        // Must build checkboxes *before* init() — DialogWrapper.init() calls createCenterPanel().
        CI3_PROFILER_BOOLEAN_KEYS.forEach { key ->
            checkboxes[key] = JCheckBox(profilerSectionLabel(key), true)
        }
        init()
    }

    fun getResult(): Ci3ProfilerSectionResult? = lastResult

    override fun createCenterPanel(): JComponent {
        val sectionsPanel = JPanel().apply {
            layout = BoxLayout(this, BoxLayout.Y_AXIS)
            border = JBUI.Borders.empty(0, 0, JBUI.scale(8), 0)
            checkboxes.values.forEach { add(it) }
        }
        val scroll = JBScrollPane(sectionsPanel).apply {
            preferredSize = Dimension(JBUI.scale(360), JBUI.scale(220))
            border = JBUI.Borders.empty()
        }
        val spinnerRow = FormBuilder.createFormBuilder()
            .addLabeledComponent(
                JLabel(MyBundle.message("action.ci3.generate.profiler.query_toggle_count")),
                spinner,
                1,
                false
            )
            .panel
        return JPanel(BorderLayout()).apply {
            border = JBUI.Borders.empty(8)
            add(scroll, BorderLayout.CENTER)
            add(spinnerRow, BorderLayout.SOUTH)
        }
    }

    override fun doOKAction() {
        lastResult = Ci3ProfilerSectionResult(
            booleanSections = CI3_PROFILER_BOOLEAN_KEYS.associateWith { checkboxes[it]!!.isSelected },
            queryToggleCount = (spinner.value as? Number)?.toInt()?.coerceIn(0, 9999) ?: 25
        )
        super.doOKAction()
    }

    private fun profilerSectionLabel(key: String): String =
        MyBundle.message("profiler.section.$key")
}
