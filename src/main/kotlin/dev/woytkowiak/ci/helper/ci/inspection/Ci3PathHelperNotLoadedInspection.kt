package dev.woytkowiak.ci.helper.ci.inspection

import com.intellij.codeInspection.LocalInspectionTool
import com.intellij.codeInspection.ProblemHighlightType
import com.intellij.codeInspection.ProblemsHolder
import com.intellij.psi.PsiElementVisitor
import com.jetbrains.php.lang.psi.visitors.PhpElementVisitor
import dev.woytkowiak.ci.helper.ci.Ci3PluginState

/**
 * Warns when a Path helper function (set_realpath) is used but the Path helper
 * was not loaded in the file (missing load->helper('path')).
 */
class Ci3PathHelperNotLoadedInspection : LocalInspectionTool() {

    override fun getStaticDescription(): String? =
        """
        Reports use of CodeIgniter 3 Path helper function <code>set_realpath()</code>
        when the Path helper has not been loaded in this file. Load the helper with <code>\$this->load->helper('path');</code> before calling this function.
        """.trimIndent()

    override fun buildVisitor(holder: ProblemsHolder, isOnTheFly: Boolean): PsiElementVisitor {
        if (!Ci3PluginState.getInstance().isEnabled) return PsiElementVisitor.EMPTY_VISITOR

        return object : PhpElementVisitor() {
            override fun visitPhpFunctionCall(reference: com.jetbrains.php.lang.psi.elements.FunctionReference) {
                val name = reference.name ?: return
                if (name !in PATH_HELPER_FUNCTIONS) return

                val file = reference.containingFile ?: return
                val fileText = file.text
                val hasLoadPath = Regex("load->helper\\s*\\(\\s*['\"]path['\"]\\s*\\)").containsMatchIn(fileText)
                if (hasLoadPath) return

                holder.registerProblem(
                    reference,
                    "Path helper function '$name()' used but Path helper not loaded. Load with: \$this->load->helper('path');",
                    ProblemHighlightType.WARNING,
                    *emptyArray()
                )
            }
        }
    }

    companion object {
        private val PATH_HELPER_FUNCTIONS = setOf("set_realpath")
    }
}
