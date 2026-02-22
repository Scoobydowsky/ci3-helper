package dev.woytkowiak.ci.helper.ci.inspection

import com.intellij.codeInspection.LocalInspectionTool
import com.intellij.codeInspection.ProblemHighlightType
import com.intellij.codeInspection.ProblemsHolder
import com.intellij.psi.PsiElementVisitor
import com.jetbrains.php.lang.psi.visitors.PhpElementVisitor
import dev.woytkowiak.ci.helper.ci.Ci3PluginState

/**
 * Warns when a Smiley helper function (get_clickable_smileys, smiley_js, parse_smileys)
 * is used but the Smiley helper was not loaded in the file (missing load->helper('smiley')).
 */
class Ci3SmileyHelperNotLoadedInspection : LocalInspectionTool() {

    override fun getStaticDescription(): String? =
        """
        Reports use of CodeIgniter 3 Smiley helper functions (e.g. <code>get_clickable_smileys()</code>, <code>smiley_js()</code>, <code>parse_smileys()</code>)
        when the Smiley helper has not been loaded in this file. Load the helper with <code>\$this->load->helper('smiley');</code> before calling these functions.
        """.trimIndent()

    override fun buildVisitor(holder: ProblemsHolder, isOnTheFly: Boolean): PsiElementVisitor {
        if (!Ci3PluginState.getInstance().isEnabled) return PsiElementVisitor.EMPTY_VISITOR

        return object : PhpElementVisitor() {
            override fun visitPhpFunctionCall(reference: com.jetbrains.php.lang.psi.elements.FunctionReference) {
                val name = reference.name ?: return
                if (name !in SMILEY_HELPER_FUNCTIONS) return

                val file = reference.containingFile ?: return
                val fileText = file.text
                val hasLoadSmiley = Regex("load->helper\\s*\\(\\s*['\"]smiley['\"]\\s*\\)").containsMatchIn(fileText)
                if (hasLoadSmiley) return

                holder.registerProblem(
                    reference,
                    "Smiley helper function '$name()' used but Smiley helper not loaded. Load with: \$this->load->helper('smiley');",
                    ProblemHighlightType.WARNING,
                    *emptyArray()
                )
            }
        }
    }

    companion object {
        private val SMILEY_HELPER_FUNCTIONS = setOf(
            "get_clickable_smileys", "smiley_js", "parse_smileys"
        )
    }
}
