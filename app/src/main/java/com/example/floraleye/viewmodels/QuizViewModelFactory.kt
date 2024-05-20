package com.example.floraleye.viewmodels

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.floraleye.repositories.IQuizRepository
import com.example.floraleye.repositories.QuizHistoryRepository
import com.example.floraleye.utils.Constants

/**
 * Classe di factory da utilizzare per la costruzione del QuizViewModel.
 */
class QuizViewModelFactory (
    private val application: Application,
    quizRepository: IQuizRepository,
    quizHistoryRepository: QuizHistoryRepository
) : ViewModelProvider.Factory {

    private val quizRepository: IQuizRepository

    private val quizHistoryRepository: QuizHistoryRepository

    init {
        this.quizRepository = quizRepository
        this.quizHistoryRepository = quizHistoryRepository
    }

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(QuizViewModel::class.java)) {
            return QuizViewModel(application, quizRepository, quizHistoryRepository) as T
        }
        throw IllegalArgumentException(Constants.UNKNOWN_VIEWMODEL)
    }
}
