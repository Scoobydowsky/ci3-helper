package dev.woytkowiak.ci.helper.ci.actions

class CreateCi3ApiControllerAction : CreateCi3FileAction(
    "CI3 API Controller.php",
    "action.create.ci3.api.text",
    "action.create.ci3.file.action.name",
    fileNameSuffix = null,
    defaultExtends = "MY_Controller",
    extendsOptions = listOf("MY_Controller", "CI_Controller"),
    defaultFolderName = "controllers",
    dialogTitleKey = "action.create.ci3.wizard.title.api"
)
