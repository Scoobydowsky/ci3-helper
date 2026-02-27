package dev.woytkowiak.ci.helper.ci.inspection

import com.intellij.codeInspection.LocalInspectionTool
import com.intellij.codeInspection.ProblemHighlightType
import com.intellij.codeInspection.ProblemsHolder
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiElementVisitor
import com.intellij.psi.PsiFile
import com.intellij.psi.util.PsiTreeUtil
import com.jetbrains.php.lang.psi.elements.Variable
import dev.woytkowiak.ci.helper.ci.Ci3PluginState
import java.util.regex.Pattern

/**
 * Warns when a variable uses a name reserved by CodeIgniter 3 ($lang, $db, $config),
 * in any scope (global or inside methods). These names conflict with the framework super object.
 */
class Ci3ReservedVariableNameInspection : LocalInspectionTool() {

    override fun getStaticDescription(): String? =
        """
        Warns when a variable uses a name reserved by CodeIgniter 3 (<code>${'$'}lang</code>, <code>${'$'}db</code>, <code>${'$'}config</code>).
        Using these names (even as local variables) may conflict with <code>\$this->config</code>, <code>\$this->db</code>, <code>\$this->lang</code>.
        """.trimIndent()

    override fun buildVisitor(holder: ProblemsHolder, isOnTheFly: Boolean): PsiElementVisitor {
        if (!Ci3PluginState.getInstance().isEnabled) return PsiElementVisitor.EMPTY_VISITOR

        val reportedOffsets = mutableSetOf<Int>()
        return object : PsiElementVisitor() {
            override fun visitElement(element: PsiElement) {
                when {
                    element is PsiFile && isPhpFile(element) -> scanFileForReservedVariables(element, holder, reportedOffsets)
                }
                element.acceptChildren(this)
            }

            private fun isPhpFile(file: PsiFile): Boolean {
                val name = file.name ?: return false
                if (name.endsWith(".php") || name.endsWith(".inc")) return true
                return file.viewProvider.languages.any { it.id == "PHP" }
            }

            private fun scanFileForReservedVariables(file: PsiFile, holder: ProblemsHolder, reported: MutableSet<Int>) {
                val text = file.text
                for (name in RESERVED_VARIABLES) {
                    val pattern = Pattern.compile("\\\$" + Pattern.quote(name) + "\\s*=")
                    val matcher = pattern.matcher(text)
                    while (matcher.find()) {
                        val offset = matcher.start()
                        if (offset in reported) continue
                        val elem = file.findElementAt(offset) ?: continue
                        val target = PsiTreeUtil.getParentOfType(elem, Variable::class.java)
                            ?: elem
                        reportVariable(target, name, holder, reported)
                    }
                }
            }

            private fun reportVariable(element: PsiElement, name: String, holder: ProblemsHolder, reported: MutableSet<Int>) {
                val offset = element.textRange.startOffset
                if (offset in reported) return
                reported.add(offset)
                holder.registerProblem(
                    element,
                    "Variable \$$name uses a name reserved by CodeIgniter 3 and may conflict with the framework.",
                    ProblemHighlightType.WARNING,
                    *emptyArray()
                )
            }
        }
    }

    companion object {
        private val RESERVED_VARIABLES = setOf("lang", "db", "config")
    }
}

