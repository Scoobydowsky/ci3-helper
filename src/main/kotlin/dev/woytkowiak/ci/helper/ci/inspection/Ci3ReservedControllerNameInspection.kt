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
 * Reports an error when a CodeIgniter 3 controller class uses a reserved name
 * (index, Default, CI_Controller), or when a controller defines a method that
 * overrides a reserved framework method (e.g. show_404, show_error).
 */
class Ci3ReservedControllerNameInspection : LocalInspectionTool() {

    override fun getStaticDescription(): String? =
        """
        Reports usage of reserved CodeIgniter 3 controller names (<code>index</code>, <code>Default</code>, <code>CI_Controller</code>)
        and reserved method names in controllers (e.g. <code>show_404</code>, <code>show_error</code>).
        These names are reserved by the framework.
        """.trimIndent()

    override fun buildVisitor(holder: ProblemsHolder, isOnTheFly: Boolean): PsiElementVisitor {
        if (!Ci3PluginState.getInstance().isEnabled) return PsiElementVisitor.EMPTY_VISITOR

        return object : PhpElementVisitor() {
            override fun visitPhpClass(phpClass: PhpClass) {
                val name = phpClass.name ?: return

                // 1) Any class named index, Default, or CI_Controller is reserved (with or without extends)
                if (isReservedControllerName(name)) {
                    holder.registerProblem(
                        phpClass.nameIdentifier ?: phpClass,
                        "Reserved CodeIgniter 3 controller name '$name' – do not use this name for a controller.",
                        ProblemHighlightType.ERROR,
                        *emptyArray()
                    )
                }

                // 2) In any class, methods must not use reserved CI3 names (show_404, show_error, etc.)
                for (method in phpClass.methods) {
                    val methodName = method.name ?: continue
                    if (methodName in RESERVED_METHOD_NAMES) {
                        holder.registerProblem(
                            method.nameIdentifier ?: method,
                            "Reserved CodeIgniter 3 method name '$methodName' – do not use this name for a method.",
                            ProblemHighlightType.ERROR,
                            *emptyArray()
                        )
                    }
                }

                // 3) In CI controllers: method must not have the same name as the class (PHP4 constructor legacy)
                if (extendsCiOrMyController(phpClass)) {
                    val className = phpClass.name ?: return@visitPhpClass
                    for (method in phpClass.methods) {
                        val methodName = method.name ?: continue
                        if (methodName.equals(className, ignoreCase = true)) {
                            holder.registerProblem(
                                method.nameIdentifier ?: method,
                                "Do not name a controller method identically to the class name '$className' – it may be invoked as constructor (PHP4 legacy).",
                                ProblemHighlightType.ERROR,
                                *emptyArray()
                            )
                        }
                    }
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

    private fun isReservedControllerName(name: String): Boolean {
        return name.equals("index", ignoreCase = true) ||
                name.equals("default", ignoreCase = true) ||
                name.equals("CI_Controller", ignoreCase = true)
    }

    companion object {
        private val RESERVED_METHOD_NAMES = setOf(
            "_stringify_attributes", "_exception_handler", "_error_handler",
            "get_instance", "function_usable", "is_https", "remove_invisible_characters",
            "html_escape", "get_mimes", "set_status_header", "log_message", "show_404",
            "show_error", "config_item", "get_config", "is_loaded", "load_class",
            "is_really_writable", "is_php"
        )
    }
}

