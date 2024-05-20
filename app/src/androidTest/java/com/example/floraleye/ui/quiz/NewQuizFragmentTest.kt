package com.example.floraleye.ui.quiz

import android.content.Intent
import android.view.View
import android.widget.RadioButton
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ActivityScenario.launch
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.closeSoftKeyboard
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.scrollTo
import androidx.test.espresso.action.ViewActions.swipeDown
import androidx.test.espresso.action.ViewActions.replaceText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.BoundedMatcher
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.isChecked
import androidx.test.espresso.matcher.ViewMatchers.isNotChecked
import androidx.test.espresso.matcher.ViewMatchers.isClickable
import androidx.test.espresso.matcher.ViewMatchers.isNotClickable
import androidx.test.espresso.matcher.ViewMatchers.isAssignableFrom
import androidx.test.espresso.matcher.ViewMatchers.hasDescendant
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
import com.google.android.material.card.MaterialCardView
import org.hamcrest.CoreMatchers.allOf
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.MatcherAssert
import org.hamcrest.Matchers
import org.junit.Rule
import org.junit.Before
import org.junit.After
import org.junit.Ignore
import org.junit.Test
import org.junit.Assert
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@LargeTest
class NewQuizFragmentTest {

    @get:Rule
    val onboardActivityRule = ActivityScenarioRule(OnboardActivity::class.java)

    @get:Rule
    val mainActivityRule = ActivityScenarioRule(MainActivity::class.java)

    private val green = ContextCompat.getColor(getInstrumentation().targetContext, R.color.green)

    private val red = ContextCompat.getColor(getInstrumentation().targetContext, R.color.red)

    private val mainActivityIntent =
        Intent(ApplicationProvider.getApplicationContext(), MainActivity::class.java)

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
     * Test dell'apertura e della visualizzazione di un NewQuizFragment.
     */
    @Test fun testNewQuizFragmentDisplayed() {
        val scenario = launch<MainActivity>(mainActivityIntent)

        val isQuizButtonClicked =
            TestUtilities.makeCyclicViewTest {
                onView(withId(R.id.navigation_quiz)).perform(click())
            }
        Assert.assertTrue(isQuizButtonClicked)

        scenario.onActivity {  activity ->
            val fragment = activity.getCurrentFragment()
            if (fragment is QuizFragment) {
                fragment.view?.findViewById<ViewPager2>(R.id.quizViewPager)
                val childFragment = fragment.childFragmentManager.fragments[0]
                MatcherAssert.assertThat(childFragment,
                    Matchers.instanceOf(NewQuizFragment::class.java))
            }
        }

        // Controllo che la domanda, i bottoni e l'immagine siano visibili.
        val areElementsDisplayed =
            TestUtilities.makeCyclicViewTest {
            checkIfElementsAreDisplayed()
        }
        Assert.assertTrue(areElementsDisplayed)
    }

    /**
     * Test della corretta presenza degli elementi di un quiz.
     */
    @Test fun testQuizDisplayed() {
        val submitString = TestUtilities.getString(R.string.str_submit_button, onboardActivityRule)
        val nextString = TestUtilities.getString(R.string.str_next_button, onboardActivityRule)

        val scenario = launch<MainActivity>(mainActivityIntent)
        var quizzes = emptyList<Quiz>()

        val isQuizButtonClicked =
            TestUtilities.makeCyclicViewTest {
                onView(withId(R.id.navigation_quiz)).perform(click())
            }
        Assert.assertTrue(isQuizButtonClicked)

        // Controllo che il Fragment mostrato sia quello relativo ai quiz e salvo
        // i quiz caricati in modo da poter controllare gli elementi del quiz.
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

        val areElementsDisplayed =
            TestUtilities.makeCyclicViewTest {
                // Controllo che la domanda, i bottoni e l'immagine siano visibili.
                checkIfElementsAreDisplayed()

                // Memorizzo la domanda e le risposte in apposite variabili.
                val question = quizzes[0].question
                val answer1 = quizzes[0].answers[0].text
                val answer2 = quizzes[0].answers[1].text
                val answer3 = quizzes[0].answers[2].text
                val answer4 = quizzes[0].answers[3].text

                // Verifico che gli elementi del quiz siano visualizzati nella pagina.
                checkIfElementsAreTheSame(question = question, answer1 = answer1,
                    answer2 = answer2, answer3 = answer3, answer4 = answer4)

                // Verifico che il bottone Submit riporti correttamente la scritta Submit.
                onView(withId(R.id.submitButton)).check(matches(withText(submitString)))

                // Verifico che il bottone Next riporti correttamente la scritta Next.
                onView(withId(R.id.nextButton)).check(matches(withText(nextString)))
            }
        Assert.assertTrue(areElementsDisplayed)
    }

