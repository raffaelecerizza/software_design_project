package com.example.floraleye.ui.dictionary

import android.content.Intent
import android.view.View
import androidx.annotation.NonNull
import androidx.recyclerview.widget.RecyclerView
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.action.ViewActions.closeSoftKeyboard
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.swipeDown
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.BoundedMatcher
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.espresso.matcher.ViewMatchers.withEffectiveVisibility
import androidx.test.espresso.matcher.ViewMatchers.Visibility
import androidx.test.espresso.matcher.ViewMatchers.hasDescendant
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry.getInstrumentation
import com.example.floraleye.MainActivity
import com.example.floraleye.R
import com.example.floraleye.TestConstants
import com.example.floraleye.TestConstants.DICTIONARY_TEST_CONTROL_NUMBER
import com.example.floraleye.TestConstants.TEXT_FLOWER_SEARCH
import com.example.floraleye.TestUtilities
import com.example.floraleye.TestUtilities.getString
import com.example.floraleye.TestUtilities.makeCyclicViewTest
import com.example.floraleye.repositories.DictionaryRepository
import com.example.floraleye.ui.onboard.OnboardActivity
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.Matchers
import org.hamcrest.core.AllOf.allOf
import org.hamcrest.core.IsNot.not
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.Ignore
import org.junit.Assert
import org.junit.runner.RunWith
import kotlin.random.Random

@RunWith(AndroidJUnit4::class)
class DictionaryFragmentTest {

    @get:Rule
    val onboardActivityRule = ActivityScenarioRule(OnboardActivity::class.java)

    private val mainActivityIntent =
        Intent(ApplicationProvider.getApplicationContext(), MainActivity::class.java)

    /**
     * Prima di eseguire ogni test è necessario effettuare il login.
     */
    @Before fun login() {
        val buttonLogin = getString(R.string.str_login_button, onboardActivityRule)

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

        openActionBarOverflowOrOptionsMenu(
            getInstrumentation().targetContext
        )

        onView(withText(R.string.str_logout)).perform(click())
    }

    /**
     * Test inizializzazione fragment Dizionario.
     */
    @Test fun initializeDictionary() {
        lateinit var repository : DictionaryRepository

        getInstrumentation().runOnMainSync {
            repository = DictionaryRepository()
        }
        val dictionarySize = repository.flowersName.size

        ActivityScenario.launch<MainActivity>(mainActivityIntent)

        Assert.assertTrue(makeCyclicViewTest {
            onView(withId(R.id.navigation_dictionary)).perform(click())
        })

        Assert.assertTrue(makeCyclicViewTest(DICTIONARY_TEST_CONTROL_NUMBER, 1000) {
            onView(withId(R.id.rvFlowerList)).check(matches(isDisplayed()))
        })

        val randomSubset = List(10) { Random.nextInt(0, dictionarySize) }
        val isValidRandomSubset = makeCyclicViewTest {

            for (i in randomSubset){

                onView(withId(R.id.rvFlowerList)).perform(
                    RecyclerViewActions.scrollToPosition<DictionaryAdapter.FlowersViewHolder>(i)
                )
                onView(withId(R.id.rvFlowerList)).check(
                    matches(hasDescendant(isDisplayed()))
                )
            }
        }

        Assert.assertTrue(isValidRandomSubset)

        val isDictionaryFilled = makeCyclicViewTest {

            for (i in 0 until dictionarySize){

                onView(withId(R.id.rvFlowerList)).perform(
                    RecyclerViewActions.scrollToPosition<DictionaryAdapter.FlowersViewHolder>(i)
                )
                onView(withId(R.id.rvFlowerList)).check(matches(atPosition
                    (i, allOf(withId(R.id.cardView), withEffectiveVisibility(Visibility.VISIBLE))))
                )
            }
        }
        Assert.assertTrue(isDictionaryFilled)
    }

    /**
     * Test refresh del Dizionario.
     */
    @Test fun refreshDictionary(){
        lateinit var repository : DictionaryRepository
        val buttonYes = getString(R.string.str_ok, onboardActivityRule)
        val buttonNo = getString(R.string.str_cancel, onboardActivityRule)

        getInstrumentation().runOnMainSync {
            repository = DictionaryRepository()
        }
        val dictionarySize = repository.flowersName.size

        ActivityScenario.launch<MainActivity>(mainActivityIntent)

        Assert.assertTrue(makeCyclicViewTest {
            onView(withId(R.id.navigation_dictionary)).perform(click())
        })

        Assert.assertTrue(makeCyclicViewTest(DICTIONARY_TEST_CONTROL_NUMBER, 1000) {
            onView(withId(R.id.rvFlowerList)).check(matches(isDisplayed()))
        })

        val isDictionaryLoading = makeCyclicViewTest {
            onView(withId(R.id.swipe_refresh_dictionary))
                .perform(swipeDown())

            onView(withText(buttonYes)).perform(click())

            onView(withId(R.id.dictionaryProgressBar))
                .check(matches(withEffectiveVisibility(Visibility.VISIBLE)))

            onView(withId(R.id.dictionaryDownloadIndicator))
                .check(matches(withEffectiveVisibility(Visibility.VISIBLE)))

            onView(withId(R.id.rvFlowerList))
                .check(matches(withEffectiveVisibility(Visibility.INVISIBLE)))
        }
        Assert.assertTrue(isDictionaryLoading)

        val isCheckViewRefreshed = makeCyclicViewTest {
            onView(withId(R.id.swipe_refresh_dictionary))
                .perform(swipeDown())

            onView(withText(buttonNo)).perform(click())

            onView(withId(R.id.dictionaryDownloadIndicator))
                .check(matches(not(withText("0 / $dictionarySize"))))
        }
        Assert.assertTrue(isCheckViewRefreshed)
    }

