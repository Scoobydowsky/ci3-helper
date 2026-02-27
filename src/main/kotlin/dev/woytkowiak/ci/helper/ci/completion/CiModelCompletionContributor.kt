package dev.woytkowiak.ci.helper.ci.completion

import com.intellij.codeInsight.completion.*
import com.intellij.codeInsight.lookup.LookupElementBuilder
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.LocalFileSystem
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.psi.PsiElement
import dev.woytkowiak.ci.helper.ci.guessProjectBaseDir
import java.io.File
import com.intellij.patterns.PlatformPatterns
import com.intellij.util.ProcessingContext
import dev.woytkowiak.ci.helper.ci.Ci3PluginState
import dev.woytkowiak.ci.helper.ci.CiViewUtils

class CiModelCompletionContributor : CompletionContributor() {

    init {
        extend(
            CompletionType.BASIC,
            PlatformPatterns.psiElement(),
            ModelCompletionProvider()
        )
    }

    private class ModelCompletionProvider :
        CompletionProvider<CompletionParameters>() {

        override fun addCompletions(
            parameters: CompletionParameters,
            context: ProcessingContext,
            result: CompletionResultSet
        ) {
            if (!Ci3PluginState.getInstance().isEnabled) return
            val position = parameters.position
            val fileText = position.containingFile.text
            val offset = parameters.offset

            val start = maxOf(0, offset - 150)
            val beforeCursor = fileText.substring(start, offset)

            val currentLine = beforeCursor.substringAfterLast('\n')
            val project = position.project

            val loadedDatabases = findLoadedDatabases(fileText)
            val loadedLibraries = findLoadedLibraries(fileText)
            val loadedDrivers = findLoadedDrivers(fileText)
            val loadedModels = findLoadedModelClasses(fileText).keys

            val isThisCall =
                currentLine.contains("\$this->") &&
                        !currentLine.substringAfter("\$this->").contains("->")
            val afterFirstArrow = currentLine.substringAfter("\$this->", "")
            val firstPropName = afterFirstArrow.substringBefore("->").trim().takeIf { it.isNotEmpty() }
            val libraryPropName = firstPropName?.takeIf { it in loadedLibraries }
            val driverPropName = firstPropName?.takeIf { it in loadedDrivers }
            val modelPropName = firstPropName?.takeIf { it in loadedModels }
            val isLibraryMethodCall = currentLine.contains("\$this->") &&
                afterFirstArrow.contains("->") &&
                !afterFirstArrow.substringAfter("->").trimStart().startsWith("(") &&
                libraryPropName != null && libraryPropName.isNotEmpty()
            val isBenchmarkMethodCall = currentLine.contains("\$this->") &&
                afterFirstArrow.contains("->") &&
                !afterFirstArrow.substringAfter("->").trimStart().startsWith("(") &&
                firstPropName == "benchmark"
            val isUriMethodCall = currentLine.contains("\$this->") &&
                afterFirstArrow.contains("->") &&
                !afterFirstArrow.substringAfter("->").trimStart().startsWith("(") &&
                firstPropName == "uri"
            val isSecurityMethodCall = currentLine.contains("\$this->") &&
                afterFirstArrow.contains("->") &&
                !afterFirstArrow.substringAfter("->").trimStart().startsWith("(") &&
                firstPropName == "security"
            val isPaginationMethodCall = currentLine.contains("\$this->") &&
                afterFirstArrow.contains("->") &&
                !afterFirstArrow.substringAfter("->").trimStart().startsWith("(") &&
                firstPropName == "pagination"
            val isFormValidationMethodCall = currentLine.contains("\$this->") &&
                afterFirstArrow.contains("->") &&
                !afterFirstArrow.substringAfter("->").trimStart().startsWith("(") &&
                firstPropName == "form_validation"
            val isMigrationMethodCall = currentLine.contains("\$this->") &&
                afterFirstArrow.contains("->") &&
                !afterFirstArrow.substringAfter("->").trimStart().startsWith("(") &&
                firstPropName == "migration"
            val isTableMethodCall = currentLine.contains("\$this->") &&
                afterFirstArrow.contains("->") &&
                !afterFirstArrow.substringAfter("->").trimStart().startsWith("(") &&
                firstPropName == "table"
            val isLoadMethodCall = currentLine.contains("\$this->load->") &&
                afterFirstArrow.contains("->") &&
                !afterFirstArrow.substringAfter("->").trimStart().startsWith("(") &&
                firstPropName == "load"
            val isDriverMethodCall = currentLine.contains("\$this->") &&
                afterFirstArrow.contains("->") &&
                !afterFirstArrow.substringAfter("->").trimStart().startsWith("(") &&
                driverPropName != null && driverPropName.isNotEmpty()
            val isModelMethodCall = currentLine.contains("\$this->") &&
                afterFirstArrow.contains("->") &&
                !afterFirstArrow.substringAfter("->").trimStart().startsWith("(") &&
                modelPropName != null && modelPropName.isNotEmpty()
            val isModelCall = currentLine.contains("load->model(")
            val isViewCall = currentLine.contains("load->view(")
            val isDatabaseLoad = currentLine.contains("load->database(")
            val isLibraryCall = currentLine.contains("load->library(")
            val isHelperCall = currentLine.contains("load->helper(")
            val isConfigLoad = currentLine.contains("load->config(")
            val isLanguageLoad = currentLine.contains("load->language(")
            val isDriverLoad = currentLine.contains("load->driver(")
            val isConfigItemCall = currentLine.contains("config->item(")
            val afterConfigArrow = currentLine.substringAfter("config->", "")
            val isConfigMethodCall = currentLine.contains("\$this->config->") && !afterConfigArrow.trimStart().startsWith("(")
            val afterInputArrow = currentLine.substringAfter("input->", "")
            val isInputMethodCall = currentLine.contains("\$this->input->") && !afterInputArrow.trimStart().startsWith("(")
            val isInputKeyCall = currentLine.contains("input->post(") || currentLine.contains("input->get(") ||
                currentLine.contains("input->cookie(") || currentLine.contains("input->post_get(") ||
                currentLine.contains("input->get_post(") || currentLine.contains("input->input_stream(")
            val isInputServerCall = currentLine.contains("input->server(")
            val isInputHeaderCall = currentLine.contains("input->get_request_header(")
            val afterOutputArrow = currentLine.substringAfter("output->", "")
            val isOutputMethodCall = currentLine.contains("\$this->output->") && !afterOutputArrow.trimStart().startsWith("(")
            val isRoutesFile = position.containingFile.name == "routes.php"
            val isRouteValue = isRoutesFile && currentLine.contains("\$route[") && (currentLine.contains("='") || currentLine.contains("=\""))
            val isHooksFile = position.containingFile.name == "hooks.php"
            val isHookPointKey = isHooksFile && currentLine.contains("\$hook[")
            val isHookArrayKey = isHooksFile && currentLine.contains("=>")

            val isDbCall =
                currentLine.contains("db->") ||
                        loadedDatabases.any { currentLine.contains("$it->") }

            // If nothing matches, exit
            if (!isThisCall && !isModelCall && !isViewCall &&
                !isDbCall && !isDatabaseLoad && !isLibraryCall && !isHelperCall &&
                !isConfigLoad && !isLanguageLoad && !isDriverLoad &&
                !isConfigItemCall && !isConfigMethodCall && !isInputKeyCall && !isInputMethodCall && !isInputServerCall &&
                !isInputHeaderCall && !isOutputMethodCall && !isRouteValue && !isLibraryMethodCall && !isDriverMethodCall && !isModelMethodCall && !isBenchmarkMethodCall && !isUriMethodCall && !isSecurityMethodCall && !isPaginationMethodCall && !isFormValidationMethodCall && !isMigrationMethodCall && !isTableMethodCall && !isLoadMethodCall &&
                !isHookPointKey && !isHookArrayKey
            ) {
                return
            }

            /* ---------- $this-> ---------- */
            if (isThisCall) {
                val baseProps = listOf(
                    "load",
                    "db",
                    "input",
                    "session",
                    "config",
                    "uri",
                    "router",
                    "output",
                    "security",
                    "form_validation",
                    "agent",
                    "parser",
                    "trackback",
                    "cache",
                    "benchmark",
                    "javascript",
                    "jquery",
                    "cart",
                    "xmlrpc",
                    "xmlrpcs",
                    "typography"
                )

                for (prop in baseProps) {
                    result.addElement(LookupElementBuilder.create(prop))
                }

                for (db in loadedDatabases) {
                    result.addElement(LookupElementBuilder.create(db))
                }

                for (lib in loadedLibraries) {
                    result.addElement(LookupElementBuilder.create(lib))
                }

                for (driver in loadedDrivers) {
                    result.addElement(LookupElementBuilder.create(driver))
                }

                for (model in loadedModels) {
                    result.addElement(LookupElementBuilder.create(model))
                }
            }

            /* ---------- $this->model_name-> (model methods) ---------- */
            if (isModelMethodCall && modelPropName != null && modelPropName.isNotEmpty()) {
                val methods = findModelMethods(project, modelPropName, fileText)
                for (method in methods) {
                    result.addElement(LookupElementBuilder.create(method))
                }
            }

            /* ---------- $this->benchmark-> (Benchmark class – always loaded) ---------- */
            if (isBenchmarkMethodCall) {
                val methods = getNativeLibraryMembers("benchmark") ?: emptyList()
                for (method in methods) {
                    result.addElement(LookupElementBuilder.create(method))
                }
            }

            /* ---------- $this->uri-> (URI class – always loaded) ---------- */
            if (isUriMethodCall) {
                val methods = getNativeLibraryMembers("uri") ?: emptyList()
                for (method in methods) {
                    result.addElement(LookupElementBuilder.create(method))
                }
            }

            /* ---------- $this->security-> (Security class – always loaded) ---------- */
            if (isSecurityMethodCall) {
                val methods = getNativeLibraryMembers("security") ?: emptyList()
                for (method in methods) {
                    result.addElement(LookupElementBuilder.create(method))
                }
            }

            /* ---------- $this->pagination-> (Pagination class – loaded via load->library) ---------- */
            if (isPaginationMethodCall) {
                val methods = getNativeLibraryMembers("pagination") ?: emptyList()
                for (method in methods) {
                    result.addElement(LookupElementBuilder.create(method))
                }
            }

            /* ---------- $this->form_validation-> (Form Validation class – loaded via load->library) ---------- */
            if (isFormValidationMethodCall) {
                val methods = getNativeLibraryMembers("form_validation") ?: emptyList()
                for (method in methods) {
                    result.addElement(LookupElementBuilder.create(method))
                }
            }

            /* ---------- $this->migration-> (Migration class – loaded via load->library) ---------- */
            if (isMigrationMethodCall) {
                val methods = getNativeLibraryMembers("migration") ?: emptyList()
                for (method in methods) {
                    result.addElement(LookupElementBuilder.create(method))
                }
            }

            /* ---------- $this->table-> (Table class – loaded via load->library) ---------- */
            if (isTableMethodCall) {
                val methods = getNativeLibraryMembers("table") ?: emptyList()
                for (method in methods) {
                    result.addElement(LookupElementBuilder.create(method))
                }
            }

            /* ---------- $this->load-> (Loader / CI_Loader methods) ---------- */
            if (isLoadMethodCall) {
                val loadMethods = listOf(
                    "library", "driver", "view", "vars", "get_var", "get_vars", "clear_vars",
                    "model", "database", "dbforge", "dbutil", "helper", "file", "language",
                    "config", "is_loaded", "add_package_path", "remove_package_path", "get_package_paths"
                )
                for (method in loadMethods) {
                    result.addElement(LookupElementBuilder.create(method))
                }
            }

            /* ---------- $this->library_name-> (library methods) ---------- */
            if (isLibraryMethodCall && libraryPropName != null && libraryPropName.isNotEmpty()) {
                val nativeMethods = getNativeLibraryMembers(libraryPropName)
                val methods = nativeMethods ?: findLibraryMethods(project, libraryPropName)
                for (method in methods) {
                    result.addElement(LookupElementBuilder.create(method))
                }
            }

            /* ---------- $this->driver_name-> (driver methods) ---------- */
            if (isDriverMethodCall && driverPropName != null && driverPropName.isNotEmpty()) {
                val nativeMethods = getNativeLibraryMembers(driverPropName)
                val methods = nativeMethods ?: findDriverMethods(project, driverPropName)
                for (method in methods) {
                    result.addElement(LookupElementBuilder.create(method))
                }
            }

            /* ---------- load->model ---------- */
            if (isModelCall) {
                val models = findModels(project)
                for (model in models) {
                    result.addElement(LookupElementBuilder.create(model))
                }
            }

            /* ---------- load->view ---------- */
            if (isViewCall) {
                val views = CiViewUtils.findViews(project)
                for (view in views) {
                    result.addElement(LookupElementBuilder.create(view))
                }
            }

            /* ---------- load->library ---------- */
            if (isLibraryCall) {
                val standardLibraries = listOf(
                    "session", "form_validation", "email", "pagination", "zip", "unit_test",
                    "upload", "image_lib", "cart", "encrypt", "encryption", "table", "ftp", "xmlrpc", "xmlrpcs",
                    "user_agent", "parser", "trackback", "javascript", "javascript/jquery",
                    "calendar", "language", "typography", "migration"
                )
                for (lib in standardLibraries) {
                    result.addElement(LookupElementBuilder.create(lib))
                }
                val customLibraries = findLibraries(project)
                for (lib in customLibraries) {
                    result.addElement(LookupElementBuilder.create(lib))
                }
            }

            /* ---------- load->helper ---------- */
            if (isHelperCall) {
                val standardHelpers = listOf(
                    "url", "form", "html", "inflector", "language", "number", "path", "security", "smiley", "string", "text", "typography", "date", "array", "directory", "download", "email", "file", "captcha", "cookie", "xml"
                )
                for (h in standardHelpers) {
                    result.addElement(LookupElementBuilder.create(h))
                }
                val customHelpers = findHelpers(project)
                for (h in customHelpers) {
                    result.addElement(LookupElementBuilder.create(h))
                }
            }

            /* ---------- db methods ---------- */
            if (isDbCall) {
                val dbMethods = listOf(
                    "select",
                    "from",
                    "where",
                    "join",
                    "get",
                    "insert",
                    "update",
                    "delete",
                    "order_by",
                    "group_by",
                    "limit",
                    "like",
                    "count_all",
                    "count_all_results"
                )

                for (method in dbMethods) {
                    result.addElement(LookupElementBuilder.create(method))
                }
            }

            /* ---------- load->database ---------- */
            if (isDatabaseLoad) {
                val connections = findDatabaseConnections(project)
                for (conn in connections) {
                    result.addElement(LookupElementBuilder.create(conn))
                }
            }

            /* ---------- load->config ---------- */
            if (isConfigLoad) {
                for (name in findConfigFileNames(project)) {
                    result.addElement(LookupElementBuilder.create(name))
                }
            }

            /* ---------- load->language ---------- */
            if (isLanguageLoad) {
                for (key in findLanguageKeys(project)) {
                    result.addElement(LookupElementBuilder.create(key))
                }
            }

            /* ---------- load->driver ---------- */
            if (isDriverLoad) {
                val drivers = listOf("cache") + findCustomDrivers(project)
                for (d in drivers.distinct()) {
                    result.addElement(LookupElementBuilder.create(d))
                }
            }

            /* ---------- config->item ---------- */
            if (isConfigItemCall) {
                val keys = findConfigKeys(project)
                for (key in keys) {
                    result.addElement(LookupElementBuilder.create(key))
                }
            }

            /* ---------- $this->config-> (Config class – always loaded) ---------- */
            if (isConfigMethodCall) {
                val configMethods = listOf(
                    "item",
                    "set_item",
                    "slash_item",
                    "load",
                    "site_url",
                    "base_url",
                    "system_url"
                )
                for (method in configMethods) {
                    result.addElement(LookupElementBuilder.create(method))
                }
            }

            /* ---------- $this->input-> (request handling methods) ---------- */
            if (isInputMethodCall) {
                val inputMethods = listOf(
                    "post",
                    "get",
                    "post_get",
                    "get_post",
                    "cookie",
                    "server",
                    "user_agent",
                    "ip_address",
                    "valid_ip",
                    "method",
                    "request_headers",
                    "get_request_header",
                    "input_stream",
                    "set_cookie",
                    "is_ajax_request",
                    "is_cli_request"
                )
                for (method in inputMethods) {
                    result.addElement(LookupElementBuilder.create(method))
                }
            }

            /* ---------- input->post / input->get / input->cookie (keys) ---------- */
            if (isInputKeyCall) {
                val keys = findInputKeys(project)
                for (key in keys) {
                    result.addElement(LookupElementBuilder.create(key))
                }
            }

            /* ---------- input->server ($_SERVER keys) ---------- */
            if (isInputServerCall) {
                val serverKeys = getServerKeys(project)
                for (key in serverKeys) {
                    result.addElement(LookupElementBuilder.create(key))
                }
            }

            /* ---------- input->get_request_header (HTTP headers) ---------- */
            if (isInputHeaderCall) {
                for (header in getRequestHeaderNames()) {
                    result.addElement(LookupElementBuilder.create(header))
                }
            }

            /* ---------- $this->output-> (Output class – always loaded) ---------- */
            if (isOutputMethodCall) {
                val outputMethods = listOf(
                    "get_output",
                    "set_output",
                    "append_output",
                    "set_header",
                    "set_status_header",
                    "set_content_type",
                    "enable_profiler",
                    "set_cache",
                    "cache"
                )
                for (method in outputMethods) {
                    result.addElement(LookupElementBuilder.create(method))
                }
            }

            /* ---------- routing: controller/method in routes.php ---------- */
            if (isRouteValue) {
                val controllerMethods = findControllerMethodsForRoutes(project)
                for (cm in controllerMethods) {
                    result.addElement(LookupElementBuilder.create(cm))
                }
            }

            /* ---------- hooks.php: hook point names ($hook['...']) ---------- */
            if (isHookPointKey) {
                for (point in HOOK_POINT_NAMES) {
                    result.addElement(LookupElementBuilder.create(point))
                }
            }

            /* ---------- hooks.php: hook array keys ('class', 'function', 'filename', 'filepath', 'params') ---------- */
            if (isHookArrayKey) {
                for (key in HOOK_ARRAY_KEYS) {
                    result.addElement(LookupElementBuilder.create(key))
                }
            }
        }

    }
}