    /**
     * Test per verificare che il click su un radio button già selezionato porti alla sua
     * deselezione.
     */
    @Test fun testUncheckRadioButton() {
        launch<MainActivity>(mainActivityIntent)

        val isQuizButtonClicked =
            TestUtilities.makeCyclicViewTest {
                onView(withId(R.id.navigation_quiz)).perform(click())
            }
        Assert.assertTrue(isQuizButtonClicked)

        val areRadioButtonsUnchecked =
            TestUtilities.makeCyclicViewTest {
            // Controllo che gli elementi del quiz siano visibili.
            checkIfElementsAreDisplayed()

            // Eseguo un click sul primo radio button e controllo che sia marcato come selezionato.
            onView(withId(R.id.radioButtonAnswer1)).perform(scrollTo(), click())
            onView(withId(R.id.radioButtonAnswer1)).check(matches(isChecked()))
            // Eseguo un altro click sul primo radio button e controllo che
            // non sia più marcato come selezionato.
            onView(withId(R.id.radioButtonAnswer1)).perform(scrollTo(), click())
            onView(withId(R.id.radioButtonAnswer1)).check(matches(isNotChecked()))

            // Eseguo un click sul secondo radio button e controllo che sia marcato come selezionato.
            onView(withId(R.id.radioButtonAnswer2)).perform(scrollTo(), click())
            onView(withId(R.id.radioButtonAnswer2)).check(matches(isChecked()))
            // Eseguo un altro click sul secondo radio button e controllo che
            // non sia più marcato come selezionato.
            onView(withId(R.id.radioButtonAnswer2)).perform(scrollTo(), click())
            onView(withId(R.id.radioButtonAnswer2)).check(matches(isNotChecked()))

            // Eseguo un click sul terzo radio button e controllo che sia marcato come selezionato.
            onView(withId(R.id.radioButtonAnswer3)).perform(scrollTo(), click())
            onView(withId(R.id.radioButtonAnswer3)).check(matches(isChecked()))
            // Eseguo un altro click sul terzo radio button e controllo che
            // non sia più marcato come selezionato.
            onView(withId(R.id.radioButtonAnswer3)).perform(scrollTo(), click())
            onView(withId(R.id.radioButtonAnswer3)).check(matches(isNotChecked()))

            // Eseguo un click sul quarto radio button e controllo che sia marcato come selezionato.
            onView(withId(R.id.radioButtonAnswer4)).perform(scrollTo(), click())
            onView(withId(R.id.radioButtonAnswer4)).check(matches(isChecked()))
            // Eseguo un altro click sul quarto radio button e controllo che
            // non sia più marcato come selezionato.
            onView(withId(R.id.radioButtonAnswer4)).perform(scrollTo(), click())
            onView(withId(R.id.radioButtonAnswer4)).check(matches(isNotChecked()))
        }
        Assert.assertTrue(areRadioButtonsUnchecked)
    }

