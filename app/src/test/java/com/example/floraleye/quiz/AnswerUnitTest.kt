package com.example.floraleye.quiz

import com.example.floraleye.models.Answer
import com.example.floraleye.utils.TestConstants
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class AnswerUnitTest {

    private lateinit var mAnswer: Answer

    /**
     * Prima di eseguire ogni test è necessario creare un oggetto Answer.
     */
    @Before
    fun testAnswerCreation() {
        mAnswer = Answer(TestConstants.ANSWER0)
    }

    /**
     * Test della correttezza dei valori iniziali dell'oggetto Answer istanziato.
     */
    @Test
    fun testAssertEqualValues() {
        Assert.assertEquals(TestConstants.ANSWER0, mAnswer.text)
    }

}
