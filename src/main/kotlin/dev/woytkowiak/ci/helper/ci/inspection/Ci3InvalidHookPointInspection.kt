package dev.woytkowiak.ci.helper.ci.inspection

import com.intellij.codeInspection.LocalInspectionTool
import com.intellij.codeInspection.ProblemHighlightType
import com.intellij.codeInspection.ProblemsHolder
import com.intellij.openapi.fileEditor.FileDocumentManager
import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiElementVisitor
import com.jetbrains.php.lang.psi.elements.StringLiteralExpression
import com.jetbrains.php.lang.psi.visitors.PhpElementVisitor
import dev.woytkowiak.ci.helper.ci.Ci3PluginState
import dev.woytkowiak.ci.helper.ci.CiHookUtils

/**
 * In application/config/hooks.php, warns when the hook point key (e.g. $hook['...'])
 * is not one of the valid CI3 hook points.
 */
class Ci3InvalidHookPointInspection : LocalInspectionTool() {

    override fun getStaticDescription(): String? =
        "In config/hooks.php, reports invalid hook point names. Valid hook points: pre_system, pre_controller, " +
        "post_controller_constructor, post_controller, display_override, cache_override, post_system."

    override fun buildVisitor(holder: ProblemsHolder, isOnTheFly: Boolean): PsiElementVisitor {
        if (!Ci3PluginState.getInstance().isEnabled) return PsiElementVisitor.EMPTY_VISITOR

        return object : PhpElementVisitor() {
            override fun visitPhpStringLiteralExpression(expression: StringLiteralExpression) {
                val file = expression.containingFile ?: return
                if (file.name != "hooks.php") return

                val value = expression.contents.trim()
                if (value.isEmpty() || value in CiHookUtils.HOOK_POINT_NAMES) return

                val doc = FileDocumentManager.getInstance().getDocument(file.virtualFile ?: return) ?: return
                val lineNum = doc.getLineNumber(expression.textOffset)
                val lineStart = doc.getLineStartOffset(lineNum)
                val lineEnd = doc.getLineEndOffset(lineNum)
                val line = doc.getText(TextRange(lineStart, lineEnd))
                // Only flag when this string is the key of $hook['...'], not a value in 'key' => 'value'
                val isHookPointKey = line.contains("\$hook['$value']") || line.contains("\$hook[\"$value\"]")
                if (!isHookPointKey) return

                holder.registerProblem(
                    expression,
                    "Invalid CI3 hook point '$value'. Valid: ${CiHookUtils.HOOK_POINT_NAMES.joinToString(", ")}",
                    ProblemHighlightType.GENERIC_ERROR_OR_WARNING
                )
            }
        }
    }
}
