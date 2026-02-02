package dev.woytkowiak.ci.helper.ci.completion

import com.intellij.codeInsight.completion.*
import com.intellij.codeInsight.lookup.LookupElementBuilder
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.patterns.PlatformPatterns
import com.intellij.util.ProcessingContext

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
            val position = parameters.position
            val fileText = position.containingFile.text
            val offset = parameters.offset

            val start = maxOf(0, offset - 150)
            val beforeCursor = fileText.substring(start, offset)

            val currentLine = beforeCursor.substringAfterLast('\n')
            val project = position.project

            val loadedDatabases = findLoadedDatabases(fileText)
            val loadedLibraries = findLoadedLibraries(fileText)

            val isThisCall =
                currentLine.contains("\$this->") &&
                        !currentLine.substringAfter("\$this->").contains("->")
            val afterFirstArrow = currentLine.substringAfter("\$this->", "")
            val libraryPropName = afterFirstArrow.substringBefore("->").trim().takeIf { it.isNotEmpty() }
            val isLibraryMethodCall = currentLine.contains("\$this->") &&
                afterFirstArrow.contains("->") &&
                !afterFirstArrow.substringAfter("->").trimStart().startsWith("(") &&
                libraryPropName != null && libraryPropName in loadedLibraries
            val isModelCall = currentLine.contains("load->model(")
            val isViewCall = currentLine.contains("load->view(")
            val isDatabaseLoad = currentLine.contains("load->database(")
            val isLibraryCall = currentLine.contains("load->library(")
            val isHelperCall = currentLine.contains("load->helper(")
            val isConfigItemCall = currentLine.contains("config->item(")
            val afterInputArrow = currentLine.substringAfter("input->", "")
            val isInputMethodCall = currentLine.contains("\$this->input->") && !afterInputArrow.trimStart().startsWith("(")
            val isInputKeyCall = currentLine.contains("input->post(") || currentLine.contains("input->get(") ||
                currentLine.contains("input->cookie(")
            val isInputServerCall = currentLine.contains("input->server(")
            val isInputHeaderCall = currentLine.contains("input->get_request_header(")
            val isRoutesFile = position.containingFile.name == "routes.php"
            val isRouteValue = isRoutesFile && currentLine.contains("\$route[") && (currentLine.contains("='") || currentLine.contains("=\""))

            val isDbCall =
                currentLine.contains("db->") ||
                        loadedDatabases.any { currentLine.contains("$it->") }

            // Jeśli nic nie pasuje, wychodzimy
            if (!isThisCall && !isModelCall && !isViewCall &&
                !isDbCall && !isDatabaseLoad && !isLibraryCall && !isHelperCall &&
                !isConfigItemCall && !isInputKeyCall && !isInputMethodCall && !isInputServerCall &&
                !isInputHeaderCall && !isRouteValue && !isLibraryMethodCall
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
                    "form_validation"
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
            }

            /* ---------- $this->nazwa_libki-> (metody biblioteki) ---------- */
            if (isLibraryMethodCall && libraryPropName != null && libraryPropName.isNotEmpty()) {
                val methods = findLibraryMethods(project, libraryPropName)
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
                val views = findViews(project)
                for (view in views) {
                    result.addElement(LookupElementBuilder.create(view))
                }
            }

            /* ---------- load->library ---------- */
            if (isLibraryCall) {
                val standardLibraries = listOf(
                    "session", "form_validation", "email", "pagination",
                    "upload", "image_lib", "cart", "encryption", "table", "ftp", "xmlrpc"
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
                    "url", "form", "html", "text", "date", "array", "file"
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

            /* ---------- config->item ---------- */
            if (isConfigItemCall) {
                val keys = findConfigKeys(project)
                for (key in keys) {
                    result.addElement(LookupElementBuilder.create(key))
                }
            }

            /* ---------- $this->input-> (metody obsługi requestów) ---------- */
            if (isInputMethodCall) {
                val inputMethods = listOf(
                    "post",
                    "get",
                    "get_post",
                    "cookie",
                    "server",
                    "user_agent",
                    "ip_address",
                    "valid_ip",
                    "method",
                    "request_headers",
                    "get_request_header",
                    "input_stream"
                )
                for (method in inputMethods) {
                    result.addElement(LookupElementBuilder.create(method))
                }
            }

            /* ---------- input->post / input->get / input->cookie (klucze) ---------- */
            if (isInputKeyCall) {
                val keys = findInputKeys(project)
                for (key in keys) {
                    result.addElement(LookupElementBuilder.create(key))
                }
            }

            /* ---------- input->server (klucze $_SERVER) ---------- */
            if (isInputServerCall) {
                val serverKeys = getServerKeys(project)
                for (key in serverKeys) {
                    result.addElement(LookupElementBuilder.create(key))
                }
            }

            /* ---------- input->get_request_header (nagłówki HTTP) ---------- */
            if (isInputHeaderCall) {
                for (header in getRequestHeaderNames()) {
                    result.addElement(LookupElementBuilder.create(header))
                }
            }

            /* ---------- routing: controller/method w routes.php ---------- */
            if (isRouteValue) {
                val controllerMethods = findControllerMethodsForRoutes(project)
                for (cm in controllerMethods) {
                    result.addElement(LookupElementBuilder.create(cm))
                }
            }
        }

    }
}