    /**
     * Test per verificare che possa essere selezionato solo un radio button alla volta.
     */
    @Test fun testOnlyOneCheckedRadioButton() {
        launch<MainActivity>(mainActivityIntent)

        val isQuizButtonClicked =
            TestUtilities.makeCyclicViewTest {
                onView(withId(R.id.navigation_quiz)).perform(click())
            }
        Assert.assertTrue(isQuizButtonClicked)

        val isOnlyOneButtonChechked =
            TestUtilities.makeCyclicViewTest {
            // Controllo che gli elementi del quiz siano visibili.
            checkIfElementsAreDisplayed()

            /*
                Controllo che dopo il click sul primo radio button, solo questo
                sia marcato come selezionato.
            */
            onView(withId(R.id.radioButtonAnswer1)).perform(scrollTo(), click())
            onView(withId(R.id.radioButtonAnswer1)).check(matches(isChecked()))
            onView(withId(R.id.radioButtonAnswer2)).check(matches(isNotChecked()))
            onView(withId(R.id.radioButtonAnswer3)).check(matches(isNotChecked()))
            onView(withId(R.id.radioButtonAnswer4)).check(matches(isNotChecked()))

            /*
                Controllo che dopo il click sul secondo radio button, solo questo
                sia marcato come selezionato.
             */
            onView(withId(R.id.radioButtonAnswer2)).perform(scrollTo(), click())
            onView(withId(R.id.radioButtonAnswer1)).check(matches(isNotChecked()))
            onView(withId(R.id.radioButtonAnswer2)).check(matches(isChecked()))
            onView(withId(R.id.radioButtonAnswer3)).check(matches(isNotChecked()))
            onView(withId(R.id.radioButtonAnswer4)).check(matches(isNotChecked()))

            /*
                Controllo che dopo il click sul terzo radio button, solo questo
                sia marcato come selezionato.
             */
            onView(withId(R.id.radioButtonAnswer3)).perform(scrollTo(), click())
            onView(withId(R.id.radioButtonAnswer1)).check(matches(isNotChecked()))
            onView(withId(R.id.radioButtonAnswer2)).check(matches(isNotChecked()))
            onView(withId(R.id.radioButtonAnswer3)).check(matches(isChecked()))
            onView(withId(R.id.radioButtonAnswer4)).check(matches(isNotChecked()))

            /*
                Controllo che dopo il click sul quarto radio button, solo questo
                sia marcato come selezionato.
             */
            onView(withId(R.id.radioButtonAnswer4)).perform(scrollTo(), click())
            onView(withId(R.id.radioButtonAnswer1)).check(matches(isNotChecked()))
            onView(withId(R.id.radioButtonAnswer2)).check(matches(isNotChecked()))
            onView(withId(R.id.radioButtonAnswer3)).check(matches(isNotChecked()))
            onView(withId(R.id.radioButtonAnswer4)).check(matches(isChecked()))
        }
        Assert.assertTrue(isOnlyOneButtonChechked)
    }

    /**
     * Test per verificare che dopo un click sul bottone Submit non possano essere
     * modificate le risposte e non possa essere nuovamente sottomessa la stessa risposta
     * eseguendo un nuovo click sul bottone Submit.
     */
    @Test fun testButtonsNotClickable() {
        val scenario = launch<MainActivity>(mainActivityIntent)

        val isQuizButtonClicked =
            TestUtilities.makeCyclicViewTest {
                onView(withId(R.id.navigation_quiz)).perform(click())
            }
        Assert.assertTrue(isQuizButtonClicked)

        val areButtonsNotClickable =
            TestUtilities.makeCyclicViewTest {
                // Controllo che gli elementi del quiz siano visibili.
                checkIfElementsAreDisplayed()

                // Eseguo un click sul primo radio button.
                onView(withId(R.id.radioButtonAnswer1)).perform(scrollTo(), click())

                // Eseguo un click sul bottone Submit per sottomettere la risposta.
                onView(withId(R.id.submitButton)).perform(scrollTo(), click())

                // Controllo che non si possano cambiare le risposte eseguendo un nuovo click.
                // E controllo che non possa essere nuovamente sottomessa la risposta.
                checkIfButtonsAreNotClickable()
            }
        Assert.assertTrue(areButtonsNotClickable)

        // Rimuovo dallo storico il quiz appena sottomesso.
        removeSubmittedQuiz(scenario)
    }

    /**
     * Test del comportamento in caso di selezione della risposta esatta.
     */
    @Test fun testCorrectAnswer() {
        val scenario = launch<MainActivity>(mainActivityIntent)
        var quizzes = emptyList<Quiz>()

        val isQuizButtonClicked =
            TestUtilities.makeCyclicViewTest {
                onView(withId(R.id.navigation_quiz)).perform(click())
            }
        Assert.assertTrue(isQuizButtonClicked)

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

        val isAnswerCorrect = TestUtilities.makeCyclicViewTest {
            // Controllo che gli elementi del quiz siano visibili.
            checkIfElementsAreDisplayed()

            // Memorizzo la soluzione e le risposte in apposite variabili.
            val solution = quizzes[0].solution
            val answers = quizzes[0].answers.map { it.text }

            // Eseguo un click sul radio button corrispondente alla soluzione.
            onView(withText(solution)).perform(scrollTo(), click())

            // Eseguo un click sul bottone Submit per sottomettere la risposta.
            onView(withId(R.id.submitButton)).perform(scrollTo(), click())

            // Controllo che la risposta corrispondente alla soluzione abbia il bordo verde.
            onView(findCardViewWithText(solution)).check(matches(matchesBorderColor(green)))

            // Controllo che le risposte diverse dalla soluzione non abbiano il bordo verde.
            for (answer in answers.filterNot { it == solution }) {
                onView(findCardViewWithText(answer)).check(matches(notMatchesBorderColor(green)))
            }
        }
        Assert.assertTrue(isAnswerCorrect)

        // Rimuovo dallo storico il quiz appena sottomesso.
        removeSubmittedQuiz(scenario)
    }

