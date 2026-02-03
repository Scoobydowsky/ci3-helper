# CodeIgniter 3 Helper

[![Sponsor](https://img.shields.io/badge/-Sponsor%20me-ea4aaa?logo=githubsponsors&logoColor=ea4aaa)](https://github.com/sponsors/Scoobydowsky)

PhpStorm plugin for working with CodeIgniter 3 projects.

<!-- Plugin description -->
**CodeIgniter 3 Helper** adds CodeIgniter 3 framework support in the IDE:

- **Code completion** — models, views, libraries, routes
- **Navigation** — Go to Definition (Ctrl+B) in `$this->load->view()`, `$this->load->model()`, `$this->load->library()`, routes
- **Views** — completion and go-to only in `load->view(...)`; Find Usages from a view file shows where the view is loaded
- **Intentions** — quick fix for "Property not found" on `CI_Controller`/`Dummy`
- **Stubs** — CI type recognition without inspection errors

If you find the plugin useful, consider [supporting the author](#support).
<!-- Plugin description end -->

Detailed CI3 feature coverage: [COVERAGE.md](COVERAGE.md).

## Requirements

- PhpStorm
- Java 21 (for building)

## Building

```bash
./gradlew buildPlugin
```

Artifact: `build/distributions/Code Igniter 3 Helper-*.zip`

## Running the plugin

To run PhpStorm with the plugin loaded:

```bash
./gradlew runIde
```

Or in the IDE: use the "Run Plugin" run configuration (`.run/Run Plugin.run.xml`) — it runs the `runIde` task and starts PhpStorm.

## Install from disk

_Settings → Plugins → ⚙️ → Install Plugin from Disk…_ and select the zip file.

## Support

If you like the plugin and want to support its development:

[![GitHub Sponsors](https://img.shields.io/badge/GitHub%20Sponsors-Support%20the%20author-ea4aaa?logo=githubsponsors)](https://github.com/sponsors/Scoobydowsky)

You can also use [GitHub Sponsors](https://github.com/sponsors/Scoobydowsky) or any other platform you prefer. Thank you for any support.

## License

Apache 2.0 — see [LICENSE](LICENSE).

## Project template

This repository was scaffolded from the [IntelliJ Platform Plugin Template](https://github.com/JetBrains/intellij-platform-plugin-template) (JetBrains s.r.o.), licensed under the Apache License 2.0. Original template license attribution: [NOTICE](NOTICE) and [LICENSE](LICENSE).
