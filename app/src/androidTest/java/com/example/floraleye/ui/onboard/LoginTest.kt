package com.example.floraleye.ui.onboard

import android.content.pm.ActivityInfo
import android.util.Log
import com.example.floraleye.TestUtilities.getString
import com.example.floraleye.TestUtilities.hasTextInputLayoutHintText
import com.example.floraleye.TestUtilities.clickClickableSpan
import androidx.appcompat.app.AppCompatDelegate
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.action.ViewActions.closeSoftKeyboard
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import androidx.test.platform.app.InstrumentationRegistry.getInstrumentation
import com.example.floraleye.R
import com.example.floraleye.TestConstants
import com.example.floraleye.TestUtilities.makeCyclicViewTest
import org.hamcrest.Matchers.allOf
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.Ignore
import org.junit.Assert
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@LargeTest
class LoginTest {

    companion object {

        private val TAG: String = LoginTest::class.java.simpleName
    }

    @get:Rule
    val onboardActivityRule = ActivityScenarioRule(OnboardActivity::class.java)

    /**
     * Prima di eseguire ogni test è necessario passare alla pagina di Login.
     */
    @Before fun switchToLogin() {
        val buttonLogin = getString(R.string.str_login_button, onboardActivityRule)

        onView(withId(R.id.switchModeTextView))
            .perform(clickClickableSpan(buttonLogin))
    }

    /**
     * Test login senza inserire alcun dato.
     */
    @Test fun loginWithNoData() {
        val errorString = getString(R.string.str_no_mail, onboardActivityRule)

        onView(withId(R.id.onboardButton)).perform(click())

        onView(withId(R.id.inputLayoutMail))
            .check(matches(hasTextInputLayoutHintText(errorString)))
    }

    /**
     * Test login con mail malformata.
     */
    @Test fun loginWithMalformedMail() {
        val errorMessage = getString(R.string.str_credential_not_valid, onboardActivityRule)

        onView(withId(R.id.inputEditTextMail))
            .perform(typeText(TestConstants.MALFORMED_EMAIL), closeSoftKeyboard())

        onView(withId(R.id.onboardButton)).perform(click())

        onView(withId(R.id.inputLayoutMail))
            .check(matches(hasTextInputLayoutHintText(errorMessage)))
    }

    /**
     * Test login con mail valida, ma password non sicura.
     */
    @Test fun loginWithWeakPassword() {
        val popupContentString = getString(R.string.str_password_not_valid, onboardActivityRule)
        val popupButtonString = getString(R.string.str_ok, onboardActivityRule)
        val errorMessage = getString(R.string.str_password_weak_error, onboardActivityRule)

        onView(withId(R.id.inputEditTextMail))
            .perform(typeText(TestConstants.TEST_ACCOUNT_EMAIL), closeSoftKeyboard())

        onView(withId(R.id.inputEditTextPassword))
            .perform(typeText(TestConstants.WEAK_PASSWORD), closeSoftKeyboard())

        onView(withId(R.id.onboardButton)).perform(click())

        onView(withText(popupContentString))
            .check(matches(isDisplayed()))

        onView(withText(popupButtonString)).perform(click())

        onView(withId(R.id.inputLayoutPassword))
            .check(matches(hasTextInputLayoutHintText(errorMessage)))
    }

    /**
     * Test cambi di configurazione. In particolare, cambio da modalità notte a modalità giorno e
     * viceversa, e cambio da portrait a landscape e viceversa.
     */
    @Test fun changeConfiguration() {
        onView(withId(R.id.inputEditTextMail))
            .perform(typeText(TestConstants.TEST_ACCOUNT_EMAIL), closeSoftKeyboard())

        onboardActivityRule.scenario.onActivity {
            AppCompatDelegate.setDefaultNightMode(
                AppCompatDelegate.MODE_NIGHT_YES
            )
        }

        onView(withId(R.id.inputEditTextMail))
            .check(matches(withText(TestConstants.TEST_ACCOUNT_EMAIL)))

        onboardActivityRule.scenario.onActivity {
            AppCompatDelegate.setDefaultNightMode(
                AppCompatDelegate.MODE_NIGHT_NO
            )
        }

        onView(withId(R.id.inputEditTextMail))
            .check(matches(withText(TestConstants.TEST_ACCOUNT_EMAIL)))

        onboardActivityRule.scenario.onActivity {activity ->
            activity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        }

        onView(withId(R.id.inputEditTextMail))
            .check(matches(withText(TestConstants.TEST_ACCOUNT_EMAIL)))

        onboardActivityRule.scenario.onActivity {activity ->
            activity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        }

        onView(withId(R.id.inputEditTextMail))
            .check(matches(withText(TestConstants.TEST_ACCOUNT_EMAIL)))
    }

    /**
     * Test login con mail non associata ad alcun utente precedentemente registrato.
     */
    @Test fun loginWithNotExistingUser() {
        onView(withId(R.id.inputEditTextMail))
            .perform(typeText(TestConstants.NOT_EXISTING_USER), closeSoftKeyboard())

        onView(withId(R.id.inputEditTextPassword))
            .perform(typeText(TestConstants.TEST_ACCOUNT_PASSWORD), closeSoftKeyboard())

        onView(withId(R.id.onboardButton)).perform(click())

        val isViewFound = makeCyclicViewTest(100, 100) {
            onView(
                allOf(
                    withId(com.google.android.material.R.id.snackbar_text)
                )
            ).check(matches(isDisplayed()))
        }
        Assert.assertTrue(isViewFound)
    }

