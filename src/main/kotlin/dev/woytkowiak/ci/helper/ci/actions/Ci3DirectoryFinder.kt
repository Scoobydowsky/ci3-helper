package dev.woytkowiak.ci.helper.ci.actions

import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.LocalFileSystem
import com.intellij.psi.PsiDirectory
import com.intellij.psi.PsiManager
import java.io.File

/**
 * Wyszukuje domyślne katalogi CodeIgniter 3: application/controllers, application/models, application/views.
 * Jeśli nie znajdzie struktury CI3, zwraca katalog wybrany przez użytkownika.
 */
object Ci3DirectoryFinder {

    private const val APPLICATION = "application"
    private val CI3_FOLDERS = listOf("controllers", "models", "views")

    /**
     * @param folderName "controllers", "models" lub "views"
     * @return katalog docelowy (np. application/controllers) albo fallback, gdy nie znajdzie
     */
    fun findDefaultDirectory(project: Project, fallback: PsiDirectory, folderName: String): PsiDirectory {
        if (folderName !in CI3_FOLDERS) return fallback
        val basePath = project.basePath ?: return fallback
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

        // controllers, models, views w root projektu
        val rootFolder = File(basePath, folderName)
        if (rootFolder.isDirectory) {
            vfs.refreshAndFindFileByIoFile(rootFolder)?.let { vf ->
                PsiManager.getInstance(project).findDirectory(vf)?.let { return it }
            }
        }

        return fallback
    }
}
