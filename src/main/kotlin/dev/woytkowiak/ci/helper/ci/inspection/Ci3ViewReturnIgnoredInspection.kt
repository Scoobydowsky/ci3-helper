package dev.woytkowiak.ci.helper.ci.inspection

import com.intellij.codeInspection.LocalInspectionTool
import com.intellij.codeInspection.ProblemHighlightType
import com.intellij.codeInspection.ProblemsHolder
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiElementVisitor
import com.intellij.psi.util.PsiTreeUtil
import com.jetbrains.php.lang.psi.elements.MethodReference
import com.jetbrains.php.lang.psi.elements.Statement
import com.jetbrains.php.lang.psi.visitors.PhpElementVisitor
import dev.woytkowiak.ci.helper.ci.Ci3PluginState

/**
 * Warns when `$this->load->view()` is called with third parameter TRUE (return as string),
 * but the return value is ignored (not assigned, returned or echoed).
 */
class Ci3ViewReturnIgnoredInspection : LocalInspectionTool() {

    override fun getStaticDescription(): String? =
        """
        Reports `$this->load->view()` calls where the third argument is `TRUE` (return as string),
        but the returned string is not used (e.g. not assigned to a variable, not concatenated or returned).
        """.trimIndent()

    override fun buildVisitor(holder: ProblemsHolder, isOnTheFly: Boolean): PsiElementVisitor {
        if (!Ci3PluginState.getInstance().isEnabled) return PsiElementVisitor.EMPTY_VISITOR

        return object : PhpElementVisitor() {
            override fun visitPhpMethodReference(reference: MethodReference) {
                val methodName = reference.name ?: return
                if (methodName != "view") return
                if (!isLoadViewCall(reference)) return

                val params = reference.parameterList ?: return
                val args = params.parameters
                if (args.size < 3) return

                val third = args[2]
                if (!isTrueLiteral(third)) return

                if (isResultUsed(reference)) return

                val message = "CI3 load->view() is called with return = TRUE but the returned string is not used."
                holder.registerProblem(
                    reference as PsiElement,
                    message,
                    ProblemHighlightType.WEAK_WARNING
                )
            }
        }
    }

    private fun isLoadViewCall(reference: MethodReference): Boolean {
        val classRef = reference.classReference as? MethodReference ?: return false
        if (classRef.name != "load") return false
        val root = classRef.classReference as? com.jetbrains.php.lang.psi.elements.Variable ?: return false
        return root.name == "this"
    }

    private fun isTrueLiteral(element: PsiElement): Boolean {
        val text = element.text.trim().lowercase()
        if (text == "true") return true
        return false
    }

    /**
     * Checks whether the result of the method call is used in an expression
     * (assigned, returned, concatenated, etc.). If the call is a top-level
     * statement, we consider it ignored.
     */
    private fun isResultUsed(reference: MethodReference): Boolean {
        val stmt = PsiTreeUtil.getParentOfType(reference, Statement::class.java, false) ?: return true
        val text = stmt.text
        if (text.contains("=")) return true
        if (text.contains("return ")) return true
        if (text.contains("echo ") || text.contains("print ")) return true

        return false
    }
}

