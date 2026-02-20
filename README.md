# CodeIgniter 3 Helper

[![Sponsor](https://img.shields.io/badge/-Sponsor%20me-ea4aaa?logo=githubsponsors&logoColor=ea4aaa)](https://github.com/sponsors/Scoobydowsky)

PhpStorm plugin for working with CodeIgniter 3 projects.

<!-- Plugin description -->
Adds CodeIgniter 3 support to PhpStorm: completion, navigation, and scaffolding so you can work faster with fewer false alarms.
[Plugin Repository](https://github.com/Scoobydowsky/ci3-phpstorm-helper)|[Donate](https://github.com/sponsors/Scoobydowsky).
- Completion — models, views, libraries, routes in load->view(), load->model(), load->library(), and routes
- Go to Definition (Ctrl+B) — jump to file or symbol from loaders and route definitions
- Views — completion and go-to in load->view(); Find Usages shows where a view is loaded
- Quick fix — one-click fix “Property not found” on CI_Controller and stubs
- Stubs — CI types recognized so the IDE does not flag valid code
- New → CodeIgniter 3 — create Controllers, Models, Views, and API Controllers from the context menu


<!-- Plugin description end -->

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

Or in the IDE: use the "Run Plugin" run configuration (`.run/Run Plugin.run.xml`) — it runs the `runIde` Gradle task and should start **PhpStorm** (the project depends on `phpstorm()`, not IntelliJ IDEA). If IntelliJ IDEA starts instead, run `./gradlew runIde` from the terminal or refresh the Gradle project so the correct platform is used.

## Install from disk

_Settings → Plugins → ⚙️ → Install Plugin from Disk…_ and select the zip file.

## Support

If you like the plugin and want to support its development:

[![GitHub Sponsors](https://img.shields.io/badge/GitHub%20Sponsors-Support%20the%20author-ea4aaa?logo=githubsponsors)](https://github.com/sponsors/Scoobydowsky)

You can also use [GitHub Sponsors](https://github.com/sponsors/Scoobydowsky) or any other platform you prefer. Thank you for any support.

## License

Apache 2.0 — see [LICENSE](LICENSE).

CodeIgniter and the CodeIgniter logo are trademarks of the EllisLab, Inc. Development Corporation. This plugin is not affiliated with or endorsed by CodeIgniter. See [NOTICE](NOTICE) for attributions.

## Project template

This repository was scaffolded from the [IntelliJ Platform Plugin Template](https://github.com/JetBrains/intellij-platform-plugin-template) (JetBrains s.r.o.), licensed under the Apache License 2.0. Original template license attribution: [NOTICE](NOTICE) and [LICENSE](LICENSE).
