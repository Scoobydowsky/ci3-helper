package dev.woytkowiak.ci.helper.ci.inspection

import com.intellij.codeInspection.LocalInspectionTool
import com.intellij.codeInspection.ProblemHighlightType
import com.intellij.codeInspection.ProblemsHolder
import com.intellij.psi.PsiElementVisitor
import com.jetbrains.php.lang.psi.visitors.PhpElementVisitor
import dev.woytkowiak.ci.helper.ci.Ci3PluginState

/**
 * Warns when an XML helper function (xml_convert) is used but the XML helper
 * was not loaded in the file (missing load->helper('xml')).
 */
class Ci3XmlHelperNotLoadedInspection : LocalInspectionTool() {

    override fun getStaticDescription(): String? =
        """
        Reports use of CodeIgniter 3 XML helper functions (e.g. <code>xml_convert()</code>)
        when the XML helper has not been loaded in this file. Load the helper with <code>\$this->load->helper('xml');</code> before calling these functions.
        """.trimIndent()

    override fun buildVisitor(holder: ProblemsHolder, isOnTheFly: Boolean): PsiElementVisitor {
        if (!Ci3PluginState.getInstance().isEnabled) return PsiElementVisitor.EMPTY_VISITOR

        return object : PhpElementVisitor() {
            override fun visitPhpFunctionCall(reference: com.jetbrains.php.lang.psi.elements.FunctionReference) {
                val name = reference.name ?: return
                if (name !in XML_HELPER_FUNCTIONS) return

                val file = reference.containingFile ?: return
                val fileText = file.text
                val hasLoadXml = Regex("load->helper\\s*\\(\\s*['\"]xml['\"]\\s*\\)").containsMatchIn(fileText)
                if (hasLoadXml) return

                holder.registerProblem(
                    reference,
                    "XML helper function '$name()' used but XML helper not loaded. Load with: \$this->load->helper('xml');",
                    ProblemHighlightType.WARNING,
                    *emptyArray()
                )
            }
        }
    }

    companion object {
        private val XML_HELPER_FUNCTIONS = setOf(
            "xml_convert"
        )
    }
}
