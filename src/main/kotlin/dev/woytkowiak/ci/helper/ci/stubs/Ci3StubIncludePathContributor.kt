package dev.woytkowiak.ci.helper.ci.stubs

import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.LocalFileSystem
import dev.woytkowiak.ci.helper.ci.guessProjectBaseDir
import com.intellij.openapi.vfs.VirtualFile
import dev.woytkowiak.ci.helper.ci.Ci3PluginState
import com.jetbrains.php.config.library.PhpIncludedPathsContributor
import java.io.File

/**
 * Adds the path to CI3 stubs (CI_Controller with @property) to the PHP Include Path,
 * so that PhpStorm recognizes $this->input, $this->load etc. in controllers.
 */
class Ci3StubIncludePathContributor : PhpIncludedPathsContributor {

    override fun getRoots(project: Project): Collection<VirtualFile> {
        if (!Ci3PluginState.getInstance().isEnabled) return emptyList()
        val basePath = project.guessProjectBaseDir()?.path ?: return emptyList()
        val baseDir = LocalFileSystem.getInstance().findFileByPath(basePath) ?: return emptyList()
        val appDir = baseDir.findChild("application") ?: return emptyList()
        val configDir = appDir.findChild("config") ?: return emptyList()
        if (configDir.findChild("config.php") == null) return emptyList() // likely not a CI3 project
        val stubsDir = File(basePath, ".ci3-helper/stubs")
        if (!stubsDir.isDirectory) stubsDir.mkdirs()

        val stubNames = listOf(
            "CI_Controller.php", "MY_Controller.php", "global_functions.php",
            "CI_Loader.php", "CI_Input.php", "CI_Config.php", "CI_DB_query_builder.php",
            "CI_Session.php", "CI_Form_validation.php", "CI_URI.php", "CI_Router.php",
            "CI_Output.php", "CI_Security.php", "CI_Zip.php", "CI_Benchmark.php",
            "CI_User_agent.php", "CI_Parser.php", "CI_Trackback.php", "CI_Cache.php",
            "CI_Unit_test.php"
        )
        stubNames.forEach { name ->
            val stubFile = File(stubsDir, name)
            javaClass.getResourceAsStream("/ci3-stubs/$name")?.use { input ->
                stubFile.outputStream().use { input.copyTo(it) }
            }
        }

        val vfsDir = LocalFileSystem.getInstance().refreshAndFindFileByPath(stubsDir.absolutePath)
        return if (vfsDir != null) listOf(vfsDir) else emptyList()
    }
}
