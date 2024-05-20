package com.example.floraleye.repositories

import android.content.ContentValues
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.floraleye.models.DictionaryFlower
import com.example.floraleye.utils.Constants
import com.example.floraleye.utils.Constants.FIREBASE_REALTIME_DB
import com.example.floraleye.utils.Constants.DICTIONARY_FAVOURITES
import com.example.floraleye.utils.Constants.DICTIONARY_FLOWERS_ARRAY
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.DatabaseError

/**
 * Repository dei fiori salvati come preferiti del Dizionario.
 */
class DictionaryFavouritesRepository {

    /**
     * Lista dei fiori preferiti del dizionario.
     */
    val favouritesFlowersList = MutableLiveData<MutableList<DictionaryFlower>>()

    /**
     * Boolean che controlla se il dizionario è inizializzato.
     */
    val bIsFavouritesInitialized: MutableLiveData<Boolean> = MutableLiveData()

    /**
     * Variabile che rappresenta l'istanza del Realtime Database di Firebase per FloralEye.
     */
    private val mDatabase: FirebaseDatabase = FirebaseDatabase.getInstance(FIREBASE_REALTIME_DB)

    /**
     * Variabile che rappresenta il riferimento al database.
     */
    private val mRefDatabase: DatabaseReference = mDatabase.reference.child(DICTIONARY_FAVOURITES)

    /**
     * Variabile che rappresenta l'identificatore univoco dell'utente corrente su Firebase.
     */
    private val mUserId = FirebaseAuth.getInstance().currentUser?.uid

    init {
        favouritesFlowersList.value = ArrayList()
    }

    companion object {
        private val TAG: String = DictionaryFavouritesRepository::class.java.simpleName
    }

    /**
     * Metodo per aggiungere un fiore alla lista dei preferiti.
     * @param dictionary Lista dei fiori del dizionario
     * @param list Lista di nomi comuni di fiori
     */
    fun parseFavouritesFlowers(dictionary: MutableList<DictionaryFlower>, list: ArrayList<*>)
    {
        val flowerList : MutableList<DictionaryFlower> = ArrayList()

        for (current in list){
            for (entry in dictionary){
                if (entry.commonName == current){
                    flowerList.add(entry)
                    entry.isFavourite = true
                    break
                }
            }
        }

        if (flowerList.size > 0){
            bIsFavouritesInitialized.value = true
            favouritesFlowersList.value = flowerList
        }
    }

    /**
     * Metodo per ottenere la lista di fiori preferiti da Firebase.
     * @param dictionary Lista dei fiori del dizionario
     */
    fun getFavouriteFlowers(dictionary: MutableList<DictionaryFlower>){

        val userId = FirebaseAuth.getInstance().currentUser?.uid.orEmpty()

        mRefDatabase.child(userId).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (childSnapshot in dataSnapshot.children) {
                    val taskObj = childSnapshot.value as ArrayList<*>
                    parseFavouritesFlowers(dictionary, taskObj)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.d(TAG, error.toString())
            }
        })
    }

    /**
     * Metodo per aggiornare la lista di fiori preferiti su Firebase.
     */
    fun updateFirebaseFavourites(){

        val userId = FirebaseAuth.getInstance().currentUser?.uid.orEmpty()

        val favouritesNames = ArrayList<String>()

        val list = favouritesFlowersList.value

        if (list != null){
            for (entry in list){
                favouritesNames.add(entry.commonName)
            }
            mRefDatabase.child(userId).child(DICTIONARY_FLOWERS_ARRAY).setValue(favouritesNames)
        }
    }

    /**
     * Metodo per cancellare la lista dei preferiti dell'utente da Firebase.
     */
    fun cleanFirebaseFavourites(){
        mUserId?.let {
            mRefDatabase.child(it).child(DICTIONARY_FLOWERS_ARRAY)
                .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    try {
                        mRefDatabase.child(mUserId).child(DICTIONARY_FLOWERS_ARRAY).setValue(null)
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
