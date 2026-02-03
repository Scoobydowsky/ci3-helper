package dev.woytkowiak.ci.helper.ci.actions

class CreateCi3ControllerAction : CreateCi3FileAction(
    "CI3 Controller.php",
    "action.create.ci3.controller.text",
    "action.create.ci3.file.action.name",
    fileNameSuffix = null,
    defaultExtends = "MY_Controller",
    extendsOptions = listOf("MY_Controller", "CI_Controller"),
    defaultFolderName = "controllers",
    dialogTitleKey = "action.create.ci3.wizard.title.controller"
)
