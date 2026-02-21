package dev.woytkowiak.ci.helper.ci.inspection

import com.intellij.codeInspection.LocalInspectionTool
import com.intellij.codeInspection.ProblemHighlightType
import com.intellij.codeInspection.ProblemsHolder
import com.intellij.psi.PsiElementVisitor
import com.jetbrains.php.lang.psi.visitors.PhpElementVisitor
import dev.woytkowiak.ci.helper.ci.Ci3PluginState

/**
 * Warns when an HTML helper function (heading, img, link_tag, ul, ol, meta, doctype, br, nbs)
 * is used but the HTML helper was not loaded in the file (missing load->helper('html')).
 */
class Ci3HtmlHelperNotLoadedInspection : LocalInspectionTool() {

    override fun getStaticDescription(): String? =
        """
        Reports use of CodeIgniter 3 HTML helper functions (e.g. <code>heading()</code>, <code>img()</code>, <code>link_tag()</code>)
        when the HTML helper has not been loaded in this file. Load the helper with <code>\$this->load->helper('html');</code> before calling these functions.
        """.trimIndent()

    override fun buildVisitor(holder: ProblemsHolder, isOnTheFly: Boolean): PsiElementVisitor {
        if (!Ci3PluginState.getInstance().isEnabled) return PsiElementVisitor.EMPTY_VISITOR

        return object : PhpElementVisitor() {
            override fun visitPhpFunctionCall(reference: com.jetbrains.php.lang.psi.elements.FunctionReference) {
                val name = reference.name ?: return
                if (name !in HTML_HELPER_FUNCTIONS) return

                val file = reference.containingFile ?: return
                val fileText = file.text
                val hasLoadHtml = Regex("load->helper\\s*\\(\\s*['\"]html['\"]\\s*\\)").containsMatchIn(fileText)
                if (hasLoadHtml) return

                holder.registerProblem(
                    reference,
                    "HTML helper function '$name()' used but HTML helper not loaded. Load with: \$this->load->helper('html');",
                    ProblemHighlightType.WARNING,
                    *emptyArray()
                )
            }
        }
    }

    companion object {
        private val HTML_HELPER_FUNCTIONS = setOf(
            "heading", "img", "link_tag", "ul", "ol", "meta", "doctype", "br", "nbs"
        )
    }
}
