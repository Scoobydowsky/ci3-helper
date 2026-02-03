package dev.woytkowiak.ci.helper.ci.statusbar

import com.intellij.ide.BrowserUtil
import com.intellij.openapi.project.Project
import dev.woytkowiak.ci.helper.ci.Ci3Icons
import com.intellij.openapi.util.NlsContexts
import com.intellij.openapi.wm.CustomStatusBarWidget
import com.intellij.openapi.wm.StatusBarWidget
import com.intellij.util.Consumer
import dev.woytkowiak.ci.helper.MyBundle
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
 * Widget na pasku statusu: ikona + tooltip z wersją i "Click to support author".
 * Klik otwiera link do wsparcia w przeglądarce.
 * Rozmiar komponentu ograniczony do 16×16, żeby ikona nie rozciągała się na pasku.
 */
class Ci3StatusBarWidget(private val project: Project) : JPanel(), CustomStatusBarWidget, StatusBarWidget {

    private val label = JLabel(Ci3Icons.Ci3).apply {
        cursor = Cursor.getPredefinedCursor(Cursor.HAND_CURSOR)
        toolTipText = buildTooltip()
        preferredSize = Dimension(ICON_SIZE, ICON_SIZE)
        minimumSize = Dimension(ICON_SIZE, ICON_SIZE)
        maximumSize = Dimension(ICON_SIZE, ICON_SIZE)
        addMouseListener(object : MouseAdapter() {
            override fun mouseClicked(e: MouseEvent) {
                if (e.button == MouseEvent.BUTTON1) BrowserUtil.browse(Ci3Support.URL)
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
    }

    private fun buildTooltip(): @NlsContexts.Tooltip String {
        return MyBundle.message("statusbar.ci3.tooltip", Ci3Support.VERSION)
    }

    override fun ID(): String = "CodeIgniter3Helper"

    override fun getPresentation(): StatusBarWidget.WidgetPresentation = object : StatusBarWidget.IconPresentation {
        override fun getIcon(): javax.swing.Icon = Ci3Icons.Ci3
        override fun getTooltipText(): String? = buildTooltip()
        override fun getClickConsumer(): Consumer<MouseEvent>? = Consumer { BrowserUtil.browse(Ci3Support.URL) }
    }

    override fun getComponent(): JPanel = this
}
