package com.example.floraleye.repositories

import android.content.Intent
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ActivityScenario.launch
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.closeSoftKeyboard
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.scrollTo
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.espresso.matcher.ViewMatchers.isChecked
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import androidx.test.platform.app.InstrumentationRegistry.getInstrumentation
import androidx.viewpager2.widget.ViewPager2
import com.example.floraleye.MainActivity
import com.example.floraleye.R
import com.example.floraleye.TestConstants
import com.example.floraleye.TestUtilities
import com.example.floraleye.models.AnsweredQuiz
import com.example.floraleye.models.Quiz
import com.example.floraleye.ui.onboard.OnboardActivity
import com.example.floraleye.ui.profile.ProfileFragment
import com.example.floraleye.ui.quiz.NewQuizFragment
import com.example.floraleye.ui.quiz.QuizFragment
import org.hamcrest.MatcherAssert
import org.hamcrest.Matchers
import org.junit.Assert
import org.junit.Before
import org.junit.After
import org.junit.Rule
import org.junit.Test
import org.junit.Ignore
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@LargeTest
@Ignore class QuizHistoryRepositoryTest {

    @get:Rule
    val onboardActivityRule = ActivityScenarioRule(OnboardActivity::class.java)

    /**
     * Prima di eseguire ogni test è necessario effettuare il login.
     */
    @Before fun login() {
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
                closeSoftKeyboard()
            )

        onView(withId(R.id.inputEditTextPassword))
            .perform(
                ViewActions.replaceText(password),
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
            getInstrumentation().targetContext
        )

