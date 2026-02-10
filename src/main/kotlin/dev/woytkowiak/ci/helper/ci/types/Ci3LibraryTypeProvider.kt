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
import dev.woytkowiak.ci.helper.ci.completion.findLoadedLibraries
import dev.woytkowiak.ci.helper.ci.completion.getLibraryClassName

/**
 * When the expression is $this->customowaLibka (or zip, etc.) and load->library(...) is in the file,
 * returns the class type so PhpStorm shows parameter name hints:
 * - Native libs (zip): stub class CI_Zip from plugin stubs.
 * - Custom libs: class from application/libraries/ (e.g. Customowa_libka) so hints come from your code.
 */
class Ci3LibraryTypeProvider : PhpTypeProvider4 {

    override fun getKey(): Char = '\u00A2' // unique character

    /** Native / core property name -> stub class (no leading backslash). Benchmark is always loaded. */
    private val nativeLibraryTypes = mapOf(
        "zip" to "CI_Zip",
        "session" to "CI_Session",
        "agent" to "CI_User_agent",
        "parser" to "CI_Parser",
        "trackback" to "CI_Trackback",
        "cache" to "CI_Cache",
        "benchmark" to "CI_Benchmark",
        "unit" to "CI_Unit_test",
        "unit_test" to "CI_Unit_test"
    )

    override fun getType(element: PsiElement): PhpType? {
        if (!Ci3PluginState.getInstance().isEnabled) return null
        if (DumbService.getInstance(element.project).isDumb) return null
        val fieldRef = element as? FieldReference
            ?: PsiTreeUtil.getParentOfType(element, FieldReference::class.java)
            ?: return null
        if (!isThisFieldReference(fieldRef)) return null
        val propName = fieldRef.name ?: return null
        val fileText = fieldRef.containingFile?.text ?: return null
        val loadedLibraries = findLoadedLibraries(fileText)
        if (propName !in loadedLibraries && propName !in nativeLibraryTypes) return null
        val project = fieldRef.project
        val className = nativeLibraryTypes[propName]
            ?: getLibraryClassName(project, propName)
            ?: return null
        return PhpType.builder().add("\\$className").build()
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