    /**
     * Test del comportamento in caso di selezione della risposta errata.
     */
    @Test fun testWrongAnswer() {
        val scenario = launch<MainActivity>(mainActivityIntent)
        var quizzes = emptyList<Quiz>()

        val isQuizButtonClicked =
            TestUtilities.makeCyclicViewTest {
                onView(withId(R.id.navigation_quiz)).perform(click())
            }
        Assert.assertTrue(isQuizButtonClicked)

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

        val isAnswerWrong = TestUtilities.makeCyclicViewTest {
            // Controllo che gli elementi del quiz siano visibili.
            checkIfElementsAreDisplayed()

            // Memorizzo la soluzione e le risposte in apposite variabili.
            val solution = quizzes[0].solution
            val answers = quizzes[0].answers.map { it.text }

            // Eseguo un click su un radio button che non corrisponde alla soluzione.
            val wrongAnswer = answers.find { it != solution }!!
            onView(withText(wrongAnswer)).perform(scrollTo(), click())

            // Eseguo un click sul bottone Submit per sottomettere la risposta.
            onView(withId(R.id.submitButton)).perform(scrollTo(), click())

            // Controllo che la soluzione abbia il bordo verde.
            onView(findCardViewWithText(solution)).check(matches(matchesBorderColor(green)))

            // Controllo che la risposta sbagliata abbia il bordo rosso.
            onView(findCardViewWithText(wrongAnswer)).check(matches(matchesBorderColor(red)))

            // Controllo che le altre risposte non abbiano il bordo né verde né rosso.
            for (answer in answers.filterNot { it == solution || it == wrongAnswer }) {
                onView(findCardViewWithText(answer)).check(matches(notMatchesBorderColor(green)))
                onView(findCardViewWithText(answer)).check(matches(notMatchesBorderColor(red)))
            }
        }
        Assert.assertTrue(isAnswerWrong)

        // Rimuovo dallo storico il quiz appena sottomesso.
        removeSubmittedQuiz(scenario)
    }