    /**
     * Test login con utente esistente, ma password errata.
     */
    @Test fun loginWithIncorrectPassword() {
        val errorMessage = getString(R.string.str_credential_not_valid, onboardActivityRule)

        onView(withId(R.id.inputEditTextMail))
            .perform(typeText(TestConstants.TEST_ACCOUNT_EMAIL), closeSoftKeyboard())

        onView(withId(R.id.inputEditTextPassword))
            .perform(typeText(TestConstants.TEST_ACCOUNT_WRONG_PASSWORD), closeSoftKeyboard())

        onView(withId(R.id.onboardButton)).perform(click())

        val isViewFound = makeCyclicViewTest(100, 100) {
            onView(
                allOf(
                    withId(com.google.android.material.R.id.snackbar_text),
                    withText(errorMessage)
                )
            ).check(matches(isDisplayed()))
        }
        Assert.assertTrue(isViewFound)
    }

    /**
     * Test login senza connessione alla rete.
     */
    @Ignore @Test fun loginWithNoConnectivity() {
        val errorMessage = getString(R.string.str_signup_network_error, onboardActivityRule)

        // Disabilitazione connettività
        getInstrumentation().uiAutomation.executeShellCommand(TestConstants.WIFI_DISABLE)
        getInstrumentation().uiAutomation.executeShellCommand(TestConstants.DATA_DISABLE)

        onView(withId(R.id.inputEditTextMail))
            .perform(typeText(TestConstants.TEST_ACCOUNT_EMAIL), closeSoftKeyboard())

        onView(withId(R.id.inputEditTextPassword))
            .perform(typeText(TestConstants.TEST_ACCOUNT_PASSWORD), closeSoftKeyboard())

        onView(withId(R.id.onboardButton)).perform(click())

        val isViewFound = makeCyclicViewTest(100, 100) {
            onView(withText(errorMessage)).check(matches(isDisplayed()))
        }

        // Riattivazione connettività
        getInstrumentation().uiAutomation.executeShellCommand(TestConstants.WIFI_ENABLE)
        getInstrumentation().uiAutomation.executeShellCommand(TestConstants.DATA_ENABLE)

        Assert.assertTrue(isViewFound)
    }

    /**
     * Test invio della mail di rispristino della password.
     */
    @Test fun loginSendResetPasswordMail() {
        val popupContentString = getString(R.string.str_send_reset_password_mail_ok,
            onboardActivityRule)

        onView(withId(R.id.inputEditTextMail))
            .perform(typeText(TestConstants.TEST_ACCOUNT_EMAIL), closeSoftKeyboard())

        onView(withId(R.id.forgotPasswordTextView)).perform(click())

        val isViewFound = makeCyclicViewTest(100, 100) {
            onView(withText(popupContentString)).check(matches(isDisplayed()))
        }
        Assert.assertTrue(isViewFound)
    }

    /**
     * Test login con account non verificato.
     */
    @Test fun loginWithNotVerifiedAccount() {
        val message = getString(R.string.str_mail_not_verified, onboardActivityRule)
        val button = getString(R.string.str_ok, onboardActivityRule)
        val messageMail = getString(R.string.str_send_verify_mail_ok, onboardActivityRule)
        val messageTooMany = getString(R.string.str_too_may_request, onboardActivityRule)

        onView(withId(R.id.inputEditTextMail))
            .perform(typeText(TestConstants.TEST_ACCOUNT_NOT_VERIFIED_EMAIL),
                closeSoftKeyboard())

        onView(withId(R.id.inputEditTextPassword))
            .perform(typeText(TestConstants.TEST_ACCOUNT_NOT_VERIFIED_PASSWORD),
                closeSoftKeyboard())

        onView(withId(R.id.onboardButton)).perform(click())

        var isViewFound = makeCyclicViewTest(100, 100) {
            onView(withText(message)).check(matches(isDisplayed()))
        }
        Assert.assertTrue(isViewFound)

        onView(withText(button)).perform(click())

        isViewFound = makeCyclicViewTest(100, 100) {
            try {
                onView(withText(messageMail)).check(matches(isDisplayed()))
                onView(withText(button)).perform(click())
            } catch (exception: Exception) {
                Log.d(TAG, "loginWithNotVerifiedAccount: ${exception.message}")
                onView(
                    allOf(
                        withId(com.google.android.material.R.id.snackbar_text),
                        withText(messageTooMany)
                    )
                ).check(matches(isDisplayed()))
            }
        }
        Assert.assertTrue(isViewFound)
    }

    /**
     * Test invio della mail di rispristino della password.
     */
    @Test fun loginResetPasswordWithNoMail() {
        val errorString = getString(R.string.str_no_mail, onboardActivityRule)

        onView(withId(R.id.forgotPasswordTextView)).perform(click())

        onView(withId(R.id.inputLayoutMail))
            .check(matches(hasTextInputLayoutHintText(errorString)))
    }

    /**
     * Test login più logout.
     */
    @Test fun loginAndLogout() {
        onView(withId(R.id.inputEditTextMail))
            .perform(typeText(TestConstants.TEST_ACCOUNT_EMAIL), closeSoftKeyboard())

        onView(withId(R.id.inputEditTextPassword))
            .perform(typeText(TestConstants.TEST_ACCOUNT_PASSWORD), closeSoftKeyboard())

        onView(withId(R.id.onboardButton)).perform(click())

        val isViewFound = makeCyclicViewTest(50, 100) {
            onView(withId(R.id.navigation_dictionary)).perform(click())
            onView(withId(R.id.navigation_profile)).perform(click())
        }
        Assert.assertTrue(isViewFound)

        openActionBarOverflowOrOptionsMenu(getInstrumentation().targetContext)

        onView(withText(R.string.str_logout)).perform(click())
    }
}
