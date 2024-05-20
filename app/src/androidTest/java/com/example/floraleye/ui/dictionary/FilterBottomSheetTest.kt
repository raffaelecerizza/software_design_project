package com.example.floraleye.ui.dictionary

import android.content.Intent
import androidx.appcompat.app.AppCompatDelegate
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.action.ViewActions.closeSoftKeyboard
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withContentDescription
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.example.floraleye.MainActivity
import com.example.floraleye.R
import com.example.floraleye.TestConstants
import com.example.floraleye.TestConstants.DICTIONARY_TEST_CONTROL_NUMBER
import com.example.floraleye.TestUtilities.makeCyclicViewTest
import com.example.floraleye.TestUtilities.getString
import com.example.floraleye.TestUtilities.clickClickableSpan
import com.example.floraleye.ui.onboard.OnboardActivity
import org.hamcrest.Matchers.not
import org.junit.Rule
import org.junit.Before
import org.junit.After
import org.junit.Assert
import org.junit.Test
import org.junit.Ignore
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class FilterBottomSheetTest {

    @get:Rule
    val onboardActivityRule = ActivityScenarioRule(OnboardActivity::class.java)

    private val mainActivityIntent =
        Intent(ApplicationProvider.getApplicationContext(), MainActivity::class.java)

    /**
     * Prima di eseguire ogni test è necessario effettuare il login.
     */
    @Before fun login() {
        val buttonLogin = getString(R.string.str_login_button, onboardActivityRule)

        onView(withId(R.id.switchModeTextView)).perform(clickClickableSpan(buttonLogin))

        onView(withId(R.id.inputEditTextMail))
            .perform(
                typeText(TestConstants.TEST_ACCOUNT_EMAIL),
                closeSoftKeyboard()
            )
        onView(withId(R.id.inputEditTextPassword))
            .perform(
                typeText(TestConstants.TEST_ACCOUNT_PASSWORD),
                closeSoftKeyboard()
            )

        onView(withId(R.id.onboardButton)).perform(click())
    }

    /**
     * Al termine di ogni test è necessario effettuare il logout.
     */
    @After fun logout() {
        onView(withId(R.id.navigation_profile)).perform(click())

        openActionBarOverflowOrOptionsMenu(
            InstrumentationRegistry
                .getInstrumentation().targetContext
        )

        onView(withText(R.string.str_logout)).perform(click())
    }

    @Test fun testExistingFiltering() {
        ActivityScenario.launch<MainActivity>(mainActivityIntent)

        Assert.assertTrue(makeCyclicViewTest {
            onView(withId(R.id.navigation_dictionary)).perform(click())
        })

        Assert.assertTrue(makeCyclicViewTest(DICTIONARY_TEST_CONTROL_NUMBER, 1000) {
            onView(withId(R.id.rvFlowerList)).check(matches(isDisplayed()))
        })

        onView(withContentDescription(R.string.str_filter_dictionary)).perform(click())

        onView(withId(R.id.genusEditText))
            .perform(
                typeText(TestConstants.TEXT_EXISTING_FILTER_GENUS),
                closeSoftKeyboard()
            )

        onView(withId(R.id.applyFiltersButton)).perform(click())

        onView(withText(R.string.str_dictionary_empty)).check(matches(not(isDisplayed())))
    }

    @Test fun testNotExistingFiltering() {
        ActivityScenario.launch<MainActivity>(mainActivityIntent)

        Assert.assertTrue(makeCyclicViewTest {
            onView(withId(R.id.navigation_dictionary)).perform(click())
        })

        Assert.assertTrue(makeCyclicViewTest(DICTIONARY_TEST_CONTROL_NUMBER, 1000) {
            onView(withId(R.id.rvFlowerList)).check(matches(isDisplayed()))
        })

        onView(withContentDescription(R.string.str_filter_dictionary)).perform(click())

        onView(withId(R.id.genusEditText))
            .perform(
                typeText(TestConstants.TEXT_NOT_EXISTING_FILTER_GENUS),
                closeSoftKeyboard()
            )

        onView(withId(R.id.applyFiltersButton)).perform(click())

        onView(withText(R.string.str_dictionary_empty)).check(matches(isDisplayed()))

        onView(withContentDescription(R.string.str_filter_dictionary)).perform(click())

        onView(withId(R.id.resetFiltersButton)).perform(click())

        onView(withText(R.string.str_dictionary_empty)).check(matches(not(isDisplayed())))
    }

    @Ignore @Test fun testFilterChangeConf() {
        val scenario = ActivityScenario.launch<MainActivity>(mainActivityIntent)

        Assert.assertTrue(makeCyclicViewTest {
            onView(withId(R.id.navigation_dictionary)).perform(click())
        })
        Assert.assertTrue(makeCyclicViewTest(DICTIONARY_TEST_CONTROL_NUMBER, 1000) {
            onView(withId(R.id.rvFlowerList)).check(matches(isDisplayed()))
        })

        onView(withContentDescription(R.string.str_filter_dictionary)).perform(click())
        onView(withId(R.id.genusEditText))
            .perform(
                typeText(TestConstants.TEXT_NOT_EXISTING_FILTER_GENUS),
                closeSoftKeyboard()
            )

        scenario.onActivity {
            AppCompatDelegate.setDefaultNightMode(
                AppCompatDelegate.MODE_NIGHT_YES
            )
        }
        onView(withId(R.id.genusEditText))
            .check(matches(withText(TestConstants.TEXT_NOT_EXISTING_FILTER_GENUS)))

        scenario.onActivity {
            AppCompatDelegate.setDefaultNightMode(
                AppCompatDelegate.MODE_NIGHT_NO
            )
        }
        onView(withId(R.id.genusEditText))
            .check(matches(withText(TestConstants.TEXT_NOT_EXISTING_FILTER_GENUS)))

        onView(withId(R.id.resetFiltersButton)).perform(click())
        onView(withText(R.string.str_dictionary_empty)).check(matches(not(isDisplayed())))
    }
}
