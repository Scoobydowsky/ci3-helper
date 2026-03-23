package dev.woytkowiak.ci.helper.ci.statusbar

import com.intellij.openapi.Disposable
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.IconLoader
import com.intellij.openapi.util.NlsContexts
import com.intellij.openapi.vfs.VirtualFileManager
import com.intellij.openapi.vfs.newvfs.BulkFileListener
import com.intellij.openapi.vfs.newvfs.events.VFileEvent
import com.intellij.openapi.wm.CustomStatusBarWidget
import com.intellij.openapi.wm.StatusBarWidget
import com.intellij.ui.JBColor
import com.intellij.ui.components.JBLabel
import com.intellij.util.ui.JBUI
import com.intellij.util.ui.UIUtil
import dev.woytkowiak.ci.helper.MyBundle
import dev.woytkowiak.ci.helper.ci.Ci3Icons
import dev.woytkowiak.ci.helper.ci.Ci3IndexPhpEnvironment
import dev.woytkowiak.ci.helper.ci.Ci3PluginState
import dev.woytkowiak.ci.helper.ci.Ci3Support
import dev.woytkowiak.ci.helper.ci.guessProjectBaseDir
import java.awt.BorderLayout
import java.awt.Cursor
import java.awt.Dimension
import java.awt.FlowLayout
import java.awt.event.MouseAdapter
import java.awt.event.MouseEvent
import javax.swing.JLabel
import javax.swing.JPanel
import javax.swing.SwingUtilities

private const val ICON_SIZE = 16

/**
 * Status bar widget: CI3 icon (click toggles plugin) + ENV pill from `index.php` (default ENVIRONMENT literal).
 */
class Ci3StatusBarWidget(private val project: Project) : JPanel(), CustomStatusBarWidget, StatusBarWidget, Disposable {

    private val state get() = Ci3PluginState.getInstance()
    private val disabledIcon = IconLoader.getDisabledIcon(Ci3Icons.Ci3)

    private val toggleOnPress = object : MouseAdapter() {
        override fun mousePressed(e: MouseEvent) {
            if (!e.isConsumed && e.button == MouseEvent.BUTTON1) {
                state.isEnabled = !state.isEnabled
                refreshAll()
                e.consume()
            }
        }
    }

    private val label = JLabel().apply {
        cursor = Cursor.getPredefinedCursor(Cursor.HAND_CURSOR)
        preferredSize = Dimension(ICON_SIZE, ICON_SIZE)
        minimumSize = Dimension(ICON_SIZE, ICON_SIZE)
        maximumSize = Dimension(ICON_SIZE, ICON_SIZE)
        isFocusable = false
        addMouseListener(toggleOnPress)
    }

    /** Fixed 16×16 hit target; padding around icon still toggles (listener on panel too). */
    private val iconPanel = JPanel(BorderLayout()).apply {
        isOpaque = false
        preferredSize = Dimension(ICON_SIZE, ICON_SIZE)
        minimumSize = Dimension(ICON_SIZE, ICON_SIZE)
        maximumSize = Dimension(ICON_SIZE, ICON_SIZE)
        cursor = Cursor.getPredefinedCursor(Cursor.HAND_CURSOR)
        add(label, BorderLayout.CENTER)
        addMouseListener(toggleOnPress)
    }

    private val envLabel = JBLabel().apply {
        border = JBUI.Borders.emptyLeft(6)
        isFocusable = false
    }

    init {
        isOpaque = false
        layout = FlowLayout(FlowLayout.LEFT, 4, 0)
        add(iconPanel)
        add(envLabel)

        project.messageBus.connect(this).subscribe(VirtualFileManager.VFS_CHANGES, object : BulkFileListener {
            override fun after(events: List<out VFileEvent>) {
                if (eventsAffectProjectIndexPhp(events)) {
                    SwingUtilities.invokeLater { refreshEnvironmentBadge() }
                }
            }
        })

        refreshAll()
    }

    private fun eventsAffectProjectIndexPhp(events: List<out VFileEvent>): Boolean {
        val base = project.guessProjectBaseDir() ?: return false
        val want = "${base.path}/index.php"
        val wantWin = "${base.path}\\index.php"
        return events.any { ev ->
            val path = ev.path
            if (path == want || path == wantWin) return@any true
            val f = ev.file
            f != null && f.name == "index.php" && (f.path == want || f.path == wantWin)
        }
    }

    private fun refreshAll() {
        label.icon = if (state.isEnabled) Ci3Icons.Ci3 else disabledIcon
        label.toolTipText = buildMainTooltip()
        refreshEnvironmentBadge()
    }

    private fun refreshEnvironmentBadge() {
        val info = Ci3IndexPhpEnvironment.readInfo(project)
        if (info?.indexFile == null) {
            envLabel.isVisible = false
            return
        }
        envLabel.isVisible = true
        envLabel.foreground = if (state.isEnabled) {
            JBColor.namedColor("StatusBar.defaultForeground", UIUtil.getLabelForeground())
        } else {
            UIUtil.getInactiveTextColor()
        }
        if (info.parsed && info.rawValue != null) {
            envLabel.text = MyBundle.message("statusbar.ci3.env.pill", info.shortLabel)
            envLabel.toolTipText = MyBundle.message("statusbar.ci3.env.tooltip", info.rawValue)
        } else {
            envLabel.text = MyBundle.message("statusbar.ci3.env.pill.unknown")
            envLabel.toolTipText = MyBundle.message("statusbar.ci3.env.tooltip.noparse")
        }
    }

    private fun buildMainTooltip(): @NlsContexts.Tooltip String {
        return if (state.isEnabled) {
            MyBundle.message("statusbar.ci3.tooltip", Ci3Support.VERSION)
        } else {
            MyBundle.message("statusbar.ci3.tooltip.disabled", Ci3Support.VERSION)
        }
    }

    override fun dispose() {}

    override fun ID(): String = "CodeIgniter3Helper"

    @Suppress("DEPRECATION", "OVERRIDE_DEPRECATION")
    override fun getPresentation(type: StatusBarWidget.PlatformType): StatusBarWidget.WidgetPresentation =
        object : StatusBarWidget.IconPresentation {
            override fun getIcon(): javax.swing.Icon = if (state.isEnabled) Ci3Icons.Ci3 else disabledIcon
            override fun getTooltipText(): String? = buildMainTooltip()
            override fun getClickConsumer(): com.intellij.util.Consumer<MouseEvent>? = null
        }

    override fun getComponent(): JPanel = this
}
