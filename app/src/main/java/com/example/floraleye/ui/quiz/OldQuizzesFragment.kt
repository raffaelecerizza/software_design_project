package com.example.floraleye.ui.quiz

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.floraleye.databinding.FragmentOldQuizzesBinding
import com.example.floraleye.models.AnsweredQuiz
import com.example.floraleye.repositories.QuizHistoryRepository
import com.example.floraleye.repositories.QuizRepository
import com.example.floraleye.viewmodels.QuizViewModel
import com.example.floraleye.viewmodels.QuizViewModelFactory

/**
 * Fragment per la gestione dei quiz già svolti dagli utenti.
 */
class OldQuizzesFragment : Fragment() {

    private lateinit var mBinding: FragmentOldQuizzesBinding

    private lateinit var mQuizViewModel: QuizViewModel

    private var mAnsweredQuizzes: List<AnsweredQuiz>? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        mBinding = FragmentOldQuizzesBinding.inflate(inflater, container, false)

        mQuizViewModel = ViewModelProvider(
            this,
            QuizViewModelFactory(
                requireActivity().application,
                QuizRepository(),
                QuizHistoryRepository()
            )
        )[QuizViewModel::class.java]

        mQuizViewModel.getQuizHistory().observe(viewLifecycleOwner) { quizzes ->
            mAnsweredQuizzes = quizzes.sortedWith(compareBy { it.time }).reversed()

            mBinding.isEmptyTextVisible = quizzes.isEmpty()

            mBinding.oldQuizzesRecyclerView.layoutManager = LinearLayoutManager(
                requireContext(),
                LinearLayoutManager.VERTICAL, false
            )

            mBinding.oldQuizzesRecyclerView.adapter = OldQuizzesAdapter(mAnsweredQuizzes)
        }

        return mBinding.root
    }

}
