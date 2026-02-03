package dev.woytkowiak.ci.helper.ci

import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile

/**
 * Shared logic for CI3 views: completion, navigation, find usages.
 * Views: application/views/ (paths like users/list → users/list.php).
 */
object CiViewUtils {

    /**
     * Views directory in the project (application/views) or null.
     */
    fun getViewsDir(project: Project): VirtualFile? {
        val baseDir = project.baseDir ?: return null
        return baseDir.findChild("application")?.findChild("views")
    }

    /**
     * All view names for completion (e.g. "header", "users/list").
     */
    fun findViews(project: Project): List<String> {
        val viewsDir = getViewsDir(project) ?: return emptyList()
        val result = mutableListOf<String>()
        collectViews(viewsDir, "", result)
        return result
    }

    /**
     * View file for the given path (e.g. "users/list" → application/views/users/list.php).
     * Also handles single names (e.g. "header" → application/views/header.php).
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