/* ---------------- MODELE ---------------- */

fun findModels(project: Project): List<String> {
    val result = mutableListOf<String>()

    val baseDir = project.baseDir ?: return emptyList()
    val modelsDir = baseDir.findChild("application")
        ?.findChild("models") ?: return emptyList()

    collectModels(modelsDir, result)

    return result
}

fun collectModels(dir: VirtualFile, result: MutableList<String>) {
    for (file in dir.children) {
        if (file.isDirectory) {
            collectModels(file, result)
        } else if (file.name.endsWith(".php")) {
            result.add(file.nameWithoutExtension)
        }
    }
}

/* ---------------- WIDOKI ---------------- */

fun findViews(project: Project): List<String> {
    val result = mutableListOf<String>()

    val baseDir = project.baseDir ?: return emptyList()
    val viewsDir = baseDir.findChild("application")
        ?.findChild("views") ?: return emptyList()

    collectViews(viewsDir, "", result)

    return result
}

fun collectViews(dir: VirtualFile, prefix: String, result: MutableList<String>) {
    for (file in dir.children) {
        if (file.isDirectory) {
            collectViews(file, "$prefix${file.name}/", result)
        } else if (file.name.endsWith(".php")) {
            val viewName = file.nameWithoutExtension
            result.add(prefix + viewName)
        }
    }
}

/* ---------------- DATABASE CONFIG ---------------- */

fun findDatabaseConnections(project: Project): List<String> {
    val baseDir = project.baseDir ?: return emptyList()

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
    val baseDir = project.baseDir ?: return emptyList()
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

/** Z pliku: load->library('X') lub load->library('X', ..., 'alias') → nazwy właściwości na $this (alias lub lowercase X). */
fun findLoadedLibraries(fileText: String): List<String> {
    val result = mutableSetOf<String>()
    // Grupa 1: nazwa biblioteki, grupa 2: opcjonalny alias (trzeci parametr)
    val regex = Regex("load->library\\s*\\(\\s*['\"]([^'\"]+)['\"]\\s*(?:,\\s*[^)]*)?(?:,\\s*['\"]([^'\"]+)['\"])?\\s*\\)")
    regex.findAll(fileText).forEach { m ->
        val alias = m.groupValues[2].trim()
        val propName = if (alias.isNotEmpty()) alias else m.groupValues[1].trim().lowercase().replace("-", "_")
        result.add(propName)
    }
    return result.distinct().sorted()
}

/** Metody publiczne (i protected) z pliku biblioteki w application/libraries/ (po nazwie właściwości, np. my_lib). */
fun findLibraryMethods(project: Project, libraryPropertyName: String): List<String> {
    val libFile = findLibraryFileByPropertyName(project, libraryPropertyName) ?: return emptyList()
    val text = String(libFile.contentsToByteArray())
    val regex = Regex("(?:public|protected)\\s+function\\s+(\\w+)\\s*\\(")
    return regex.findAll(text).map { it.groupValues[1] }.distinct().sorted().toList()
}

private fun findLibraryFileByPropertyName(project: Project, propertyName: String): VirtualFile? {
    val baseDir = project.baseDir ?: return null
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

/* ---------------- HELPERY ---------------- */

fun findHelpers(project: Project): List<String> {
    val result = mutableListOf<String>()
    val baseDir = project.baseDir ?: return emptyList()
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

/* ---------------- CONFIG KEYS ($this->config->item) ---------------- */

fun findConfigKeys(project: Project): List<String> {
    val result = mutableSetOf<String>()
    val baseDir = project.baseDir ?: return emptyList()
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

/* ---------------- INPUT KEYS (post/get z formularzy i projektu) ---------------- */

fun findInputKeys(project: Project): List<String> {
    val result = mutableSetOf<String>()
    val baseDir = project.baseDir ?: return emptyList()
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

/* ---------------- INPUT->SERVER (klucze $_SERVER) ---------------- */

fun getServerKeys(project: Project): List<String> {
    val result = mutableSetOf<String>()
    val baseDir = project.baseDir ?: return emptyList()
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

/* ---------------- ROUTING (controller/method w routes.php) ---------------- */

fun findControllerMethodsForRoutes(project: Project): List<String> {
    val result = mutableListOf<String>()
    val baseDir = project.baseDir ?: return emptyList()
    val controllersDir = baseDir.findChild("application")
        ?.findChild("controllers") ?: return emptyList()

    for (file in controllersDir.children) {
        if (file.isDirectory) continue
        if (!file.name.endsWith(".php")) continue
        val controllerName = file.nameWithoutExtension
        val methods = findControllerMethods(file)
        result.add(controllerName)
        for (method in methods) {
            result.add("$controllerName/$method")
        }
    }
    return result.distinct().sorted()
}

private fun findControllerMethods(controllerFile: VirtualFile): List<String> {
    val text = String(controllerFile.contentsToByteArray())
    val regex = Regex("function\\s+(\\w+)\\s*\\(")
    return regex.findAll(text).map { it.groupValues[1] }.toList()
}

