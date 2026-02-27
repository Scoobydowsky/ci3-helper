package dev.woytkowiak.ci.helper.ci.inspection

import com.intellij.codeInspection.LocalInspectionTool
import com.intellij.codeInspection.LocalQuickFix
import com.intellij.codeInspection.ProblemDescriptor
import com.intellij.codeInspection.ProblemHighlightType
import com.intellij.codeInspection.ProblemsHolder
import com.intellij.openapi.command.WriteCommandAction
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiDocumentManager
import com.intellij.psi.PsiElementVisitor
import com.jetbrains.php.lang.psi.elements.Method
import com.jetbrains.php.lang.psi.elements.PhpClass
import com.jetbrains.php.lang.psi.visitors.PhpElementVisitor
import dev.woytkowiak.ci.helper.ci.Ci3PluginState

class Ci3CliOnlyControllerShouldCheckIsCliInspection : LocalInspectionTool() {

    override fun getStaticDescription(): String? =
        """
        CLI-only controllers and tasks should be protected with an <code>is_cli()</code> guard
        so they cannot be executed from a browser request.
        """.trimIndent()

    override fun buildVisitor(holder: ProblemsHolder, isOnTheFly: Boolean): PsiElementVisitor {
        if (!Ci3PluginState.getInstance().isEnabled) return PsiElementVisitor.EMPTY_VISITOR

        return object : PhpElementVisitor() {
            override fun visitPhpClass(phpClass: PhpClass) {
                if (!extendsCiOrMyController(phpClass)) return
                if (!looksLikeCliOnlyController(phpClass)) return

                for (method in phpClass.methods) {
                    if (method.name == "__construct") continue
                    if (!method.text.contains("{")) continue // skip abstract/interface stubs
                    if (method.text.contains("is_cli")) continue

                    holder.registerProblem(
                        method.nameIdentifier ?: method,
                        "CLI-only controller method should guard execution with is_cli().",
                        ProblemHighlightType.WARNING,
                        AddIsCliGuardQuickFix()
                    )
                }
            }
        }
    }

    private fun extendsCiOrMyController(phpClass: PhpClass): Boolean {
        val direct = phpClass.superFQN
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

    private fun looksLikeCliOnlyController(phpClass: PhpClass): Boolean {
        val name = phpClass.name.orEmpty()
        val path = phpClass.containingFile?.virtualFile?.path.orEmpty().replace('\\', '/')

        if (path.contains("/application/controllers/cli/")) return true
        return listOf("Cron", "Cli", "Task", "Tools").any { name.contains(it, ignoreCase = true) }
    }
}

private class AddIsCliGuardQuickFix : LocalQuickFix {
    override fun getFamilyName(): String = "Add is_cli() guard"

    override fun applyFix(project: Project, descriptor: ProblemDescriptor) {
        val method = descriptor.psiElement.parent as? Method
            ?: descriptor.psiElement as? Method
            ?: return
        if (method.text.contains("is_cli")) return

        val file = method.containingFile ?: return
        val document = PsiDocumentManager.getInstance(project).getDocument(file) ?: return

        val methodText = method.text
        val braceIndex = methodText.indexOf('{')
        if (braceIndex < 0) return

        val insertOffset = method.textRange.startOffset + braceIndex + 1

        val startLine = document.getLineNumber(method.textRange.startOffset)
        val lineStartOffset = document.getLineStartOffset(startLine)
        val prefix = document.text.substring(lineStartOffset, method.textRange.startOffset)
        val indent = prefix.takeWhile { it == ' ' || it == '\t' }
        val bodyIndent = indent + "    "

        val guard = "\n${bodyIndent}if ( ! is_cli()) { show_404(); }\n"

        WriteCommandAction.runWriteCommandAction(project) {
            document.insertString(insertOffset, guard)
            PsiDocumentManager.getInstance(project).commitDocument(document)
        }
    }
}

