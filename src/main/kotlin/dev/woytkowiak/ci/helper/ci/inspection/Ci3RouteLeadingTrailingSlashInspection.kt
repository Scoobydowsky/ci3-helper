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
 * In application/config/routes.php, warns when a route key has leading or trailing slashes.
 * CI3 docs: "Do not use leading/trailing slashes."
 * See https://codeigniter.com/userguide3/general/routing.html
 */
class Ci3RouteLeadingTrailingSlashInspection : LocalInspectionTool() {

    override fun getStaticDescription(): String? =
        "In config/routes.php, reports route keys that start or end with a slash. " +
        "CodeIgniter 3 routing does not use leading/trailing slashes."

    override fun buildVisitor(holder: ProblemsHolder, isOnTheFly: Boolean): PsiElementVisitor {
        if (!Ci3PluginState.getInstance().isEnabled) return PsiElementVisitor.EMPTY_VISITOR

        return object : PhpElementVisitor() {
            override fun visitPhpStringLiteralExpression(expression: StringLiteralExpression) {
                val file = expression.containingFile ?: return
                if (file.name != "routes.php") return

                val value = expression.contents.trim()
                if (value.isEmpty()) return

                val doc = FileDocumentManager.getInstance().getDocument(file.virtualFile ?: return) ?: return
                val lineNum = doc.getLineNumber(expression.textOffset)
                val lineStart = doc.getLineStartOffset(lineNum)
                val lineEnd = doc.getLineEndOffset(lineNum)
                val line = doc.getText(TextRange(lineStart, lineEnd))

                if (!line.contains("\$route[") || !line.contains("=")) return
                val eqIdx = line.indexOf('=')
                val exprStart = expression.textOffset - lineStart
                if (exprStart >= eqIdx) return

                val hasLeading = value.startsWith("/")
                val hasTrailing = value.endsWith("/")
                if (!hasLeading && !hasTrailing) return

                val msg = when {
                    hasLeading && hasTrailing -> "Route key must not have leading or trailing slashes."
                    hasLeading -> "Route key must not have a leading slash."
                    else -> "Route key must not have a trailing slash."
                }
                holder.registerProblem(
                    expression,
                    "CI3 routing: $msg",
                    ProblemHighlightType.GENERIC_ERROR_OR_WARNING
                )
            }
        }
    }
}
