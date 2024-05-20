package com.example.floraleye.ui.photo

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.net.Uri
import androidx.appcompat.app.AppCompatDelegate
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.scrollTo
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.example.floraleye.MainActivity
import com.example.floraleye.R
import com.example.floraleye.TestConstants
import com.example.floraleye.TestUtilities
import com.example.floraleye.TestUtilities.getString
import com.example.floraleye.TestUtilities.makeCyclicViewTest
import com.example.floraleye.ui.onboard.OnboardActivity
import org.hamcrest.MatcherAssert
import org.hamcrest.Matchers
import org.junit.Rule
import org.junit.Test
import org.junit.Before
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.runner.RunWith
import java.io.InputStream


@RunWith(AndroidJUnit4::class)
class PhotoFragmentTest {

    @get:Rule
    val onboardActivityRule = ActivityScenarioRule(OnboardActivity::class.java)

    /**
     * Prima di eseguire ogni test è necessario effettuare il login.
     */
    @Before fun login() {
        val buttonLogin = getString(R.string.str_login_button, onboardActivityRule)

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

        openActionBarOverflowOrOptionsMenu(InstrumentationRegistry
            .getInstrumentation().targetContext)

        onView(withText(R.string.str_logout)).perform(click())
    }

    /**
     * Test pop up per l'acquisizione di una immagine.
     */
    @Test fun testChoosePhotoPopUp() {
        val popUpMessage = getString(R.string.str_choose_photo_message, onboardActivityRule)
        val cancelButton = getString(R.string.str_cancel, onboardActivityRule)

        val intent = Intent(ApplicationProvider.getApplicationContext(), MainActivity::class.java)

        ActivityScenario.launch<MainActivity>(intent)

        val isPhotoButtonClicked =
            makeCyclicViewTest {
                onView(withId(R.id.navigation_photo)).perform(click())
            }
        assertTrue(isPhotoButtonClicked)

        onView(withId(R.id.choosePhotoButton)).perform(click())

        onView(withText(popUpMessage)).check(matches(isDisplayed()))

        onView(withText(cancelButton)).perform(click())
    }

    /**
     * Simulazione di inserimento di una immagine.
     */
    @Test fun testSetPhotoByBitmap() {
        val intent = Intent(ApplicationProvider.getApplicationContext(), MainActivity::class.java)
        val scenario = ActivityScenario.launch<MainActivity>(intent)

        val isPhotoButtonClicked =
            makeCyclicViewTest {
                onView(withId(R.id.navigation_photo)).perform(click())
            }
        assertTrue(isPhotoButtonClicked)

        val imageColor = Color.RED
        val imageColorRgb = Color.valueOf(imageColor)
        val image = TestUtilities.createImage(128, 128, imageColor)

        scenario.onActivity { activity ->
            val fragment = activity.getCurrentFragment()
            MatcherAssert.assertThat(fragment, Matchers.instanceOf(PhotoFragment::class.java))
            val photoFragment: PhotoFragment = fragment as PhotoFragment

            if (image != null) {
                photoFragment.setImageByBitmap(image)
            }
        }

        Thread.sleep(1000)

        scenario.onActivity { activity ->
            val fragment = activity.getCurrentFragment()
            MatcherAssert.assertThat(fragment, Matchers.instanceOf(PhotoFragment::class.java))
            val photoFragment: PhotoFragment = fragment as PhotoFragment

            val currentImage = photoFragment.getCurrentDisplayedImage()
            val currentImageColor =
                currentImage.getColor(currentImage.width/2,currentImage.height/2)
            assertEquals(imageColorRgb, currentImageColor)
        }
    }

    /**
     * Test inserimento di una immagine con uri non valido.
     */
    @Test fun testSetPhotoWithWrongUri() {
        val intent = Intent(ApplicationProvider.getApplicationContext(), MainActivity::class.java)
        val scenario = ActivityScenario.launch<MainActivity>(intent)

        val isPhotoButtonClicked =
            makeCyclicViewTest {
                onView(withId(R.id.navigation_photo)).perform(click())
            }
        assertTrue(isPhotoButtonClicked)

        val uri = Uri.parse("")

        scenario.onActivity { activity ->
            val fragment = activity.getCurrentFragment()
            MatcherAssert.assertThat(fragment, Matchers.instanceOf(PhotoFragment::class.java))
            val photoFragment: PhotoFragment = fragment as PhotoFragment

            photoFragment.setImageByUri(uri)
        }
    }

