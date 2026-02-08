package dev.woytkowiak.ci.helper.ci.types

import com.intellij.openapi.project.DumbService
import com.intellij.psi.PsiElement
import com.intellij.psi.util.PsiTreeUtil
import com.jetbrains.php.lang.psi.elements.FieldReference
import com.jetbrains.php.lang.psi.elements.PhpClass
import com.jetbrains.php.lang.psi.elements.Variable
import com.jetbrains.php.lang.psi.resolve.types.PhpType
import com.jetbrains.php.lang.psi.resolve.types.PhpTypeProvider4
import dev.woytkowiak.ci.helper.ci.Ci3PluginState
import dev.woytkowiak.ci.helper.ci.completion.findLoadedModelClasses

/**
 * When the expression is $this->Order_model (or alias) and Order_model is loaded via load->model(),
 * returns the model class type so PhpStorm can show parameter hints from application/models/Order_model.php.
 */
class Ci3ModelTypeProvider : PhpTypeProvider4 {

    override fun getKey(): Char = '\u00A1' // unique character (different from Ci3ThisTypeProvider)

    override fun getType(element: PsiElement): PhpType? {
        if (!Ci3PluginState.getInstance().isEnabled) return null
        if (DumbService.getInstance(element.project).isDumb) return null
        val fieldRef = element as? FieldReference
            ?: PsiTreeUtil.getParentOfType(element, FieldReference::class.java)
            ?: return null
        if (!isThisFieldReference(fieldRef)) return null
        val propName = fieldRef.name ?: return null
        val fileText = fieldRef.containingFile?.text ?: return null
        val loadedModels = findLoadedModelClasses(fileText)
        val modelClassName = loadedModels[propName] ?: return null
        return PhpType.builder().add("\\$modelClassName").build()
    }

    override fun complete(expression: String, project: com.intellij.openapi.project.Project): PhpType =
        PhpType.EMPTY

    override fun getBySignature(
        expression: String,
        visited: MutableSet<String>,
        depth: Int,
        project: com.intellij.openapi.project.Project
    ): Collection<com.jetbrains.php.lang.psi.elements.PhpNamedElement> = emptyList()

    private fun isThisFieldReference(fieldRef: FieldReference): Boolean {
        val classRef = fieldRef.classReference ?: return false
        if (classRef !is Variable) return false
        if (classRef.name != "this") return false
        val phpClass = PsiTreeUtil.getParentOfType(fieldRef, PhpClass::class.java) ?: return false
        return extendsCiOrMyController(phpClass)
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
