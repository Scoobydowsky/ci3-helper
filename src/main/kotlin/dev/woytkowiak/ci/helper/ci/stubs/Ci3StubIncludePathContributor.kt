package dev.woytkowiak.ci.helper.ci.stubs

import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.LocalFileSystem
import com.intellij.openapi.vfs.VirtualFile
import com.jetbrains.php.config.library.PhpIncludedPathsContributor
import java.io.File

/**
 * Adds the path to CI3 stubs (CI_Controller with @property) to the PHP Include Path,
 * so that PhpStorm recognizes $this->input, $this->load etc. in controllers.
 */
class Ci3StubIncludePathContributor : PhpIncludedPathsContributor {

    override fun getRoots(project: Project): Collection<VirtualFile> {
        val basePath = project.basePath ?: return emptyList()
        val baseDir = LocalFileSystem.getInstance().findFileByPath(basePath) ?: return emptyList()
        val appDir = baseDir.findChild("application") ?: return emptyList()
        val configDir = appDir.findChild("config") ?: return emptyList()
        if (configDir.findChild("config.php") == null) return emptyList() // likely not a CI3 project
        val stubsDir = File(basePath, ".ci3-helper/stubs")
        if (!stubsDir.isDirectory) stubsDir.mkdirs()

        listOf("CI_Controller.php", "MY_Controller.php", "global_functions.php").forEach { name ->
            val stubFile = File(stubsDir, name)
            if (!stubFile.exists()) {
                javaClass.getResourceAsStream("/ci3-stubs/$name")?.use { input ->
                    stubFile.outputStream().use { input.copyTo(it) }
                }
            }
        }

        val vfsDir = LocalFileSystem.getInstance().refreshAndFindFileByPath(stubsDir.absolutePath)
        return if (vfsDir != null) listOf(vfsDir) else emptyList()
    }
}
