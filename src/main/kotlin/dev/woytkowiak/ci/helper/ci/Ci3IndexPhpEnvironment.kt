package dev.woytkowiak.ci.helper.ci

import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile

/**
 * Reads the default `ENVIRONMENT` value from the project `index.php` (CI3 front controller).
 *
 * Typical line:
 * `define('ENVIRONMENT', isset($_SERVER['CI_ENV']) ? $_SERVER['CI_ENV'] : 'development');`
 * Runtime may override via `$_SERVER['CI_ENV']`; this reflects the **fallback** literal in code.
 */
object Ci3IndexPhpEnvironment {

    fun readInfo(project: Project): CiEnvironmentInfo? {
        val base = project.guessProjectBaseDir() ?: return null
        val index = base.findChild("index.php") ?: return null
        val text = try {
            String(index.contentsToByteArray(), Charsets.UTF_8)
        } catch (_: Exception) {
            return CiEnvironmentInfo(
                rawValue = null,
                shortLabel = "—",
                indexFile = index,
                parsed = false
            )
        }
        val raw = parseEnvironmentLiteral(text)
        return if (raw != null) {
            CiEnvironmentInfo(
                rawValue = raw,
                shortLabel = abbreviate(raw),
                indexFile = index,
                parsed = true
            )
        } else {
            CiEnvironmentInfo(
                rawValue = null,
                shortLabel = "?",
                indexFile = index,
                parsed = false
            )
        }
    }

    /**
     * Extracts the default environment string from `index.php` source.
     */
    fun parseEnvironmentLiteral(text: String): String? {
        val defineBlock = Regex(
            """define\s*\(\s*['"]ENVIRONMENT['"]\s*,([^;]+);""",
            setOf(RegexOption.IGNORE_CASE, RegexOption.DOT_MATCHES_ALL)
        ).find(text) ?: return null
        val inner = defineBlock.groupValues[1].trim()
        // Ternary: last `: 'value'` is the fallback (e.g. after $_SERVER['CI_ENV'] : 'development')
        val colonQuoted = Regex(""":\s*['"]([^'"]+)['"]""").findAll(inner).toList()
        if (colonQuoted.isNotEmpty()) {
            colonQuoted.last().groupValues[1].trim().takeIf { it.isNotEmpty() }?.let { return it }
        }
        // Simple: define('ENVIRONMENT', 'production');
        val simple = Regex("""^\s*['"]([^'"]+)['"]\s*$""", RegexOption.MULTILINE).find(inner)
        return simple?.groupValues?.get(1)?.trim()?.takeIf { it.isNotEmpty() }
    }

    private fun abbreviate(s: String): String = when (s.lowercase()) {
        "development" -> "dev"
        "production" -> "prod"
        "testing" -> "test"
        else -> if (s.length > 10) s.take(10) + "…" else s
    }
}

data class CiEnvironmentInfo(
    /** Literal from index.php, e.g. `development`. */
    val rawValue: String?,
    /** Short text for the status bar pill. */
    val shortLabel: String,
    val indexFile: VirtualFile?,
    /** False if no `define('ENVIRONMENT'...)` was recognized. */
    val parsed: Boolean
)
