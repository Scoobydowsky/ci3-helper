package dev.woytkowiak.ci.helper.ci.inspection

import com.intellij.codeInspection.LocalInspectionTool
import com.intellij.codeInspection.ProblemHighlightType
import com.intellij.codeInspection.ProblemsHolder
import com.intellij.psi.PsiElementVisitor
import com.jetbrains.php.lang.psi.visitors.PhpElementVisitor
import dev.woytkowiak.ci.helper.ci.Ci3PluginState

/**
 * Warns when a String helper function (random_string, increment_string, alternator, repeater,
 * reduce_double_slashes, strip_slashes, trim_slashes, reduce_multiples, quotes_to_entities, strip_quotes)
 * is used but the String helper was not loaded in the file (missing load->helper('string')).
 */
class Ci3StringHelperNotLoadedInspection : LocalInspectionTool() {

    override fun getStaticDescription(): String? =
        """
        Reports use of CodeIgniter 3 String helper functions (e.g. <code>random_string()</code>, <code>increment_string()</code>, <code>alternator()</code>)
        when the String helper has not been loaded in this file. Load the helper with <code>\$this->load->helper('string');</code> before calling these functions.
        """.trimIndent()

    override fun buildVisitor(holder: ProblemsHolder, isOnTheFly: Boolean): PsiElementVisitor {
        if (!Ci3PluginState.getInstance().isEnabled) return PsiElementVisitor.EMPTY_VISITOR

        return object : PhpElementVisitor() {
            override fun visitPhpFunctionCall(reference: com.jetbrains.php.lang.psi.elements.FunctionReference) {
                val name = reference.name ?: return
                if (name !in STRING_HELPER_FUNCTIONS) return

                val file = reference.containingFile ?: return
                val fileText = file.text
                val hasLoadString = Regex("load->helper\\s*\\(\\s*['\"]string['\"]\\s*\\)").containsMatchIn(fileText)
                if (hasLoadString) return

                holder.registerProblem(
                    reference,
                    "String helper function '$name()' used but String helper not loaded. Load with: \$this->load->helper('string');",
                    ProblemHighlightType.WARNING,
                    *emptyArray()
                )
            }
        }
    }

    companion object {
        private val STRING_HELPER_FUNCTIONS = setOf(
            "random_string",
            "increment_string",
            "alternator",
            "repeater",
            "reduce_double_slashes",
            "strip_slashes",
            "trim_slashes",
            "reduce_multiples",
            "quotes_to_entities",
            "strip_quotes"
        )
    }
}
