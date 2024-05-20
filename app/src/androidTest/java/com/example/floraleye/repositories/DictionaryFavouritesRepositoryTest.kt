package com.example.floraleye.repositories

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.action.ViewActions.closeSoftKeyboard
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.example.floraleye.R
import com.example.floraleye.TestConstants
import com.example.floraleye.TestUtilities
import com.example.floraleye.ui.onboard.OnboardActivity
import org.junit.Assert
import org.junit.After
import org.junit.Test
import org.junit.Before
import org.junit.Rule
import org.junit.runner.RunWith
import org.junit.Ignore

@RunWith(AndroidJUnit4::class)
@Ignore class DictionaryFavouritesRepositoryTest {

    @get:Rule
    val onboardActivityRule = ActivityScenarioRule(OnboardActivity::class.java)

    private lateinit var favouritesRepo: DictionaryFavouritesRepository
    private lateinit var dictionaryRepo : DictionaryRepository

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

        InstrumentationRegistry.getInstrumentation().runOnMainSync {
            dictionaryRepo = DictionaryRepository()
            favouritesRepo = DictionaryFavouritesRepository()
        }
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

    @Test fun testDictionaryFavourites(){
        InstrumentationRegistry.getInstrumentation().runOnMainSync {
            dictionaryRepo.initializeFlowersList()
        }

        TestUtilities.makeCyclicViewTest(60, 1000) {
            dictionaryRepo.bIsFlowerListInitialized.value?.let { Assert.assertTrue(it) }
        }

        dictionaryRepo.flowersList.value?.let {
            favouritesRepo.getFavouriteFlowers(it)
        }

        Assert.assertNotEquals(0, favouritesRepo.favouritesFlowersList.value?.size)
    }
}
