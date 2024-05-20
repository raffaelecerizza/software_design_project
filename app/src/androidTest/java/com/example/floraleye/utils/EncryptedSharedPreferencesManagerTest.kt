package com.example.floraleye.utils

import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.example.floraleye.ui.onboard.OnboardActivity
import org.junit.Assert
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Classe di test per EncryptedSharedPreferencesManager.
 */
@RunWith(AndroidJUnit4::class)
@LargeTest
class EncryptedSharedPreferencesManagerTest {

    @get:Rule
    val onboardActivityRule = ActivityScenarioRule(OnboardActivity::class.java)

    /**
     * Test classe EncryptedSharedPreferencesManager.
     */
    @Test
    fun encryptedSharedPrefManager() {
        val testFile = "test_file"
        val testKey = "test_key"
        val testValue = "test_value"

        onboardActivityRule.scenario.onActivity { activity ->
            val sharedPrefManager = EncryptedSharedPreferencesManager(activity.application,
                testFile)

            sharedPrefManager.writeSecretString(testKey, testValue)

            var value = sharedPrefManager.readSecretString(testKey, "")

            Assert.assertEquals(testValue, value)

            sharedPrefManager.deleteSecretData(testKey)

            value = sharedPrefManager.readSecretString(testKey, "")

            Assert.assertEquals("", value)
        }
    }
}