    /**
     * Test del click sul bottone Next dopo aver sottomesso una risposta.
     */
    @Test fun testNextButtonWithAnswer() {
        val scenario = launch<MainActivity>(mainActivityIntent)
        var quizzes = emptyList<Quiz>()

        val isQuizButtonClicked =
            TestUtilities.makeCyclicViewTest {
                onView(withId(R.id.navigation_quiz)).perform(click())
            }
        Assert.assertTrue(isQuizButtonClicked)

        // Controllo che il Fragment mostrato sia quello relativo ai quiz e salvo
        // i quiz caricati in modo da poterli utilizzare.
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

        // Controllo che gli elementi del quiz siano visibili.
        val areElementsVisibile =
            TestUtilities.makeCyclicViewTest {
                checkIfElementsAreDisplayed()
            }
        Assert.assertTrue(areElementsVisibile)

        // Memorizzo la soluzione dei primi due quiz.
        val solutionFirstQuiz = quizzes[0].solution
        val solutionSecondQuiz = quizzes[1].solution

        val isNextButtonClicked =
            TestUtilities.makeCyclicViewTest {
                // Eseguo un click sul radio button corrispondente alla soluzione del primo quiz.
                onView(withText(solutionFirstQuiz)).perform(scrollTo(), click())

                // Eseguo un click sul bottone Submit per sottomettere la risposta.
                onView(withId(R.id.submitButton)).perform(scrollTo(), click())

                // Eseguo un click sul bottone Next per passare a un nuovo quiz.
                onView(withId(R.id.nextButton)).check(matches(isDisplayed()))
                onView(withId(R.id.nextButton)).perform(scrollTo(), click())
            }
        Assert.assertTrue(isNextButtonClicked)

        val isSecondQuizCorrect =
            TestUtilities.makeCyclicViewTest {
                // Controllo che nel nuovo quiz ci sia la soluzione del secondo quiz
                // ed eseguo un click sul radio button corrispondente.
                onView(withText(solutionSecondQuiz)).perform(scrollTo(), click())

                val radioIds = intArrayOf(R.id.radioButtonAnswer1, R.id.radioButtonAnswer2,
                    R.id.radioButtonAnswer3, R.id.radioButtonAnswer4)
                val cardViewIds = intArrayOf(R.id.cardView_radioButtonAnswer1,
                    R.id.cardView_radioButtonAnswer2, R.id.cardView_radioButtonAnswer3,
                    R.id.cardView_radioButtonAnswer4)

                for (i in radioIds.indices) {
                    // Controllo che i radio button siano clickabili.
                    onView(withId(radioIds[i])).check(matches(isClickable()))
                    // Controllo che il bordo delle CardView non sia né verde né rosso.
                    onView(withId(cardViewIds[i])).check(matches(notMatchesBorderColor(green)))
                    onView(withId(cardViewIds[i])).check(matches(notMatchesBorderColor(red)))
                }

                // Eseguo un click sul bottone Submit per sottomettere la risposta.
                onView(withId(R.id.submitButton)).perform(scrollTo(), click())

                // Controllo che la soluzione del secondo quiz sia corretta.
                onView(findCardViewWithText(solutionSecondQuiz))
                    .check(matches(matchesBorderColor(green)))
            }
        Assert.assertTrue(isSecondQuizCorrect)

        // Rimuovo dallo storico i due quiz sottomessi.
        removeSubmittedQuiz(scenario)
        removeSubmittedQuiz(scenario)
    }

    /**
     * Test del click sul bottone Next senza aver prima sottomesso una risposta.
     */
    @Test fun testNextButtonWithoutAnswer() {
        launch<MainActivity>(mainActivityIntent)

        val isQuizButtonClicked =
            TestUtilities.makeCyclicViewTest {
                onView(withId(R.id.navigation_quiz)).perform(click())
            }
        Assert.assertTrue(isQuizButtonClicked)

        // Controllo che gli elementi del quiz siano visibili.
        var areElementsVisibile =
            TestUtilities.makeCyclicViewTest {
            checkIfElementsAreDisplayed()
        }
        Assert.assertTrue(areElementsVisibile)

        val isNextButtonClicked =
            TestUtilities.makeCyclicViewTest {
                // Eseguo un click sul bottone Next per passare a un nuovo quiz.
                onView(withId(R.id.nextButton)).perform(scrollTo(), click())
            }
        Assert.assertTrue(isNextButtonClicked)

        // Controllo che gli elementi del quiz siano visibili.
        areElementsVisibile =
            TestUtilities.makeCyclicViewTest {
                checkIfElementsAreDisplayed()
            }
        Assert.assertTrue(areElementsVisibile)

        val isNewQuizShown =
            TestUtilities.makeCyclicViewTest {
                val radioIds = intArrayOf(R.id.radioButtonAnswer1, R.id.radioButtonAnswer2,
                    R.id.radioButtonAnswer3, R.id.radioButtonAnswer4)
                val cardViewIds = intArrayOf(R.id.cardView_radioButtonAnswer1,
                    R.id.cardView_radioButtonAnswer2, R.id.cardView_radioButtonAnswer3,
                    R.id.cardView_radioButtonAnswer4)

                for (i in radioIds.indices) {
                    // Controllo che i radio button siano clickabili.
                    onView(withId(radioIds[i])).check(matches(isClickable()))
                    // Controllo che il bordo delle CardView non sia né verde né rosso.
                    onView(withId(cardViewIds[i])).check(matches(notMatchesBorderColor(green)))
                    onView(withId(cardViewIds[i])).check(matches(notMatchesBorderColor(red)))
                }
            }
        Assert.assertTrue(isNewQuizShown)
    }

