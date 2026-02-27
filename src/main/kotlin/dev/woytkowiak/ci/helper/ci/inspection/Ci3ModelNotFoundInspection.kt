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
import dev.woytkowiak.ci.helper.ci.completion.resolveModelFile
import dev.woytkowiak.ci.helper.ci.guessProjectBaseDir

/**
 * Warns when `$this->load->model('path')` points to a non-existing CI3 model file under application/models.
 * Also offers a quick-fix to create the missing model file.
 */
class Ci3ModelNotFoundInspection : LocalInspectionTool() {

    override fun getStaticDescription(): String? =
        """
        Reports `$this->load->model('path')` calls whose model file does not exist under `application/models`.
        The inspection also provides a quick-fix to create the missing model file (CI_Model template).
        """.trimIndent()

    override fun buildVisitor(holder: ProblemsHolder, isOnTheFly: Boolean): PsiElementVisitor {
        if (!Ci3PluginState.getInstance().isEnabled) return PsiElementVisitor.EMPTY_VISITOR

        return object : PhpElementVisitor() {
            override fun visitPhpMethodReference(reference: MethodReference) {
                val methodName = reference.name ?: return
                if (methodName != "model") return

                if (!isLoadModelCall(reference)) return

                val params = reference.parameterList ?: return
                val modelArg = params.firstPsiChild ?: return
                val modelName = normalizeModelName(modelArg.text) ?: return

                val project = reference.project
                val existing = resolveModelFile(project, modelName)
                if (existing != null) return

                registerMissingModelProblem(holder, modelArg, project, modelName)
            }
        }
    }

    private fun isLoadModelCall(reference: MethodReference): Boolean {
        val classRef = reference.classReference as? MethodReference ?: return false
        if (classRef.name != "load") return false
        val root = classRef.classReference as? com.jetbrains.php.lang.psi.elements.Variable ?: return false
        return root.name == "this"
    }

    private fun normalizeModelName(rawText: String?): String? {
        if (rawText == null) return null
        val trimmed = rawText.trim().trim('\'', '"')
        if (trimmed.isEmpty()) return null
        return trimmed
    }

    private fun registerMissingModelProblem(
        holder: ProblemsHolder,
        element: PsiElement,
        project: Project,
        modelName: String
    ) {
        val message = "CI3 model '$modelName' not found under application/models."
        holder.registerProblem(
            element,
            message,
            ProblemHighlightType.GENERIC_ERROR_OR_WARNING,
            CreateCi3ModelQuickFix(modelName)
        )
    }
}

/**
 * Quick-fix that creates `application/models/{path}.php` for a missing CI3 model.
 * Uses the built-in "CI3 Model.php" template; class name is derived from path (e.g. blog/queries → Queries).
 */
class CreateCi3ModelQuickFix(
    private val modelName: String
) : com.intellij.codeInspection.LocalQuickFix {

    override fun getFamilyName(): String = "Create CI3 model '$modelName'"

    override fun applyFix(project: Project, descriptor: com.intellij.codeInspection.ProblemDescriptor) {
        val modelsDir = getModelsDir(project) ?: return
        val targetFile = createModelFile(project, modelsDir, modelName) ?: return

        val psiFile = com.intellij.psi.PsiManager.getInstance(project).findFile(targetFile) ?: return
        val editor = com.intellij.openapi.fileEditor.FileEditorManager.getInstance(project)
        editor.openFile(targetFile, true)
        val document = com.intellij.openapi.fileEditor.FileDocumentManager.getInstance().getDocument(targetFile)
        if (document != null) {
            val openedEditor = editor.selectedTextEditor
            if (openedEditor != null && openedEditor.document == document) {
                openedEditor.caretModel.moveToOffset(document.textLength)
            }
        }
    }

    private fun getModelsDir(project: Project): VirtualFile? {
        val baseDir = project.guessProjectBaseDir() ?: return null
        return baseDir.findChild("application")?.findChild("models")
    }

    private fun createModelFile(
        project: Project,
        modelsDir: VirtualFile,
        modelPath: String
    ): VirtualFile? {
        return com.intellij.openapi.command.WriteCommandAction.writeCommandAction(project).compute<VirtualFile?, RuntimeException> {
            var dir: VirtualFile = modelsDir
            val segments = modelPath.split("/")
            for (i in 0 until segments.size - 1) {
                val segment = segments[i]
                val existing = dir.findChild(segment)
                dir = if (existing != null && existing.isDirectory) {
                    existing
                } else {
                    dir.createChildDirectory(this, segment)
                }
            }
            val lastSegment = segments.last()
            val className = lastSegment.replaceFirstChar { it.uppercaseChar() }
            val fileName = "$className.php"
            val existingFile = dir.findChild(fileName)
            if (existingFile != null) {
                existingFile
            } else {
                val created = dir.createChildData(this, fileName)
                initializeModelFileFromTemplate(project, created, className)
                created
            }
        }
    }

    private fun initializeModelFileFromTemplate(
        project: Project,
        file: VirtualFile,
        className: String
    ) {
        val manager = com.intellij.ide.fileTemplates.FileTemplateManager.getInstance(project)
        val template = manager.getInternalTemplate("CI3 Model.php") ?: return
        val props = manager.defaultProperties.also {
            it["NAME"] = className
            it["EXTENDS"] = "CI_Model"
        }
        val text = try {
            template.getText(props)
        } catch (e: Exception) {
            """<?php
defined('BASEPATH') OR exit('No direct script access allowed');

class $className extends CI_Model {
}
"""
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
