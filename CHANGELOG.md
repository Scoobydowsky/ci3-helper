<!-- Keep a Changelog guide -> https://keepachangelog.com -->

# ci3-phpstorm-helper Changelog

## [0.1.0]
### Added
- **CI3 Routes tool window** (right side): panel that scans `application/controllers` and parses `application/config/routes.php`; shows all routes in a **nested tree** (Controllers and Custom routes by path segments). Double-click opens the controller file at the method or `routes.php` at the route line; toolbar Refresh rescans.
### Changed
- First minor release. Plugin feature set from 0.0.x (routing, hooks, views, models, helpers, inspections, CLI) is now considered stable.

## [0.0.14]
### Added
- **Routing** ([CI3 URI Routing](https://codeigniter.com/userguide3/general/routing.html)):
  - Completion in `routes.php` for reserved route keys: `default_controller`, `404_override`, `translate_uri_dashes` (with "(reserved)" tail), and wildcards `(:num)`, `(:any)` (with tail text).
  - **Dynamic routes**: completion for route value includes templates `controller/$1`, `controller/method/$1`, `controller/method/$1/$2` (back-references); Go to Declaration already resolves the controller part for values like `'catalog/product_lookup_by_id/$1'`.
  - Inspection: `default_controller` value must not contain a directory (slash) — CI3 does not allow it.
  - Inspection: route key must not have leading or trailing slashes.

## [0.0.13]
### Added
- **Output / page caching** ([CI3 Caching](https://codeigniter.com/userguide3/general/caching.html)): `delete_cache()` and `delete_cache($uri)` in CI_Output stub and completion; clarified `cache($n)` docblock (minutes, view requirement).
- **Inspection**: `output->cache()` without `load->view()` in the same method — warns that page caching only works with view output (weak warning).
- **Quick doc**: CI_Output stub notes for `cache()` and `delete_cache()` — manual cache deletion after config changes; cache stored in `application/cache/`.
- **Cache driver** ([CI3 Caching library](https://codeigniter.com/userguide3/libraries/caching.html)): CI_Cache stub class doc updated with load example and `key_prefix` usage note.
- **Compatibility functions** ([CI3 Compatibility](https://codeigniter.com/userguide3/general/compatibility_functions.html)): stubs in `global_functions.php` for password hashing (`password_hash`, `password_verify`, `password_get_info`, `password_needs_rehash`), hash (`hash_equals`, `hash_pbkdf2`), multibyte (`mb_strlen`, `mb_strpos`, `mb_substr`), and standard backports (`array_column`, `hex2bin`); constants `PASSWORD_DEFAULT`, `PASSWORD_BCRYPT`, `MB_ENABLED`.

## [0.0.12]
### Added
- **CI3 Hooks** ([docs](https://codeigniter.com/userguide3/general/hooks.html)): completion in `hooks.php` for hook points and array keys; Go to Declaration from `filename`/`filepath` to hook file or dir; New → Hook action with template and optional `hooks.php` entry; inspections for invalid hook point and missing hook file; line marker and Find Usages for hook files.
- **Views (from [CI3 Views](https://codeigniter.com/userguide3/general/views.html)):**
  - Inspection: missing view file for `load->view('path')` with quick-fix to create the view under `application/views`.
  - Inspection: warn when `load->view(..., TRUE)` return value is not used.
  - Gutter line markers: jump from `load->view('name')` to the view file, and from a view file to controllers that load it.
- **Models (from [CI3 Models](https://codeigniter.com/userguide3/general/models.html)):**
  - Completion in `load->model()` now includes models in subdirectories (e.g. `blog/queries` for `application/models/blog/Queries.php`); Go to Declaration and type resolution support subdir paths and alias (second parameter).
  - Inspection: missing model file for `load->model('path')` with quick-fix to create the model under `application/models`.
  - Gutter line markers: jump from `load->model('name')` to the model file, and from a model file to controllers that load it.
  - Find Usages for model files (application/models/...) finds all `load->model('...')` call sites.
- Relaxed PHP inspections in `application/views`: unused local variable, unused parameter, missing return type, and missing return statement are suppressed in view files (in addition to undefined variable from `$data`).

## [0.0.11]
### Added
- Now use of reserved Names shows error or warning in IDE problems section.
- **Controllers (from [CI3 Controllers](https://codeigniter.com/userguide3/general/controllers.html)):**
  - Inspection: controller method must not have the same name as the class (PHP4 constructor legacy).
  - Inspection: if a controller defines `__construct()`, it must call `parent::__construct()`.
  - Support for controllers in subdirectories: "Go to Declaration" in `routes.php` and route completion now resolve e.g. `products/shoes` to `application/controllers/products/Shoes.php`.
- New controller file template includes `__construct()` with `parent::__construct()`.


## [0.0.10]
### Added
- Added support to ```Html``` helper
- Added support to ```Inflector``` helper
- Added support to ```Language``` helper
- Added support to ```Number``` helper
- Added support to ```Path``` helper
- Added support to ```Security``` helper
- Added support to ```Smiley``` helper
- Added support to ```String``` helper
- Added support to ```Text``` helper
- Added support to ```Typography``` helper
- Added support to ```Url``` helper
- Added support to ```XML``` helper

## [0.0.9]
Now im started on including Helpers. 

### Added
- Added support to ```Array``` helper
- Added support to ```CAPTCHA``` helper
- Added support to ```Directory``` helper
- Added support to ```Email``` helper (valid_email, send_email)
- Added support to ```File``` helper (read_file, write_file, delete_files, get_filenames, get_dir_file_info, get_file_info, get_mime_by_extension, symbolic_permissions, octal_permissions)
- Added support to ```Cookie``` helper
- Added support to ```Date``` helper
- Added support to ```Download``` helper
- Added support to ```Form``` helper

## [0.0.8]
### Added
- Added support to ```Security``` library
- Added support to ```Pagination``` library
- Added support to ```Form Validation``` library
- Added support to ```Input``` library
- Added support to ```Image Manipulation``` library
- Added initial support for the ```Migration``` library (advanced migration management coming soon)
- Added support to ```HTML table``` library

## [0.0.7]
### Added
- Added support to ```Email```
- Added support to ```Typography```
- Added support to ```URI```
- Added Create CI3 Library action in context menu RMB->New->CodeIngiter
## [0.0.6]
### Added
- Added support to ```Encrypt``` library
- Added support to ```Output``` library
- Added support to ```Encryption``` library
- Added support to ```FTP``` library 
- Added support to ```Shopping Cart``` library (throws info about deprecation when is used)
- Added support to ```Config``` library
- Added support to ```XML-RPC``` library


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
