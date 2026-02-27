package dev.woytkowiak.ci.helper.ci.actions

import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.actionSystem.PlatformDataKeys
import com.intellij.psi.util.PsiTreeUtil
import com.jetbrains.php.lang.psi.elements.Method
import com.jetbrains.php.lang.psi.elements.PhpClass
import dev.woytkowiak.ci.helper.ci.guessProjectBaseDir

data class Ci3CliControllerContext(
    val controller: String,
    val method: String,
)

object Ci3CliControllerContextResolver {

    fun resolve(e: AnActionEvent): Ci3CliControllerContext? {
        val project = e.project ?: return null
        val editor = e.getData(CommonDataKeys.EDITOR) ?: return null
        val psiFile = e.getData(CommonDataKeys.PSI_FILE) ?: return null

        val element = psiFile.findElementAt(editor.caretModel.offset) ?: return null
        val method = PsiTreeUtil.getParentOfType(element, Method::class.java)
        val phpClass = method?.containingClass
            ?: PsiTreeUtil.getParentOfType(element, PhpClass::class.java)
            ?: return null

        if (!extendsCiOrMyController(phpClass)) return null

        val controllerCli = resolveControllerCliName(project.guessProjectBaseDir()?.path, phpClass)
            ?: phpClass.name?.replaceFirstChar { it.lowercase() }
            ?: return null

        val methodName = method?.name?.takeIf { it.isNotBlank() } ?: "index"

        return Ci3CliControllerContext(
            controller = controllerCli,
            method = methodName
        )
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

    private fun resolveControllerCliName(projectBasePath: String?, phpClass: PhpClass): String? {
        val vf = phpClass.containingFile?.virtualFile ?: return null
        val base = projectBasePath?.trim()?.takeIf { it.isNotBlank() } ?: return null

        // CI3 default: <base>/application/controllers/<path>/<Controller>.php
        val controllersRoot = java.io.File(base, "application/controllers").path.trimEnd('/')
        val filePath = vf.path
        if (!filePath.startsWith(controllersRoot)) return null

        val rel = filePath.removePrefix(controllersRoot).trimStart('/')
        if (!rel.endsWith(".php", ignoreCase = true)) return null

        val noExt = rel.removeSuffix(".php")
        val parts = noExt.split('/').filter { it.isNotBlank() }
        if (parts.isEmpty()) return null

        val mapped = parts.mapIndexed { idx, seg ->
            if (idx == parts.lastIndex) seg.replaceFirstChar { it.lowercase() } else seg
        }
        return mapped.joinToString("/")
    }
}

