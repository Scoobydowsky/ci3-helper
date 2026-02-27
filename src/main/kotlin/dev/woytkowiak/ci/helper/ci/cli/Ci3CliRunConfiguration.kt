package dev.woytkowiak.ci.helper.ci.cli

import com.intellij.execution.Executor
import com.intellij.execution.configurations.RunConfigurationBase
import com.intellij.execution.configurations.RunConfigurationOptions
import com.intellij.execution.configurations.RunProfileState
import com.intellij.execution.runners.ExecutionEnvironment
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.JDOMExternalizerUtil
import com.intellij.util.execution.ParametersListUtil
import dev.woytkowiak.ci.helper.ci.guessProjectBaseDir
import com.intellij.openapi.options.SettingsEditor
import com.intellij.execution.configurations.RunConfiguration

class Ci3CliRunConfiguration(
    project: Project,
    factory: Ci3CliConfigurationFactory,
    name: String
) : RunConfigurationBase<RunConfigurationOptions>(project, factory, name) {

    var phpPath: String = ""
    var workingDirectory: String = ""
    var indexPhpPath: String = "index.php"
    var controller: String = ""
    var method: String = "index"
    var args: String = ""

    override fun getConfigurationEditor(): SettingsEditor<out RunConfiguration> =
        Ci3CliSettingsEditor(project)

    override fun getState(executor: Executor, environment: ExecutionEnvironment): RunProfileState {
        val resolvedWorkingDir = workingDirectory.trim().ifBlank {
            project.guessProjectBaseDir()?.path.orEmpty()
        }
        val phpExe = phpPath.trim().ifBlank { "php" }
        val index = indexPhpPath.trim().ifBlank { "index.php" }
        val ctrl = controller.trim()
        val m = method.trim().ifBlank { "index" }
        val extraArgs = ParametersListUtil.parse(args.trim())

        return Ci3CliCommandLineState(
            environment = environment,
            phpExe = phpExe,
            workingDir = resolvedWorkingDir,
            indexPhp = index,
            controller = ctrl,
            method = m,
            args = extraArgs
        )
    }

    /**
     * Compatibility for older IDE builds that might still call legacy externalization.
     * The actual data is stored in [Ci3CliRunConfigurationOptions].
     */
    override fun readExternal(element: org.jdom.Element) {
        super.readExternal(element)
        phpPath = JDOMExternalizerUtil.readField(element, "ci3.phpPath").orEmpty()
        workingDirectory = JDOMExternalizerUtil.readField(element, "ci3.workingDirectory").orEmpty()
        indexPhpPath = JDOMExternalizerUtil.readField(element, "ci3.indexPhpPath") ?: "index.php"
        controller = JDOMExternalizerUtil.readField(element, "ci3.controller").orEmpty()
        method = JDOMExternalizerUtil.readField(element, "ci3.method") ?: "index"
        args = JDOMExternalizerUtil.readField(element, "ci3.args").orEmpty()
    }

    override fun writeExternal(element: org.jdom.Element) {
        super.writeExternal(element)
        JDOMExternalizerUtil.writeField(element, "ci3.phpPath", phpPath)
        JDOMExternalizerUtil.writeField(element, "ci3.workingDirectory", workingDirectory)
        JDOMExternalizerUtil.writeField(element, "ci3.indexPhpPath", indexPhpPath)
        JDOMExternalizerUtil.writeField(element, "ci3.controller", controller)
        JDOMExternalizerUtil.writeField(element, "ci3.method", method)
        JDOMExternalizerUtil.writeField(element, "ci3.args", args)
    }
}

