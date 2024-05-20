package com.example.floraleye.quiz

import com.example.floraleye.models.Quiz
import com.example.floraleye.utils.TestConstants
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class QuizUnitTest {

    private lateinit var mQuiz: Quiz

    /**
     * Prima di eseguire ogni test è necessario creare un Quiz.
     */
    @Before
    fun testQuizCreation() {
        mQuiz = Quiz.Builder(identifier = TestConstants.IDENTIFIER)
            .question(TestConstants.QUESTION)
            .image(TestConstants.IMAGE)
            .addAnswer(TestConstants.ANSWER0)
            .addAnswer(TestConstants.ANSWER1)
            .addAnswer(TestConstants.ANSWER2)
            .addAnswer(TestConstants.ANSWER3)
            .solution(TestConstants.SOLUTION)
            .build()
    }

    /**
     * Test della correttezza dei valori iniziali del Quiz istanziato.
     */
    @Test
    fun testAssertEqualValues() {
        assertEquals(TestConstants.IDENTIFIER, mQuiz.identifier)
        assertEquals(TestConstants.QUESTION, mQuiz.question)
        assertEquals(TestConstants.IMAGE, mQuiz.image)
        assertEquals(TestConstants.ANSWER0, mQuiz.answers[0].text)
        assertEquals(TestConstants.ANSWER1, mQuiz.answers[1].text)
        assertEquals(TestConstants.ANSWER2, mQuiz.answers[2].text)
        assertEquals(TestConstants.ANSWER3, mQuiz.answers[3].text)
        assertEquals(TestConstants.SOLUTION, mQuiz.solution)
    }

}
