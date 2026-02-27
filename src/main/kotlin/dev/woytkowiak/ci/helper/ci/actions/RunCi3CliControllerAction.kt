package dev.woytkowiak.ci.helper.ci.actions

import com.intellij.execution.ProgramRunnerUtil
import com.intellij.execution.RunManager
import com.intellij.execution.configurations.ConfigurationTypeUtil
import com.intellij.execution.executors.DefaultRunExecutor
import com.intellij.openapi.actionSystem.ActionUpdateThread
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import dev.woytkowiak.ci.helper.MyBundle
import dev.woytkowiak.ci.helper.ci.Ci3Icons
import dev.woytkowiak.ci.helper.ci.cli.Ci3CliConfigurationType
import dev.woytkowiak.ci.helper.ci.cli.Ci3CliRunConfiguration
import dev.woytkowiak.ci.helper.ci.guessProjectBaseDir

class RunCi3CliControllerAction : AnAction(
    MyBundle.messagePointer("action.ci3.cli.run.text"),
    MyBundle.messagePointer("action.ci3.cli.run.description"),
    Ci3Icons.Ci3Menu
) {
    override fun getActionUpdateThread(): ActionUpdateThread = ActionUpdateThread.BGT

    override fun update(e: AnActionEvent) {
        e.presentation.isEnabledAndVisible = Ci3CliControllerContextResolver.resolve(e) != null
    }

    override fun actionPerformed(e: AnActionEvent) {
        val project = e.project ?: return
        val ctx = Ci3CliControllerContextResolver.resolve(e) ?: return

        val type = ConfigurationTypeUtil.findConfigurationType(Ci3CliConfigurationType::class.java)
        val factory = type.configurationFactories.firstOrNull() ?: return

        val runManager = RunManager.getInstance(project)
        val name = "CI3 CLI: ${ctx.controller}/${ctx.method}"
        val settings = runManager.createConfiguration(name, factory).apply { isTemporary = true }
        val configuration = settings.configuration as? Ci3CliRunConfiguration ?: return

        configuration.workingDirectory = project.guessProjectBaseDir()?.path.orEmpty()
        configuration.indexPhpPath = "index.php"
        configuration.controller = ctx.controller
        configuration.method = ctx.method

        ProgramRunnerUtil.executeConfiguration(settings, DefaultRunExecutor.getRunExecutorInstance())
    }
}

