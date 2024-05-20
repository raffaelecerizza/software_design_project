package com.example.floraleye.ui.dictionary

import android.content.Intent
import androidx.recyclerview.widget.RecyclerView
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.action.ViewActions.closeSoftKeyboard
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.pressBack
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.espresso.matcher.ViewMatchers.withEffectiveVisibility
import androidx.test.espresso.matcher.ViewMatchers.Visibility
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.floraleye.MainActivity
import com.example.floraleye.R
import com.example.floraleye.TestConstants
import com.example.floraleye.TestConstants.DICTIONARY_TEST_CONTROL_NUMBER
import com.example.floraleye.TestUtilities
import com.example.floraleye.ui.onboard.OnboardActivity
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.Assert
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class DetailsFragmentTest {

    @get:Rule
    val onboardActivityRule = ActivityScenarioRule(OnboardActivity::class.java)

    /**
     * Prima di eseguire ogni test è necessario effettuare il login.
     */
    @Before fun login() {
        val buttonLogin = TestUtilities.getString(R.string.str_login_button, onboardActivityRule)

        onView(withId(R.id.switchModeTextView))
            .perform(TestUtilities.clickClickableSpan(buttonLogin))

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

        Espresso.openActionBarOverflowOrOptionsMenu(
            InstrumentationRegistry.getInstrumentation().targetContext
        )

        onView(withText(R.string.str_logout)).perform(click())
    }

    @Test fun testFlowersDetails() {
        val intent = Intent(ApplicationProvider.getApplicationContext(), MainActivity::class.java)
        ActivityScenario.launch<MainActivity>(intent)

        Assert.assertTrue(TestUtilities.makeCyclicViewTest {
            onView(withId(R.id.navigation_dictionary)).perform(click())
        })

        Assert.assertTrue(TestUtilities.makeCyclicViewTest(
            DICTIONARY_TEST_CONTROL_NUMBER,
            1000) {
            onView(withId(R.id.rvFlowerList)).check(matches(isDisplayed()))
        })

        onView(withId(R.id.rvFlowerList))
            .perform(RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                0, click()))

        onView(withId(R.id.imageView)).check(matches(isDisplayed()))
        onView(withId(R.id.scientificName)).check(matches(isDisplayed()))
        onView(withId(R.id.commonName)).check(matches(isDisplayed()))

        onView(withId(R.id.navigation_dictionary)).perform(pressBack())

        onView(withId(R.id.rvFlowerList))
            .check(matches(withEffectiveVisibility(Visibility.VISIBLE)))
    }
}

