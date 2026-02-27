package dev.woytkowiak.ci.helper.ci.inspection

import com.intellij.codeInspection.LocalInspectionTool
import com.intellij.codeInspection.ProblemHighlightType
import com.intellij.codeInspection.ProblemsHolder
import com.intellij.psi.PsiElementVisitor
import com.jetbrains.php.lang.psi.elements.Function
import com.jetbrains.php.lang.psi.visitors.PhpElementVisitor
import dev.woytkowiak.ci.helper.ci.Ci3PluginState

/**
 * Reports an error when a global function is declared with a name reserved by CodeIgniter 3.
 */
class Ci3ReservedFunctionNameInspection : LocalInspectionTool() {

    override fun getStaticDescription(): String? =
        """
        Reports declaration of functions using names reserved by CodeIgniter 3
        (e.g. <code>get_instance()</code>, <code>log_message()</code>, <code>show_error()</code>).
        These functions are provided by the framework and must not be redeclared in user code.
        """.trimIndent()

    override fun buildVisitor(holder: ProblemsHolder, isOnTheFly: Boolean): PsiElementVisitor {
        if (!Ci3PluginState.getInstance().isEnabled) return PsiElementVisitor.EMPTY_VISITOR

        return object : PhpElementVisitor() {
            override fun visitPhpFunction(function: Function) {
                val name = function.name ?: return
                if (name !in RESERVED_FUNCTIONS) return

                holder.registerProblem(
                    function.nameIdentifier ?: function,
                    "Reserved CodeIgniter 3 function name '$name' – this function must not be redeclared.",
                    ProblemHighlightType.ERROR,
                    *emptyArray()
                )
            }
        }
    }

    companion object {
        private val RESERVED_FUNCTIONS = setOf(
            "_stringify_attributes",
            "_exception_handler",
            "_error_handler",
            "get_instance",
            "function_usable",
            "is_https",
            "remove_invisible_characters",
            "html_escape",
            "get_mimes",
            "set_status_header",
            "log_message",
            "show_404",
            "show_error",
            "config_item",
            "get_config",
            "is_loaded",
            "load_class",
            "is_really_writable",
            "is_php"
        )
    }
}

