package com.example.floraleye.quiz

import com.example.floraleye.models.AnsweredQuiz
import com.example.floraleye.models.Quiz
import com.example.floraleye.utils.TestConstants
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class AnsweredQuizUnitTest {

    private lateinit var mQuiz: Quiz

    private lateinit var mAnsweredQuiz: AnsweredQuiz

    /**
     * Prima di eseguire ogni test è necessario creare un Quiz.
     */
    @Before
    fun testAnsweredQuizCreation() {
        mQuiz = Quiz.Builder(identifier = TestConstants.IDENTIFIER)
            .question(TestConstants.QUESTION)
            .image(TestConstants.IMAGE)
            .addAnswer(TestConstants.ANSWER1)
            .addAnswer(TestConstants.ANSWER2)
            .addAnswer(TestConstants.ANSWER3)
            .addAnswer(TestConstants.ANSWER4)
            .solution(TestConstants.SOLUTION)
            .build()

        mAnsweredQuiz = AnsweredQuiz.Builder(quizHistoryId = TestConstants.QUIZ_HISTORY_ID)
            .setQuiz(mQuiz)
            .setUserAnswer(TestConstants.USER_ANSWER)
            .setTime(TestConstants.TIME)
            .build()
    }

    /**
     * Test della correttezza dei valori iniziali del Quiz istanziato.
     */
    @Test
    fun testAssertEqualValues() {
        assertEquals(TestConstants.QUIZ_HISTORY_ID, mAnsweredQuiz.quizHistoryId)
        assertEquals(TestConstants.IDENTIFIER, mAnsweredQuiz.quiz.identifier)
        assertEquals(TestConstants.QUESTION, mAnsweredQuiz.quiz.question)
        assertEquals(TestConstants.IMAGE, mAnsweredQuiz.quiz.image)
        assertEquals(TestConstants.ANSWER1, mAnsweredQuiz.quiz.answers[0].text)
        assertEquals(TestConstants.ANSWER2, mAnsweredQuiz.quiz.answers[1].text)
        assertEquals(TestConstants.ANSWER3, mAnsweredQuiz.quiz.answers[2].text)
        assertEquals(TestConstants.ANSWER4, mAnsweredQuiz.quiz.answers[3].text)
        assertEquals(TestConstants.SOLUTION, mAnsweredQuiz.quiz.solution)
        assertEquals(TestConstants.USER_ANSWER, mAnsweredQuiz.userAnswer)
        assertEquals(TestConstants.TIME, mAnsweredQuiz.time)
    }

}
