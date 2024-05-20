package com.example.floraleye.ui.dictionary

import android.content.Intent
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.PerformException
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.isAssignableFrom
import androidx.test.espresso.matcher.ViewMatchers.hasChildCount
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.example.floraleye.MainActivity
import com.example.floraleye.R
import com.example.floraleye.TestConstants
import com.example.floraleye.TestConstants.DICTIONARY_TEST_CONTROL_NUMBER
import com.example.floraleye.TestUtilities
import com.example.floraleye.ui.onboard.OnboardActivity
import com.google.android.material.tabs.TabLayout
import org.hamcrest.Matcher
import org.hamcrest.core.AllOf.allOf
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.Assert
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class FavouritesFragmentTest {

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
                ViewActions.typeText(TestConstants.TEST_ACCOUNT_EMAIL),
                ViewActions.closeSoftKeyboard()
            )
        onView(withId(R.id.inputEditTextPassword))
            .perform(
                ViewActions.typeText(TestConstants.TEST_ACCOUNT_PASSWORD),
                ViewActions.closeSoftKeyboard()
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

        onView(ViewMatchers.withText(R.string.str_logout)).perform(click())
    }

    /**
     * Test aggiunta di un fiore ai preferiti.
     */
    @Test fun tabFavourites(){

        val intent = Intent(ApplicationProvider.getApplicationContext(), MainActivity::class.java)
        ActivityScenario.launch<MainActivity>(intent)

        Assert.assertTrue(TestUtilities.makeCyclicViewTest {
            onView(withId(R.id.navigation_dictionary)).perform(click())
        })

        Assert.assertTrue(TestUtilities.makeCyclicViewTest(
            DICTIONARY_TEST_CONTROL_NUMBER, 1000) {
            onView(withId(R.id.rvFlowerList)).check(matches(isDisplayed()))
        })

        Assert.assertTrue(TestUtilities.makeCyclicViewTest {
            onView(withId(R.id.rvFlowerList))
                .perform(
                    RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                        0, clickChildViewWithId(R.id.star_dictionary)
                    )
                )
        })

        Assert.assertTrue(TestUtilities.makeCyclicViewTest {
            onView(withId(R.id.tab_layout)).perform(selectTabAtPosition(1))
        })

        onView(withId(R.id.rvFavouriteFlowers)).check(matches(hasChildCount(1)))

        onView(withId(R.id.tab_layout)).perform(selectTabAtPosition(0))

        onView(withId(R.id.rvFlowerList))
            .perform(RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                0, clickChildViewWithId(R.id.star_dictionary))
            )

        Assert.assertTrue(TestUtilities.makeCyclicViewTest {
            onView(withId(R.id.tab_layout)).perform(selectTabAtPosition(1))
        })

        onView(withId(R.id.rvFavouriteFlowers)).check(matches(hasChildCount(0)))
    }

    private fun clickChildViewWithId(id: Int): ViewAction {
        return object : ViewAction {
            override fun getConstraints(): Matcher<View>? {
                return null
            }

            override fun getDescription(): String {
                return ""
            }

            override fun perform(uiController: UiController?, view: View) {
                val item = view.findViewById<View>(id)
                item.performClick()
            }
        }
    }

    private fun selectTabAtPosition(tabIndex: Int): ViewAction {
        return object : ViewAction {
            override fun getDescription() = "with tab at index $tabIndex"

            override fun getConstraints() = allOf(isDisplayed(),
                isAssignableFrom(TabLayout::class.java))

            override fun perform(uiController: UiController, view: View) {
                val tabLayout = view as TabLayout
                val tabAtIndex: TabLayout.Tab = tabLayout.getTabAt(tabIndex)
                    ?: throw PerformException.Builder()
                        .withCause(Throwable("No tab at index $tabIndex"))
                        .build()

                tabAtIndex.select()
            }
        }
    }
}
