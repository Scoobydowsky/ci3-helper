# CodeIgniter 3 Helper

Plugin do PhpStorm/IntelliJ IDEA wspomagający pracę z projektami CodeIgniter 3.

<!-- Plugin description -->
**CodeIgniter 3 Helper** dodaje obsługę frameworka CodeIgniter 3 w IDE:

- **Autouzupełnianie** — modele, widoki, biblioteki, trasy
- **Nawigacja** — przejście do definicji (Ctrl+B) w `$this->load->view()`, `$this->load->model()`, `$this->load->library()`, trasach
- **Widoki** — completion i go-to tylko w `load->view(...)`; Find Usages z pliku widoku pokazuje, gdzie widok jest ładowany
- **Intencje** — szybka poprawka „Property not found” dla `CI_Controller`/`Dummy`
- **Stuby** — rozpoznawanie typów CI bez błędów inspekcji

Jeśli plugin się przydaje, rozważ [wsparcie autora](#wesprzyj) — link w README.
<!-- Plugin description end -->

Szczegółowe pokrycie funkcjonalności CI3: [COVERAGE.md](COVERAGE.md).

## Wymagania

- PhpStorm lub IntelliJ IDEA z wtyczką PHP
- Java 21 (do budowania)

## Budowanie

```bash
./gradlew buildPlugin
```

Artefakt: `build/distributions/Code Igniter 3 Helper-*.zip`

## Uruchomienie pluginu (PhpStorm)

Build jest skonfigurowany pod **PhpStorm** (nie IntelliJ IDEA). Uruchomienie instancji PhpStorm z załadowanym pluginem:

```bash
./gradlew runIde
```

Albo w IDE: konfiguracja „Run Plugin” (`.run/Run Plugin.run.xml`) → uruchamia zadanie `runIde` → startuje **PhpStorm**.

## Instalacja z dysku

_Settings → Plugins → ⚙️ → Install Plugin from Disk…_ i wybierz plik zip.

## Wesprzyj

Jeśli plugin Ci się podoba i chcesz wesprzeć jego rozwój, możesz postawić mi wirtualną kawę lub zostawić wsparcie:

- **[Tu wklej link, np. Ko-fi / Buy Me a Coffee / GitHub Sponsors]**

Dziękuję za każde wsparcie.

## Licencja

Apache 2.0 — patrz [LICENSE](LICENSE).

## Szablon projektu

Scaffold repozytorium powstał na podstawie [IntelliJ Platform Plugin Template](https://github.com/JetBrains/intellij-platform-plugin-template) (JetBrains s.r.o.), udostępnionego na licencji Apache License 2.0. Atrybucja oryginalnej licencji szablonu: [NOTICE](NOTICE) i [LICENSE](LICENSE).
