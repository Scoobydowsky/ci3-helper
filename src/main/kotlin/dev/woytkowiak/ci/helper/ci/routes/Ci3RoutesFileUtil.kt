package dev.woytkowiak.ci.helper.ci.routes

import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import dev.woytkowiak.ci.helper.ci.guessProjectBaseDir

/**
 * Locates and edits `application/config/routes.php`.
 */
object Ci3RoutesFileUtil {

    fun findRoutesFile(project: Project): VirtualFile? {
        val baseDir = project.guessProjectBaseDir() ?: return null
        return baseDir.findChild("application")
            ?.findChild("config")
            ?.findChild("routes.php")
    }

    /**
     * Returns true if a line already defines this URI pattern (same key as in $route['key']).
     */
    fun hasRoutePattern(project: Project, uriPattern: String): Boolean {
        val file = findRoutesFile(project) ?: return false
        val content = String(file.contentsToByteArray(), Charsets.UTF_8)
        val escaped = Regex.escape(uriPattern)
        val re = Regex("\\\$route\\s*\\[\\s*['\"]" + escaped + "['\"]\\s*\\]")
        return re.containsMatchIn(content)
    }

    /**
     * Inserts `$route['uri'] = 'target';` at the **end** of the file (avoids breaking multi-line comments).
     */
    fun formatRouteLine(uriPattern: String, target: String): String {
        val keyLit = phpSingleQuotedLiteral(uriPattern)
        val valLit = phpSingleQuotedLiteral(target)
        return "\$route[$keyLit] = $valLit;\n"
    }

    /** PHP single-quoted string literal content: wraps in single quotes, escapes \\ and '. */
    fun phpSingleQuotedLiteral(s: String): String {
        val escaped = s.replace("\\", "\\\\").replace("'", "\\'")
        return "'$escaped'"
    }

    fun insertRouteLine(content: String, line: String): String {
        val trimmed = content.trimEnd()
        return if (trimmed.isEmpty()) {
            line
        } else {
            trimmed + "\n" + line
        }
    }
}
