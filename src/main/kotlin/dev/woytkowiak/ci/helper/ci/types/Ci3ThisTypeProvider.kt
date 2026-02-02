package dev.woytkowiak.ci.helper.ci.types

import com.intellij.psi.PsiElement
import com.intellij.psi.util.PsiTreeUtil
import com.jetbrains.php.lang.psi.elements.PhpClass
import com.jetbrains.php.lang.psi.elements.Variable
import com.jetbrains.php.lang.psi.resolve.types.PhpType
import com.jetbrains.php.lang.psi.resolve.types.PhpTypeProvider4

/**
 * Gdy $this jest w klasie rozszerzającej CI_Controller lub MY_Controller,
 * zwracamy typ CI_Controller – PhpStorm weźmie wtedy @property ze stubu (include path).
 * Działa bez ręcznego dodawania @property do MY_Controller.
 */
class Ci3ThisTypeProvider : PhpTypeProvider4 {

    override fun getKey(): Char = '\u00A0' // unikalny znak dla tego providera

    override fun getType(element: PsiElement): PhpType? {
        if (!isThisVariable(element)) return null
        val phpClass = PsiTreeUtil.getParentOfType(element, PhpClass::class.java) ?: return null
        if (!extendsCiOrMyController(phpClass)) return null
        return PhpType.from("\\CI_Controller")
    }

    override fun complete(expression: String, project: com.intellij.openapi.project.Project): PhpType =
        PhpType.EMPTY

    override fun getBySignature(
        expression: String,
        visited: MutableSet<String>,
        depth: Int,
        project: com.intellij.openapi.project.Project
    ): Collection<com.jetbrains.php.lang.psi.elements.PhpNamedElement> = emptyList()

    private fun isThisVariable(element: PsiElement): Boolean {
        if (element is Variable) return element.name == "this"
        return element.text == "this" || element.text == "\$this"
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
}
