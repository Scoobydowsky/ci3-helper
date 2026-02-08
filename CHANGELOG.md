<!-- Keep a Changelog guide -> https://keepachangelog.com -->

# ci3-phpstorm-helper Changelog


## [0.0.3]
### Fixed
- Now plugin helps with model metods like ```$this->employee_model``` you will get lis with all methods available in Employee_model
- Loaded models + model methods completion
- Type providers for zip, models, and custom libs (parameter name hints)
### Changed
### Added 
- Now plugin shows what any method got parameters


## [0.0.2]
### Fixed
- Replace internal API `PhpType.from(String[])` with public `PhpType.builder().add(...).build()` in `Ci3ThisTypeProvider` for plugin marketplace verification

### Changed
- Plugin is now compatible with PhpStorm only (removed IntelliJ IDEA support via `incompatible-with` Java module)
- Plugin verification runs only for PhpStorm

## [0.0.1]
### Added
- Initial scaffold created from [IntelliJ Platform Plugin Template](https://github.com/JetBrains/intellij-platform-plugin-template)
