package com.example.floraleye.viewmodels

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.floraleye.repositories.PhotoRepository
import com.example.floraleye.utils.Constants


/**
 * Classe da utilizzare per la costruzione di un PhotoViewModel.
 */
class PhotoViewModelFactory(
    private val application: Application,
    photoRepository: PhotoRepository
) : ViewModelProvider.Factory {

    private val photoRepository: PhotoRepository

    init {

        this.photoRepository = photoRepository
    }

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PhotoViewModel::class.java)) {
            return PhotoViewModel(application, photoRepository) as T
        }
        throw IllegalArgumentException(Constants.UNKNOWN_VIEWMODEL)
    }
}
