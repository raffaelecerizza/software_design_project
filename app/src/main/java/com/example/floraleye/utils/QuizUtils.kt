package com.example.floraleye.utils

import android.view.animation.LinearInterpolator
import android.widget.ScrollView
import androidx.cardview.widget.CardView
import com.example.floraleye.models.AnsweredQuiz

/**
 * Classe contenente metodi di utilità per quanto concerne lo svolgimento dei quiz.
 */
object QuizUtils {

    /**
     * Metodo utilizzato per animare l'opacità del bordo di una CardView in modo che
     * passi gradualmente dalla trasparenza alla visibilità completa.
     * @param cardView CardView di cui si vuole animare l'opacità del bordo.
     */
    fun playAnimationAnswer(cardView: CardView) {
        cardView.alpha = Constants.START_OPACITY
        cardView.animate().apply {
            interpolator = LinearInterpolator()
            duration = Constants.ANSWER_ANIMATION_DURATION.toLong()
            alpha(Constants.END_OPACITY)
            start()
        }
    }

    /**
     * Metodo utilizzato per animare il passaggio al quiz successivo. L'animazione consiste
     * nella dissolvenza del vecchio quiz prima della visibilità completa del nuovo quiz.
     * @param scrollView ScrollView su cui operare l'animazione.
     */
    fun playAnimationNextButton(scrollView: ScrollView) {
        scrollView.alpha = Constants.START_OPACITY
        scrollView.animate().apply {
            interpolator = LinearInterpolator()
            duration = Constants.NEXT_BUTTON_ANIMATION_DURATION.toLong()
            alpha(Constants.END_OPACITY)
            start()
        }
    }

    /**
     * Metodo per calcolare il numero di risposte corrette date da un utente ai quiz che
     * gli sono stati sottoposti.
     * @param quizzes Lista di quiz per cui l'utente ha sottomesso una risposta.
     */
    fun computeCorrectAnswers(quizzes: List<AnsweredQuiz>): Int {
        return quizzes.count {it.userAnswer == it.quiz.solution}
    }

    /**
     * Metodo per calcolare il numero di risposte errate date da un utente ai quiz che
     * gli sono stati sottoposti.
     * @param quizzes Lista di quiz per cui l'utente ha sottomesso una risposta.
     */
    fun computeWrongAnswers(quizzes: List<AnsweredQuiz>): Int {
        return quizzes.count {it.userAnswer != it.quiz.solution}
    }

}
