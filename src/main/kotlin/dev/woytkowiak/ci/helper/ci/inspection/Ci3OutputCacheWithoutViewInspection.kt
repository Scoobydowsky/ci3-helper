package dev.woytkowiak.ci.helper.ci.inspection

import com.intellij.codeInspection.LocalInspectionTool
import com.intellij.codeInspection.ProblemHighlightType
import com.intellij.codeInspection.ProblemsHolder
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiElementVisitor
import com.intellij.psi.util.PsiTreeUtil
import com.jetbrains.php.lang.psi.elements.Method
import com.jetbrains.php.lang.psi.elements.MethodReference
import com.jetbrains.php.lang.psi.visitors.PhpElementVisitor
import dev.woytkowiak.ci.helper.ci.Ci3PluginState

/**
 * Warns when `$this->output->cache($n)` is used in a controller method that does not call
 * `$this->load->view()` anywhere. CI3 page caching only works when output is generated via a view.
 *
 * @see https://codeigniter.com/userguide3/general/caching.html
 */
class Ci3OutputCacheWithoutViewInspection : LocalInspectionTool() {

    override fun getStaticDescription(): String? =
        """
        Reports `${'$'}this->output->cache(${'$'}n)` calls in controller methods that do not call `${'$'}this->load->view()`.
        CodeIgniter 3 page caching only works when the output is generated via a view; otherwise the cache has no effect.
        """.trimIndent()

    override fun buildVisitor(holder: ProblemsHolder, isOnTheFly: Boolean): PsiElementVisitor {
        if (!Ci3PluginState.getInstance().isEnabled) return PsiElementVisitor.EMPTY_VISITOR

        return object : PhpElementVisitor() {
            override fun visitPhpMethodReference(reference: MethodReference) {
                val methodName = reference.name ?: return
                if (methodName != "cache") return
                if (!isOutputCacheCall(reference)) return

                val containingMethod = PsiTreeUtil.getParentOfType(reference, Method::class.java) ?: return
                if (methodContainsLoadView(containingMethod)) return

                val message = "CI3 output->cache() has no effect without load->view() in this method. Page caching only works with view output."
                holder.registerProblem(
                    reference as PsiElement,
                    message,
                    ProblemHighlightType.WEAK_WARNING
                )
            }
        }
    }

    private fun isOutputCacheCall(reference: MethodReference): Boolean {
        val classRef = reference.classReference as? MethodReference ?: return false
        if (classRef.name != "output") return false
        val root = classRef.classReference as? com.jetbrains.php.lang.psi.elements.Variable ?: return false
        return root.name == "this"
    }

    private fun methodContainsLoadView(method: Method): Boolean {
        var found = false
        method.accept(object : PhpElementVisitor() {
            override fun visitPhpMethodReference(ref: MethodReference) {
                if (ref.name == "view" && isLoadViewCall(ref)) found = true
                super.visitPhpMethodReference(ref)
            }
        })
        return found
    }

    private fun isLoadViewCall(reference: MethodReference): Boolean {
        val classRef = reference.classReference as? MethodReference ?: return false
        if (classRef.name != "load") return false
        val root = classRef.classReference as? com.jetbrains.php.lang.psi.elements.Variable ?: return false
        return root.name == "this"
    }
}
