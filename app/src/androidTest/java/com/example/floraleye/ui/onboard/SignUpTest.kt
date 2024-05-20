package com.example.floraleye.ui.onboard

import android.content.pm.ActivityInfo
import androidx.appcompat.app.AppCompatDelegate
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.action.ViewActions.closeSoftKeyboard
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import androidx.test.platform.app.InstrumentationRegistry.getInstrumentation
import com.example.floraleye.R
import com.example.floraleye.TestConstants
import com.example.floraleye.TestUtilities.getString
import com.example.floraleye.TestUtilities.hasTextInputLayoutHintText
import com.example.floraleye.TestUtilities.clickClickableSpan
import com.example.floraleye.TestUtilities.makeCyclicViewTest
import org.hamcrest.MatcherAssert
import org.hamcrest.Matchers.allOf
import org.hamcrest.Matchers.instanceOf
import org.junit.Assert.assertTrue
import org.junit.Ignore
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


/**
 * Classe di test per la sezione di Onboard.
 */
@RunWith(AndroidJUnit4::class)
@LargeTest
class SignUpTest {

    @get:Rule
    val onboardActivityRule = ActivityScenarioRule(OnboardActivity::class.java)

    /**
     * Test tentativo di registrazione senza inserire alcun dato.
     */
    @Test fun signUpWithNoData() {
        val errorString = getString(R.string.str_no_mail, onboardActivityRule)

        onView(withId(R.id.onboardButton)).perform(click())

        onView(withId(R.id.inputLayoutMail))
            .check(matches(hasTextInputLayoutHintText(errorString)))
    }

    /**
     * Test tentativo di registrazione con mail non valida.
     */
    @Test fun signUpWithMalformedEmail() {
        val errorMessage = getString(R.string.str_credential_not_valid, onboardActivityRule)

        onView(withId(R.id.inputEditTextMail))
            .perform(typeText(TestConstants.MALFORMED_EMAIL), closeSoftKeyboard())

        onView(withId(R.id.onboardButton)).perform(click())

        onView(withId(R.id.inputLayoutMail))
            .check(matches(hasTextInputLayoutHintText(errorMessage)))
    }

    /**
     * Test registrazione con mail valida ma senza ripetizione della password.
     */
    @Test fun signUpWithNoRepeatedPassword() {
        val errorMessage = getString(R.string.str_no_repeat_password, onboardActivityRule)

        onView(withId(R.id.inputEditTextMail))
            .perform(typeText(TestConstants.NOT_EXISTING_USER), closeSoftKeyboard())

        onView(withId(R.id.inputEditTextPassword))
            .perform(typeText(TestConstants.TEST_ACCOUNT_WRONG_PASSWORD), closeSoftKeyboard())

        onView(withId(R.id.onboardButton)).perform(click())

        onView(withId(R.id.inputLayoutPassword))
            .check(matches(hasTextInputLayoutHintText(errorMessage)))
    }

    /**
     * Test registrazione con mail valida, password ripetuta, ma password non sicura secondo
     * i nostri criteri di sicurezza.
     */
    @Test fun signUpWithWeakPassword() {
        val popupContentString = getString(R.string.str_password_not_valid, onboardActivityRule)
        val popupButtonString = getString(R.string.str_ok, onboardActivityRule)
        val errorMessage = getString(R.string.str_password_weak_error, onboardActivityRule)

        onView(withId(R.id.inputEditTextMail))
            .perform(typeText(TestConstants.NOT_EXISTING_USER), closeSoftKeyboard())

        onView(withId(R.id.inputEditTextPassword))
            .perform(typeText(TestConstants.WEAK_PASSWORD), closeSoftKeyboard())

        onView(withId(R.id.inputEditTextRepeatPassword))
            .perform(typeText(TestConstants.WEAK_PASSWORD), closeSoftKeyboard())

        onView(withId(R.id.onboardButton)).perform(click())

        onView(withText(popupContentString)).check(matches(isDisplayed()))

        onView(withText(popupButtonString)).perform(click())

        onView(withId(R.id.inputLayoutPassword))
            .check(matches(hasTextInputLayoutHintText(errorMessage)))
    }

    /**
     * Test tentativo di registrazione con mail valida, password sicura, ma ripetizione della
     * password errata.
     */
    @Test fun signUpWithNoMatchingPassword() {
        val errorMessage = getString(R.string.str_password_no_match, onboardActivityRule)

        onView(withId(R.id.inputEditTextMail))
            .perform(typeText(TestConstants.NOT_EXISTING_USER), closeSoftKeyboard())

        onView(withId(R.id.inputEditTextPassword))
            .perform(typeText(TestConstants.TEST_ACCOUNT_WRONG_PASSWORD), closeSoftKeyboard())

        onView(withId(R.id.inputEditTextRepeatPassword))
            .perform(typeText(TestConstants.TEST_ACCOUNT_WRONG_PASSWORD + "1"),
                closeSoftKeyboard())

        onView(withId(R.id.onboardButton)).perform(click())

        onView(withId(R.id.inputLayoutPassword))
            .check(matches(hasTextInputLayoutHintText(errorMessage)))
    }