/** CI3 hook points (application/config/hooks.php). */
private val HOOK_POINT_NAMES = listOf(
    "pre_system",
    "pre_controller",
    "post_controller_constructor",
    "post_controller",
    "display_override",
    "cache_override",
    "post_system"
)

/** Keys for hook definition array: class, function, filename, filepath, params. */
private val HOOK_ARRAY_KEYS = listOf("class", "function", "filename", "filepath", "params")

/* ---------------- MODELS ---------------- */

fun findModels(project: Project): List<String> {
    val result = mutableListOf<String>()
    val baseDir = project.guessProjectBaseDir() ?: return emptyList()
    val modelsDir = baseDir.findChild("application")
        ?.findChild("models") ?: return emptyList()
    collectModels(modelsDir, "", result)
    return result
}

/** pathPrefix: relative path from models dir, e.g. "blog/" for subdir. CI3 uses lowercase in load->model('blog/queries'). */
private fun collectModels(dir: VirtualFile, pathPrefix: String, result: MutableList<String>) {
    for (file in dir.children) {
        if (file.isDirectory) {
            collectModels(file, pathPrefix + file.name.lowercase() + "/", result)
        } else if (file.name.endsWith(".php")) {
            result.add(pathPrefix + file.nameWithoutExtension)
        }
    }
}

