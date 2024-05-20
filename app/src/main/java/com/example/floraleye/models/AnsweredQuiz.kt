package com.example.floraleye.models

/**
 * Classe che rappresenta un singolo quiz a cui un utente ha risposto.
 */
class AnsweredQuiz private constructor(

    /**
     * Variabile che rappresenta l'identificatore dello specifico quiz svolto dall'utente.
     */
    val quizHistoryId: String,

    /**
     * Variabile che rappresenta il quiz svolto dall'utente.
     */
    val quiz: Quiz,

    /**
     * Variabile che rappresenta la risposta dell'utente al quiz.
     */
    val userAnswer: String,

    /**
     * Variabile che rappresenta il tempo di risposta al quiz.
     */
    val time: String
) {

    /**
     * Classe Builder per la creazione di oggetti della classe Quiz.
     */
    class Builder(
        /**
         * Variabile che rappresenta l'identificatore dello specifico quiz svolto dall'utente.
         */
        private val quizHistoryId: String
    ) {
        /**
         * Variabile che il quiz svolto dall'utente.
         */
        private lateinit var quiz: Quiz

        /**
         * Variabile che rappresenta la risposta dell'utente al quiz.
         */
        private var userAnswer: String = ""

        /**
         * Variabile che rappresenta il minuto di risposta al quiz.
         */
        private var time: String = ""

        /**
         * Metodo della classe Builder per modificare il valore della variabile quiz.
         */
        fun setQuiz(quiz: Quiz) = apply { this.quiz = quiz }

        /**
         * Metodo della classe Builder per modificare il valore della variabile userAnswer.
         */
        fun setUserAnswer(userAnswer: String) = apply { this.userAnswer = userAnswer }

        /**
         * Metodo della classe Builder per modificare il valore della variabile time.
         */
        fun setTime(time: String) = apply { this.time = time }

        /**
         * Metodo della classe Builder per inizializzare un oggetto della classe Quiz.
         */
        fun build() = AnsweredQuiz(
            quizHistoryId = quizHistoryId,
            quiz = quiz,
            userAnswer = userAnswer,
            time = time
        )
    }
}
