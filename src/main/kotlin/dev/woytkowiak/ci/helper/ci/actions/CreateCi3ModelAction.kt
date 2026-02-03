package dev.woytkowiak.ci.helper.ci.actions

class CreateCi3ModelAction : CreateCi3FileAction(
    "CI3 Model.php",
    "action.create.ci3.model.text",
    "action.create.ci3.file.action.name",
    fileNameSuffix = "_model",
    defaultExtends = "CI_Model",
    extendsOptions = listOf("CI_Model", "MY_Model"),
    defaultFolderName = "models",
    dialogTitleKey = "action.create.ci3.wizard.title.model"
)