/** From file: load->model('X') or load->model('X', 'alias') → property name on $this -> model class/path for resolution.
 * For subdir paths (e.g. 'blog/queries') CI3 uses last segment as property name ($this->queries); we keep path for resolveModelFile. */
fun findLoadedModelClasses(fileText: String): Map<String, String> {
    val result = mutableMapOf<String, String>()
    val regex = Regex("load->model\\s*\\(\\s*['\"]([^'\"]+)['\"]\\s*(?:,\\s*['\"]([^'\"]+)['\"])?\\s*\\)")
    regex.findAll(fileText).forEach { m ->
        val modelClass = m.groupValues[1].trim()
        val alias = m.groupValues[2].trim()
        val propertyName = when {
            alias.isNotEmpty() -> alias
            "/" in modelClass -> modelClass.substringAfterLast("/")
            else -> modelClass
        }
        result[propertyName] = modelClass
    }
    return result
}

/** Class name declared in the model file (for type provider when model path is e.g. blog/queries). */
fun getModelClassNameFromFile(project: Project, modelPathOrClass: String): String? {
    val file = resolveModelFile(project, modelPathOrClass) ?: return null
    val text = String(file.contentsToByteArray())
    val match = Regex("\\bclass\\s+(\\w+)\\s+extends").find(text) ?: return null
    return match.groupValues[1]
}

