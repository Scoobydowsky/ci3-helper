package dev.woytkowiak.ci.helper.ci.actions

class CreateCi3LibraryAction : CreateCi3FileAction(
    "CI3 Library.php",
    "action.create.ci3.library.text",
    "action.create.ci3.file.action.name",
    fileNameSuffix = null,
    defaultExtends = null,
    extendsOptions = emptyList(),
    defaultFolderName = "libraries",
    dialogTitleKey = "action.create.ci3.wizard.title.library"
)
