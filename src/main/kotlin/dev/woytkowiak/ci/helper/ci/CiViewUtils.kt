package dev.woytkowiak.ci.helper.ci

import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile

/**
 * Wspólna logika dla widoków CI3: completion, nawigacja, find usages.
 * Widoki: application/views/ (ścieżki jak users/list → users/list.php).
 */
object CiViewUtils {

    /**
     * Katalog views w projekcie (application/views) lub null.
     */
    fun getViewsDir(project: Project): VirtualFile? {
        val baseDir = project.baseDir ?: return null
        return baseDir.findChild("application")?.findChild("views")
    }

    /**
     * Wszystkie nazwy widoków do completion (np. "header", "users/list").
     */
    fun findViews(project: Project): List<String> {
        val viewsDir = getViewsDir(project) ?: return emptyList()
        val result = mutableListOf<String>()
        collectViews(viewsDir, "", result)
        return result
    }

    /**
     * Plik widoku dla danej ścieżki (np. "users/list" → application/views/users/list.php).
     * Obsługuje też pojedyncze nazwy (np. "header" → application/views/header.php).
     */
    fun resolveViewFile(project: Project, viewPath: String): VirtualFile? {
        val viewsDir = getViewsDir(project) ?: return null
        val clean = viewPath.trim().trim('\'', '"')
        if (clean.isEmpty()) return null
        return viewsDir.findFileByRelativePath("$clean.php")
    }

    private fun collectViews(dir: VirtualFile, prefix: String, result: MutableList<String>) {
        for (file in dir.children) {
            if (file.isDirectory) {
                collectViews(file, "$prefix${file.name}/", result)
            } else if (file.name.endsWith(".php")) {
                result.add(prefix + file.nameWithoutExtension)
            }
        }
    }
}
