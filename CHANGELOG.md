<!-- Keep a Changelog guide -> https://keepachangelog.com -->

# ci3-phpstorm-helper Changelog

## [0.0.6]
### Added
- Added support to ```Encrypt``` library
- Added support to ```Output``` library
- Added support to ```Encryption``` library
- Added support to ```FTP``` library 
- Added support to ```Shopping Cart``` library (throws info about deprecation when is used)
- Added support to ```Config``` library


## [0.0.5]
### Fixed
- Replace deprecated `TextFieldWithBrowseButton.addBrowseFolderListener(...)` with `addBrowseFolderListener(Project, FileChooserDescriptor)` in CreateCi3ClassDialog
- Replace deprecated `Project.getBaseDir()` / `project.basePath` with `guessProjectBaseDir()` (ModuleManager/content roots) in completion, navigation, views, stubs and directory finder
- Replace deprecated `StatusBarWidget.getPresentation()` with `getPresentation(PlatformType)` in Ci3StatusBarWidget

### Changed 
- Improvements in ```loading``` library.
### Added
- Added support to ```javascript```library.
- Added support to ```caching``` library.
- Added support to ```file upload``` library.
- Added support to ```calendar``` library.
- Added support to ```language``` library.


## [0.0.4]
### Fixed
- Removed action registration to non-existent group ```StatusbarPopupGroup```; "Support author" stays in Help menu only
- Removed ```intentionAction``` registration from ```plugin.xml``` to fix "implementation class is not specified" error (intention class kept in code, no longer shown in Alt+Enter until re-registered)

### Added
- Added support to ```session``` library
- Added support to ```unit testing``` library
- Added support to ```User Agent``` library
- Added support to ```Trackback``` library
- Added support to custom ```drivers```

## [0.0.3]
### Fixed
- Now plugin helps with model metods like ```$this->employee_model``` you will get lis with all methods available in Employee_model
- Loaded models + model methods completion
- Type providers for zip, models, and custom libs (parameter name hints)
### Changed
### Added 
- Now plugin shows what any method got parameters
- Added support to benchmark native library


## [0.0.2]
### Fixed
- Replace internal API `PhpType.from(String[])` with public `PhpType.builder().add(...).build()` in `Ci3ThisTypeProvider` for plugin marketplace verification

### Changed
- Plugin is now compatible with PhpStorm only (removed IntelliJ IDEA support via `incompatible-with` Java module)
- Plugin verification runs only for PhpStorm

## [0.0.1]
### Added
- Initial scaffold created from [IntelliJ Platform Plugin Template](https://github.com/JetBrains/intellij-platform-plugin-template)
