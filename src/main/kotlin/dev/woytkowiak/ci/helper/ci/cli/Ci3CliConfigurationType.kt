package dev.woytkowiak.ci.helper.ci.cli

import com.intellij.execution.configurations.ConfigurationFactory
import com.intellij.execution.configurations.ConfigurationTypeBase
import dev.woytkowiak.ci.helper.ci.Ci3Icons

class Ci3CliConfigurationType : ConfigurationTypeBase(
    ID,
    "CodeIgniter 3 CLI",
    "Run CodeIgniter 3 controllers via CLI: php index.php controller method [args...]",
    Ci3Icons.Ci3Menu
) {
    init {
        addFactory(Ci3CliConfigurationFactory(this))
    }

    override fun getConfigurationFactories(): Array<ConfigurationFactory> = super.getConfigurationFactories()

    companion object {
        const val ID = "CI3_CLI_RUN_CONFIGURATION"
    }
}