/** Public (and protected) methods from the model file (by $this property name, e.g. Order_model or alias). */
fun findModelMethods(project: Project, modelPropertyName: String, fileText: String): List<String> {
    val loadedModels = findLoadedModelClasses(fileText)
    val modelPathOrClass = loadedModels[modelPropertyName] ?: return emptyList()
    val modelFile = resolveModelFile(project, modelPathOrClass) ?: return emptyList()
    val text = String(modelFile.contentsToByteArray())
    val regex = Regex("(?:public|protected)\\s+function\\s+(\\w+)\\s*\\(")
    return regex.findAll(text).map { it.groupValues[1] }.distinct().sorted().toList()
}

/** Models directory (application/models) or null. */
fun getModelsDir(project: Project): VirtualFile? {
    val baseDir = project.guessProjectBaseDir() ?: return null
    return baseDir.findChild("application")?.findChild("models")
}

/** Model path for load->model() from a file under application/models (e.g. blog/Queries.php → "blog/queries"). */
fun getModelNameForFile(project: Project, file: VirtualFile): String? {
    val modelsDir = getModelsDir(project) ?: return null
    val basePath = modelsDir.path
    val path = file.path
    if (!path.startsWith(basePath)) return null
    val relative = path.removePrefix(basePath).trimStart('/').removeSuffix(".php")
    if (relative.isEmpty()) return null
    return relative.split("/").joinToString("/") { it.lowercase() }
}

/** All load->model('name') usages of the given model path in the project. */
fun findModelUsages(project: Project, modelName: String): List<PsiElement> {
    val baseDir = project.guessProjectBaseDir() ?: return emptyList()
    val result = mutableListOf<PsiElement>()
    fun scanDir(dir: VirtualFile) {
        for (child in dir.children) {
            if (child.isDirectory) {
                scanDir(child)
            } else if (child.name.endsWith(".php")) {
                val psiFile = com.intellij.psi.PsiManager.getInstance(project).findFile(child) ?: return
                psiFile.accept(object : com.jetbrains.php.lang.psi.visitors.PhpElementVisitor() {
                    override fun visitPhpMethodReference(reference: com.jetbrains.php.lang.psi.elements.MethodReference) {
                        val methodName = reference.name ?: return
                        if (methodName != "model") return
                        val classRef = reference.classReference as? com.jetbrains.php.lang.psi.elements.MethodReference ?: return
                        if (classRef.name != "load") return
                        val root = classRef.classReference as? com.jetbrains.php.lang.psi.elements.Variable ?: return
                        if (root.name != "this") return
                        val params = reference.parameterList as? com.jetbrains.php.lang.psi.elements.ParameterList ?: return
                        val first = params.parameters.firstOrNull() as? com.jetbrains.php.lang.psi.elements.StringLiteralExpression ?: return
                        val name = first.contents.trim().trim('\'', '"')
                        if (name.equals(modelName, true)) result.add(first)
                    }
                })
            }
        }
    }
    scanDir(baseDir)
    return result
}

