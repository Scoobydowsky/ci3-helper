package dev.woytkowiak.ci.helper.ci

import com.intellij.ide.util.PropertiesComponent
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.components.Service

private const val KEY_ENABLED = "ci3.helper.enabled"

/**
 * Application-level state: whether the CI3 Helper plugin features are enabled.
 * Can be toggled from the status bar icon (left click) without opening Settings.
 */
@Service(Service.Level.APP)
class Ci3PluginState {

    var isEnabled: Boolean
        get() = PropertiesComponent.getInstance().getBoolean(KEY_ENABLED, true)
        set(value) {
            PropertiesComponent.getInstance().setValue(KEY_ENABLED, value, true)
        }

    companion object {
        fun getInstance(): Ci3PluginState =
            ApplicationManager.getApplication().getService(Ci3PluginState::class.java)
    }
}
