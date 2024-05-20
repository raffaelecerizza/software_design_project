package com.example.floraleye.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.floraleye.models.Answer
import com.example.floraleye.models.AnsweredQuiz
import com.example.floraleye.models.Quiz
import com.example.floraleye.repositories.IQuizRepository
import com.example.floraleye.repositories.QuizHistoryRepository
import java.time.ZonedDateTime

/**
 * ViewModel per la gestione delle funzionalità relative alla sottoposizione di quiz
 * agli utenti e allo storico dei quiz.
 */
class QuizViewModel (
    application: Application,
    private val quizRepository: IQuizRepository,
    private val quizHistoryRepository: QuizHistoryRepository
) : AndroidViewModel(application) {

    /**
     * Variabile che contiene come Live Data la lista di quiz da sottoporre agli utenti.
     * Viene utilizzata per salvare lo stato della lista di quiz utilizzata nella UI.
     */
    private var mQuizzes: MutableLiveData<List<Quiz>> = MutableLiveData()

    /**
     * Variabile che contiene come Live Data la lista di quiz svolti dall'utente corrente.
     */
    private var mQuizHistory: MutableLiveData<List<AnsweredQuiz>> = MutableLiveData()

    init {
        loadQuizzes()
    }

    /**
     * Metodo utilizzato per recuperare la lista di quiz da sottoporre agli utenti.
     * @return LiveData della lista di quiz.
     */
    fun getQuizzes(): LiveData<List<Quiz>> {
        return mQuizzes
    }

    /**
     * Metodo utilizzato per recuperare la lista di quiz svolti dall'utente corrente.
     * @return LiveData della lista di quiz.
     */
    fun getQuizHistory(): LiveData<List<AnsweredQuiz>> {
        return mQuizHistory
    }

    /**
     * Metodo utilizzato per caricare la lista di quiz da Firebase Realtime Database tramite
     * QuizRepository.
     */
    private fun loadQuizzes() {
        mQuizzes = quizRepository.getQuizzesMutableLiveData()
        mQuizHistory = quizHistoryRepository.getQuizzesMutableLiveData()
    }

    /**
     * Metodo utilizzato per salvare la risposta di un utente a un quiz tramite
     * QuizHistoryRepository.
     * @param quiz Quiz a cui l'utente ha risposto.
     * @param userAnswer Risposta dell'utente al quiz.
     * @param time Riferimento temporale della risposta dell'utente.
     */
    fun addQuizToHistory(quiz: Quiz, userAnswer: Answer, time: ZonedDateTime) {
        quizHistoryRepository.addQuizToHistory(quiz, userAnswer, time)
    }

    /**
     * Metodo utilizzato per rimuovere un quiz dallo storico dell'utente.
     * @param quizHistoryId Id del quiz svolto nello storico dei quiz dell'utente.
     */
    fun removeQuizFromHistory(quizHistoryId: String) {
        quizHistoryRepository.removeQuizFromHistory(quizHistoryId)
    }

    /**
     * Metodo per rimuovere dallo storico tutti i quiz svolti dall'utente corrente.
     */
    fun removeAllQuizzesFromHistory() {
        quizHistoryRepository.removeAllQuizzesFromHistory()
    }

    /**
     * Metodo per controllare se il thread che ottiene la lista di quiz è partito.
     * @return Boolean indica se il thread per il caricamento dei quiz è partito.
     */
    fun isLoadingQuizzes() : Boolean {
        return quizRepository.isThreadStarted
    }

}
