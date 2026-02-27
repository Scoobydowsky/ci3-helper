package dev.woytkowiak.ci.helper.ci.inspection

import com.intellij.codeInspection.LocalInspectionTool
import com.intellij.codeInspection.ProblemHighlightType
import com.intellij.codeInspection.ProblemsHolder
import com.intellij.psi.PsiElementVisitor
import com.jetbrains.php.lang.psi.elements.Method
import com.jetbrains.php.lang.psi.elements.PhpClass
import com.jetbrains.php.lang.psi.visitors.PhpElementVisitor
import dev.woytkowiak.ci.helper.ci.Ci3PluginState

/**
 * In CodeIgniter 3 controllers, if you define __construct() you MUST call parent::__construct()
 * so that the framework initializes correctly. See:
 * https://codeigniter.com/userguide3/general/controllers.html#class-constructors
 */
class Ci3ControllerConstructorInspection : LocalInspectionTool() {

    override fun getStaticDescription(): String? =
        """
        In CodeIgniter 3 controllers, a custom <code>__construct()</code> must call <code>parent::__construct()</code>
        so that the framework initializes the Super Object. Otherwise the controller will not work correctly.
        """.trimIndent()

    override fun buildVisitor(holder: ProblemsHolder, isOnTheFly: Boolean): PsiElementVisitor {
        if (!Ci3PluginState.getInstance().isEnabled) return PsiElementVisitor.EMPTY_VISITOR

        return object : PhpElementVisitor() {
            override fun visitPhpClass(phpClass: PhpClass) {
                if (!extendsCiOrMyController(phpClass)) return
                val construct = phpClass.findMethodByName("__construct") ?: return
                if (!callsParentConstruct(construct)) {
                    holder.registerProblem(
                        construct.nameIdentifier ?: construct,
                        "CodeIgniter 3 controller constructor must call parent::__construct().",
                        ProblemHighlightType.ERROR,
                        *emptyArray()
                    )
                }
            }
        }
    }

    private fun extendsCiOrMyController(phpClass: PhpClass): Boolean {
        val direct = phpClass.superFQN ?: return false
        if (isCiOrMyController(direct)) return true
        phpClass.superClasses.forEach { superClass ->
            if (isCiOrMyController(superClass.fqn)) return true
        }
        return false
    }

    private fun isCiOrMyController(fqn: String?): Boolean {
        if (fqn == null) return false
        val n = fqn.trimStart('\\')
        return n == "CI_Controller" || n == "MY_Controller"
    }

    private fun callsParentConstruct(method: Method): Boolean {
        return method.text.contains("parent::__construct")
    }
}
