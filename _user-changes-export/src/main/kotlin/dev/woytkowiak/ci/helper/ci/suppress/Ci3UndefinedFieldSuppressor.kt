package dev.woytkowiak.ci.helper.ci.suppress

import com.intellij.codeInspection.InspectionSuppressor
import com.intellij.codeInspection.SuppressQuickFix
import com.intellij.psi.PsiElement
import com.intellij.psi.util.PsiTreeUtil
import com.jetbrains.php.lang.psi.elements.FieldReference
import com.jetbrains.php.lang.psi.elements.Variable
import dev.woytkowiak.ci.helper.ci.completion.findLibraries

/**
 * Wyłącza „Property not found” dla:
 * – Super Object CI3 ($this->load, $this->input, $this->db itd.),
 * – załadowanych bibliotek ($this->session, $this->my_lib po load->library('My_lib')).
 */
class Ci3UndefinedFieldSuppressor : InspectionSuppressor {

    private val ci3SuperObjectProperties = setOf(
        "load", "input", "db", "config", "session",
        "uri", "router", "output", "security", "form_validation"
    )

    override fun isSuppressedFor(element: PsiElement, toolId: String): Boolean {
        if (toolId != "PhpUndefinedFieldInspection") return false
        if (!element.containingFile?.name.orEmpty().endsWith(".php")) return false

        val fieldRef = PsiTreeUtil.getParentOfType(element, FieldReference::class.java)
        val name = fieldRef?.name ?: element.text ?: return false

        if (name in ci3SuperObjectProperties) {
            if (fieldRef == null) return true
            val classRef = fieldRef.classReference ?: return true
            return classRef is Variable && classRef.name == "this"
        }

        if (fieldRef != null && fieldRef.classReference is Variable && (fieldRef.classReference as Variable).name == "this") {
            if (name in getLibraryPropertyNames(element.project)) return true
        }
        return false
    }

    private fun getLibraryPropertyNames(project: com.intellij.openapi.project.Project): Set<String> {
        val libs = findLibraries(project)
        return libs.map { it.lowercase().replace("-", "_") }.toSet()
    }

    override fun getSuppressActions(element: PsiElement?, toolId: String): Array<SuppressQuickFix> = emptyArray()
}
