package com.example.floraleye.viewmodels

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.floraleye.repositories.DictionaryRepository
import com.example.floraleye.repositories.FiltersRepository

/**
 * Classe di factory da utilizzare per la costruzione del DictionaryViewModel.
 */
class DictionaryViewModelFactory(
    private val application: Application
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return DictionaryViewModel(
            application,
            DictionaryRepository(),
            FiltersRepository(application)) as T
    }
}
