# Gdzie wrzucić te zmiany (instrukcja przywracania)

Ten folder zawiera **wszystkie Twoje zmiany** (plugin CodeIgniter 3 Helper). Żeby przywrócić je w nowym projekcie (np. świeży clone z "Use this template"), skopiuj pliki zgodnie z mapowaniem poniżej.

**Ścieżki względem roota repozytorium (np. `ci3-phpstorm-helper/`):**

| Z tego folderu (`_user-changes-export/`) | Wrzuć do |
|------------------------------------------|----------|
| `src/main/kotlin/dev/woytkowiak/ci/helper/*` | `src/main/kotlin/dev/woytkowiak/ci/helper/` |
| `src/main/resources/META-INF/plugin.xml` | `src/main/resources/META-INF/plugin.xml` |
| `src/main/resources/messages/MyBundle.properties` | `src/main/resources/messages/MyBundle.properties` |
| `src/main/resources/ci3-stubs/*` | `src/main/resources/ci3-stubs/` |
| `src/test/kotlin/dev/woytkowiak/ci/helper/Ci3HelperPluginTest.kt` | `src/test/kotlin/dev/woytkowiak/ci/helper/Ci3HelperPluginTest.kt` |
| `spec.md` | `spec.md` (w root) |
| `README.md` | `README.md` (w root) |
| `CHANGELOG.md` | `CHANGELOG.md` (w root) |
| `NOTICE` | `NOTICE` (w root) |
| `CONTRIBUTING.md` | `CONTRIBUTING.md` (w root) |
| `gradle.properties` | `gradle.properties` (w root) – **nadpisuje** cały plik (pluginGroup, pluginName, pluginRepositoryUrl, pluginVersion itd.) |

**Dodatkowo po przywróceniu:**

1. **Usuń** z nowego projektu pliki/gałęzie szablonu, których nie ma w tym eksporcie:
   - `src/main/kotlin/org/jetbrains/plugins/template/` (cały pakiet – MyBundle, services, startup, toolWindow)
   - Stare testy: `src/test/kotlin/org/jetbrains/plugins/template/`, `src/test/testData/rename/`
2. **Remote:** ustaw `origin` na swoje repo:  
   `git remote add origin https://github.com/Scoobydowsky/ci3-phpstorm-helper.git`  
   (albo usuń stary origin i dodaj ten.)
3. **Commit messages:** po angielsku (zgodnie z CONTRIBUTING.md).

**AI / asystent:** przy przywracaniu używaj tej tabeli – kopiuj zawartość z `_user-changes-export/` do odpowiednich ścieżek w docelowym repozytorium.
