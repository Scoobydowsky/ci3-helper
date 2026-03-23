package dev.woytkowiak.ci.helper.ci.routes

import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.ComboBox
import com.intellij.openapi.ui.DialogWrapper
import com.intellij.openapi.ui.ValidationInfo
import com.intellij.ui.components.JBLabel
import com.intellij.ui.components.JBTextField
import com.intellij.util.ui.FormBuilder
import dev.woytkowiak.ci.helper.MyBundle
import java.awt.Component
import javax.swing.DefaultComboBoxModel
import javax.swing.JComponent
import javax.swing.JList
import javax.swing.JPanel
import javax.swing.ListCellRenderer

/**
 * Suggested values when opening from a tree selection (controller route or existing custom route).
 */
data class Ci3AddRouteSuggestions(
    val uriPattern: String? = null,
    val target: String? = null
)

/**
 * Dialog: URI pattern + controller and method from dropdowns → `$route[...] = ...` target string.
 */
class Ci3AddRouteDialog(
    private val dialogProject: Project,
    private val suggestions: Ci3AddRouteSuggestions?
) : DialogWrapper(dialogProject) {

    private val uriField = JBTextField(32).apply {
        toolTipText = MyBundle.message("ci3.routes.add.uri.tooltip")
    }

    private val controllerCombo = ComboBox<Ci3ControllerOption>().apply {
        isSwingPopup = false
        renderer = object : ListCellRenderer<Ci3ControllerOption> {
            private val delegate = javax.swing.DefaultListCellRenderer()
            override fun getListCellRendererComponent(
                list: JList<out Ci3ControllerOption>?,
                value: Ci3ControllerOption?,
                index: Int,
                isSelected: Boolean,
                cellHasFocus: Boolean
            ): Component {
                val c = delegate.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus)
                (c as? javax.swing.JLabel)?.text = value?.routePrefix ?: ""
                return c
            }
        }
    }

    /** First item = default (CI3 index); rest = method names. */
    private val methodCombo = ComboBox<String>().apply {
        isSwingPopup = false
        renderer = object : ListCellRenderer<String> {
            private val delegate = javax.swing.DefaultListCellRenderer()
            override fun getListCellRendererComponent(
                list: JList<out String>?,
                value: String?,
                index: Int,
                isSelected: Boolean,
                cellHasFocus: Boolean
            ): Component {
                val c = delegate.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus)
                val label = if (value == METHOD_DEFAULT) {
                    MyBundle.message("ci3.routes.add.method.default")
                } else {
                    value ?: ""
                }
                (c as? javax.swing.JLabel)?.text = label
                return c
            }
        }
    }

    private var suppressControllerChange = false

    val uriPattern: String get() = uriField.text.trim()
    val target: String get() = buildTargetString()

    init {
        title = MyBundle.message("ci3.routes.add.title")
        suggestions?.uriPattern?.let { uriField.text = it }
        init()
    }

    override fun createCenterPanel(): JComponent {
        val options = Ci3RoutesCollector.listControllerOptions(dialogProject)
        controllerCombo.model = DefaultComboBoxModel(options.toTypedArray())

        controllerCombo.addActionListener {
            if (!suppressControllerChange) {
                refillMethodsForSelectedController()
            }
        }

        suppressControllerChange = true
        try {
            refillMethodsForSelectedController()
            applyTargetSuggestion(suggestions?.target)
        } finally {
            suppressControllerChange = false
        }

        return FormBuilder.createFormBuilder()
            .addLabeledComponent(JBLabel(MyBundle.message("ci3.routes.add.uri")), uriField, 1, false)
            .addLabeledComponent(JBLabel(MyBundle.message("ci3.routes.add.controller")), controllerCombo, 1, false)
            .addLabeledComponent(JBLabel(MyBundle.message("ci3.routes.add.method")), methodCombo, 1, false)
            .addComponentFillVertically(JPanel(), 0)
            .panel
    }

    private fun refillMethodsForSelectedController() {
        val opt = controllerCombo.selectedItem as? Ci3ControllerOption ?: run {
            methodCombo.model = DefaultComboBoxModel(arrayOf(METHOD_DEFAULT))
            return
        }
        val methods = Ci3RoutesCollector.listActionMethodsForController(opt.file)
        val items = mutableListOf(METHOD_DEFAULT)
        items.addAll(methods)
        methodCombo.model = DefaultComboBoxModel(items.toTypedArray())
        methodCombo.selectedIndex = 0
    }

    private fun applyTargetSuggestion(target: String?) {
        if (target.isNullOrBlank()) return
        val options = Ci3RoutesCollector.listControllerOptions(dialogProject)
        if (options.isEmpty()) return
        val match = findBestControllerForTarget(target, options) ?: return
        suppressControllerChange = true
        try {
            controllerCombo.selectedItem = match.opt
            refillMethodsForSelectedController()
            val method = match.method
            if (method.isNullOrEmpty()) {
                methodCombo.selectedIndex = 0
            } else {
                val model = methodCombo.model
                for (i in 0 until model.size) {
                    if (model.getElementAt(i) == method) {
                        methodCombo.selectedIndex = i
                        return
                    }
                }
                methodCombo.selectedIndex = 0
            }
        } finally {
            suppressControllerChange = false
        }
    }

    private fun buildTargetString(): String {
        val opt = controllerCombo.selectedItem as? Ci3ControllerOption ?: return ""
        val method = methodCombo.selectedItem as? String ?: return opt.routePrefix
        if (method == METHOD_DEFAULT) return opt.routePrefix
        return "${opt.routePrefix}/$method"
    }

    override fun doValidate(): ValidationInfo? {
        if (uriPattern.isEmpty()) {
            return ValidationInfo(MyBundle.message("ci3.routes.add.error.uri"), uriField)
        }
        if (controllerCombo.itemCount == 0 || controllerCombo.selectedItem == null) {
            return ValidationInfo(MyBundle.message("ci3.routes.add.error.no.controllers"), controllerCombo)
        }
        val t = buildTargetString()
        if (t.isEmpty()) {
            return ValidationInfo(MyBundle.message("ci3.routes.add.error.target"), methodCombo)
        }
        if (Ci3RoutesFileUtil.hasRoutePattern(dialogProject, uriPattern)) {
            return ValidationInfo(MyBundle.message("ci3.routes.add.error.duplicate", uriPattern), uriField)
        }
        return null
    }

    companion object {
        private const val METHOD_DEFAULT = ""
    }
}

private data class TargetControllerMatch(val opt: Ci3ControllerOption, val method: String?)

/** Longest route prefix match for a CI3 target like `welcome`, `welcome/index`, `admin/users/create`. */
private fun findBestControllerForTarget(
    target: String,
    options: List<Ci3ControllerOption>
): TargetControllerMatch? {
    val sorted = options.sortedByDescending { it.routePrefix.length }
    for (opt in sorted) {
        if (target == opt.routePrefix) return TargetControllerMatch(opt, null)
        val prefix = opt.routePrefix + "/"
        if (target.startsWith(prefix)) {
            val rest = target.removePrefix(prefix)
            if (rest.isNotEmpty() && !rest.contains("/")) {
                return TargetControllerMatch(opt, rest)
            }
        }
    }
    return null
}
