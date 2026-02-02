package dev.woytkowiak.ci.helper.ci.intentions

import com.intellij.codeInsight.intention.PsiElementBaseIntentionAction
import com.intellij.openapi.command.WriteCommandAction
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import dev.woytkowiak.ci.helper.MyBundle

/**
 * Intention: dodaj @property Super Object CI3 do klasy rozszerzającej CI_Controller lub MY_Controller.
 * Dzięki temu PhpStorm rozpoznaje $this->input, $this->load itd.
 */
class AddCi3SuperObjectPropertyIntention : PsiElementBaseIntentionAction() {

    override fun getText(): String = MyBundle.message("intention.add.ci3.super.object.property")

    override fun getFamilyName(): String = familyName

    override fun isAvailable(project: Project, editor: Editor?, element: PsiElement): Boolean {
        val file = element.containingFile ?: return false
        if (!file.name.endsWith(".php")) return false
        val text = file.text
        if (!text.contains("extends CI_Controller") && !text.contains("extends MY_Controller")) return false
        if (text.contains("@property CI_Input")) return false
        return true
    }

    override fun invoke(project: Project, editor: Editor?, element: PsiElement) {
        val file = element.containingFile as? PsiFile ?: return
        val doc = editor?.document ?: return
        val text = file.text
        val classIdx = text.indexOf("class ")
        if (classIdx < 0) return
        val dollar = '$'
        val docBlock = """
            |/**
            | * @property CI_Loader ${dollar}load
            | * @property CI_Input ${dollar}input
            | * @property CI_DB_query_builder ${dollar}db
            | * @property CI_Config ${dollar}config
            | * @property CI_Session ${dollar}session
            | * @property CI_URI ${dollar}uri
            | * @property CI_Router ${dollar}router
            | * @property CI_Output ${dollar}output
            | * @property CI_Security ${dollar}security
            | * @property CI_Form_validation ${dollar}form_validation
            | */
            |
        """.trimMargin()
        WriteCommandAction.runWriteCommandAction(project) {
            doc.insertString(classIdx, docBlock)
        }
    }

    companion object {
        const val familyName = "CodeIgniter 3"
    }
}
