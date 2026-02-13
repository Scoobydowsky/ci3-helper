package dev.woytkowiak.ci.helper.ci

import com.intellij.openapi.module.ModuleManager
import com.intellij.openapi.project.Project
import com.intellij.openapi.roots.ModuleRootManager
import com.intellij.openapi.vfs.VirtualFile

/**
 * Replacement for deprecated [com.intellij.openapi.project.Project.getBaseDir].
 * Returns the first content root of the first module (typical project base for single-module CI3 projects).
 */
fun Project.guessProjectBaseDir(): VirtualFile? {
    val modules = ModuleManager.getInstance(this).modules
    if (modules.isEmpty()) return null
    val contentRoots = ModuleRootManager.getInstance(modules[0]).contentRoots
    return contentRoots.firstOrNull()
}