    /**
     * Test dell'identificazione di un'immagine che non rappresenta un fiore.
     */
    @Test fun testNotFlowerIdentification() {
        val notFlowerText = getString(R.string.str_not_flower, onboardActivityRule)
        val intent = Intent(ApplicationProvider.getApplicationContext(), MainActivity::class.java)
        val scenario = ActivityScenario.launch<MainActivity>(intent)

        val isPhotoButtonClicked =
            makeCyclicViewTest {
                onView(withId(R.id.navigation_photo)).perform(click())
            }
        assertTrue(isPhotoButtonClicked)

        val imageColor = Color.RED
        val image = TestUtilities.createImage(128, 128, imageColor)

        scenario.onActivity { activity ->
            val fragment = activity.getCurrentFragment()
            MatcherAssert.assertThat(fragment, Matchers.instanceOf(PhotoFragment::class.java))
            val photoFragment: PhotoFragment = fragment as PhotoFragment

            if (image != null) {
                photoFragment.setImageByBitmap(image)
            }
        }

        Thread.sleep(1000)

        val isIdentifyButtonClicked = makeCyclicViewTest {
            onView(withId(R.id.identifyButton)).perform(scrollTo(), click())
            Thread.sleep(1000)
            onView(withId(R.id.textViewFirstResult)).check(matches(withText(notFlowerText)))
        }
        assertTrue(isIdentifyButtonClicked)
    }

    /**
     * Test dell'identificazione di un'immagine che rappresenta un fiore.
     */
    @Test fun testFlowerIdentification() {
        val sunflowerText = TestConstants.SUNFLOWER_TEXT
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        val inputStream: InputStream = context.assets.open(TestConstants.SUNFLOWER_IMAGE)
        val bitmap: Bitmap = BitmapFactory.decodeStream(inputStream)

        val intent = Intent(ApplicationProvider.getApplicationContext(), MainActivity::class.java)
        val scenario = ActivityScenario.launch<MainActivity>(intent)

        val isPhotoButtonClicked =
            makeCyclicViewTest {
                onView(withId(R.id.navigation_photo)).perform(click())
            }
        assertTrue(isPhotoButtonClicked)

        scenario.onActivity { activity ->
            val fragment = activity.getCurrentFragment()
            MatcherAssert.assertThat(fragment, Matchers.instanceOf(PhotoFragment::class.java))
            val photoFragment: PhotoFragment = fragment as PhotoFragment
            photoFragment.setImageByBitmap(bitmap)
        }

        Thread.sleep(1000)

        val isIdentifyButtonClicked = makeCyclicViewTest {
            onView(withId(R.id.identifyButton)).perform(scrollTo(), click())
            Thread.sleep(1000)
            onView(withId(R.id.textViewFirstResult)).check(matches(withText(sunflowerText)))
            onView(withId(R.id.textViewFirstConfidence)).check(matches(
                ViewMatchers.withEffectiveVisibility(
                    ViewMatchers.Visibility.VISIBLE
                )
            ))
        }
        assertTrue(isIdentifyButtonClicked)
    }

    /**
     * Test del cambio di configurazione del PhotoFragment dopo un'identificazione.
     */
    @Test fun testChangeConfigurationPhoto() {
        val intent = Intent(ApplicationProvider.getApplicationContext(), MainActivity::class.java)
        val scenario = ActivityScenario.launch<MainActivity>(intent)

        val isPhotoButtonClicked =
            makeCyclicViewTest {
                onView(withId(R.id.navigation_photo)).perform(click())
            }
        assertTrue(isPhotoButtonClicked)

        Thread.sleep(1000)

        val isIdentifyButtonClicked = makeCyclicViewTest {
            onView(withId(R.id.identifyButton)).perform(scrollTo(), click())
        }
        assertTrue(isIdentifyButtonClicked)

        // Cambio tema da light a dark.
        scenario.onActivity { AppCompatDelegate.setDefaultNightMode(
            AppCompatDelegate.MODE_NIGHT_YES) }

        // Cambio tema da dark a light.
        scenario.onActivity { AppCompatDelegate.setDefaultNightMode(
            AppCompatDelegate.MODE_NIGHT_NO)}
    }

}
