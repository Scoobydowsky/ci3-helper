package dev.woytkowiak.ci.helper.ci.routes

import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import dev.woytkowiak.ci.helper.ci.guessProjectBaseDir

/**
 * Scans application/controllers and application/config/routes.php for CI3 routes.
 */
/** One controller class under application/controllers (with CI3 route prefix). */
data class Ci3ControllerOption(
    val routePrefix: String,
    val file: VirtualFile
)

object Ci3RoutesCollector {

    /**
     * Controllers for pickers (one entry per controller .php file), sorted by route prefix.
     */
    fun listControllerOptions(project: Project): List<Ci3ControllerOption> {
        val result = mutableListOf<Ci3ControllerOption>()
        val baseDir = project.guessProjectBaseDir() ?: return result
        val controllersDir = baseDir.findChild("application")?.findChild("controllers") ?: return result
        collectControllerOptions(controllersDir, "", result)
        return result.sortedBy { it.routePrefix }
    }

    private fun collectControllerOptions(
        dir: VirtualFile,
        pathPrefix: String,
        out: MutableList<Ci3ControllerOption>
    ) {
        for (file in dir.children) {
            if (file.isDirectory) {
                collectControllerOptions(file, "$pathPrefix${file.name}/", out)
            } else if (file.name.endsWith(".php")) {
                val segment = file.nameWithoutExtension.lowercase()
                val routePrefix = if (pathPrefix.isEmpty()) segment else pathPrefix.lowercase() + segment
                out.add(Ci3ControllerOption(routePrefix, file))
            }
        }
    }

    /**
     * Public action method names for a controller file (excludes __construct, private/protected).
     */
    fun listActionMethodsForController(controllerFile: VirtualFile): List<String> {
        val text = String(controllerFile.contentsToByteArray())
        val lines = text.split('\n')
        val names = linkedSetOf<String>()
        val fnRegex = Regex("function\\s+(\\w+)\\s*\\(")
        for (line in lines) {
            val trimmed = line.trimStart()
            if (trimmed.startsWith("private ") || trimmed.startsWith("protected ")) continue
            if (trimmed.contains("private function") || trimmed.contains("protected function")) continue
            val match = fnRegex.find(line) ?: continue
            val name = match.groupValues[1]
            if (name == "__construct") continue
            names.add(name)
        }
        return names.sorted()
    }

    /**
     * Collects route entries from application/controllers (recursive).
     * Excludes __construct; other methods are treated as CI3 actions.
     */
    fun collectControllerRoutes(project: Project): List<Ci3RouteItem.FromController> {
        val result = mutableListOf<Ci3RouteItem.FromController>()
        val baseDir = project.guessProjectBaseDir() ?: return result
        val controllersDir = baseDir.findChild("application")?.findChild("controllers") ?: return result
        collectControllerRoutes(controllersDir, "", result)
        return result.sortedWith(compareBy({ it.routePath }, { it.methodName.orEmpty() }))
    }

    /**
     * Parses application/config/routes.php for $route['key'] = 'value' entries.
     */
    fun collectCustomRoutes(project: Project): List<Ci3RouteItem.FromRoutesFile> {
        val baseDir = project.guessProjectBaseDir() ?: return emptyList()
        val routesFile = baseDir.findChild("application")
            ?.findChild("config")
            ?.findChild("routes.php")
            ?: return emptyList()
        return parseRoutesFile(routesFile)
    }

    private fun collectControllerRoutes(
        dir: VirtualFile,
        pathPrefix: String,
        result: MutableList<Ci3RouteItem.FromController>
    ) {
        for (file in dir.children) {
            if (file.isDirectory) {
                collectControllerRoutes(file, "$pathPrefix${file.name}/", result)
            } else if (file.name.endsWith(".php")) {
                val segment = file.nameWithoutExtension.lowercase()
                val routePrefix = if (pathPrefix.isEmpty()) segment else pathPrefix.lowercase() + segment
                val methodsWithLines = findControllerMethodsWithLinesInternal(file)
                result.add(Ci3RouteItem.FromController(routePrefix, file, null, null))
                for ((methodName, lineNumber) in methodsWithLines) {
                    if (methodName == "__construct") continue
                    result.add(Ci3RouteItem.FromController("$routePrefix/$methodName", file, methodName, lineNumber))
                }
            }
        }
    }

    private fun findControllerMethodsWithLinesInternal(controllerFile: VirtualFile): List<Pair<String, Int>> {
        val text = String(controllerFile.contentsToByteArray())
        val regex = Regex("function\\s+(\\w+)\\s*\\(")
        val lines = text.split('\n')
        val result = mutableListOf<Pair<String, Int>>()
        for ((lineIndex, line) in lines.withIndex()) {
            for (match in regex.findAll(line)) {
                result.add(match.groupValues[1] to lineIndex + 1)
            }
        }
        return result
    }

    /** Matches $route['key'] = 'value' or $route["key"] = "value" (single line). */
    private val routeLineRegex = Regex(
        "\\\$route\\s*\\[\\s*['\"]([^'\"]*)['\"]\\s*\\]\\s*=\\s*['\"]([^'\"]*)['\"]"
    )

    private fun parseRoutesFile(routesFile: VirtualFile): List<Ci3RouteItem.FromRoutesFile> {
        val text = String(routesFile.contentsToByteArray())
        val result = mutableListOf<Ci3RouteItem.FromRoutesFile>()
        text.split('\n').forEachIndexed { index, line ->
            val match = routeLineRegex.find(line) ?: return@forEachIndexed
            val pattern = match.groupValues[1]
            val value = match.groupValues[2]
            result.add(Ci3RouteItem.FromRoutesFile(pattern, value, routesFile, index + 1))
        }
        return result.sortedBy { it.pattern }
    }
}