        onView(withText(R.string.str_logout)).perform(click())
    }

    /**
     * Test del recupero della lista di quiz svolti da un utente come LiveData.
     */
    @Ignore @Test fun testGetQuizHistory() {
        val intent = Intent(ApplicationProvider.getApplicationContext(), MainActivity::class.java)
        val scenario = launch<MainActivity>(intent)
        var quizzes = emptyList<Quiz>()

        val isQuizButtonClicked =
            TestUtilities.makeCyclicViewTest {
                onView(withId(R.id.navigation_quiz)).perform(click())
            }
        Assert.assertTrue(isQuizButtonClicked)

        val isQuizLoaded = TestUtilities.makeCyclicViewTest {
            onView(withId(R.id.questionImageView))
                .check(matches(ViewMatchers.withEffectiveVisibility(
                    ViewMatchers.Visibility.VISIBLE)))
        }
        Assert.assertTrue(isQuizLoaded)

        // Controllo che il Fragment mostrato sia quello relativo ai quiz e salvo
        // i quiz caricati in modo da poter controllare le risposte e la soluzione.
        scenario.onActivity {  activity ->
            val fragment = activity.getCurrentFragment()
            if (fragment is QuizFragment) {
                fragment.view?.findViewById<ViewPager2>(R.id.quizViewPager)
                val newQuizFragment: NewQuizFragment =
                    fragment.childFragmentManager.fragments[0] as NewQuizFragment
                Thread.sleep(1000)
                quizzes = newQuizFragment.getQuizzes()
            }
        }

        // Memorizzo gli elementi del quiz in apposite variabili.
        val quiz = Quiz.Builder(
            identifier = quizzes[0].identifier
        )
            .question(quizzes[0].question)
            .solution(quizzes[0].solution)
            .image(quizzes[0].image)
            .addAnswer(quizzes[0].answers[0].text)
            .addAnswer(quizzes[0].answers[1].text)
            .addAnswer(quizzes[0].answers[2].text)
            .addAnswer(quizzes[0].answers[3].text)
            .build()
        val answers = quizzes[0].answers.map { it.text }
        val wrongAnswer = answers.find { it != quizzes[0].solution }!!

        // Controllo che la domanda, i bottoni e l'immagine siano visibili.
        val areElementsDisplayed =
            TestUtilities.makeCyclicViewTest {
                checkIfElementsAreDisplayed()
            }
        Assert.assertTrue(areElementsDisplayed)

        val isSubmitButtonClicked =
            TestUtilities.makeCyclicViewTest {
                // Eseguo un click su un radio button che non corrisponde alla soluzione.
                onView(withText(wrongAnswer)).perform(click())

                // Controllo se il radio button è selezionato.
                onView(withText(wrongAnswer)).check(matches(isChecked()))

                // Eseguo un click sul bottone Submit per sottomettere la risposta.
                onView(withId(R.id.submitButton)).perform(scrollTo(), click())
            }
        Assert.assertTrue(isSubmitButtonClicked)

        val isViewFound = TestUtilities.makeCyclicViewTest {
            onView(withId(R.id.navigation_profile)).perform(click())
        }
        Assert.assertTrue(isViewFound)

        checkIfQuizIsAdded(scenario, quiz, wrongAnswer)
    }

    /**
     * Metodo per verificare se tutti gli elementi del NewQuizFragment sono visibili.
     */
    private fun checkIfElementsAreDisplayed() {
        onView(withId(R.id.questionTextView)).check(matches(isDisplayed()))
        onView(withId(R.id.questionImageView)).check(matches(isDisplayed()))
        onView(withId(R.id.radioButtonAnswer1)).check(matches(isDisplayed()))
        onView(withId(R.id.radioButtonAnswer2)).check(matches(isDisplayed()))
        onView(withId(R.id.radioButtonAnswer3)).check(matches(isDisplayed()))
        onView(withId(R.id.radioButtonAnswer4)).check(matches(isDisplayed()))
        onView(withId(R.id.submitButton)).check(matches(isDisplayed()))
        onView(withId(R.id.nextButton)).check(matches(isDisplayed()))
    }

    /**
     * Metodo per verificare se il quiz svolto è stato aggiunto al database dello storico
     * dei quiz.
     * @param scenario Scenario della MainActivity utilizzato per svolgere il test.
     * @param quiz Quiz svolto dall'utente.
     * @param wrongAnswer Risposta data dall'utente al quiz.
     */
    private fun checkIfQuizIsAdded(scenario: ActivityScenario<MainActivity>,
                                   quiz: Quiz, wrongAnswer: String) {
        scenario.onActivity {  activity ->
            val fragment = activity.getCurrentFragment()
            MatcherAssert.assertThat(fragment, Matchers.instanceOf(ProfileFragment::class.java))
            val profileFragment: ProfileFragment = fragment as ProfileFragment
            var userQuizzes: List<AnsweredQuiz>? = null
            var isQuizRemoved = false
            var attemptCount = 0
            while (userQuizzes.isNullOrEmpty() && attemptCount < 10 && !isQuizRemoved) {
                Thread.sleep(1000)
                userQuizzes = profileFragment.getQuizHistory()
                userQuizzes = userQuizzes.sortedWith(compareBy { it.time }).reversed()
                if (userQuizzes.isNotEmpty()) {
                    Assert.assertEquals(quiz.identifier, userQuizzes[0].quiz.identifier)
                    Assert.assertEquals(quiz.image, userQuizzes[0].quiz.image)
                    Assert.assertEquals(quiz.question, userQuizzes[0].quiz.question)
                    Assert.assertEquals(quiz.answers[0].text, userQuizzes[0].quiz.answers[0].text)
                    Assert.assertEquals(quiz.answers[1].text, userQuizzes[0].quiz.answers[1].text)
                    Assert.assertEquals(quiz.answers[2].text, userQuizzes[0].quiz.answers[2].text)
                    Assert.assertEquals(quiz.answers[3].text, userQuizzes[0].quiz.answers[3].text)
                    Assert.assertEquals(quiz.solution, userQuizzes[0].quiz.solution)
                    Assert.assertEquals(wrongAnswer, userQuizzes[0].userAnswer)
                    profileFragment.removeQuizFromHistory(userQuizzes[0].quizHistoryId)
                    isQuizRemoved = true
                }
                attemptCount++
            }
            Assert.assertTrue(isQuizRemoved)
        }

    }

}
