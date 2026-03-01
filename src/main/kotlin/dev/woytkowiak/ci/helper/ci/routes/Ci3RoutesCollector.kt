package dev.woytkowiak.ci.helper.ci.routes

import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import dev.woytkowiak.ci.helper.ci.guessProjectBaseDir

/**
 * Scans application/controllers and application/config/routes.php for CI3 routes.
 */
object Ci3RoutesCollector {

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
                val methodsWithLines = findControllerMethodsWithLines(file)
                result.add(Ci3RouteItem.FromController(routePrefix, file, null, null))
                for ((methodName, lineNumber) in methodsWithLines) {
                    if (methodName == "__construct") continue
                    result.add(Ci3RouteItem.FromController("$routePrefix/$methodName", file, methodName, lineNumber))
                }
            }
        }
    }

    private fun findControllerMethodsWithLines(controllerFile: VirtualFile): List<Pair<String, Int>> {
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
