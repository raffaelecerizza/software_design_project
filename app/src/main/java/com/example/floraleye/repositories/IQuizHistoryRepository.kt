package com.example.floraleye.repositories

import androidx.lifecycle.MutableLiveData
import com.example.floraleye.models.Answer
import com.example.floraleye.models.AnsweredQuiz
import com.example.floraleye.models.Quiz
import java.time.ZonedDateTime

/**
 * Interfaccia da utilizzare per la realizzazione del repository relativo allo storico
 * dei quiz a cui gli utenti hanno risposto.
 */
interface IQuizHistoryRepository {

    /**
     * Metodo utilizzato per recuperare la lista di quiz svolti dall'utente corrente
     * da Firebase Realtime Database.
     * @return LiveData da osservare per conoscere il risultato del recupero dei quiz.
     */
    fun getQuizzesMutableLiveData(): MutableLiveData<List<AnsweredQuiz>>

    /**
     * Metodo utilizzato per aggiungere un quiz svolto da un utente allo storico.
     * @param quiz Quiz sottoposto all'utente.
     * @param userAnswer Risposta dell'utente al quiz.
     * @param time Riferimento temporale della risposta dell'utente al quiz.
     */
    fun addQuizToHistory(quiz: Quiz, userAnswer: Answer, time: ZonedDateTime)

    /**
     * Metodo utilizzato per rimuovere un quiz dallo storico dell'utente.
     * @param quizHistoryId Id del quiz svolto nello storico dei quiz dell'utente.
     */
    fun removeQuizFromHistory(quizHistoryId: String)

    /**
     * Metodo utilizzato per rimuovere dallo storico tutti i quiz svolti dall'utente corrente.
     */
    fun removeAllQuizzesFromHistory()

}
