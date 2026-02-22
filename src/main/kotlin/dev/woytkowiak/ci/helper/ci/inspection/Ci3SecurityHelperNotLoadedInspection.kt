package dev.woytkowiak.ci.helper.ci.inspection

import com.intellij.codeInspection.LocalInspectionTool
import com.intellij.codeInspection.ProblemHighlightType
import com.intellij.codeInspection.ProblemsHolder
import com.intellij.psi.PsiElementVisitor
import com.jetbrains.php.lang.psi.visitors.PhpElementVisitor
import dev.woytkowiak.ci.helper.ci.Ci3PluginState

/**
 * Warns when a Security helper function (xss_clean, sanitize_filename, do_hash, strip_image_tags, encode_php_tags)
 * is used but the Security helper was not loaded in the file (missing load->helper('security')).
 */
class Ci3SecurityHelperNotLoadedInspection : LocalInspectionTool() {

    override fun getStaticDescription(): String? =
        """
        Reports use of CodeIgniter 3 Security helper functions (<code>xss_clean</code>, <code>sanitize_filename</code>,
        <code>do_hash</code>, <code>strip_image_tags</code>, <code>encode_php_tags</code>) when the Security helper
        has not been loaded in this file. Load the helper with <code>\$this->load->helper('security');</code> before calling these functions.
        """.trimIndent()

    override fun buildVisitor(holder: ProblemsHolder, isOnTheFly: Boolean): PsiElementVisitor {
        if (!Ci3PluginState.getInstance().isEnabled) return PsiElementVisitor.EMPTY_VISITOR

        return object : PhpElementVisitor() {
            override fun visitPhpFunctionCall(reference: com.jetbrains.php.lang.psi.elements.FunctionReference) {
                val name = reference.name ?: return
                if (name !in SECURITY_HELPER_FUNCTIONS) return

                val file = reference.containingFile ?: return
                val fileText = file.text
                val hasLoadSecurity = Regex("load->helper\\s*\\(\\s*['\"]security['\"]\\s*\\)").containsMatchIn(fileText)
                if (hasLoadSecurity) return

                holder.registerProblem(
                    reference,
                    "Security helper function '$name()' used but Security helper not loaded. Load with: \$this->load->helper('security');",
                    ProblemHighlightType.WARNING,
                    *emptyArray()
                )
            }
        }
    }

    companion object {
        private val SECURITY_HELPER_FUNCTIONS = setOf(
            "xss_clean",
            "sanitize_filename",
            "do_hash",
            "strip_image_tags",
            "encode_php_tags"
        )
    }
}