/** Model file in application/models/ (e.g. Order_model or blog/queries). Supports subdirs; path is relative to models. */
fun resolveModelFile(project: Project, modelPathOrClass: String): VirtualFile? {
    val baseDir = project.guessProjectBaseDir() ?: return null
    val modelsDir = baseDir.findChild("application")?.findChild("models") ?: return null
    val path = modelPathOrClass.trim()
    if (path.isEmpty()) return null
    val segments = path.split("/")
    if (segments.size == 1) {
        val fileName = "$path.php"
        modelsDir.findFileByRelativePath(fileName)?.let { return it }
        fun findRecursive(dir: VirtualFile): VirtualFile? {
            dir.findChild(fileName)?.let { return it }
            for (child in dir.children) {
                if (child.isDirectory) findRecursive(child)?.let { return it }
            }
            return null
        }
        return findRecursive(modelsDir)
    }
    var dir: VirtualFile = modelsDir
    for (i in 0 until segments.size - 1) {
        val segment = segments[i]
        val next = dir.findChild(segment) ?: dir.children.find { it.isDirectory && it.name.equals(segment, true) }
        if (next == null || !next.isDirectory) return null
        dir = next
    }
    val lastSegment = segments.last()
    val exact = dir.findChild("$lastSegment.php")
    if (exact != null) return exact
    dir.children.find { it.name.endsWith(".php") && it.nameWithoutExtension.equals(lastSegment, true) }?.let { return it }
    return null
}

/* ---------------- DATABASE CONFIG ---------------- */

fun findDatabaseConnections(project: Project): List<String> {
    val baseDir = project.guessProjectBaseDir() ?: return emptyList()

    val dbConfig = baseDir.findChild("application")
        ?.findChild("config")
        ?.findChild("database.php")
        ?: return emptyList()

    val text = String(dbConfig.contentsToByteArray())

    val regex = Regex("\\\$db\\['([^']+)'\\]")
    return regex.findAll(text)
        .map { it.groupValues[1] }
        .distinct()
        .toList()
}

fun findLoadedDatabases(fileText: String): List<String> {
    val regex = Regex("load->database\\('([^']+)'")
    return regex.findAll(fileText)
        .map { it.groupValues[1] }
        .distinct()
        .toList()
}

/* ---------------- BIBLIOTEKI (LIBRARIES) ---------------- */

fun findLibraries(project: Project): List<String> {
    val result = mutableListOf<String>()
    val baseDir = project.guessProjectBaseDir() ?: return emptyList()
    val libsDir = baseDir.findChild("application")
        ?.findChild("libraries") ?: return emptyList()
    collectLibraries(libsDir, result)
    return result.distinct().sorted()
}

private fun collectLibraries(dir: VirtualFile, result: MutableList<String>) {
    for (file in dir.children) {
        if (file.isDirectory) {
            collectLibraries(file, result)
        } else if (file.name.endsWith(".php")) {
            result.add(file.nameWithoutExtension)
        }
    }
}

/** From file: load->library('X') or load->library('X', ..., 'alias') → property names on $this (alias or lowercase X). */
fun findLoadedLibraries(fileText: String): List<String> {
    val result = mutableSetOf<String>()
    // Group 1: library name, group 2: optional alias (third parameter)
    val regex = Regex("load->library\\s*\\(\\s*['\"]([^'\"]+)['\"]\\s*(?:,\\s*[^)]*)?(?:,\\s*['\"]([^'\"]+)['\"])?\\s*\\)")
    regex.findAll(fileText).forEach { m ->
        val alias = m.groupValues[2].trim()
        val libName = m.groupValues[1].trim().lowercase().replace("-", "_")
        val propName = if (alias.isNotEmpty()) alias else libName
        result.add(propName)
        // CI3 driver-style load: 'javascript/jquery' → $this->jquery; add segment after last /
        if (propName == libName && "/" in libName) {
            result.add(libName.substringAfterLast("/"))
        }
    }
    return result.distinct().sorted()
}

/** From file: load->driver('X') or load->driver('X', ..., 'alias') → property names on $this (alias or lowercase X). */
fun findLoadedDrivers(fileText: String): List<String> {
    val result = mutableSetOf<String>()
    // Group 1: driver name, group 2: optional alias (third parameter)
    val regex = Regex("load->driver\\s*\\(\\s*['\"]([^'\"]+)['\"]\\s*(?:,\\s*[^)]*)?(?:,\\s*['\"]([^'\"]+)['\"])?\\s*\\)")
    regex.findAll(fileText).forEach { m ->
        val alias = m.groupValues[2].trim()
        val propName = if (alias.isNotEmpty()) alias else m.groupValues[1].trim().lowercase().replace("-", "_")
        result.add(propName)
    }
    return result.distinct().sorted()
}/**
 * Members (methods + properties) of native CI3 libraries loaded with load->library('name').
 * Returns null for custom libraries (use findLibraryMethods for application/libraries/).
 */