    /**
     * Test dello swipe refresh per passare a un nuovo quiz.
     */
    @Test fun testSwipeRefresh() {
        launch<MainActivity>(mainActivityIntent)

        val isQuizButtonClicked =
            TestUtilities.makeCyclicViewTest {
                onView(withId(R.id.navigation_quiz)).perform(click())
            }
        Assert.assertTrue(isQuizButtonClicked)

        val isScrollViewSwiped =
            TestUtilities.makeCyclicViewTest {
                // Eseguo uno swipe verso il basso per passare a un nuovo quiz.
                onView(withId(R.id.scrollViewQuizFragment)).perform(swipeDown())
            }
        Assert.assertTrue(isScrollViewSwiped)
    }

    /**
     * Test del cambio di configurazione da tema chiaro a tema scuro e viceversa.
     */
    @Test fun testChangeDarkLightTheme() {
        val scenario = launch<MainActivity>(mainActivityIntent)
        var quizzes = emptyList<Quiz>()

        val isQuizButtonClicked =
            TestUtilities.makeCyclicViewTest {
                onView(withId(R.id.navigation_quiz)).perform(click())
            }
        Assert.assertTrue(isQuizButtonClicked)

        // Controllo che il Fragment mostrato sia quello relativo ai quiz e salvo
        // i quiz caricati in modo da poter controllare gli elementi del quiz dopo il
        // cambio di configurazione.
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

        // Controllo che la domanda, i bottoni e l'immagine siano visibili.
        var areElementsDisplayed =
            TestUtilities.makeCyclicViewTest {
                checkIfElementsAreDisplayed()
            }
        Assert.assertTrue(areElementsDisplayed)

        // Memorizzo la domanda, la soluzione e le risposte in apposite variabili.
        val question = quizzes[0].question
        val answer1 = quizzes[0].answers[0].text
        val answer2 = quizzes[0].answers[1].text
        val answer3 = quizzes[0].answers[2].text
        val answer4 = quizzes[0].answers[3].text
        val answers = quizzes[0].answers.map { it.text }
        val solution = quizzes[0].solution

        // Eseguo un click su un radio button che non corrisponde alla soluzione.
        val wrongAnswer = answers.find { it != solution }!!
        onView(withText(wrongAnswer)).perform(scrollTo(), click())

        // Eseguo un click sul bottone Submit per sottomettere la risposta.
        onView(withId(R.id.submitButton)).perform(scrollTo(), click())

        // Controllo che la soluzione abbia il bordo verde.
        onView(findCardViewWithText(solution)).check(matches(matchesBorderColor(green)))

        // Controllo che la risposta sbagliata abbia il bordo rosso.
        onView(findCardViewWithText(wrongAnswer)).check(matches(matchesBorderColor(red)))

        // Cambio tema da light a dark.
        scenario.onActivity { AppCompatDelegate.setDefaultNightMode(
                AppCompatDelegate.MODE_NIGHT_YES) }

        // Controllo che la domanda, i bottoni e l'immagine siano ancora visibili.
        areElementsDisplayed =
            TestUtilities.makeCyclicViewTest {
                checkIfElementsAreDisplayed()
            }
        Assert.assertTrue(areElementsDisplayed)

        // Controllo che la domanda e le risposte del quiz siano ancora le stesse.
        checkIfElementsAreTheSame(question = question, answer1 = answer1,
            answer2 = answer2, answer3 = answer3, answer4 = answer4)

        // Controllo la consistenza dei dati dopo il cambio di configurazione.
        checkConfigChangeConsistency(wrongAnswer = wrongAnswer, solution = solution)

        // Controllo che i radio button e il botton Submit non siano selezionabili visto che
        // prima del cambio di configurazione era stato clickato il bottone Submit.
        checkIfButtonsAreNotClickable()

        // Cambio tema da dark a light.
        scenario.onActivity { AppCompatDelegate.setDefaultNightMode(
                AppCompatDelegate.MODE_NIGHT_NO)}

        // Controllo che la domanda, i bottoni e l'immagine siano ancora visibili.
        areElementsDisplayed =
            TestUtilities.makeCyclicViewTest {
                checkIfElementsAreDisplayed()
            }
        Assert.assertTrue(areElementsDisplayed)

        // Controllo che la domanda e le risposte del quiz siano ancora le stesse.
        checkIfElementsAreTheSame(question = question, answer1 = answer1,
            answer2 = answer2, answer3 = answer3, answer4 = answer4)

        // Controllo la consistenza dei dati dopo il cambio di configurazione.
        checkConfigChangeConsistency(wrongAnswer = wrongAnswer, solution = solution)

        // Controllo che i radio button e il botton Submit non siano selezionabili visto che
        // prima del cambio di configurazione era stato clickato il bottone Submit.
        checkIfButtonsAreNotClickable()

        // Rimuovo dallo storico il quiz appena sottomesso.
        removeSubmittedQuiz(scenario)
    }

