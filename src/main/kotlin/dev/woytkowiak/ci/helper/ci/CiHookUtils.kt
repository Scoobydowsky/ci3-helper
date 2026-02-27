package dev.woytkowiak.ci.helper.ci

import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiManager
import dev.woytkowiak.ci.helper.ci.guessProjectBaseDir

/**
 * Shared logic for CI3 hooks: resolution, find usages.
 * Hooks are defined in application/config/hooks.php; hook files live under application/hooks/ (or custom filepath).
 */
object CiHookUtils {

    /** Valid hook point names (CI3 docs). */
    val HOOK_POINT_NAMES = listOf(
        "pre_system",
        "pre_controller",
        "post_controller_constructor",
        "post_controller",
        "display_override",
        "cache_override",
        "post_system"
    )

    /**
     * Resolve hook file: application/{filepath}/{filename}.
     * filepath is relative to application/ (e.g. "hooks" or "hooks/utilities").
     */
    fun resolveHookFile(project: Project, filepath: String, filename: String): VirtualFile? {
        val baseDir = project.guessProjectBaseDir() ?: return null
        val appDir = baseDir.findChild("application") ?: return null
        val path = filepath.trim().trimEnd('/')
        val dir = if (path.isEmpty()) appDir else appDir.findFileByRelativePath(path) ?: return null
        return dir?.findChild(filename) ?: dir?.findChild(filename.trim())
    }

    /**
     * Hooks directory (application/hooks).
     */
    fun getHooksDir(project: Project): VirtualFile? {
        val baseDir = project.guessProjectBaseDir() ?: return null
        return baseDir.findChild("application")?.findChild("hooks")
    }

    /**
     * Check if [file] is under application/hooks.
     */
    fun isHookFile(project: Project, file: VirtualFile): Boolean {
        val hooksDir = getHooksDir(project) ?: return false
        return file.path.startsWith(hooksDir.path)
    }

    /**
     * From a hook file path (e.g. application/hooks/My_hook.php), return relative path under application/
     * (e.g. "hooks/My_hook.php") and simple filename "My_hook.php".
     */
    fun getHookFilepathAndFilename(project: Project, file: VirtualFile): Pair<String, String>? {
        val baseDir = project.guessProjectBaseDir() ?: return null
        val appDir = baseDir.findChild("application") ?: return null
        val appPath = appDir.path
        if (!file.path.startsWith(appPath)) return null
        val relative = file.path.removePrefix(appPath).trimStart('/')
        val segments = relative.split("/")
        if (segments.size < 2) return null
        val filepath = segments.dropLast(1).joinToString("/")
        val filename = segments.last()
        return filepath to filename
    }

    /**
     * Parse hooks.php to find the hook definition (map of key -> value) that contains the given offset.
     * Returns map with keys 'class', 'function', 'filename', 'filepath', 'params' (string values only).
     */
    fun getContainingHookDefinition(document: com.intellij.openapi.editor.Document, offset: Int): Map<String, String>? {
        val text = document.text
        if (offset < 0 || offset > text.length) return null
        // Find innermost array( that contains offset (its closing ) is after offset)
        var pos = offset
        while (pos >= 0) {
            val idx = text.lastIndexOf("array(", pos)
            if (idx < 0) break
            val parenStart = text.indexOf("(", idx)
            val arrayEnd = findMatchingParen(text, parenStart) ?: break
            if (arrayEnd > offset) {
                val arrayContent = text.substring(parenStart + 1, arrayEnd)
                val map = mutableMapOf<String, String>()
                val keyValueRegex = Regex("['\"]([^'\"]+)['\"]\\s*=>\\s*['\"]([^'\"]*)['\"]")
                keyValueRegex.findAll(arrayContent).forEach { m ->
                    map[m.groupValues[1]] = m.groupValues[2]
                }
                return map
            }
            pos = idx - 1
        }
        return null
    }

    private fun findMatchingParen(text: String, openPos: Int): Int? {
        var depth = 1
        var i = openPos + 1
        while (i < text.length) {
            when {
                text.substring(i).startsWith("array(") -> { depth++; i += 6 }
                text[i] == '(' -> { depth++; i++ }
                text[i] == ')' -> { depth--; if (depth == 0) return i; i++ }
                else -> i++
            }
        }
        return null
    }

    /**
     * Detect which hook array key the element is the value of (by line), e.g. "filename", "filepath".
     */
    fun getHookKeyFromLine(line: String, valueText: String): String? {
        val cleanValue = valueText.trim().trim('\'', '"')
        for (key in listOf("class", "function", "filename", "filepath", "params")) {
            if (line.contains("'$key'") && (line.contains("'$cleanValue'") || line.contains("\"$cleanValue\""))) return key
            if (line.contains("\"$key\"") && (line.contains("'$cleanValue'") || line.contains("\"$cleanValue\""))) return key
        }
        return null
    }

    /**
     * Find all references to this hook file in hooks.php (entries that point to this file).
     * Returns list of PsiElement (string literals) that reference the hook file.
     */
    fun findHookUsages(project: Project, filepath: String, filename: String): List<PsiElement> {
        val baseDir = project.guessProjectBaseDir() ?: return emptyList()
        val configDir = baseDir.findChild("application")?.findChild("config") ?: return emptyList()
        val hooksVf = configDir.findChild("hooks.php") ?: return emptyList()
        val psiFile = PsiManager.getInstance(project).findFile(hooksVf) ?: return emptyList()
        val doc = com.intellij.openapi.fileEditor.FileDocumentManager.getInstance().getDocument(hooksVf) ?: return emptyList()
        val text = doc.text
        val result = mutableListOf<PsiElement>()
        val regex = Regex("['\"]filename['\"]\\s*=>\\s*['\"]([^'\"]*)['\"]")
        for (match in regex.findAll(text)) {
            val valueStart = match.range.first + match.value.indexOf(match.groupValues[1])
            val hookDef = getContainingHookDefinition(doc, valueStart) ?: continue
            val fp = hookDef["filepath"]?.trim()?.trimEnd('/') ?: "hooks"
            val fn = hookDef["filename"]?.trim() ?: continue
            if (fp != filepath || fn != filename) continue
            com.intellij.psi.util.PsiTreeUtil.findElementOfClassAtOffset(
                psiFile, valueStart, com.jetbrains.php.lang.psi.elements.StringLiteralExpression::class.java, false
            )?.let { result.add(it) }
        }
        return result
    }
}
