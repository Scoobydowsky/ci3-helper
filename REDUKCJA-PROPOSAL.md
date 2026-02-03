# Analiza zbędnych plików i propozycja redukcji

## 1. Katalog `_user-changes-export/` – **do usunięcia (zalecane)**

**Rola:** Eksport/backup „zmian użytkownika” do przywrócenia w nowym projekcie (np. z szablonu „Use this template”).

**Dlaczego zbędny:**
- Zawiera **starszą, uboższą** wersję pluginu niż obecny `src/`:
  - brak: `actions/`, `settings/`, `statusbar/`, `Ci3Icons.kt`, `Ci3Support.kt`, `CiViewUtils.kt`
  - brak: `CiHelperReferenceContributor`, `CiModelReferenceContributor`, `CiViewFindUsagesProvider`
  - brak: `fileTemplates/`, `icons/`, `global_functions.php`
- Źródłem prawdy jest **`src/`** – tam jest pełna, aktualna implementacja.
- Żaden plik w projekcie nie referencuje tego folderu (poza wewnętrznym `RESTORE.md`).
- Jeśli kiedyś będzie potrzebny „restore”, można trzymać ten eksport w osobnym repo, branchu lub archiwum (zip).

**Zawartość do usunięcia (cały katalog):**
- `_user-changes-export/` (wraz z podkatalogami i plikami: CHANGELOG, CONTRIBUTING, gradle.properties, NOTICE, README, RESTORE.md, spec.md, src/…)

**Efekt:** Usunięcie duplikatów dokumentacji i starej kopii kodu; mniej miejsca i mniej ryzyka pomyłki przy edycji.

---

## 2. Plik `MANIFEST.md` – **do usunięcia (opcjonalne)**

**Rola:** Opis „eksportu zmian z sesji” i lista zmienionych/dodanych plików (widoki, pokrycie CI3, load->config/language/driver, get_instance(), suppress).

**Dlaczego zbędny:**
- To dokument z **jednej sesji migracji**, nie żywa dokumentacja projektu.
- Informacje o tym, co plugin robi, są w **README.md** i **COVERAGE.md**.
- Nie jest referencowany w buildach ani w innych plikach.

**Efekt:** Mniej martwej dokumentacji w rootcie.

---

## 3. Testy z szablonu – **do uporządkowania / usunięcia**

**Lokalizacja:**  
`src/test/kotlin/com/github/scoobydowsky/ci3phpstormhelper/MyPluginTest.kt`  
oraz `src/test/testData/rename/` (foo.xml, foo_after.xml).

**Problem:**
- Test importuje i używa **`MyProjectService`** – takiej usługi **nie ma** w projekcie (kod pluginu jest w `dev.woytkowiak.ci.helper`, nie w `com.github.scoobydowsky...`).
- Testy `testXMLFile`, `testRename`, `testProjectService` pochodzą z **szablonu pluginu JetBrains**, a nie z logiki CI3 Helper.

**Opcje:**
- **A) Usunąć:** `MyPluginTest.kt` oraz katalog `src/test/testData/rename/` – projekt nie ma wtedy żadnych testów, ale build nie zależy od nieistniejącego serwisu.
- **B) Zostawić strukturę, wyciąć niedziałające:** Usunąć tylko test `testProjectService` (i import `MyProjectService`), żeby reszta testów się kompilowała – nadal to będą testy szablonu (XML, rename), nie CI3.
- **C) Docelowo:** Dodać prawdziwe testy dla pluginu CI3 (np. completion, nawigacja) i wtedy usunąć lub przepisać `MyPluginTest.kt` i ewentualnie `testData/rename/`.

**Rekomendacja:** A lub C – albo usunąć zbędne testy szablonu (A), albo planować nowe testy CI3 i posprzątać stare (C).

---

## Podsumowanie

| Element | Akcja | Uzasadnienie |
|--------|--------|--------------|
| **`_user-changes-export/`** | Usunąć cały katalog | Stara kopia kodu i duplikaty docs; źródło prawdy to `src/`. |
| **`MANIFEST.md`** | Usunąć | Dokument z jednej sesji; nieużywany. |
| **`MyPluginTest.kt`** | Usunąć lub przepisać | Odnosi się do nieistniejącego `MyProjectService`; testy z szablonu. |
| **`src/test/testData/rename/`** | Usunąć, jeśli usuwamy `MyPluginTest.kt` | Używane tylko przez `testRename()`. |

**Szacunkowa redukcja:**  
- Usunięcie `_user-changes-export/` + `MANIFEST.md`: **znacznie mniej plików** (cały drzewiaste katalog + 1 plik).  
- Dodatkowo testy szablonu: **1 plik Kotlin + 2 pliki XML** w testData.

Po tych krokach w repozytorium zostanie tylko aktualny kod (`src/`), żywa dokumentacja (README, CHANGELOG, COVERAGE, CONTRIBUTING, NOTICE) oraz konfiguracja (Gradle, GitHub, qodana itd.) – bez duplikatów i bez zależności od nieistniejących klas.
