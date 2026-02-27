package dev.woytkowiak.ci.helper.ci.cli

import com.intellij.execution.configurations.CommandLineState
import com.intellij.execution.process.ProcessHandler
import com.intellij.execution.runners.ExecutionEnvironment
import com.intellij.execution.configurations.GeneralCommandLine
import com.intellij.execution.process.OSProcessHandler

class Ci3CliCommandLineState(
    environment: ExecutionEnvironment,
    private val phpExe: String,
    private val workingDir: String,
    private val indexPhp: String,
    private val controller: String,
    private val method: String,
    private val args: List<String>
) : CommandLineState(environment) {

    override fun startProcess(): ProcessHandler {
        val cmd = GeneralCommandLine()
        cmd.exePath = phpExe
        if (workingDir.isNotBlank()) cmd.withWorkDirectory(workingDir)

        val params = mutableListOf<String>()
        params += indexPhp
        if (controller.isNotBlank()) params += controller
        if (method.isNotBlank()) params += method
        params += args
        cmd.addParameters(params)

        cmd.charset = Charsets.UTF_8
        return OSProcessHandler(cmd).also { it.setShouldDestroyProcessRecursively(true) }
    }
}

