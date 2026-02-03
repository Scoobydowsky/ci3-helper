package dev.woytkowiak.ci.helper.ci.actions

class CreateCi3ViewAction : CreateCi3FileAction(
    "CI3 View.php",
    "action.create.ci3.view.text",
    "action.create.ci3.file.action.name",
    fileNameSuffix = null,
    defaultExtends = null,
    extendsOptions = emptyList(),
    defaultFolderName = "views",
    dialogTitleKey = "action.create.ci3.wizard.title.view"
)
