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

/**
 * In application/config/routes.php, warns when [default_controller] value contains a slash (directory).
 * CI3 docs: "You can NOT use a directory as a part of this setting!"
 * See https://codeigniter.com/userguide3/general/routing.html#reserved-routes
 */
class Ci3DefaultControllerNoDirectoryInspection : LocalInspectionTool() {

    override fun getStaticDescription(): String? =
        "In config/routes.php, reports when \$route['default_controller'] is set to a value containing a slash. " +
        "CodeIgniter 3 does not allow a directory as part of default_controller."

    override fun buildVisitor(holder: ProblemsHolder, isOnTheFly: Boolean): PsiElementVisitor {
        if (!Ci3PluginState.getInstance().isEnabled) return PsiElementVisitor.EMPTY_VISITOR

        return object : PhpElementVisitor() {
            override fun visitPhpStringLiteralExpression(expression: StringLiteralExpression) {
                val file = expression.containingFile ?: return
                if (file.name != "routes.php") return

                val value = expression.contents.trim()
                if (!value.contains("/")) return

                val doc = FileDocumentManager.getInstance().getDocument(file.virtualFile ?: return) ?: return
                val lineNum = doc.getLineNumber(expression.textOffset)
                val lineStart = doc.getLineStartOffset(lineNum)
                val lineEnd = doc.getLineEndOffset(lineNum)
                val line = doc.getText(TextRange(lineStart, lineEnd))

                if (!line.contains("default_controller") || !line.contains("\$route[") || !line.contains("=")) return
                if (!line.contains(value)) return

                holder.registerProblem(
                    expression,
                    "CI3 default_controller must not contain a directory (slash). Use controller or controller/method only.",
                    ProblemHighlightType.GENERIC_ERROR_OR_WARNING
                )
            }
        }
    }
}
