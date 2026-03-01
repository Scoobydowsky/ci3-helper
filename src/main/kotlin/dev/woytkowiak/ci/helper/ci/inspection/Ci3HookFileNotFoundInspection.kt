package dev.woytkowiak.ci.helper.ci.inspection

import com.intellij.codeInspection.LocalInspectionTool
import com.intellij.codeInspection.ProblemHighlightType
import com.intellij.codeInspection.ProblemsHolder
import com.intellij.openapi.fileEditor.FileDocumentManager
import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiElementVisitor
import com.jetbrains.php.lang.psi.elements.StringLiteralExpression
import com.jetbrains.php.lang.psi.visitors.PhpElementVisitor
import dev.woytkowiak.ci.helper.ci.Ci3PluginState
import dev.woytkowiak.ci.helper.ci.CiHookUtils

/**
 * In application/config/hooks.php, warns when a hook definition's 'filename' + 'filepath'
 * point to a non-existing file under application/.
 */
class Ci3HookFileNotFoundInspection : LocalInspectionTool() {

    override fun getStaticDescription(): String? =
        "In config/hooks.php, reports hook definitions whose file (application/{filepath}/{filename}) does not exist."

    override fun buildVisitor(holder: ProblemsHolder, isOnTheFly: Boolean): PsiElementVisitor {
        if (!Ci3PluginState.getInstance().isEnabled) return PsiElementVisitor.EMPTY_VISITOR

        return object : PhpElementVisitor() {
            override fun visitPhpStringLiteralExpression(expression: StringLiteralExpression) {
                val file = expression.containingFile ?: return
                if (file.name != "hooks.php") return

                val doc = FileDocumentManager.getInstance().getDocument(file.virtualFile ?: return) ?: return
                val lineNum = doc.getLineNumber(expression.textOffset)
                val lineStart = doc.getLineStartOffset(lineNum)
                val lineEnd = doc.getLineEndOffset(lineNum)
                val line = doc.getText(TextRange(lineStart, lineEnd))

                val valueText = expression.contents.trim()
                val key = CiHookUtils.getHookKeyFromLine(line, valueText) ?: return
                if (key != "filename") return

                val hookDef = CiHookUtils.getContainingHookDefinition(doc, expression.textOffset) ?: return
                val filepath = (hookDef["filepath"] ?: "").trim().trimEnd('/').ifEmpty { "hooks" }
                val filename = (hookDef["filename"] ?: "").trim()
                if (filename.isEmpty()) return

                val project = expression.project
                val target = CiHookUtils.resolveHookFile(project, filepath, filename)
                if (target != null) return

                holder.registerProblem(
                    expression,
                    "CI3 hook file not found: application/$filepath/$filename",
                    ProblemHighlightType.GENERIC_ERROR_OR_WARNING
                )
            }
        }
    }
}
