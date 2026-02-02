package dev.woytkowiak.ci.helper

import com.intellij.testFramework.fixtures.BasePlatformTestCase

/**
 * Minimalny test pluginu CodeIgniter 3 Helper.
 */
class Ci3HelperPluginTest : BasePlatformTestCase() {

    fun testPluginBundle() {
        val msg = MyBundle.message("intention.add.ci3.super.object.property")
        assertTrue(msg.isNotEmpty())
    }
}
