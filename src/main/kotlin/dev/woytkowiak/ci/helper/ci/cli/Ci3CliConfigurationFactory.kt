package dev.woytkowiak.ci.helper.ci.cli

import com.intellij.execution.configurations.ConfigurationFactory
import com.intellij.execution.configurations.RunConfiguration
import com.intellij.openapi.project.Project

class Ci3CliConfigurationFactory(type: Ci3CliConfigurationType) : ConfigurationFactory(type) {

    override fun getId(): String = "Ci3CliConfigurationFactory"

    override fun createTemplateConfiguration(project: Project): RunConfiguration {
        return Ci3CliRunConfiguration(project, this, "CI3 CLI")
    }
}

