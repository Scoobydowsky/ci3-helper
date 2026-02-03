package dev.woytkowiak.ci.helper.ci.settings

import com.intellij.ide.BrowserUtil
import com.intellij.openapi.options.Configurable
import dev.woytkowiak.ci.helper.ci.Ci3Support
import dev.woytkowiak.ci.helper.MyBundle
import java.awt.FlowLayout
import javax.swing.JButton
import javax.swing.JComponent
import javax.swing.JPanel

/**
 * Settings page: Tools → CodeIgniter 3 Helper with a "Support" button.
 */
class Ci3SettingsConfigurable : Configurable {

    private var panel: JPanel? = null

    override fun getDisplayName(): String = MyBundle.message("settings.ci3.displayName")

    override fun createComponent(): JComponent {
        val p = JPanel(FlowLayout(FlowLayout.LEFT))
        val button = JButton(MyBundle.message("action.support.author.text"))
        button.addActionListener { BrowserUtil.browse(Ci3Support.URL) }
        p.add(button)
        panel = p
        return p
    }

    override fun isModified(): Boolean = false
    override fun apply() = Unit
    override fun reset() = Unit
}
