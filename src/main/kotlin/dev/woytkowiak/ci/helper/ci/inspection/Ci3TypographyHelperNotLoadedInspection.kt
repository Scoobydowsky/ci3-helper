package dev.woytkowiak.ci.helper.ci.inspection

import com.intellij.codeInspection.LocalInspectionTool
import com.intellij.codeInspection.ProblemHighlightType
import com.intellij.codeInspection.ProblemsHolder
import com.intellij.psi.PsiElementVisitor
import com.jetbrains.php.lang.psi.visitors.PhpElementVisitor
import dev.woytkowiak.ci.helper.ci.Ci3PluginState

/**
 * Warns when a Typography helper function (auto_typography, nl2br_except_pre, entity_decode)
 * is used but the Typography helper was not loaded in the file (missing load->helper('typography')).
 */
class Ci3TypographyHelperNotLoadedInspection : LocalInspectionTool() {

    override fun getStaticDescription(): String? =
        """
        Reports use of CodeIgniter 3 Typography helper functions (e.g. <code>auto_typography()</code>, <code>nl2br_except_pre()</code>, <code>entity_decode()</code>)
        when the Typography helper has not been loaded in this file. Load the helper with <code>\$this->load->helper('typography');</code> before calling these functions.
        """.trimIndent()

    override fun buildVisitor(holder: ProblemsHolder, isOnTheFly: Boolean): PsiElementVisitor {
        if (!Ci3PluginState.getInstance().isEnabled) return PsiElementVisitor.EMPTY_VISITOR

        return object : PhpElementVisitor() {
            override fun visitPhpFunctionCall(reference: com.jetbrains.php.lang.psi.elements.FunctionReference) {
                val name = reference.name ?: return
                if (name !in TYPOGRAPHY_HELPER_FUNCTIONS) return

                val file = reference.containingFile ?: return
                val fileText = file.text
                val hasLoadTypography = Regex("load->helper\\s*\\(\\s*['\"]typography['\"]\\s*\\)").containsMatchIn(fileText)
                if (hasLoadTypography) return

                holder.registerProblem(
                    reference,
                    "Typography helper function '$name()' used but Typography helper not loaded. Load with: \$this->load->helper('typography');",
                    ProblemHighlightType.WARNING,
                    *emptyArray()
                )
            }
        }
    }

    companion object {
        private val TYPOGRAPHY_HELPER_FUNCTIONS = setOf(
            "auto_typography",
            "nl2br_except_pre",
            "entity_decode"
        )
    }
}
