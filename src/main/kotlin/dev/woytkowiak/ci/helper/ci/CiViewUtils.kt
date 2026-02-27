package dev.woytkowiak.ci.helper.ci

import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiManager
import com.jetbrains.php.lang.psi.elements.MethodReference
import com.jetbrains.php.lang.psi.elements.ParameterList
import com.jetbrains.php.lang.psi.elements.StringLiteralExpression

/**
 * Shared logic for CI3 views: completion, navigation, find usages.
 * Views: application/views/ (paths like users/list → users/list.php).
 */
object CiViewUtils {

    /**
     * Views directory in the project (application/views) or null.
     */
    fun getViewsDir(project: Project): VirtualFile? {
        val baseDir = project.guessProjectBaseDir() ?: return null
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

    /**
     * View name (e.g. "users/list") for a given view file under application/views.
     */
    fun getViewNameForFile(project: Project, file: VirtualFile): String? {
        val viewsDir = getViewsDir(project) ?: return null
        val basePath = viewsDir.path
        val path = file.path
        if (!path.startsWith(basePath)) return null
        val relative = path.removePrefix(basePath).trimStart('/').removeSuffix(".php")
        return relative.ifEmpty { null }
    }

    /**
     * All `$this->load->view('name')` usages of the given view in the project.
     */
    fun findViewUsages(project: Project, viewName: String): List<PsiElement> {
        val baseDir = project.guessProjectBaseDir() ?: return emptyList()
        val result = mutableListOf<PsiElement>()

        fun scanDir(dir: VirtualFile) {
            for (child in dir.children) {
                if (child.isDirectory) {
                    scanDir(child)
                } else if (child.name.endsWith(".php")) {
                    val psiFile = PsiManager.getInstance(project).findFile(child) ?: continue
                    psiFile.accept(object : com.jetbrains.php.lang.psi.visitors.PhpElementVisitor() {
                        override fun visitPhpMethodReference(reference: MethodReference) {
                            val methodName = reference.name ?: return
                            if (methodName != "view") return
                            val params = reference.parameterList as? ParameterList ?: return
                            val first = params.parameters.firstOrNull() as? StringLiteralExpression ?: return
                            val name = first.contents.trim()
                            if (name != viewName) return
                            val classRef = reference.classReference as? MethodReference ?: return
                            if (classRef.name != "load") return
                            val root = classRef.classReference as? com.jetbrains.php.lang.psi.elements.Variable ?: return
                            if (root.name != "this") return
                            result.add(first)
                        }
                    })
                }
            }
        }

        scanDir(baseDir)
        return result
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
