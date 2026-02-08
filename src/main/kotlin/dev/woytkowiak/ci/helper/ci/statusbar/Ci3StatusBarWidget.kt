package dev.woytkowiak.ci.helper.ci.statusbar

import com.intellij.openapi.project.Project
import com.intellij.openapi.util.IconLoader
import com.intellij.openapi.util.NlsContexts
import com.intellij.openapi.wm.CustomStatusBarWidget
import com.intellij.openapi.wm.StatusBarWidget
import com.intellij.util.Consumer
import dev.woytkowiak.ci.helper.MyBundle
import dev.woytkowiak.ci.helper.ci.Ci3Icons
import dev.woytkowiak.ci.helper.ci.Ci3PluginState
import dev.woytkowiak.ci.helper.ci.Ci3Support
import java.awt.Dimension
import java.awt.Cursor
import java.awt.FlowLayout
import java.awt.event.MouseAdapter
import java.awt.event.MouseEvent
import javax.swing.JLabel
import javax.swing.JPanel

private const val ICON_SIZE = 16

/**
 * Status bar widget: icon + tooltip. Left click toggles plugin on/off (icon grays out when disabled).
 * Right click shows the IDE's status bar menu (widget list). Support author: Help → Support author.
 */
class Ci3StatusBarWidget(private val project: Project) : JPanel(), CustomStatusBarWidget, StatusBarWidget {

    private val state get() = Ci3PluginState.getInstance()
    private val disabledIcon = IconLoader.getDisabledIcon(Ci3Icons.Ci3)

    private val label = JLabel().apply {
        cursor = Cursor.getPredefinedCursor(Cursor.HAND_CURSOR)
        preferredSize = Dimension(ICON_SIZE, ICON_SIZE)
        minimumSize = Dimension(ICON_SIZE, ICON_SIZE)
        maximumSize = Dimension(ICON_SIZE, ICON_SIZE)
        addMouseListener(object : MouseAdapter() {
            override fun mouseClicked(e: MouseEvent) {
                if (e.button == MouseEvent.BUTTON1) {
                    state.isEnabled = !state.isEnabled
                    updateIconAndTooltip()
                }
            }
        })
    }

    init {
        isOpaque = false
        layout = FlowLayout(FlowLayout.CENTER, 0, 0)
        preferredSize = Dimension(ICON_SIZE, ICON_SIZE)
        minimumSize = Dimension(ICON_SIZE, ICON_SIZE)
        maximumSize = Dimension(ICON_SIZE, ICON_SIZE)
        add(label)
        updateIconAndTooltip()
    }

    private fun updateIconAndTooltip() {
        label.icon = if (state.isEnabled) Ci3Icons.Ci3 else disabledIcon
        label.toolTipText = buildTooltip()
    }

    private fun buildTooltip(): @NlsContexts.Tooltip String {
        return if (state.isEnabled) {
            MyBundle.message("statusbar.ci3.tooltip", Ci3Support.VERSION)
        } else {
            MyBundle.message("statusbar.ci3.tooltip.disabled", Ci3Support.VERSION)
        }
    }

    override fun ID(): String = "CodeIgniter3Helper"

    override fun getPresentation(): StatusBarWidget.WidgetPresentation = object : StatusBarWidget.IconPresentation {
        override fun getIcon(): javax.swing.Icon = if (state.isEnabled) Ci3Icons.Ci3 else disabledIcon
        override fun getTooltipText(): String? = buildTooltip()
        override fun getClickConsumer(): Consumer<MouseEvent>? = null
    }

    override fun getComponent(): JPanel = this
}
