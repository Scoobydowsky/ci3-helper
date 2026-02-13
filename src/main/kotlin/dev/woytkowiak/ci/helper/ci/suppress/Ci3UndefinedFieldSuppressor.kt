package dev.woytkowiak.ci.helper.ci.suppress

import com.intellij.codeInspection.InspectionSuppressor
import com.intellij.codeInspection.SuppressQuickFix
import com.intellij.psi.PsiElement
import com.intellij.psi.util.PsiTreeUtil
import com.jetbrains.php.lang.psi.elements.FieldReference
import com.jetbrains.php.lang.psi.elements.Variable
import dev.woytkowiak.ci.helper.ci.Ci3PluginState
import dev.woytkowiak.ci.helper.ci.CiViewUtils
import dev.woytkowiak.ci.helper.ci.completion.findLibraries
import dev.woytkowiak.ci.helper.ci.completion.findLoadedModelClasses

/**
 * Suppresses inspections in CI3 projects:
 * – **PhpUndefinedFieldInspection**: Super Object ($this->load, $this->db…), loaded libraries.
 * – **PhpUndefinedVariableInspection**: in view files (application/views/) — variables from $data.
 */
class Ci3UndefinedFieldSuppressor : InspectionSuppressor {

    private val ci3SuperObjectProperties = setOf(
        "load", "input", "db", "config", "session",
        "uri", "router", "output", "security", "form_validation",
        "benchmark"
    )

    /** Native CI3 libraries loaded with load->library('name') — property names on $this. */
    private val nativeLibraryProperties = setOf(
        "zip", "email", "pagination", "upload", "image_lib",
        "cart", "encryption", "table", "ftp", "xmlrpc",
        "javascript", "jquery", "calendar", "lang"
    )

    override fun isSuppressedFor(element: PsiElement, toolId: String): Boolean {
        if (!Ci3PluginState.getInstance().isEnabled) return false
        if (!element.containingFile?.name.orEmpty().endsWith(".php")) return false

        if (toolId == "PhpUndefinedVariableInspection") {
            return isInCi3View(element)
        }
        if (toolId != "PhpUndefinedFieldInspection") return false

        val fieldRef = PsiTreeUtil.getParentOfType(element, FieldReference::class.java)
        val name = fieldRef?.name ?: element.text ?: return false

        if (name in ci3SuperObjectProperties) {
            if (fieldRef == null) return true
            val classRef = fieldRef.classReference ?: return true
            return classRef is Variable && classRef.name == "this"
        }

        if (fieldRef != null && fieldRef.classReference is Variable && (fieldRef.classReference as Variable).name == "this") {
            if (name in nativeLibraryProperties) return true
            if (name in getLibraryPropertyNames(element.project)) return true
            val fileText = element.containingFile?.text ?: ""
            if (name in findLoadedModelClasses(fileText).keys) return true
        }
        return false
    }

    private fun isInCi3View(element: PsiElement): Boolean {
        val vFile = element.containingFile?.virtualFile ?: return false
        val viewsDir = CiViewUtils.getViewsDir(element.project) ?: return false
        return vFile.path.startsWith(viewsDir.path)
    }

    private fun getLibraryPropertyNames(project: com.intellij.openapi.project.Project): Set<String> {
        val libs = findLibraries(project)
        return libs.map { it.lowercase().replace("-", "_") }.toSet()
    }

    override fun getSuppressActions(element: PsiElement?, toolId: String): Array<SuppressQuickFix> = emptyArray()
}