fun getNativeLibraryMembers(libraryPropertyName: String): List<String>? {
    return when (libraryPropertyName) {
        "zip" -> listOf(
            "compression_level",
            "add_data", "add_dir", "read_file", "read_dir",
            "archive", "download", "get_zip", "clear_data"
        )
        "session" -> listOf(
            "userdata", "set_userdata", "has_userdata", "unset_userdata",
            "flashdata", "set_flashdata", "mark_as_flash", "keep_flashdata",
            "set_tempdata", "tempdata", "mark_as_temp", "unset_tempdata",
            "session_id", "sess_destroy"
        )
        "agent" -> listOf(
            "is_browser", "is_mobile", "is_robot", "is_referral",
            "browser", "version", "mobile", "robot", "platform",
            "referrer", "agent_string", "accept_lang", "languages",
            "accept_charset", "charsets", "parse"
        )
        "parser" -> listOf(
            "parse", "parse_string", "set_delimiters"
        )
        "trackback" -> listOf(
            "send", "receive", "send_error", "send_success", "data",
            "process", "extract_urls", "validate_url", "get_id",
            "convert_xml", "limit_characters", "convert_ascii",
            "set_error", "display_errors"
        )
        "cache" -> listOf(
            "is_supported", "get", "save", "delete",
            "increment", "decrement", "clean",
            "cache_info", "get_metadata",
            "apc", "file", "memcached", "wincache", "redis", "dummy"
        )
        "benchmark" -> listOf("mark", "elapsed_time", "memory_usage")
        "uri" -> listOf(
            "segment", "rsegment", "slash_segment", "slash_rsegment",
            "uri_to_assoc", "ruri_to_assoc", "assoc_to_uri",
            "uri_string", "ruri_string",
            "total_segments", "total_rsegments",
            "segment_array", "rsegment_array"
        )
        "security" -> listOf(
            "xss_clean", "sanitize_filename", "get_csrf_token_name",
            "get_csrf_hash", "entity_decode", "get_random_bytes"
        )
        "pagination" -> listOf(
            "initialize", "create_links"
        )
        "form_validation" -> listOf(
            "set_rules", "set_data", "set_message", "set_error_delimiters",
            "error", "error_array", "error_string", "run", "reset_validation",
            "has_rule", "set_value", "set_select", "set_radio", "set_checkbox"
        )
        "migration" -> listOf(
            "current", "error_string", "find_migrations", "latest", "version"
        )
        "table" -> listOf(
            "generate", "set_caption", "set_heading", "add_row",
            "make_columns", "set_template", "set_empty", "clear"
        )
        "calendar" -> listOf(
            "initialize", "generate", "get_month_name", "get_day_names",
            "adjust_date", "get_total_days", "default_template", "parse_template"
        )
        "javascript", "jquery" -> listOf(
            "compile", "script", "clear_js", "external", "inline",
            "blur", "change", "click", "dblclick", "error", "focus", "hover",
            "keydown", "keyup", "load", "mousedown", "mouseup", "mouseover",
            "resize", "scroll", "unload",
            "hide", "show", "toggle", "animate", "fadeIn", "fadeOut", "toggleClass",
            "slideUp", "slideDown", "slideToggle", "effect",
            "corner", "tablesorter", "modal", "calendar"
        )
        "email" -> listOf(
            "from", "reply_to", "to", "cc", "bcc", "subject", "message",
            "set_alt_message", "set_header", "clear", "send", "attach",
            "attachment_cid", "print_debugger", "initialize"
        )
        "typography" -> listOf(
            "auto_typography", "format_characters", "nl2br_except_pre", "protect_braced_quotes"
        )
        "unit", "unit_test" -> listOf(
            "run", "report", "result", "use_strict", "active",
            "set_test_items", "set_template"
        )
        "lang" -> listOf("load", "line")
        "language" -> listOf("load", "line")
        "upload" -> listOf(
            "initialize", "do_upload", "data", "display_errors",
            "set_upload_path", "set_filename", "set_max_filesize", "set_max_filename",
            "set_max_width", "set_max_height", "set_min_width", "set_min_height",
            "set_allowed_types", "set_image_properties", "set_xss_clean", "set_error",
            "is_image", "is_allowed_filetype", "is_allowed_filesize", "is_allowed_dimensions",
            "validate_upload_path", "get_extension", "limit_filename_length", "do_xss_clean"
        )
        "image_lib" -> listOf(
            "initialize", "resize", "crop", "rotate", "watermark",
            "clear", "display_errors"
        )
        "encrypt" -> listOf(
            "encode", "decode", "set_cipher", "set_mode", "encode_from_legacy"
        )
        "encryption" -> listOf(
            "initialize", "encrypt", "decrypt", "create_key", "hkdf"
        )
        "ftp" -> listOf(
            "connect", "upload", "download", "rename", "move",
            "delete_file", "delete_dir", "list_files", "mirror",
            "mkdir", "chmod", "changedir", "close"
        )
        "cart" -> listOf(
            "insert", "update", "remove", "total", "total_items",
            "contents", "get_item", "has_options", "product_options",
            "format_number", "destroy"
        )
        "xmlrpc" -> listOf(
            "initialize", "server", "timeout", "method", "request",
            "send_request", "display_error", "display_response",
            "send_error_message", "send_response"
        )
        "xmlrpcs" -> listOf("initialize", "serve")
        else -> null
    }
}

/** Public (and protected) methods from the library file in application/libraries/ (by property name, e.g. my_lib). */
fun findLibraryMethods(project: Project, libraryPropertyName: String): List<String> {
    val libFile = findLibraryFileByPropertyName(project, libraryPropertyName) ?: return emptyList()
    val text = String(libFile.contentsToByteArray())
    val regex = Regex("(?:public|protected)\\s+function\\s+(\\w+)\\s*\\(")
    return regex.findAll(text).map { it.groupValues[1] }.distinct().sorted().toList()
}

/** Public (and protected) methods from the driver parent class in application/libraries/ (by property name, e.g. cache). */
fun findDriverMethods(project: Project, driverPropertyName: String): List<String> {
    val driverFile = findDriverFileByPropertyName(project, driverPropertyName) ?: return emptyList()
    val text = String(driverFile.contentsToByteArray())
    val regex = Regex("(?:public|protected)\\s+function\\s+(\\w+)\\s*\\(")
    return regex.findAll(text).map { it.groupValues[1] }.distinct().sorted().toList()
}

private fun findDriverFileByPropertyName(project: Project, propertyName: String): VirtualFile? {
    val baseDir = project.guessProjectBaseDir() ?: return null
    val libsDir = baseDir.findChild("application")?.findChild("libraries") ?: return null
    val withUcfirst = propertyName.split("_").joinToString("_") { it.replaceFirstChar { c -> c.uppercaseChar() } }
    fun findRecursive(dir: VirtualFile, fileName: String): VirtualFile? {
        dir.findChild(fileName)?.let { return it }
        for (child in dir.children) {
            if (child.isDirectory) {
                // Check if it's a driver directory (e.g. Cache/Cache.php)
                val driverDir = child.findChild("drivers")
                if (driverDir != null) {
                    // Parent class is in the driver directory's parent
                    child.findChild(fileName)?.let { return it }
                }
                findRecursive(child, fileName)?.let { return it }
            }
        }
        return null
    }
    return findRecursive(libsDir, "$withUcfirst.php")
        ?: findRecursive(libsDir, "${propertyName.lowercase()}.php")
}

