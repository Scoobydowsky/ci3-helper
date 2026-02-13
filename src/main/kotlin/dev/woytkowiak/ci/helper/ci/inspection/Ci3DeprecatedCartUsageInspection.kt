package dev.woytkowiak.ci.helper.ci.inspection

import com.intellij.codeInspection.LocalInspectionTool
import com.intellij.codeInspection.ProblemHighlightType
import com.intellij.codeInspection.ProblemsHolder
import com.intellij.psi.PsiElementVisitor
import com.jetbrains.php.lang.psi.elements.FieldReference
import com.jetbrains.php.lang.psi.elements.MethodReference
import com.jetbrains.php.lang.psi.elements.Variable
import com.jetbrains.php.lang.psi.visitors.PhpElementVisitor
import dev.woytkowiak.ci.helper.ci.Ci3PluginState

/**
 * Reports a warning when the deprecated CodeIgniter 3 Cart library is used
 * (e.g. `$this->cart->insert()`, `$this->cart->update()`).
 * The Cart library is deprecated and kept for backwards compatibility only.
 */
class Ci3DeprecatedCartUsageInspection : LocalInspectionTool() {

    override fun getStaticDescription(): String? =
        """
        Reports usage of the deprecated CodeIgniter 3 Cart library (e.g. <code>\$this->cart->insert()</code>, <code>\$this->cart->update()</code>).
        The Cart library is deprecated and kept for backwards compatibility only.
        Consider migrating to a modern cart or session-based solution.
        """.trimIndent()

    override fun buildVisitor(holder: ProblemsHolder, isOnTheFly: Boolean): PsiElementVisitor {
        if (!Ci3PluginState.getInstance().isEnabled) return PsiElementVisitor.EMPTY_VISITOR

        return object : PhpElementVisitor() {
            override fun visitPhpMethodReference(reference: MethodReference) {
                val name = reference.name ?: return
                if (name !in CART_METHOD_NAMES) return

                val classRef = reference.classReference ?: return
                val cartFieldRef = classRef as? FieldReference ?: return
                if (cartFieldRef.name != "cart") return

                val cartClassRef = cartFieldRef.classReference ?: return
                val thisVar = cartClassRef as? Variable ?: return
                if (thisVar.name != "this") return

                holder.registerProblem(
                    reference,
                    "CodeIgniter 3 Cart library is deprecated and kept for backwards compatibility only.",
                    ProblemHighlightType.WARNING,
                    *emptyArray()
                )
            }
        }
    }

    companion object {
        private val CART_METHOD_NAMES = setOf(
            "insert", "update", "remove", "total", "total_items",
            "contents", "get_item", "has_options", "product_options",
            "format_number", "destroy"
        )
    }
}
