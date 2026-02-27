package dev.woytkowiak.ci.helper.ci.inspection

import com.intellij.codeInspection.LocalInspectionTool
import com.intellij.codeInspection.ProblemHighlightType
import com.intellij.codeInspection.ProblemsHolder
import com.intellij.psi.PsiElementVisitor
import com.jetbrains.php.lang.psi.elements.Constant
import com.jetbrains.php.lang.psi.elements.FunctionReference
import com.jetbrains.php.lang.psi.visitors.PhpElementVisitor
import dev.woytkowiak.ci.helper.ci.Ci3PluginState

/**
 * Reports an error when a constant is declared with a name reserved by CodeIgniter 3.
 */
class Ci3ReservedConstantNameInspection : LocalInspectionTool() {

    override fun getStaticDescription(): String? =
        """
        Reports declarations of constants using names reserved by CodeIgniter 3
        (e.g. <code>CI_VERSION</code>, <code>BASEPATH</code>, <code>EXIT_ERROR</code>).
        These constants are defined by the framework and must not be redeclared in user code.
        """.trimIndent()

    override fun buildVisitor(holder: ProblemsHolder, isOnTheFly: Boolean): PsiElementVisitor {
        if (!Ci3PluginState.getInstance().isEnabled) return PsiElementVisitor.EMPTY_VISITOR

        return object : PhpElementVisitor() {
            override fun visitPhpConstant(constant: Constant) {
                val name = constant.name ?: return
                if (name !in RESERVED_CONSTANTS) return

                holder.registerProblem(
                    constant.nameIdentifier ?: constant,
                    "Reserved CodeIgniter 3 constant '$name' is being redeclared.",
                    ProblemHighlightType.ERROR,
                    *emptyArray()
                )
            }

            override fun visitPhpFunctionCall(reference: FunctionReference) {
                val functionName = reference.name ?: return
                if (!functionName.equals("define", ignoreCase = true)) return

                val parameters = reference.parameters
                if (parameters.isEmpty()) return

                val firstParam = parameters[0]
                val constName = firstParam.text.trim('\'', '"')
                if (constName !in RESERVED_CONSTANTS) return

                holder.registerProblem(
                    firstParam,
                    "Reserved CodeIgniter 3 constant '$constName' is being redeclared.",
                    ProblemHighlightType.ERROR,
                    *emptyArray()
                )
            }
        }
    }

    companion object {
        private val RESERVED_CONSTANTS = setOf(
            "EXIT__AUTO_MAX",
            "EXIT__AUTO_MIN",
            "EXIT_DATABASE",
            "EXIT_USER_INPUT",
            "EXIT_UNKNOWN_METHOD",
            "EXIT_UNKNOWN_CLASS",
            "EXIT_UNKNOWN_FILE",
            "EXIT_CONFIG",
            "EXIT_ERROR",
            "EXIT_SUCCESS",
            "SHOW_DEBUG_BACKTRACE",
            "FOPEN_READ_WRITE_CREATE_STRICT",
            "FOPEN_WRITE_CREATE_STRICT",
            "FOPEN_READ_WRITE_CREATE",
            "FOPEN_WRITE_CREATE",
            "FOPEN_READ_WRITE_CREATE_DESTRUCTIVE",
            "FOPEN_WRITE_CREATE_DESTRUCTIVE",
            "FOPEN_READ_WRITE",
            "FOPEN_READ",
            "DIR_WRITE_MODE",
            "DIR_READ_MODE",
            "FILE_WRITE_MODE",
            "FILE_READ_MODE",
            "UTF8_ENABLED",
            "ICONV_ENABLED",
            "MB_ENABLED",
            "CI_VERSION",
            "VIEWPATH",
            "APPPATH",
            "BASEPATH",
            "SELF",
            "FCPATH",
            "ENVIRONMENT"
        )
    }
}

