package dev.woytkowiak.ci.helper.ci.inspection

import com.intellij.codeInspection.LocalInspectionTool
import com.intellij.codeInspection.ProblemHighlightType
import com.intellij.codeInspection.ProblemsHolder
import com.intellij.psi.PsiElementVisitor
import com.jetbrains.php.lang.psi.visitors.PhpElementVisitor
import dev.woytkowiak.ci.helper.ci.Ci3PluginState

/**
 * Warns when a URL helper function (site_url, base_url, anchor, redirect, etc.)
 * is used but the URL helper was not loaded in the file (missing load->helper('url')).
 */
class Ci3UrlHelperNotLoadedInspection : LocalInspectionTool() {

    override fun getStaticDescription(): String? =
        """
        Reports use of CodeIgniter 3 URL helper functions (e.g. <code>site_url()</code>, <code>base_url()</code>, <code>anchor()</code>, <code>redirect()</code>)
        when the URL helper has not been loaded in this file. Load the helper with <code>\$this->load->helper('url');</code> before calling these functions.
        """.trimIndent()

    override fun buildVisitor(holder: ProblemsHolder, isOnTheFly: Boolean): PsiElementVisitor {
        if (!Ci3PluginState.getInstance().isEnabled) return PsiElementVisitor.EMPTY_VISITOR

        return object : PhpElementVisitor() {
            override fun visitPhpFunctionCall(reference: com.jetbrains.php.lang.psi.elements.FunctionReference) {
                val name = reference.name ?: return
                if (name !in URL_HELPER_FUNCTIONS) return

                val file = reference.containingFile ?: return
                val fileText = file.text
                val hasLoadUrl = Regex("load->helper\\s*\\(\\s*['\"]url['\"]\\s*\\)").containsMatchIn(fileText)
                if (hasLoadUrl) return

                holder.registerProblem(
                    reference,
                    "URL helper function '$name()' used but URL helper not loaded. Load with: \$this->load->helper('url');",
                    ProblemHighlightType.WARNING,
                    *emptyArray()
                )
            }
        }
    }

    companion object {
        private val URL_HELPER_FUNCTIONS = setOf(
            "site_url",
            "base_url",
            "current_url",
            "uri_string",
            "index_page",
            "anchor",
            "anchor_popup",
            "mailto",
            "safe_mailto",
            "auto_link",
            "url_title",
            "prep_url",
            "redirect"
        )
    }
}
