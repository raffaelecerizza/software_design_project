package com.example.floraleye.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.floraleye.repositories.DictionaryFavouritesRepository

/**
 * Classe di factory da utilizzare per la costruzione del FavouritesViewModel.
 */
class FavouritesViewModelFactory(
    repository: DictionaryFavouritesRepository
) : ViewModelProvider.Factory {

    private val favouritesRepository: DictionaryFavouritesRepository

    init {
        this.favouritesRepository = repository
    }

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return FavouritesViewModel(favouritesRepository) as T
    }
}
