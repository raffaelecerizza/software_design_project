package com.example.floraleye.ui.quiz

import android.content.Intent
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.scrollTo
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import androidx.test.platform.app.InstrumentationRegistry
import androidx.viewpager2.widget.ViewPager2
import com.example.floraleye.MainActivity
import com.example.floraleye.R
import com.example.floraleye.TestConstants
import com.example.floraleye.TestUtilities
import com.example.floraleye.ui.onboard.OnboardActivity
import org.hamcrest.MatcherAssert
import org.hamcrest.Matchers
import org.junit.Rule
import org.junit.Before
import org.junit.After
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.Assert

@RunWith(AndroidJUnit4::class)
@LargeTest
class OldQuizzesFragmentTest {

    @get:Rule
    val onboardActivityRule = ActivityScenarioRule(OnboardActivity::class.java)

    @get:Rule
    val mainActivityRule = ActivityScenarioRule(MainActivity::class.java)

    private val mainActivityIntent =
        Intent(ApplicationProvider.getApplicationContext(), MainActivity::class.java)

    /**
     * Prima di eseguire ogni test è necessario effettuare il login.
     */
    @Before
    fun login() {
        val mail: String = TestConstants.TEST_ACCOUNT_EMAIL
        val password: String = TestConstants.TEST_ACCOUNT_PASSWORD
        val hasToSwitchView = true
        val buttonLogin = TestUtilities.getString(R.string.str_login_button, onboardActivityRule)

        if (hasToSwitchView) {
            onView(withId(R.id.switchModeTextView))
                .perform(TestUtilities.clickClickableSpan(buttonLogin))
        }

        onView(withId(R.id.inputEditTextMail))
            .perform(
                ViewActions.replaceText(mail),
                ViewActions.closeSoftKeyboard()
            )

        onView(withId(R.id.inputEditTextPassword))
            .perform(
                ViewActions.replaceText(password),
                ViewActions.closeSoftKeyboard()
            )

        onView(withId(R.id.onboardButton)).perform(click())
    }

    /**
     * Al termine di ogni test è necessario effettuare il logout.
     */
    @After
    fun logout() {
        onView(withId(R.id.navigation_profile)).perform(click())

        Espresso.openActionBarOverflowOrOptionsMenu(
            InstrumentationRegistry.getInstrumentation().targetContext
        )

        onView(withText(R.string.str_logout)).perform(click())
    }

    /**
     * Test della visualizzazione della storia dei quiz dell'utente a partire dal Profilo.
     */
    @Test fun testQuizHistoryFromProfile() {
        val scenario = ActivityScenario.launch<MainActivity>(mainActivityIntent)

        val isProfileButtonClicked =
            TestUtilities.makeCyclicViewTest(10, 1000) {
                onView(withId(R.id.navigation_profile)).perform(click())
            }
        Assert.assertTrue(isProfileButtonClicked)

        val isQuizButtonClicked =
            TestUtilities.makeCyclicViewTest(10, 1000) {
                onView(withId(R.id.showQuizzesButton)).perform(scrollTo(), click())
            }
        Assert.assertTrue(isQuizButtonClicked)

        scenario.onActivity {  activity ->
            val fragment = activity.getCurrentFragment()
            if (fragment is QuizFragment) {
                fragment.view?.findViewById<ViewPager2>(R.id.quizViewPager)
                val oldQuizzesFragment = fragment.childFragmentManager.fragments[0]
                MatcherAssert.assertThat(oldQuizzesFragment,
                    Matchers.instanceOf(OldQuizzesFragment::class.java))
            }
        }
    }

}
