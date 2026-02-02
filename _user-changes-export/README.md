# CodeIgniter 3 Helper

Plugin do PhpStorm/IntelliJ IDEA wspomagający pracę z projektami CodeIgniter 3.

<!-- Plugin description -->
**CodeIgniter 3 Helper** dodaje obsługę frameworka CodeIgniter 3 w IDE:

- **Autouzupełnianie** — modele, widoki, biblioteki, trasy
- **Nawigacja** — przejście do definicji (Ctrl+B) w `$this->load->view()`, `$this->load->model()`, `$this->load->library()`, trasach
- **Intencje** — szybka poprawka „Property not found” dla `CI_Controller`/`Dummy`
- **Stuby** — rozpoznawanie typów CI bez błędów inspekcji
<!-- Plugin description end -->

## Wymagania

- PhpStorm lub IntelliJ IDEA z wtyczką PHP
- Java 21 (do budowania)

## Budowanie

```bash
./gradlew buildPlugin
```

Artefakt: `build/distributions/Code Igniter 3 Helper-*.zip`

## Instalacja z dysku

_Settings → Plugins → ⚙️ → Install Plugin from Disk…_ i wybierz plik zip.

## Licencja

Apache 2.0 — patrz [LICENSE](LICENSE).

## Szablon projektu

Scaffold repozytorium powstał na podstawie [IntelliJ Platform Plugin Template](https://github.com/JetBrains/intellij-platform-plugin-template) (JetBrains s.r.o.), udostępnionego na licencji Apache License 2.0. Atrybucja oryginalnej licencji szablonu: [NOTICE](NOTICE) i [LICENSE](LICENSE).
