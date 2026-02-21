package dev.woytkowiak.ci.helper.ci.inspection

import com.intellij.codeInspection.LocalInspectionTool
import com.intellij.codeInspection.ProblemHighlightType
import com.intellij.codeInspection.ProblemsHolder
import com.intellij.psi.PsiElementVisitor
import com.jetbrains.php.lang.psi.visitors.PhpElementVisitor
import dev.woytkowiak.ci.helper.ci.Ci3PluginState

/**
 * Warns when an Inflector helper function (singular, plural, camelize, underscore, humanize, word_is_countable)
 * is used but the Inflector helper was not loaded in the file (missing load->helper('inflector')).
 */
class Ci3InflectorHelperNotLoadedInspection : LocalInspectionTool() {

    override fun getStaticDescription(): String? =
        """
        Reports use of CodeIgniter 3 Inflector helper functions (e.g. <code>singular()</code>, <code>plural()</code>, <code>camelize()</code>)
        when the Inflector helper has not been loaded in this file. Load the helper with <code>\$this->load->helper('inflector');</code> before calling these functions.
        """.trimIndent()

    override fun buildVisitor(holder: ProblemsHolder, isOnTheFly: Boolean): PsiElementVisitor {
        if (!Ci3PluginState.getInstance().isEnabled) return PsiElementVisitor.EMPTY_VISITOR

        return object : PhpElementVisitor() {
            override fun visitPhpFunctionCall(reference: com.jetbrains.php.lang.psi.elements.FunctionReference) {
                val name = reference.name ?: return
                if (name !in INFLECTOR_HELPER_FUNCTIONS) return

                val file = reference.containingFile ?: return
                val fileText = file.text
                val hasLoadInflector = Regex("load->helper\\s*\\(\\s*['\"]inflector['\"]\\s*\\)").containsMatchIn(fileText)
                if (hasLoadInflector) return

                holder.registerProblem(
                    reference,
                    "Inflector helper function '$name()' used but Inflector helper not loaded. Load with: \$this->load->helper('inflector');",
                    ProblemHighlightType.WARNING,
                    *emptyArray()
                )
            }
        }
    }

    companion object {
        private val INFLECTOR_HELPER_FUNCTIONS = setOf(
            "singular", "plural", "camelize", "underscore", "humanize", "word_is_countable"
        )
    }
}
