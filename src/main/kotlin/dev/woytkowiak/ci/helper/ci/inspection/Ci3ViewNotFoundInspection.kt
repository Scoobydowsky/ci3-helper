package dev.woytkowiak.ci.helper.ci.inspection

import com.intellij.codeInspection.LocalInspectionTool
import com.intellij.codeInspection.ProblemHighlightType
import com.intellij.codeInspection.ProblemsHolder
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiElementVisitor
import com.jetbrains.php.lang.psi.elements.MethodReference
import com.jetbrains.php.lang.psi.elements.ParameterList
import com.jetbrains.php.lang.psi.visitors.PhpElementVisitor
import dev.woytkowiak.ci.helper.ci.Ci3PluginState
import dev.woytkowiak.ci.helper.ci.CiViewUtils

/**
 * Warns when `$this->load->view('path')` points to a non-existing CI3 view file under application/views.
 * Also offers a quick-fix to create the missing view file.
 */
class Ci3ViewNotFoundInspection : LocalInspectionTool() {

    override fun getStaticDescription(): String? =
        """
        Reports `$this->load->view('path')` calls whose view file does not exist under `application/views`.
        The inspection also provides a quick-fix to create the missing view file from the controller usage.
        """.trimIndent()

    override fun buildVisitor(holder: ProblemsHolder, isOnTheFly: Boolean): PsiElementVisitor {
        if (!Ci3PluginState.getInstance().isEnabled) return PsiElementVisitor.EMPTY_VISITOR

        return object : PhpElementVisitor() {
            override fun visitPhpMethodReference(reference: MethodReference) {
                val methodName = reference.name ?: return
                if (methodName != "view") return

                if (!isLoadViewCall(reference)) return

                val params = reference.parameterList ?: return
                val viewArg = params.firstPsiChild ?: return
                val viewName = normalizeViewName(viewArg.text) ?: return

                val project = reference.project
                val existing = CiViewUtils.resolveViewFile(project, viewName)
                if (existing != null) return

                registerMissingViewProblem(holder, viewArg, project, viewName)
            }
        }
    }

    private fun isLoadViewCall(reference: MethodReference): Boolean {
        val classRef = reference.classReference as? MethodReference ?: return false
        if (classRef.name != "load") return false
        val root = classRef.classReference as? com.jetbrains.php.lang.psi.elements.Variable ?: return false
        return root.name == "this"
    }

    private fun normalizeViewName(rawText: String?): String? {
        if (rawText == null) return null
        val trimmed = rawText.trim().trim('\'', '"')
        if (trimmed.isEmpty()) return null
        return trimmed
    }

    private fun registerMissingViewProblem(
        holder: ProblemsHolder,
        element: PsiElement,
        project: Project,
        viewName: String
    ) {
        val viewsDir = CiViewUtils.getViewsDir(project) ?: return
        val targetFile = viewsDir.findFileByRelativePath("$viewName.php")
        if (targetFile != null) return

        val message = "CI3 view '$viewName' not found under application/views."
        holder.registerProblem(
            element,
            message,
            ProblemHighlightType.GENERIC_ERROR_OR_WARNING,
            CreateCi3ViewQuickFix(viewName)
        )
    }
}

/**
 * Quick-fix that creates `application/views/{path}.php` for a missing CI3 view.
 * The created file uses the built-in "CI3 View.php" internal template when available.
 */
class CreateCi3ViewQuickFix(
    private val viewName: String
) : com.intellij.codeInspection.LocalQuickFix {

    override fun getFamilyName(): String = "Create CI3 view '$viewName'"

    override fun applyFix(project: Project, descriptor: com.intellij.codeInspection.ProblemDescriptor) {
        val viewsDir = CiViewUtils.getViewsDir(project) ?: return
        val targetFile = createViewFile(project, viewsDir, viewName) ?: return

        val psiFile = com.intellij.psi.PsiManager.getInstance(project).findFile(targetFile) ?: return
        val editor = com.intellij.openapi.fileEditor.FileEditorManager.getInstance(project)
        editor.openFile(targetFile, true)
        val document = com.intellij.openapi.fileEditor.FileDocumentManager.getInstance().getDocument(targetFile)
        if (document != null) {
            val offset = document.textLength
            val openedEditor = editor.selectedTextEditor
            if (openedEditor != null && openedEditor.document == document) {
                openedEditor.caretModel.moveToOffset(offset)
            }
        }
    }

    private fun createViewFile(
        project: Project,
        viewsDir: VirtualFile,
        viewPath: String
    ): VirtualFile? {
        return com.intellij.openapi.command.WriteCommandAction.writeCommandAction(project).compute<VirtualFile?, RuntimeException> {
            var dir: VirtualFile = viewsDir
            val segments = viewPath.split('/')
            for (i in 0 until segments.size - 1) {
                val segment = segments[i]
                val existing = dir.findChild(segment)
                dir = if (existing != null && existing.isDirectory) {
                    existing
                } else {
                    dir.createChildDirectory(this, segment)
                }
            }

            val fileName = segments.last() + ".php"
            val existingFile = dir.findChild(fileName)
            if (existingFile != null) {
                existingFile
            } else {
                val created = dir.createChildData(this, fileName)
                initializeViewFileFromTemplate(project, created, segments.last())
                created
            }
        }
    }

    private fun initializeViewFileFromTemplate(
        project: Project,
        file: VirtualFile,
        name: String
    ) {
        val manager = com.intellij.ide.fileTemplates.FileTemplateManager.getInstance(project)
        val template = manager.getInternalTemplate("CI3 View.php") ?: return
        val props = manager.defaultProperties.also { it["NAME"] = name }
        val text = try {
            template.getText(props)
        } catch (e: Exception) {
            "<?php defined('BASEPATH') OR exit('No direct script access allowed'); ?>\n<!-- View: $name -->\n"
        }
        val documentManager = com.intellij.openapi.fileEditor.FileDocumentManager.getInstance()
        val document = documentManager.getDocument(file)
        if (document != null) {
            document.setText(text)
            documentManager.saveDocument(document)
        } else {
            file.setBinaryContent(text.toByteArray())
        }
    }
}

