# Eksport zmian z sesji – CodeIgniter 3 Helper

Zmiany wprowadzone w tej sesji: widoki, pokrycie CI3 (~99%), load->config/language/driver, get_instance(), suppress „Undefined variable” w widokach.

## Jak przywrócić

Skopiuj pliki z tego folderu nad odpowiadające pliki w głównym projekcie (zachowaj ścieżki względne). Katalog `src/` odpowiada `src/` w projekcie, pliki w root (COVERAGE.md, README.md) – rootowi projektu.

---

## Lista zmienionych / dodanych plików

| Ścieżka w eksporcie | W projekcie | Opis |
|---------------------|-------------|------|
| **Nowe pliki** | | |
| `src/main/kotlin/.../ci/CiViewUtils.kt` | to samo | Wspólna logika widoków (findViews, resolveViewFile, getViewsDir) |
| `src/main/kotlin/.../navigation/CiViewFindUsagesProvider.kt` | to samo | Find Usages dla plików z application/views/ |
| `src/main/resources/ci3-stubs/global_functions.php` | to samo | Stub get_instance() → CI_Controller |
| `COVERAGE.md` | `COVERAGE.md` | Raport pokrycia funkcjonalności CI3 |
| **Zmodyfikowane** | | |
| `src/main/kotlin/.../navigation/CiViewReferenceContributor.kt` | to samo | Scoping tylko do load->view(), nazwy bez /, CiViewUtils |
| `src/main/kotlin/.../completion/CiModelCompletionContributor.kt` | to samo | CiViewUtils.findViews; load->config, load->language, load->driver; findConfigFileNames, findLanguageKeys, findCustomDrivers |
| `src/main/kotlin/.../suppress/Ci3UndefinedFieldSuppressor.kt` | to samo | PhpUndefinedVariableInspection w application/views/ |
| `src/main/kotlin/.../stubs/Ci3StubIncludePathContributor.kt` | to samo | Kopiowanie global_functions.php do .ci3-helper/stubs |
| `src/main/resources/META-INF/plugin.xml` | to samo | Rejestracja CiViewFindUsagesProvider |
| `README.md` | `README.md` | Linia o widokach, link do COVERAGE.md |

---

## Krótki opis funkcjonalności

- **Widoki**: completion i go-to tylko w `load->view(...)`; Find Usages z pliku widoku; brak błędów „Undefined variable” w plikach z `application/views/`.
- **Loader**: completion dla `load->config()`, `load->language()`, `load->driver()` (config z application/config/, language z application/language/, driver: cache + własne *_driver.php).
- **get_instance()**: stub z @return CI_Controller – poprawne podpowiedzi dla `$CI->load` itd.
- **COVERAGE.md**: tabela pokrycia standardowej funkcjonalności CI3 (~99%).
