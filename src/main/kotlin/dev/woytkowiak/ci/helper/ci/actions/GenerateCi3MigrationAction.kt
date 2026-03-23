package dev.woytkowiak.ci.helper.ci.actions

import com.intellij.openapi.actionSystem.ActionUpdateThread
import com.intellij.openapi.command.WriteCommandAction
import com.intellij.openapi.fileEditor.FileEditorManager
import com.intellij.openapi.project.DumbAwareAction
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.Messages
import com.intellij.openapi.vfs.LocalFileSystem
import com.intellij.openapi.vfs.VfsUtil
import com.intellij.psi.PsiManager
import dev.woytkowiak.ci.helper.MyBundle
import dev.woytkowiak.ci.helper.ci.Ci3Icons
import dev.woytkowiak.ci.helper.ci.Ci3PluginState
import dev.woytkowiak.ci.helper.ci.guessProjectBaseDir
import java.io.File
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

/**
 * Code -> Generate: create CI3 migration file in application/migrations.
 */
class GenerateCi3MigrationAction : DumbAwareAction(
    MyBundle.messagePointer("action.ci3.generate.migration.text"),
    MyBundle.messagePointer("action.ci3.generate.migration.description"),
    Ci3Icons.Ci3Menu
) {

    override fun getActionUpdateThread(): ActionUpdateThread = ActionUpdateThread.BGT

    override fun update(e: com.intellij.openapi.actionSystem.AnActionEvent) {
        val presentation = e.presentation
        val project = e.project
        if (!Ci3PluginState.getInstance().isEnabled || project == null) {
            presentation.isEnabledAndVisible = false
            return
        }
        presentation.isEnabledAndVisible = findApplicationDir(project) != null
    }

    override fun actionPerformed(e: com.intellij.openapi.actionSystem.AnActionEvent) {
        val project = e.project ?: return
        val applicationDir = findApplicationDir(project) ?: return

        val initialType = detectMigrationType(project)
        val dialog = Ci3MigrationDialog(project, "create_table_name", initialType)
        if (!dialog.showAndGet()) return
        val result = dialog.getResult() ?: return

        val version = when (result.migrationType) {
            Ci3MigrationType.TIMESTAMP -> LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"))
            Ci3MigrationType.SEQUENTIAL -> nextSequentialVersion(applicationDir)
        }
        val fileName = "${version}_${result.migrationName}.php"
        val className = "Migration_${toCiMigrationClassSuffix(result.migrationName)}"
        val content = buildMigrationTemplate(className)

        val migrationsDirIo = File(applicationDir, "migrations")
        WriteCommandAction.runWriteCommandAction(
            project,
            MyBundle.message("action.ci3.generate.migration.undo"),
            MyBundle.message("action.ci3.generate.migration.undo.context"),
            Runnable {
                if (!migrationsDirIo.exists()) migrationsDirIo.mkdirs()
                val target = File(migrationsDirIo, fileName)
                if (target.exists()) {
                    Messages.showWarningDialog(
                        project,
                        MyBundle.message("action.ci3.generate.migration.error.exists", fileName),
                        MyBundle.message("action.ci3.generate.migration.title")
                    )
                    return@Runnable
                }
                target.writeText(content, Charsets.UTF_8)
            }
        )

        val vf = LocalFileSystem.getInstance().refreshAndFindFileByIoFile(File(migrationsDirIo, fileName))
        if (vf != null) {
            PsiManager.getInstance(project).findFile(vf)
            FileEditorManager.getInstance(project).openFile(vf, true)
        }
    }

    private fun findApplicationDir(project: Project): File? {
        val base = project.guessProjectBaseDir()?.path ?: return null
        val app = File(base, "application")
        if (app.isDirectory) return app
        return null
    }

    private fun detectMigrationType(project: Project): Ci3MigrationType {
        val base = project.guessProjectBaseDir() ?: return Ci3MigrationType.TIMESTAMP
        val config = base.findFileByRelativePath("application/config/migration.php") ?: return Ci3MigrationType.TIMESTAMP
        val text = VfsUtil.loadText(config)
        val match = Regex("migration_type'\\]\\s*=\\s*'([^']+)'", RegexOption.IGNORE_CASE).find(text)
        val raw = match?.groupValues?.getOrNull(1)?.lowercase()
        return if (raw == "sequential") Ci3MigrationType.SEQUENTIAL else Ci3MigrationType.TIMESTAMP
    }

    private fun nextSequentialVersion(applicationDir: File): String {
        val dir = File(applicationDir, "migrations")
        if (!dir.isDirectory) return "001"
        val max = dir.listFiles()
            ?.mapNotNull { Regex("^(\\d{3})_.*\\.php$").find(it.name)?.groupValues?.getOrNull(1)?.toIntOrNull() }
            ?.maxOrNull()
            ?: 0
        return (max + 1).toString().padStart(3, '0')
    }

    private fun toCiMigrationClassSuffix(migrationName: String): String =
        migrationName.split('_')
            .filter { it.isNotBlank() }
            .joinToString("_") { part -> part.replaceFirstChar { it.uppercase() } }

    private fun buildMigrationTemplate(className: String): String = """
        |<?php
        |
        |defined('BASEPATH') OR exit('No direct script access allowed');
        |
        |class $className extends CI_Migration {
        |
        |    public function up()
        |    {
        |        // TODO: add schema changes
        |    }
        |
        |    public function down()
        |    {
        |        // TODO: rollback schema changes
        |    }
        |}
        |
    """.trimMargin()
}
