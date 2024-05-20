package com.example.floraleye.repositories

import android.graphics.Bitmap
import androidx.lifecycle.MutableLiveData


/**
 * Repository per la gestione dell'acquisizione di una immagine.
 */
class PhotoRepository {

    private val imageBitmap: MutableLiveData<Bitmap?> = MutableLiveData()

    /**
     * Metodo da utilizzare per osservare l'immagine acquisita.
     * @return MutableLiveData osservabile e modificabile per gestire l'immagine
     */
    fun getImageBitmap(): MutableLiveData<Bitmap?> {
        return imageBitmap
    }
}
