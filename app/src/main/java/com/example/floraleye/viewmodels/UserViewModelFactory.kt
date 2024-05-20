package com.example.floraleye.viewmodels

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.floraleye.repositories.UserRepository

/**
 * Classe di factory da utilizzare per la costruzione dello UserViewModel.
 */
class UserViewModelFactory(
    private val application: Application
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return UserViewModel(application, UserRepository(application)) as T
    }
}