private fun findLibraryFileByPropertyName(project: Project, propertyName: String): VirtualFile? {
    val baseDir = project.guessProjectBaseDir() ?: return null
    val libsDir = baseDir.findChild("application")?.findChild("libraries") ?: return null
    val withUcfirst = propertyName.split("_").joinToString("_") { it.replaceFirstChar { c -> c.uppercaseChar() } }
    fun findRecursive(dir: VirtualFile, fileName: String): VirtualFile? {
        dir.findChild(fileName)?.let { return it }
        for (child in dir.children) {
            if (child.isDirectory) findRecursive(child, fileName)?.let { return it }
        }
        return null
    }
    return findRecursive(libsDir, "$withUcfirst.php")
        ?: findRecursive(libsDir, "${propertyName.lowercase()}.php")
}

/** Class name of a custom library in application/libraries/ (file name without .php). Returns null if not found. */
fun getLibraryClassName(project: Project, propertyName: String): String? =
    findLibraryFileByPropertyName(project, propertyName)?.nameWithoutExtension

/* ---------------- HELPERS ---------------- */

fun findHelpers(project: Project): List<String> {
    val result = mutableListOf<String>()
    val baseDir = project.guessProjectBaseDir() ?: return emptyList()
    val helpersDir = baseDir.findChild("application")
        ?.findChild("helpers") ?: return emptyList()
    collectHelpers(helpersDir, result)
    return result.distinct().sorted()
}

private fun collectHelpers(dir: VirtualFile, result: MutableList<String>) {
    for (file in dir.children) {
        if (file.isDirectory) {
            collectHelpers(file, result)
        } else if (file.name.endsWith("_helper.php")) {
            val name = file.name.removeSuffix("_helper.php")
            result.add(name)
        }
    }
}

/** Native CI3 helpers that have stubs in .ci3-helper/stubs/ for "Go to Declaration" when not overridden in application/helpers. */
private val NATIVE_HELPERS_WITH_STUBS = setOf("array", "captcha", "cookie", "date", "directory", "download", "email", "file", "form", "html", "inflector", "language", "number", "path", "security", "smiley", "string", "text", "typography", "url", "xml")

/** Helper file in application/helpers/ (e.g. form → application/helpers/form_helper.php). Also searches subdirectories. Falls back to .ci3-helper/stubs/ for native helpers. */
fun resolveHelperFile(project: Project, helperName: String): VirtualFile? {
    val baseDir = project.guessProjectBaseDir() ?: return null
    val helpersDir = baseDir.findChild("application")?.findChild("helpers")
    val fileName = "${helperName}_helper.php"
    if (helpersDir != null) {
        helpersDir.findChild(fileName)?.let { return it }
        fun findRecursive(dir: VirtualFile): VirtualFile? {
            dir.findChild(fileName)?.let { return it }
            for (child in dir.children) {
                if (child.isDirectory) findRecursive(child)?.let { return it }
            }
            return null
        }
        findRecursive(helpersDir)?.let { return it }
    }
    if (helperName in NATIVE_HELPERS_WITH_STUBS) {
        val stubFile = File(baseDir.path, ".ci3-helper/stubs/$fileName")
        return LocalFileSystem.getInstance().refreshAndFindFileByPath(stubFile.absolutePath)
    }
    return null
}

/* ---------------- CONFIG FILES (load->config) ---------------- */

fun findConfigFileNames(project: Project): List<String> {
    val baseDir = project.guessProjectBaseDir() ?: return emptyList()
    val configDir = baseDir.findChild("application")
        ?.findChild("config") ?: return emptyList()
    return configDir.children
        .filter { !it.isDirectory && it.name.endsWith(".php") }
        .map { it.nameWithoutExtension }
        .sorted()
}

/** Config file application/config/{name}.php for load->config('name'). */
fun resolveConfigFile(project: Project, configName: String): VirtualFile? {
    val baseDir = project.guessProjectBaseDir() ?: return null
    val configDir = baseDir.findChild("application")?.findChild("config") ?: return null
    return configDir.findChild("$configName.php")
}

/* ---------------- LANGUAGE FILES (load->language) ---------------- */

fun findLanguageKeys(project: Project): List<String> {
    val result = mutableSetOf<String>()
    val baseDir = project.guessProjectBaseDir() ?: return emptyList()
    val langDir = baseDir.findChild("application")
        ?.findChild("language") ?: return emptyList()
    for (localeDir in langDir.children) {
        if (!localeDir.isDirectory) continue
        val locale = localeDir.name
        for (file in localeDir.children) {
            if (!file.isDirectory && file.name.endsWith("_lang.php")) {
                val baseName = file.name.removeSuffix("_lang.php")
                result.add("$locale/$baseName")
                result.add(baseName)
            }
        }
    }
    return result.sorted()
}

/* ---------------- DRIVERS (load->driver) - cache + custom ---------------- */

fun findCustomDrivers(project: Project): List<String> {
    val result = mutableSetOf<String>()
    val baseDir = project.guessProjectBaseDir() ?: return emptyList()
    val libsDir = baseDir.findChild("application")
        ?.findChild("libraries") ?: return emptyList()
    for (file in libsDir.children) {
        if (file.isDirectory) continue
        if (file.name.endsWith("_driver.php")) {
            result.add(file.nameWithoutExtension.removeSuffix("_driver"))
        }
    }
    return result.sorted()
}

/* ---------------- CONFIG KEYS ($this->config->item) ---------------- */

fun findConfigKeys(project: Project): List<String> {
    val result = mutableSetOf<String>()
    val baseDir = project.guessProjectBaseDir() ?: return emptyList()
    val configDir = baseDir.findChild("application")
        ?.findChild("config") ?: return emptyList()

    for (file in configDir.children) {
        if (!file.isDirectory && file.name.endsWith(".php")) {
            val text = String(file.contentsToByteArray())
            val regex = Regex("\\\$config\\s*\\[\\s*['\"]([^'\"]+)['\"]\\s*\\]")
            regex.findAll(text).forEach { result.add(it.groupValues[1]) }
        }
    }
    return result.sorted()
}

