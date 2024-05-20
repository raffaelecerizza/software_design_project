package com.example.floraleye.repositories

import android.content.ContentValues
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.floraleye.models.Answer
import com.example.floraleye.models.AnsweredQuiz
import com.example.floraleye.models.Quiz
import com.example.floraleye.utils.Constants
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.DatabaseError
import java.time.ZonedDateTime

/**
 * Repository relativo ai quiz svolti dagli utenti.
 */
class QuizHistoryRepository : IQuizHistoryRepository {

    /**
     * Variabile che rappresenta l'istanza del Realtime Database di Firebase per FloralEye.
     */
    private val mDatabase: FirebaseDatabase =
        FirebaseDatabase.getInstance(Constants.FIREBASE_REALTIME_DB)

    /**
     * Variabile che rappresenta il riferimento al database. Utilizzato in particalore come
     * riferimento al database dei singoli quiz da sottoporre agli utenti.
     */
    private val mRefDatabase: DatabaseReference = mDatabase.reference.child(Constants.QUIZ_HISTORY)

    /**
     * Variabile che rappresenta l'identificatore univoco dell'utente corrente su Firebase.
     */
    private val mUserId = FirebaseAuth.getInstance().currentUser?.uid

    /**
     * Metodo utilizzato per recuperare la lista di quiz svolti dall'utente corrente
     * da Firebase Realtime Database.
     * @return LiveData da osservare per conoscere il risultato del recupero dei quiz.
     */
    override fun getQuizzesMutableLiveData(): MutableLiveData<List<AnsweredQuiz>> {
        val quizzesLiveData: MutableLiveData<List<AnsweredQuiz>> = MutableLiveData()
        val userId = FirebaseAuth.getInstance().currentUser?.uid.orEmpty()

        mRefDatabase.child(userId).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val quizzesList: ArrayList<AnsweredQuiz> = ArrayList()
                for (childSnapshot in dataSnapshot.children) {
                    val taskObj = childSnapshot.value as HashMap<*, *>
                    try {
                        val quiz = Quiz.Builder(
                            identifier = taskObj[Constants.QUIZ_ID].toString()
                        )
                            .question(taskObj[Constants.QUESTION].toString())
                            .solution(taskObj[Constants.SOLUTION].toString())
                            .image(taskObj[Constants.IMAGE].toString())
                            .addAnswer(taskObj[Constants.ANSWER1].toString())
                            .addAnswer(taskObj[Constants.ANSWER2].toString())
                            .addAnswer(taskObj[Constants.ANSWER3].toString())
                            .addAnswer(taskObj[Constants.ANSWER4].toString())
                            .build()
                        val answeredQuiz = AnsweredQuiz.Builder(
                            quizHistoryId = taskObj[Constants.QUIZ_HISTORY_ID].toString()
                        )
                            .setQuiz(quiz)
                            .setUserAnswer(taskObj[Constants.USER_ANSWER].toString())
                            .setTime(taskObj[Constants.TIME].toString())
                            .build()
                        quizzesList.add(answeredQuiz)
                    } catch (e : java.lang.Exception) {
                        Log.e(ContentValues.TAG, e.toString())
                    }
                }
                quizzesLiveData.postValue(quizzesList)
            }

            override fun onCancelled(error: DatabaseError) {
                Log.w(ContentValues.TAG, Constants.FAILED_READ, error.toException())
            }
        })

        return quizzesLiveData
    }

    /**
     * Metodo utilizzato per aggiungere un quiz svolto da un utente allo storico.
     * @param quiz Quiz sottoposto all'utente.
     * @param userAnswer Risposta dell'utente al quiz.
     * @param time Riferimento temporale della risposta dell'utente al quiz.
     */
    override fun addQuizToHistory(quiz: Quiz, userAnswer: Answer, time: ZonedDateTime) {
        val userId = FirebaseAuth.getInstance().currentUser?.uid.orEmpty()
        val quizHistoryId: String = mRefDatabase.push().key.toString()

        val values: HashMap<String, Any> = HashMap()
        values[Constants.QUIZ_HISTORY_ID] = quizHistoryId
        values[Constants.QUIZ_ID] = quiz.identifier
        values[Constants.QUESTION] = quiz.question
        values[Constants.IMAGE] = quiz.image
        values[Constants.ANSWER1] = quiz.answers[Constants.INDEX_ANSWER1].text
        values[Constants.ANSWER2] = quiz.answers[Constants.INDEX_ANSWER2].text
        values[Constants.ANSWER3] = quiz.answers[Constants.INDEX_ANSWER3].text
        values[Constants.ANSWER4] = quiz.answers[Constants.INDEX_ANSWER4].text
        values[Constants.SOLUTION] = quiz.solution
        values[Constants.USER_ANSWER] = userAnswer.text
        values[Constants.TIME] = time.toString()

        try {
            mRefDatabase.child(userId).child(quizHistoryId).setValue(values)
        } catch (e : java.lang.Exception) {
            Log.e(ContentValues.TAG, e.toString())
        }
    }

    /**
     * Metodo utilizzato per rimuovere un quiz dallo storico dell'utente.
     * @param quizHistoryId Id del quiz svolto nello storico dei quiz dell'utente.
     */
    override fun removeQuizFromHistory(quizHistoryId: String) {
        val userId = FirebaseAuth.getInstance().currentUser?.uid.orEmpty()
        mRefDatabase.child(userId).child(quizHistoryId)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    val quizzesList: ArrayList<DataSnapshot> = ArrayList()
                    for (childSnapshot in dataSnapshot.children) {
                        quizzesList.add(childSnapshot)
                    }
                    if (quizzesList.isNotEmpty()) {
                        try {
                            mRefDatabase.child(userId).child(quizHistoryId).setValue(null)
                        } catch (e : java.lang.Exception) {
                            Log.e(ContentValues.TAG, e.toString())
                        }
                    } else {
                        Log.w(ContentValues.TAG, Constants.EMPTY_QUIZ_HISTORY)
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.w(ContentValues.TAG, Constants.FAILED_READ, error.toException())
                }
            })
    }

    /**
     * Metodo utilizzato per rimuovere dallo storico tutti i quiz svolti dall'utente corrente.
     */
    override fun removeAllQuizzesFromHistory() {
        mUserId?.let {
            mRefDatabase.child(it).addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    try {
                        mRefDatabase.child(mUserId).setValue(null)
                    } catch (e : java.lang.Exception) {
                        Log.e(ContentValues.TAG, e.toString())
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.w(ContentValues.TAG, Constants.FAILED_READ, error.toException())
                }
            })
        }
    }

}
