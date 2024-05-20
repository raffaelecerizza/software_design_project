package com.example.floraleye.ui.profile

import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.action.ViewActions.closeSoftKeyboard
import androidx.test.espresso.action.ViewActions.replaceText
import androidx.test.espresso.action.ViewActions.scrollTo
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withContentDescription
import androidx.test.espresso.matcher.ViewMatchers.withEffectiveVisibility
import androidx.test.espresso.matcher.ViewMatchers.Visibility.GONE
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.example.floraleye.MainActivity
import com.example.floraleye.R
import com.example.floraleye.TestConstants
import com.example.floraleye.TestUtilities
import com.example.floraleye.ui.onboard.OnboardActivity
import org.hamcrest.Matchers
import org.junit.Rule
import org.junit.Test
import org.junit.Assert.assertTrue
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class ProfileFragmentTest {

    @get:Rule
    val onboardActivityRule = ActivityScenarioRule(OnboardActivity::class.java)

    @get:Rule
    val mainActivityRule = ActivityScenarioRule(MainActivity::class.java)

    /**
     * Test per verificare se la mail mostrata nel profilo è quella dell'utente effettivamente
     * autenticato in quel momento.
     */
    @Test fun isMailShowed() {
        val loginButtonText =
            TestUtilities.getString(R.string.str_login_button, onboardActivityRule)

        executeLogin(loginButtonText)

        val isViewFound = TestUtilities.makeCyclicViewTest(10, 1000) {
            onView(withId(R.id.navigation_profile)).perform(click())
            onView(withText(TestConstants.TEST_ACCOUNT_EMAIL)).perform(click())
        }
        assertTrue(isViewFound)

        executeLogout()
    }

    /**
     * Test di click sul bottone per la cancellazione dell'utente, per verificare se compare un
     * pop up di conferma dell'operazione. L'operazione di eliminazione NON viene confermata.
     */
    @Test fun deleteUserButtonClicked() {
        val deleteMessage = TestUtilities.getString(R.string.str_delete_user_confirmation,
            onboardActivityRule)
        val button = TestUtilities.getString(R.string.str_cancel, onboardActivityRule)
        val loginButtonText =
            TestUtilities.getString(R.string.str_login_button, onboardActivityRule)

        executeLogin(loginButtonText)

        val isViewFound = TestUtilities.makeCyclicViewTest(10, 1000) {
            onView(withId(R.id.navigation_profile)).perform(click())
            onView(withId(R.id.deleteUserButton)).perform(scrollTo(), click())
        }
        assertTrue(isViewFound)

        onView(withText(deleteMessage)).check(matches(isDisplayed()))
        onView(withText(button)).perform(click())

        executeLogout()
    }

    /**
     * Test tentativo di modifica della password con nuova password uguale a quella corrente.
     */
    @Test fun changePasswordWithSame() {
        val editMessage = TestUtilities.getString(R.string.str_edit_profile_message,
            onboardActivityRule)
        val button = TestUtilities.getString(R.string.str_ok, onboardActivityRule)
        val error = TestUtilities.getString(R.string.str_change_password_equal, onboardActivityRule)
        val loginButtonText =
            TestUtilities.getString(R.string.str_login_button, onboardActivityRule)

        executeLogin(loginButtonText)

        val isViewFound = TestUtilities.makeCyclicViewTest(10, 1000) {
            onView(withId(R.id.navigation_profile)).perform(click())
            onView(withContentDescription(R.string.str_edit_profile)).perform(click())
        }
        assertTrue(isViewFound)

        onView(withText(editMessage)).check(matches(isDisplayed()))
        onView(withText(button)).perform(click())

        onView(withId(R.id.inputEditTextOldPassword))
            .perform(
                typeText(TestConstants.TEST_ACCOUNT_PASSWORD),
                closeSoftKeyboard()
            )
        onView(withId(R.id.inputEditTextNewPassword))
            .perform(
                typeText(TestConstants.TEST_ACCOUNT_PASSWORD),
                closeSoftKeyboard()
            )
        onView(withId(R.id.inputEditTextRepeatNewPassword))
            .perform(
                typeText(TestConstants.TEST_ACCOUNT_PASSWORD),
                closeSoftKeyboard()
            )
        onView(withId(R.id.editButton)).perform(click())

        onView(
            Matchers.allOf(
                withId(com.google.android.material.R.id.snackbar_text),
                withText(error)
            )
        ).check(matches(isDisplayed()))

        Espresso.pressBackUnconditionally()

        executeLogout()
    }

    /**
     * Test tentativo di utilizzo come nuova password una password non sicura secondo i criteri
     * di sicurezza definiti dall'applicazione.
     */
    @Test fun changePasswordWithWeaK() {
        val editMessage = TestUtilities.getString(R.string.str_edit_profile_message,
            onboardActivityRule)
        val button = TestUtilities.getString(R.string.str_ok, onboardActivityRule)
        val error = TestUtilities.getString(R.string.str_password_not_valid, onboardActivityRule)
        val loginButtonText =
            TestUtilities.getString(R.string.str_login_button, onboardActivityRule)

        executeLogin(loginButtonText)

        val isViewFound = TestUtilities.makeCyclicViewTest(10, 1000) {
            onView(withId(R.id.navigation_profile)).perform(click())
            onView(withContentDescription(R.string.str_edit_profile)).perform(click())
        }
        assertTrue(isViewFound)

        onView(withText(editMessage)).check(matches(isDisplayed()))
        onView(withText(button)).perform(click())

        onView(withId(R.id.inputEditTextOldPassword))
            .perform(
                typeText(TestConstants.WEAK_PASSWORD),
                closeSoftKeyboard()
            )
        onView(withId(R.id.editButton)).perform(click())

        onView(withText(error)).check(matches(isDisplayed()))
        onView(withText(button)).perform(click())

        onView(withId(R.id.inputEditTextOldPassword))
            .perform(
                replaceText(TestConstants.TEST_ACCOUNT_WRONG_PASSWORD),
                closeSoftKeyboard()
            )
        onView(withId(R.id.inputEditTextNewPassword))
            .perform(
                typeText(TestConstants.WEAK_PASSWORD),
                closeSoftKeyboard()
            )
        onView(withId(R.id.inputEditTextRepeatNewPassword))
            .perform(
                typeText(TestConstants.WEAK_PASSWORD),
                closeSoftKeyboard()
            )
        onView(withId(R.id.editButton)).perform(click())

        onView(withText(error)).check(matches(isDisplayed()))
        onView(withText(button)).perform(click())

        Espresso.pressBackUnconditionally()

        executeLogout()
    }

    /**
     * Test tentativo di modifica della password con password attuale errata.
     */
    @Test fun changePasswordWrongAuth() {
        val editMessage = TestUtilities.getString(R.string.str_edit_profile_message,
            onboardActivityRule)
        val button = TestUtilities.getString(R.string.str_ok, onboardActivityRule)
        val error = TestUtilities.getString(R.string.str_credential_not_valid, onboardActivityRule)
        val loginButtonText =
            TestUtilities.getString(R.string.str_login_button, onboardActivityRule)

        executeLogin(loginButtonText)

        var isViewFound = TestUtilities.makeCyclicViewTest(10, 1000) {
            onView(withId(R.id.navigation_profile)).perform(click())
            onView(withContentDescription(R.string.str_edit_profile)).perform(click())
        }
        assertTrue(isViewFound)

        onView(withText(editMessage)).check(matches(isDisplayed()))
        onView(withText(button)).perform(click())

        onView(withId(R.id.inputEditTextOldPassword))
            .perform(
                typeText(TestConstants.TEST_ACCOUNT_WRONG_PASSWORD),
                closeSoftKeyboard()
            )
        onView(withId(R.id.inputEditTextNewPassword))
            .perform(
                typeText(TestConstants.TEST_ACCOUNT_PASSWORD),
                closeSoftKeyboard()
            )
        onView(withId(R.id.inputEditTextRepeatNewPassword))
            .perform(
                typeText(TestConstants.TEST_ACCOUNT_PASSWORD),
                closeSoftKeyboard()
            )
        onView(withId(R.id.editButton)).perform(click())

        isViewFound = TestUtilities.makeCyclicViewTest(50, 200) {
            onView(
                Matchers.allOf(
                    withId(com.google.android.material.R.id.snackbar_text),
                    withText(error)
                )
            ).check(matches(isDisplayed()))
        }
        assertTrue(isViewFound)

        Espresso.pressBackUnconditionally()

        executeLogout()
    }

    /**
     * Test modifica della password completata con successo. Per eseguire questo test è stata
     * creata una casella di posta dedicata, e si sono considerate due password che si
     * alternano tra loro nelle successive esecuzioni di questo test.
     */
    @Test fun changePassword() {
        var currentPassword = TestConstants.TEST_ACCOUNT_CHANGE_PASS_PASSWORD_1
        var newPassword = TestConstants.TEST_ACCOUNT_CHANGE_PASS_PASSWORD_2
        val editMessage = TestUtilities.getString(R.string.str_edit_profile_message,
            onboardActivityRule)
        val button = TestUtilities.getString(R.string.str_ok, onboardActivityRule)
        val error = TestUtilities.getString(R.string.str_credential_not_valid, onboardActivityRule)
        val loginButtonText =
            TestUtilities.getString(R.string.str_login_button, onboardActivityRule)

        executeLogin(loginButtonText, TestConstants.TEST_ACCOUNT_CHANGE_PASS_EMAIL, currentPassword)
        var isLoginOk = TestUtilities.makeCyclicViewTest(10, 1000) {
            onView(withId(R.id.navigation_profile)).perform(click())
            onView(withText(TestConstants.TEST_ACCOUNT_CHANGE_PASS_EMAIL)).perform(click())
        }
        if (!isLoginOk) {
            currentPassword = TestConstants.TEST_ACCOUNT_CHANGE_PASS_PASSWORD_2
            newPassword = TestConstants.TEST_ACCOUNT_CHANGE_PASS_PASSWORD_1
            executeLogin(
                loginButtonText = loginButtonText,
                mail = TestConstants.TEST_ACCOUNT_CHANGE_PASS_EMAIL,
                password = currentPassword, haveToSwitchView = false
            )
            isLoginOk = TestUtilities.makeCyclicViewTest(10, 1000) {
                onView(withId(R.id.navigation_profile)).perform(click())
                onView(withText(TestConstants.TEST_ACCOUNT_CHANGE_PASS_EMAIL)).perform(click())
            }
        }
        assertTrue(isLoginOk)

        performPasswordChange(
            currentPassword = currentPassword, newPassword = newPassword,
            editMessage = editMessage, button = button
        )

        val isLogoutOk = TestUtilities.makeCyclicViewTest(10, 1000) {
            onView(withId(R.id.flowerIcon)).check(matches(isDisplayed()))
        }
        assertTrue(isLogoutOk)

        // Verifica se non è possibile accedere con la vecchia password.
        executeLogin(loginButtonText, TestConstants.TEST_ACCOUNT_CHANGE_PASS_EMAIL, currentPassword)
        var isViewFound = TestUtilities.makeCyclicViewTest(10, 1000) {
            onView(
                Matchers.allOf(
                    withId(com.google.android.material.R.id.snackbar_text),
                    withText(error)
                )
            ).check(matches(isDisplayed()))
        }
        assertTrue(isViewFound)

        // Verifica se è possibile accedere con la nuova password.
        executeLogin(
            loginButtonText = loginButtonText,
            mail = TestConstants.TEST_ACCOUNT_CHANGE_PASS_EMAIL,
            password = newPassword, haveToSwitchView = false
        )
        isViewFound = TestUtilities.makeCyclicViewTest(10, 1000) {
            onView(withId(R.id.navigation_profile)).perform(click())
            onView(withText(TestConstants.TEST_ACCOUNT_CHANGE_PASS_EMAIL)).perform(click())
        }
        assertTrue(isViewFound)

        executeLogout()
    }

    /**
     * Test della corretta visualizzazione dei dati di uno storico dei quiz vuoto.
     */
    @Test fun testScoreEmptyQuizHistory() {
        val totalAnswers =
            TestUtilities.getString(R.string.str_total_zero, onboardActivityRule)
        val correctAnswers =
            TestUtilities.getString(R.string.str_correct_zero, onboardActivityRule)
        val wrongAnswers =
            TestUtilities.getString(R.string.str_wrong_zero, onboardActivityRule)
        val loginButtonText =
            TestUtilities.getString(R.string.str_login_button, onboardActivityRule)

        executeLogin(loginButtonText = loginButtonText,
            mail = TestConstants.TEST_ACCOUNT_EMPTY_EMAIL,
            password = TestConstants.TEST_ACCOUNT_EMPTY_PASSWORD, haveToSwitchView = true)

        navigateToProfile()

        onView(withId(R.id.textViewTotalAnswers)).check(matches(withText(totalAnswers)))
        onView(withId(R.id.textViewCorrectAnswers)).check(matches(withText(correctAnswers)))
        onView(withId(R.id.textViewWrongAnswers)).check(matches(withText(wrongAnswers)))

        val isTextCounterGone = TestUtilities.makeCyclicViewTest(10, 1000) {
            onView(withId(R.id.progressText)).check(matches(withEffectiveVisibility(GONE)))
        }
        assertTrue(isTextCounterGone)

        navigateToProfile()
        executeLogout()
    }

    /**
     * Metodo utilizzato per eseguire il login con mail e password passate.
     */
    private fun executeLogin(
        loginButtonText: String,
        mail: String = TestConstants.TEST_ACCOUNT_EMAIL,
        password: String = TestConstants.TEST_ACCOUNT_PASSWORD,
        haveToSwitchView: Boolean = true
    ) {
        if (haveToSwitchView) {
            onView(withId(R.id.switchModeTextView))
                .perform(TestUtilities.clickClickableSpan(loginButtonText))
        }

        onView(withId(R.id.inputEditTextMail))
            .perform(
                replaceText(mail),
                closeSoftKeyboard()
            )

        onView(withId(R.id.inputEditTextPassword))
            .perform(
                replaceText(password),
                closeSoftKeyboard()
            )

        onView(withId(R.id.onboardButton)).perform(click())
    }

    /**
     * Metodo utilizzato per eseguire il logout.
     */
    private fun executeLogout() {
        openActionBarOverflowOrOptionsMenu(
            InstrumentationRegistry.getInstrumentation().targetContext
        )

        onView(withText(R.string.str_logout)).perform(click())
    }

    /**
     * Metoo utilizzato per riempire i campi relativi al cambio password.
     */
    private fun performPasswordChange(
        currentPassword: String,
        newPassword: String,
        editMessage: String,
        button: String
    ) {
        onView(withContentDescription(R.string.str_edit_profile)).perform(click())
        onView(withText(editMessage)).check(matches(isDisplayed()))
        onView(withText(button)).perform(click())

        onView(withId(R.id.inputEditTextOldPassword))
            .perform(
                typeText(currentPassword),
                closeSoftKeyboard()
            )
        onView(withId(R.id.inputEditTextNewPassword))
            .perform(
                typeText(newPassword),
                closeSoftKeyboard()
            )
        onView(withId(R.id.inputEditTextRepeatNewPassword))
            .perform(
                typeText(newPassword),
                closeSoftKeyboard()
            )
        onView(withId(R.id.editButton)).perform(click())
    }

    /**
     * Metodo per navigare verso la sezione Profile.
     */
    private fun navigateToProfile() {
        val isViewFound = TestUtilities.makeCyclicViewTest(10, 1000) {
            onView(withId(R.id.navigation_profile)).perform(click())
        }
        assertTrue(isViewFound)
    }

}