    /**
     * Test del funzionamento del NewQuizFragment in assenza di connessione alla rete.
     */
    @Ignore
    @Test fun testNoConnectivity() {
        val scenario = launch<MainActivity>(mainActivityIntent)
        var quizzes = emptyList<Quiz>()

        val isQuizButtonClicked =
            TestUtilities.makeCyclicViewTest(10, 1000) {
                onView(withId(R.id.navigation_quiz)).perform(click())
            }
        Assert.assertTrue(isQuizButtonClicked)

        // Controllo che il Fragment mostrato sia quello relativo ai quiz e salvo
        // i quiz caricati in modo da poter controllare gli elementi del quiz dopo la
        // disconnessione dalla rete.
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

        // Controllo che la domanda, i bottoni e l'immagine siano visibili.
        var areElementsDisplayed =
            TestUtilities.makeCyclicViewTest(10, 1000) {
                checkIfElementsAreDisplayed()
            }
        Assert.assertTrue(areElementsDisplayed)

        // Memorizzo la domanda e le risposte in apposite variabili.
        val question = quizzes[0].question
        val answer1 = quizzes[0].answers[0].text
        val answer2 = quizzes[0].answers[1].text
        val answer3 = quizzes[0].answers[2].text
        val answer4 = quizzes[0].answers[3].text

        // Disabilitazione connettività.
        getInstrumentation().uiAutomation.executeShellCommand(TestConstants.WIFI_DISABLE)
        getInstrumentation().uiAutomation.executeShellCommand(TestConstants.DATA_DISABLE)

        // Controllo che la domanda, i bottoni e l'immagine siano ancora visibili.
        areElementsDisplayed =
            TestUtilities.makeCyclicViewTest(10, 1000) {
                checkIfElementsAreDisplayed()
            }
        Assert.assertTrue(areElementsDisplayed)

        // Controllo che la domanda e le risposte del quiz siano ancora le stesse.
        checkIfElementsAreTheSame(question = question, answer1 = answer1,
            answer2 = answer2, answer3 = answer3, answer4 = answer4)

        // Riattivazione connettività.
        getInstrumentation().uiAutomation.executeShellCommand(TestConstants.WIFI_ENABLE)
        getInstrumentation().uiAutomation.executeShellCommand(TestConstants.DATA_ENABLE)

        // Controllo che la domanda, i bottoni e l'immagine siano ancora visibili.
        areElementsDisplayed =
            TestUtilities.makeCyclicViewTest(10, 1000) {
                checkIfElementsAreDisplayed()
            }
        Assert.assertTrue(areElementsDisplayed)

        // Controllo che la domanda e le risposte del quiz siano ancora le stesse.
        checkIfElementsAreTheSame(question = question, answer1 = answer1,
            answer2 = answer2, answer3 = answer3, answer4 = answer4)
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
     * Metodo per verificare se gli elementi visibili del quiz corrispondono a quelli
     * del quiz ottenuto da Firebase Realtime Database.
     * @param question stringa rappresentante la domanda del quiz.
     * @param answer1 stringa rappresentante la prima risposta del quiz.
     * @param answer2 stringa rappresentante la seconda risposta del quiz.
     * @param answer3 stringa rappresentante la terza risposta del quiz.
     * @param answer4 stringa rappresentante la quarta risposta del quiz.
     */
    private fun checkIfElementsAreTheSame(question: String,
                                          answer1: String, answer2: String,
                                          answer3: String, answer4: String) {
        onView(withId(R.id.questionTextView)).check(matches(withText(question)))
        onView(withId(R.id.radioButtonAnswer1)).check(matches(withText(answer1)))
        onView(withId(R.id.radioButtonAnswer2)).check(matches(withText(answer2)))
        onView(withId(R.id.radioButtonAnswer3)).check(matches(withText(answer3)))
        onView(withId(R.id.radioButtonAnswer4)).check(matches(withText(answer4)))
    }

    /**
     * Metodo per verificare che i radio button e il bottone Submit non siano clickabili.
     */
    private fun checkIfButtonsAreNotClickable() {
        onView((withId(R.id.radioButtonAnswer1))).check(matches(isNotClickable()))
        onView((withId(R.id.radioButtonAnswer2))).check(matches(isNotClickable()))
        onView((withId(R.id.radioButtonAnswer3))).check(matches(isNotClickable()))
        onView((withId(R.id.radioButtonAnswer4))).check(matches(isNotClickable()))
        onView((withId(R.id.submitButton))).check(matches(isNotClickable()))
    }

    /**
     * Metodo per verificare se dopo il cambio di configurazione vi è consistenza
     * con riferimento alla risposta data e alla soluzione.
     * @param wrongAnswer stringa rappresentante una risposta errata al quiz.
     * @param solution stringa rappresentante la soluzione del quiz.
     */
    private fun checkConfigChangeConsistency(wrongAnswer: String, solution: String) {
        // Controllo che la risposta selezionata prima del cambio di configurazione
        // sia ancora selezionata.
        onView(withText(wrongAnswer)).check(matches(isChecked()))

        // Controllo che la soluzione abbia ancora il bordo verde.
        onView(findCardViewWithText(solution)).check(matches(matchesBorderColor(green)))

        // Controllo che la risposta sbagliata abbia ancora il bordo rosso.
        onView(findCardViewWithText(wrongAnswer)).check(matches(matchesBorderColor(red)))
    }

    /**
     * Metodo per verificare se il bordo di una CardView ha un colore specifico.
     * @param color colore di cui si vuole verificare la presenza nel bordo.
     * @return Matcher da utilizzare nei metodi di Espresso.
     */
    private fun matchesBorderColor(color: Int): Matcher<View> {
        return object : BoundedMatcher<View, View>(View::class.java) {
            override fun describeTo(description: Description) {
                description.appendText(TestConstants.BORDER_TINT_MATCH + color)
            }

            override fun matchesSafely(view: View): Boolean {
                val materialCardView = view as MaterialCardView
                val borderColorStateList = materialCardView.strokeColorStateList
                return borderColorStateList != null && borderColorStateList.defaultColor == color
            }
        }
    }

    /**
     * Metodo per verificare se il bordo di una CardView non ha un colore specifico.
     * @param color colore di cui si vuole verificare l'assenza nel bordo.
     * @return Matcher da utilizzare nei metodi di Espresso.
     */
    private fun notMatchesBorderColor(color: Int): Matcher<View> {
        return object : BoundedMatcher<View, View>(View::class.java), Matcher<View> {
            override fun describeTo(description: Description) {
                description.appendText(TestConstants.BORDER_TINT_NOT_MATCH + color)
            }

            override fun matchesSafely(view: View): Boolean {
                val materialCardView = view as MaterialCardView
                val borderColorStateList = materialCardView.strokeColorStateList
                return borderColorStateList == null || borderColorStateList.defaultColor != color
            }
        }
    }

    /**
     * Metodo per trovare una CardView contenente un radio button con un testo specifico.
     * @param text testo che deve essere presente nel radio button.
     * @return Matcher da utilizzare nei metodi di Espresso.
     */
    private fun findCardViewWithText(text: String): Matcher<View> {
        return allOf(
            // Cerca una View di tipo CardView.
            isAssignableFrom(MaterialCardView::class.java),
            // Controlla che la View sia visibile.
            isDisplayed(),
            // Cerca una View con figli che sono RadioButton.
            hasDescendant(isAssignableFrom(RadioButton::class.java)),
            // Controlla che uno dei RadioButton abbia il testo uguale
            // al testo passato come parametro.
            hasDescendant(withText(text))
        )
    }

    /**
     * Metodo per rimuovere il quiz sottomesso per completare un test.
     * @param scenario Scenario della MainActivity utilizzato per svolgere il test.
     */
    private fun removeSubmittedQuiz(scenario: ActivityScenario<MainActivity>) {
        val isViewFound = TestUtilities.makeCyclicViewTest(10, 1000) {
            onView(withId(R.id.navigation_profile)).perform(click())
        }
        Assert.assertTrue(isViewFound)

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
                    profileFragment.removeQuizFromHistory(userQuizzes[0].quizHistoryId)
                    isQuizRemoved = true
                }
                attemptCount++
            }
            Assert.assertTrue(isQuizRemoved)
        }
    }

}
