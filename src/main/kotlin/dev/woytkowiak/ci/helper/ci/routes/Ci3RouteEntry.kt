package dev.woytkowiak.ci.helper.ci.routes

import com.intellij.openapi.vfs.VirtualFile

/**
 * A single CI3 route: from controllers (controller/method) or from config/routes.php (pattern → value).
 */
sealed class Ci3RouteItem {

    /** From application/controllers scan: controller or controller/method. */
    data class FromController(
        val routePath: String,
        val file: VirtualFile,
        val methodName: String?,
        val lineNumber: Int?
    ) : Ci3RouteItem() {
        val displaySegment: String get() = methodName ?: routePath.substringAfterLast('/').ifEmpty { routePath }
    }

    /** From application/config/routes.php: $route['pattern'] = 'value'. */
    data class FromRoutesFile(
        val pattern: String,
        val value: String,
        val routesFile: VirtualFile,
        val lineNumber: Int
    ) : Ci3RouteItem() {
        val displaySegment: String get() = pattern
        val displayValue: String get() = value
    }
}