    /**
     * Test ricerca di un fiore nel Dizionario.
     */
    @Test fun searchFlower(){
        ActivityScenario.launch<MainActivity>(mainActivityIntent)

        Assert.assertTrue(makeCyclicViewTest {
            onView(withId(R.id.navigation_dictionary)).perform(click())
        })

        Assert.assertTrue(makeCyclicViewTest(DICTIONARY_TEST_CONTROL_NUMBER, 1000) {
            onView(withId(R.id.rvFlowerList)).check(matches(isDisplayed()))
        })

        val hasFlowerBeenFound = makeCyclicViewTest {
            onView(withId(R.id.dictionarySearch))
                .perform(click())

            onView(withId(androidx.appcompat.R.id.search_src_text))
                .perform(typeText(TEXT_FLOWER_SEARCH), closeSoftKeyboard())

            onView(withId(R.id.rvFlowerList)).check(matches(atPosition
                (0, allOf(withId(R.id.cardView),
                withEffectiveVisibility(Visibility.VISIBLE))))
            )

            onView(withId(R.id.rvFlowerList)).check(matches(atPosition
                (0,hasDescendant(withText(TEXT_FLOWER_SEARCH))))
            )
        }
        Assert.assertTrue(hasFlowerBeenFound)
    }

    /**
     * Test dialog refresh del Dizionario.
     */
    @Test fun checkDialogAlert(){
        val buttonYes = getString(R.string.str_ok, onboardActivityRule)

        ActivityScenario.launch<MainActivity>(mainActivityIntent)

        Assert.assertTrue(makeCyclicViewTest {
            onView(withId(R.id.navigation_dictionary)).perform(click())
        })

        Assert.assertTrue(makeCyclicViewTest(DICTIONARY_TEST_CONTROL_NUMBER, 1000) {
            onView(withId(R.id.rvFlowerList)).check(matches(isDisplayed()))
        })

        val isDictionaryLoading = makeCyclicViewTest {
            onView(withId(R.id.swipe_refresh_dictionary))
                .perform(swipeDown())

            onView(withText(buttonYes)).perform(click())

            onView(withId(R.id.dictionaryProgressBar))
                .check(matches(withEffectiveVisibility(Visibility.VISIBLE)))

            onView(withId(R.id.dictionaryDownloadIndicator))
                .check(matches(withEffectiveVisibility(Visibility.VISIBLE)))

            onView(withId(R.id.rvFlowerList))
                .check(matches(withEffectiveVisibility(Visibility.INVISIBLE)))
        }
        Assert.assertTrue(isDictionaryLoading)
    }

    /**
     * Test gestione notifiche errore caricamento Dizionario.
     */
    @Ignore @Test fun checkErrorDictionary(){
        val buttonYes = getString(R.string.str_ok, onboardActivityRule)

        val hasMovedToDictionary = makeCyclicViewTest(100,100) {
            onView(withId(R.id.navigation_dictionary))
                .perform(click())

            onView(withId(R.id.rvFlowerList))
                .check(matches(withEffectiveVisibility(Visibility.VISIBLE)))
        }
        Assert.assertTrue(hasMovedToDictionary)

        getInstrumentation().uiAutomation.executeShellCommand(TestConstants.WIFI_DISABLE)
        getInstrumentation().uiAutomation.executeShellCommand(TestConstants.DATA_DISABLE)

        val hasRefreshedDictionary = makeCyclicViewTest(100,100) {
            onView(withId(R.id.swipe_refresh_dictionary))
                .perform(swipeDown())
            onView(withText(buttonYes)).perform(click())
        }
        Assert.assertTrue(hasRefreshedDictionary)

        val isErrorFound = makeCyclicViewTest(100, 100) {
            onView(
                Matchers.allOf(
                    withId(com.google.android.material.R.id.snackbar_text),
                    withText(R.string.str_snack_notification_dictionary)
                )
            ).check(matches(isDisplayed()))
        }
        Assert.assertTrue(isErrorFound)

        getInstrumentation().uiAutomation.executeShellCommand(TestConstants.WIFI_ENABLE)
        getInstrumentation().uiAutomation.executeShellCommand(TestConstants.DATA_ENABLE)
    }

    private fun atPosition(position: Int, @NonNull itemMatcher: Matcher<View?>): Matcher<View?> {
        return object : BoundedMatcher<View?, RecyclerView>(RecyclerView::class.java) {
            override fun describeTo(description: Description) {
                itemMatcher.describeTo(description)
            }

            override fun matchesSafely(view: RecyclerView): Boolean {
                val viewHolder = view.findViewHolderForAdapterPosition(position)
                    ?: return false
                return itemMatcher.matches(viewHolder.itemView)
            }
        }
    }
}