    /**
     * Test switch tra le le pagine di Registrazione e Login.
     */
    @Test fun switchFromSignUpToLoginAndVice() {
        val buttonLogin = getString(R.string.str_login_button, onboardActivityRule)
        val buttonSignUp = getString(R.string.str_signup_button, onboardActivityRule)

        onboardActivityRule.scenario.onActivity { activity ->
            val fragment = activity.getDisplayedFragment()
            MatcherAssert.assertThat(fragment, instanceOf(SignUpFragment::class.java))
        }

        onView(withId(R.id.switchModeTextView)).perform(clickClickableSpan(buttonLogin))

        onboardActivityRule.scenario.onActivity { activity ->
            val fragment = activity.getDisplayedFragment()
            MatcherAssert.assertThat(fragment, instanceOf(LoginFragment::class.java))
        }

        onView(withId(R.id.switchModeTextView)).perform(clickClickableSpan(buttonSignUp))

        onboardActivityRule.scenario.onActivity { activity ->
            val fragment = activity.getDisplayedFragment()
            MatcherAssert.assertThat(fragment, instanceOf(SignUpFragment::class.java))
        }
    }

    /**
     * Test comportamente con cambio di configurazione. In particolare, passaggio da dark a light,
     * e passaggio da portrait a landscape.
     */
    @Test fun changeConfiguration() {

        onView(withId(R.id.inputEditTextMail))
            .perform(typeText(TestConstants.NOT_EXISTING_USER), closeSoftKeyboard())

        onboardActivityRule.scenario.onActivity {
            AppCompatDelegate.setDefaultNightMode(
                AppCompatDelegate.MODE_NIGHT_YES
            )
        }

        onView(withId(R.id.inputEditTextMail))
            .check(matches(withText(TestConstants.NOT_EXISTING_USER)))

        onboardActivityRule.scenario.onActivity {
            AppCompatDelegate.setDefaultNightMode(
                AppCompatDelegate.MODE_NIGHT_NO
            )
        }

        onView(withId(R.id.inputEditTextMail))
            .check(matches(withText(TestConstants.NOT_EXISTING_USER)))

        onboardActivityRule.scenario.onActivity {activity ->
            activity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        }

        onView(withId(R.id.inputEditTextMail))
            .check(matches(withText(TestConstants.NOT_EXISTING_USER)))

        onboardActivityRule.scenario.onActivity {activity ->
            activity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        }

        onView(withId(R.id.inputEditTextMail))
            .check(matches(withText(TestConstants.NOT_EXISTING_USER)))
    }

    /**
     * Test registrazione con mail già in uso da un altro utente.
     */
    @Test fun signUpWithExistingUser() {
        val errorMessage = getString(R.string.str_mail_already_in_use_error, onboardActivityRule)

        onView(withId(R.id.inputEditTextMail))
            .perform(typeText(TestConstants.TEST_ACCOUNT_EMAIL), closeSoftKeyboard())

        onView(withId(R.id.inputEditTextPassword))
            .perform(typeText(TestConstants.TEST_ACCOUNT_WRONG_PASSWORD), closeSoftKeyboard())

        onView(withId(R.id.inputEditTextRepeatPassword))
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
        assertTrue(isViewFound)
    }

    /**
     * Test registrazione senza connettività ad Internet.
     */
    @Ignore
    @Test fun signUpWithNoConnectivity() {
        val errorMessage = getString(R.string.str_signup_network_error, onboardActivityRule)

        // Disabilitazione connettività
        getInstrumentation().uiAutomation.executeShellCommand(TestConstants.WIFI_DISABLE)
        getInstrumentation().uiAutomation.executeShellCommand(TestConstants.DATA_DISABLE)

        onView(withId(R.id.inputEditTextMail))
            .perform(typeText(TestConstants.TEST_ACCOUNT_EMAIL), closeSoftKeyboard())

        onView(withId(R.id.inputEditTextPassword))
            .perform(typeText(TestConstants.TEST_ACCOUNT_WRONG_PASSWORD), closeSoftKeyboard())

        onView(withId(R.id.inputEditTextRepeatPassword))
            .perform(typeText(TestConstants.TEST_ACCOUNT_WRONG_PASSWORD), closeSoftKeyboard())

        onView(withId(R.id.onboardButton)).perform(click())

        val isViewFound = makeCyclicViewTest(100, 100) {
            onView(withText(errorMessage)).check(matches(isDisplayed()))
        }

        // Riattivazione connettività
        getInstrumentation().uiAutomation.executeShellCommand(TestConstants.WIFI_ENABLE)
        getInstrumentation().uiAutomation.executeShellCommand(TestConstants.DATA_ENABLE)

        assertTrue(isViewFound)
    }
}
