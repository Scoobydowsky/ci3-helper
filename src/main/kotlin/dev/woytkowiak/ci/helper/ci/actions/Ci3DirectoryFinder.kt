package dev.woytkowiak.ci.helper.ci.actions

import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.LocalFileSystem
import dev.woytkowiak.ci.helper.ci.guessProjectBaseDir
import com.intellij.psi.PsiDirectory
import com.intellij.psi.PsiManager
import java.io.File

/**
 * Finds default CodeIgniter 3 directories: application/controllers, application/models, application/views, application/libraries.
 * If CI3 structure is not found, returns the directory chosen by the user.
 */
object Ci3DirectoryFinder {

    private const val APPLICATION = "application"
    private val CI3_FOLDERS = listOf("controllers", "models", "views", "libraries")

    /**
     * @param folderName "controllers", "models", "views" or "libraries"
     * @return target directory (e.g. application/controllers) or fallback when not found
     */
    fun findDefaultDirectory(project: Project, fallback: PsiDirectory, folderName: String): PsiDirectory {
        if (folderName !in CI3_FOLDERS) return fallback
        val basePath = project.guessProjectBaseDir()?.path ?: return fallback
        val vfs = LocalFileSystem.getInstance()

        // application/controllers, application/models, application/views
        val applicationDir = File(basePath, APPLICATION)
        if (applicationDir.isDirectory) {
            val target = File(applicationDir, folderName)
            if (target.isDirectory) {
                vfs.refreshAndFindFileByIoFile(target)?.let { vf ->
                    PsiManager.getInstance(project).findDirectory(vf)?.let { return it }
                }
            }
        }

        // controllers, models, views in project root
        val rootFolder = File(basePath, folderName)
        if (rootFolder.isDirectory) {
            vfs.refreshAndFindFileByIoFile(rootFolder)?.let { vf ->
                PsiManager.getInstance(project).findDirectory(vf)?.let { return it }
            }
        }

        return fallback
    }
}
