package com.example.floraleye.repositories

import androidx.lifecycle.MutableLiveData
import com.example.floraleye.models.Quiz

/**
 * Interfaccia da utilizzare per la realizzazione del repository relativo ai quiz
 * da sottopporre agli utenti.
 */
interface IQuizRepository {

    /**
     * Variabile booleana che controlla se il download della lista di quiz è inziato.
     */
    val isThreadStarted: Boolean

    /**
     * Metodo utilizzato per recuperare la lista di quiz da Firebase Realtime Database.
     * @return LiveData da osservare per conoscere il risultato del recupero dei quiz.
     */
    fun getQuizzesMutableLiveData(): MutableLiveData<List<Quiz>>

}
