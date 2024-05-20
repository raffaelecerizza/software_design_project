package com.example.floraleye.repositories

import android.content.ContentValues.TAG
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.floraleye.models.Quiz
import com.example.floraleye.utils.Constants
import com.example.floraleye.utils.Constants.FIREBASE_REALTIME_DB
import com.example.floraleye.utils.Constants.QUIZZES
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.DatabaseError

/**
 * Repository relativo ai singoli quiz sottoposti agli utenti.
 * Utilizzato principalmente per recuperare i quiz da Firebase Realtime Database.
 */
class QuizRepository : IQuizRepository {

    /**
     * Variabile che rappresenta l'istanza del Realtime Database di Firebase per FloralEye.
     */
    private val mDatabase: FirebaseDatabase = FirebaseDatabase.getInstance(FIREBASE_REALTIME_DB)

    /**
     * Variabile che rappresenta il riferimento al database. Utilizzato in particalore come
     * riferimento al database dei singoli quiz da sottoporre agli utenti.
     */
    private val mRefDatabase: DatabaseReference = mDatabase.reference.child(QUIZZES)

    /**
     * Variabile booleana che controlla se il download della lista di quiz è inziato.
     */
    override var isThreadStarted: Boolean = false
        private set

    /**
     * Metodo utilizzato per recuperare la lista di quiz da Firebase Realtime Database.
     * @return LiveData da osservare per conoscere il risultato del recupero dei quiz.
     */
    override fun getQuizzesMutableLiveData(): MutableLiveData<List<Quiz>> {
        isThreadStarted = true

        val quizzesLiveData: MutableLiveData<List<Quiz>> = MutableLiveData()
        val quizzesList = ArrayList<Quiz>()

        mRefDatabase.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (childSnapshot in dataSnapshot.children) {
                    val taskObj = childSnapshot.value as? HashMap<*, *>
                    if (taskObj != null) {
                        try {
                            val quiz = Quiz.Builder(taskObj[Constants.IDENTIFIER].toString())
                                .question(taskObj[Constants.QUESTION].toString())
                                .solution(taskObj[Constants.SOLUTION].toString())
                                .image(taskObj[Constants.IMAGE].toString())
                                .addAnswer(taskObj[Constants.ANSWER1].toString())
                                .addAnswer(taskObj[Constants.ANSWER2].toString())
                                .addAnswer(taskObj[Constants.ANSWER3].toString())
                                .addAnswer(taskObj[Constants.ANSWER4].toString())
                                .build()
                            quizzesList.add(quiz)
                        } catch (e : java.lang.Exception) {
                            Log.e(TAG, e.toString())
                        }
                    } else {
                        Log.w(TAG, "Invalid dataSnapshot value: $childSnapshot")
                    }
                }
                quizzesList.shuffle()
                quizzesLiveData.postValue(quizzesList)
            }

            override fun onCancelled(error: DatabaseError) {
                Log.w(TAG, Constants.FAILED_READ, error.toException())
            }
        })

        isThreadStarted = false
        return quizzesLiveData
    }

}