/* ---------------- INPUT KEYS (post/get from forms and project) ---------------- */

fun findInputKeys(project: Project): List<String> {
    val result = mutableSetOf<String>()
    val baseDir = project.guessProjectBaseDir() ?: return emptyList()
    val appDir = baseDir.findChild("application") ?: return emptyList()

    fun scanFile(content: String, patterns: List<Regex>) {
        for (re in patterns) {
            re.findAll(content).forEach { result.add(it.groupValues[1]) }
        }
    }

    val formPatterns = listOf(
        Regex("form_input\\s*\\(\\s*['\"]([^'\"]+)['\"]"),
        Regex("form_hidden\\s*\\(\\s*['\"]([^'\"]+)['\"]"),
        Regex("set_value\\s*\\(\\s*['\"]([^'\"]+)['\"]"),
        Regex("set_checkbox\\s*\\(\\s*['\"]([^'\"]+)['\"]"),
        Regex("set_radio\\s*\\(\\s*['\"]([^'\"]+)['\"]"),
        Regex("form_dropdown\\s*\\(\\s*['\"]([^'\"]+)['\"]"),
        Regex("->post\\s*\\(\\s*['\"]([^'\"]+)['\"]"),
        Regex("->get\\s*\\(\\s*['\"]([^'\"]+)['\"]"),
        Regex("->cookie\\s*\\(\\s*['\"]([^'\"]+)['\"]"),
        Regex("->post_get\\s*\\(\\s*['\"]([^'\"]+)['\"]"),
        Regex("->get_post\\s*\\(\\s*['\"]([^'\"]+)['\"]"),
        Regex("->input_stream\\s*\\(\\s*['\"]([^'\"]+)['\"]"),
        Regex("set_cookie\\s*\\(\\s*['\"]([^'\"]+)['\"]"),
        Regex("get_cookie\\s*\\(\\s*['\"]([^'\"]+)['\"]")
    )

    fun scanDir(dir: VirtualFile) {
        for (file in dir.children) {
            if (file.isDirectory) {
                scanDir(file)
            } else if (file.name.endsWith(".php")) {
                val text = String(file.contentsToByteArray())
                scanFile(text, formPatterns)
            }
        }
    }
    scanDir(appDir)
    return result.sorted()
}

/* ---------------- INPUT->SERVER ($_SERVER keys) ---------------- */

fun getServerKeys(project: Project): List<String> {
    val result = mutableSetOf<String>()
    val baseDir = project.guessProjectBaseDir() ?: return emptyList()
    val appDir = baseDir.findChild("application") ?: return emptyList()

    val standardServerKeys = listOf(
        "REQUEST_URI", "REQUEST_METHOD", "HTTP_HOST", "HTTP_USER_AGENT",
        "SERVER_NAME", "QUERY_STRING", "REMOTE_ADDR", "SCRIPT_NAME",
        "DOCUMENT_ROOT", "HTTP_REFERER", "HTTP_ACCEPT", "HTTP_ACCEPT_LANGUAGE",
        "HTTP_X_REQUESTED_WITH", "HTTPS", "SERVER_PORT", "SERVER_PROTOCOL",
        "PATH_INFO", "PHP_SELF", "HTTP_COOKIE", "HTTP_CONNECTION",
        "CONTENT_TYPE", "CONTENT_LENGTH", "HTTP_ACCEPT_ENCODING"
    )
    result.addAll(standardServerKeys)

    val serverRegex = Regex("\\\$_SERVER\\s*\\[\\s*['\"]([^'\"]+)['\"]\\s*\\]")
    fun scanDir(dir: VirtualFile) {
        for (file in dir.children) {
            if (file.isDirectory) scanDir(file)
            else if (file.name.endsWith(".php")) {
                serverRegex.findAll(String(file.contentsToByteArray())).forEach {
                    result.add(it.groupValues[1])
                }
            }
        }
    }
    scanDir(appDir)
    return result.sorted()
}

/* ---------------- REQUEST HEADERS (get_request_header) ---------------- */

fun getRequestHeaderNames(): List<String> = listOf(
    "Content-Type",
    "Content-Length",
    "Accept",
    "Accept-Language",
    "Accept-Encoding",
    "Authorization",
    "X-Requested-With",
    "X-Forwarded-For",
    "X-Forwarded-Proto",
    "Host",
    "User-Agent",
    "Referer",
    "Origin",
    "Cache-Control",
    "Cookie",
    "Connection"
)

/* ---------------- ROUTING (controller/method in routes.php) ---------------- */

fun findControllerMethodsForRoutes(project: Project): List<String> {
    val result = mutableListOf<String>()
    val baseDir = project.guessProjectBaseDir() ?: return emptyList()
    val controllersDir = baseDir.findChild("application")
        ?.findChild("controllers") ?: return emptyList()

    collectControllerRoutes(controllersDir, "", result)
    return result.distinct().sorted()
}

/**
 * Recursively collects route segments for controllers (including subdirectories).
 * CI3: application/controllers/products/Shoes.php -> "products/shoes", "products/shoes/show", ...
 */
private fun collectControllerRoutes(dir: VirtualFile, pathPrefix: String, result: MutableList<String>) {
    for (file in dir.children) {
        if (file.isDirectory) {
            collectControllerRoutes(file, "$pathPrefix${file.name}/", result)
        } else if (file.name.endsWith(".php")) {
            val segment = file.nameWithoutExtension.lowercase()
            val routePrefix = if (pathPrefix.isEmpty()) segment else pathPrefix.lowercase() + segment
            val methods = findControllerMethods(file)
            result.add(routePrefix)
            for (method in methods) {
                result.add("$routePrefix/$method")
            }
        }
    }
}

private fun findControllerMethods(controllerFile: VirtualFile): List<String> {
    val text = String(controllerFile.contentsToByteArray())
    val regex = Regex("function\\s+(\\w+)\\s*\\(")
    return regex.findAll(text).map { it.groupValues[1] }.toList()
}
