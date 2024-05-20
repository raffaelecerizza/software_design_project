package com.example.floraleye.viewmodels

import android.app.Application
import android.content.ContentResolver
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.floraleye.repositories.PhotoRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.FileNotFoundException
import java.io.InputStream


/**
 * ViewModel per la gestione dell'acquisizione di una immagine.
 */
class PhotoViewModel(
    private val application: Application,
    private val photoRepository: PhotoRepository,
    private val defaultDispatcher: CoroutineDispatcher = Dispatchers.IO
) : AndroidViewModel(application) {

    companion object {

        private val TAG: String = PhotoViewModel::class.java.simpleName
    }

    private val imageBitmap: MutableLiveData<Bitmap?>
        get() = photoRepository.getImageBitmap()

    /**
     * Metodo da utilizzare per settare una nuova immagine tramite uri.
     * @param uri identificativo univoco dell'immagine selezionata
     */
    fun setImageByUri(uri: Uri) {
        viewModelScope.launch(defaultDispatcher) {
            application.applicationContext.contentResolver.let { contentRes: ContentResolver ->
                val readUriPermission: Int = Intent.FLAG_GRANT_READ_URI_PERMISSION
                try {
                    contentRes.takePersistableUriPermission(uri, readUriPermission)
                    contentRes.openInputStream(uri)?.use { inputStream: InputStream ->
                        imageBitmap.postValue(BitmapFactory.decodeStream(inputStream))
                    }
                } catch (exception: SecurityException) {
                    Log.d(TAG, "setImageByUri: security exception " +
                            "${exception.message} for uri $uri")
                } catch (exception: FileNotFoundException) {
                    Log.d(TAG, "setImageByUri: file not found exception " +
                            "${exception.message} for uri $uri")
                }
            }
        }
    }

    /**
     * Metodo da utilizzare per ottenere il LiveData da osservare per venire a conoscenza dell'
     * immagine acquisita.
     * @return LiveData relativo all'immagine acquisita
     */
    fun getImage(): LiveData<Bitmap?> {
        return imageBitmap
    }

    /**
     * Metodo da utilizzare per settare l'immagine acquisita tramite una immagine Bitmap.
     * @param image immagine acquisita in formato Bitmap
     */
    fun setImage(image: Bitmap) {
        imageBitmap.value = image
    }
}
