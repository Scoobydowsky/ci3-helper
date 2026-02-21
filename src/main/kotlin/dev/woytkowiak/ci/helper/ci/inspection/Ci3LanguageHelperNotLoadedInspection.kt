package dev.woytkowiak.ci.helper.ci.inspection

import com.intellij.codeInspection.LocalInspectionTool
import com.intellij.codeInspection.ProblemHighlightType
import com.intellij.codeInspection.ProblemsHolder
import com.intellij.psi.PsiElementVisitor
import com.jetbrains.php.lang.psi.visitors.PhpElementVisitor
import dev.woytkowiak.ci.helper.ci.Ci3PluginState

/**
 * Warns when a Language helper function (lang) is used but the Language helper
 * was not loaded in the file (missing load->helper('language')).
 */
class Ci3LanguageHelperNotLoadedInspection : LocalInspectionTool() {

    override fun getStaticDescription(): String? =
        """
        Reports use of CodeIgniter 3 Language helper function <code>lang()</code>
        when the Language helper has not been loaded in this file. Load the helper with <code>\$this->load->helper('language');</code> before calling this function.
        """.trimIndent()

    override fun buildVisitor(holder: ProblemsHolder, isOnTheFly: Boolean): PsiElementVisitor {
        if (!Ci3PluginState.getInstance().isEnabled) return PsiElementVisitor.EMPTY_VISITOR

        return object : PhpElementVisitor() {
            override fun visitPhpFunctionCall(reference: com.jetbrains.php.lang.psi.elements.FunctionReference) {
                val name = reference.name ?: return
                if (name !in LANGUAGE_HELPER_FUNCTIONS) return

                val file = reference.containingFile ?: return
                val fileText = file.text
                val hasLoadLanguage = Regex("load->helper\\s*\\(\\s*['\"]language['\"]\\s*\\)").containsMatchIn(fileText)
                if (hasLoadLanguage) return

                holder.registerProblem(
                    reference,
                    "Language helper function '$name()' used but Language helper not loaded. Load with: \$this->load->helper('language');",
                    ProblemHighlightType.WARNING,
                    *emptyArray()
                )
            }
        }
    }

    companion object {
        private val LANGUAGE_HELPER_FUNCTIONS = setOf("lang")
    }
}
