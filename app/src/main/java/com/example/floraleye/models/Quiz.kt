package com.example.floraleye.models

/**
 * Classe che rappresenta un singolo quiz da sottoporre agli utenti.
 */
class Quiz private constructor(
    /**
     * Variabile che rappresenta l'identificatore di un quiz.
     */
    val identifier: String,

    /**
     * Variabile che rappresenta la domanda del quiz.
     */
    val question: String,

    /**
     * Variabile che rappresenta l'immagine di un fiore utilizzata dal quiz.
     */
    val image: String,

    /**
     * Variabile che rappresenta la lista di possibili risposte proposte all'utente.
     */
    val answers: List<Answer>,

    /**
     * Variabile che rappresenta la soluzione del quiz.
     */
    val solution: String
) {

    /**
     * Classe Builder per la creazione di oggetti della classe Quiz.
     */
    class Builder(
        /**
         * Variabile che rappresenta l'identificatore di un quiz.
         */
        private val identifier: String
    ) {

        /**
         * Variabile che rappresenta la domanda del quiz.
         */
        private var question: String = ""

        /**
         * Variabile che rappresenta l'immagine di un fiore utilizzata dal quiz.
         */
        private var image: String = ""

        /**
         * Variabile che rappresenta la lista di possibili risposte proposte all'utente.
         */
        private val answers: MutableList<Answer> = mutableListOf()

        /**
         * Variabile che rappresenta la soluzione del quiz.
         */
        private var solution: String = ""

        /**
         * Metodo della classe Builder per modificare il valore della variabile question.
         */
        fun question(question: String) = apply { this.question = question }

        /**
         * Metodo della classe Builder per modificare il valore della variabile solution.
         */
        fun solution(solution: String) = apply { this.solution = solution }

        /**
         * Metodo della classe Builder per modificare il valore della variabile image.
         */
        fun image(image: String) = apply { this.image = image }

        /**
         * Metodo della classe Builder per aggiungere una risposta alla lista delle risposte del quiz.
         */
        fun addAnswer(text: String) = apply { answers.add(Answer(text)) }

        /**
         * Metodo della classe Builder per inizializzare un oggetto della classe Quiz.
         */
        fun build() = Quiz(
            identifier = identifier,
            question = question,
            image = image,
            answers = answers.toList(),
            solution = solution
        )
    }
}
