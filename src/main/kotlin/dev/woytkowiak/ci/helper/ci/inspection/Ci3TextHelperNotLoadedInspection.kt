package dev.woytkowiak.ci.helper.ci.inspection

import com.intellij.codeInspection.LocalInspectionTool
import com.intellij.codeInspection.ProblemHighlightType
import com.intellij.codeInspection.ProblemsHolder
import com.intellij.psi.PsiElementVisitor
import com.jetbrains.php.lang.psi.visitors.PhpElementVisitor
import dev.woytkowiak.ci.helper.ci.Ci3PluginState

/**
 * Warns when a Text helper function (word_limiter, character_limiter, ascii_to_entities,
 * convert_accented_characters, word_censor, highlight_code, highlight_phrase, word_wrap, ellipsize)
 * is used but the Text helper was not loaded in the file (missing load->helper('text')).
 */
class Ci3TextHelperNotLoadedInspection : LocalInspectionTool() {

    override fun getStaticDescription(): String? =
        """
        Reports use of CodeIgniter 3 Text helper functions (e.g. <code>word_limiter()</code>, <code>character_limiter()</code>, <code>ellipsize()</code>)
        when the Text helper has not been loaded in this file. Load the helper with <code>\$this->load->helper('text');</code> before calling these functions.
        """.trimIndent()

    override fun buildVisitor(holder: ProblemsHolder, isOnTheFly: Boolean): PsiElementVisitor {
        if (!Ci3PluginState.getInstance().isEnabled) return PsiElementVisitor.EMPTY_VISITOR

        return object : PhpElementVisitor() {
            override fun visitPhpFunctionCall(reference: com.jetbrains.php.lang.psi.elements.FunctionReference) {
                val name = reference.name ?: return
                if (name !in TEXT_HELPER_FUNCTIONS) return

                val file = reference.containingFile ?: return
                val fileText = file.text
                val hasLoadText = Regex("load->helper\\s*\\(\\s*['\"]text['\"]\\s*\\)").containsMatchIn(fileText)
                if (hasLoadText) return

                holder.registerProblem(
                    reference,
                    "Text helper function '$name()' used but Text helper not loaded. Load with: \$this->load->helper('text');",
                    ProblemHighlightType.WARNING,
                    *emptyArray()
                )
            }
        }
    }

    companion object {
        private val TEXT_HELPER_FUNCTIONS = setOf(
            "word_limiter",
            "character_limiter",
            "ascii_to_entities",
            "convert_accented_characters",
            "word_censor",
            "highlight_code",
            "highlight_phrase",
            "word_wrap",
            "ellipsize"
        )
    }
}
