package com.example.floraleye.ui.profile

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.View
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.floraleye.R
import com.example.floraleye.databinding.FragmentProfileBinding
import com.example.floraleye.models.AnsweredQuiz
import com.example.floraleye.repositories.DictionaryFavouritesRepository
import com.example.floraleye.repositories.QuizHistoryRepository
import com.example.floraleye.repositories.QuizRepository
import com.example.floraleye.utils.Constants
import com.example.floraleye.utils.GeneralUtils
import com.example.floraleye.utils.OnboardUtils
import com.example.floraleye.utils.QuizUtils
import com.example.floraleye.viewmodels.DictionaryViewModel
import com.example.floraleye.viewmodels.DictionaryViewModelFactory
import com.example.floraleye.viewmodels.QuizViewModel
import com.example.floraleye.viewmodels.QuizViewModelFactory
import com.example.floraleye.viewmodels.UserViewModel
import com.example.floraleye.viewmodels.UserViewModelFactory
import com.example.floraleye.viewmodels.FavouritesViewModel
import com.example.floraleye.viewmodels.FavouritesViewModelFactory
import com.google.android.material.dialog.MaterialAlertDialogBuilder

/**
 * Fragment per la gestione del Profilo.
 */
class ProfileFragment : Fragment() {

    private lateinit var mBinding: FragmentProfileBinding

    private lateinit var userViewModel: UserViewModel

    private lateinit var mQuizViewModel: QuizViewModel

    private lateinit var dictionaryViewModel: DictionaryViewModel

    private lateinit var favouritesViewModel: FavouritesViewModel

    private var mQuizHistory: List<AnsweredQuiz>? = null

    private var mTotalAnswers = 0

    private var mCorrectAnswers = 0

    private var mWrongAnswers = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        mBinding = FragmentProfileBinding.inflate(inflater, container, false)

        userViewModel = ViewModelProvider(
            this,
            UserViewModelFactory(requireActivity().application)
        )[UserViewModel::class.java]

        mQuizViewModel = ViewModelProvider(
            this,
            QuizViewModelFactory(
                requireActivity().application,
                QuizRepository(),
                QuizHistoryRepository()
            )
        )[QuizViewModel::class.java]

        dictionaryViewModel = ViewModelProvider(
            this,
            DictionaryViewModelFactory(requireActivity().application)
        )[DictionaryViewModel::class.java]

        favouritesViewModel = ViewModelProvider(
            this,
            FavouritesViewModelFactory(DictionaryFavouritesRepository())
        )[FavouritesViewModel::class.java]

        mQuizViewModel.getQuizHistory().observe(viewLifecycleOwner) { quizzes ->
            displayQuizScore(quizzes)

            val totalAnswersText =
                getString(R.string.str_total_answers_number).format(mTotalAnswers.toString())
            mBinding.textViewTotalAnswers.text = totalAnswersText

            val correctAnswersText =
                getString(R.string.str_correct_answers_number).format(mCorrectAnswers.toString())
            mBinding.textViewCorrectAnswers.text = correctAnswersText

            val wrongAnswersText =
                getString(R.string.str_wrong_answers_number).format(mWrongAnswers.toString())
            mBinding.textViewWrongAnswers.text = wrongAnswersText
        }

        mBinding.mailTextView.text =
            userViewModel.userMail ?: resources.getString(R.string.str_no_user)

        mBinding.deleteUserButton.setOnClickListener {
            MaterialAlertDialogBuilder(requireContext())
                .setTitle(R.string.str_warning)
                .setMessage(R.string.str_delete_user_confirmation)
                .setPositiveButton(R.string.str_ok) { _, _ ->
                    deleteCurrentUser()
                }
                .setNeutralButton(R.string.str_cancel, null)
                .setCancelable(true)
                .show()
        }

        mBinding.showQuizzesButton.setOnClickListener {
            val bundle = Bundle()
            bundle.putInt(Constants.ARG_PARAM_QUIZ_TAB, 1)
            findNavController().navigate(R.id.action_navigation_profile_to_navigation_quiz, bundle)
        }

        return mBinding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.toolbar_menu_profile, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.item_logout -> {
                dictionaryViewModel.clean(requireContext())
                userViewModel.logout()
            }
            R.id.item_edit_profile -> {
                MaterialAlertDialogBuilder(requireContext())
                    .setMessage(R.string.str_edit_profile_message)
                    .setPositiveButton(R.string.str_ok) { _, _ ->
                        val intent = Intent(
                            activity,
                            EditProfileActivity::class.java
                        )
                        startActivity(intent)
                    }
                    .setNeutralButton(R.string.str_cancel, null)
                    .setCancelable(true)
                    .show()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun deleteCurrentUser() {
        mBinding.deleteUserButton.isEnabled = false
        mBinding.deleteUserProgressBar.visibility = View.VISIBLE

        userViewModel.deleteUser().observe(viewLifecycleOwner) { result ->
            mBinding.deleteUserButton.isEnabled = true
            mBinding.deleteUserProgressBar.visibility = View.INVISIBLE

            if (result == Constants.FIREBASE_AUTH_OK) {
                dictionaryViewModel.clean(requireContext())
                favouritesViewModel.cleanStored()
                removeAllQuizzesFromHistory()
            }

            val resultMessage =
                OnboardUtils.getOnboardResultErrorMessage(result, resources) ?: return@observe

            GeneralUtils.showGenericMessageOnView(
                message = resultMessage,
                context = requireContext(),
                root = mBinding.root,
                anchorView = mBinding.deleteUserButton
            )
        }
    }

    /**
     * Metodo utilizzato per visualizzare il quiz score dell'utente.
     * @param quizzes Lista di quiz per cui l'utente ha sottomesso una risposta.
     */
    private fun displayQuizScore(quizzes: List<AnsweredQuiz>) {
        if (quizzes.isNotEmpty()) {
            mQuizHistory = quizzes.sortedWith(compareBy { it.time }).reversed()
            mTotalAnswers = mQuizHistory?.size ?: 0
            mCorrectAnswers = mQuizHistory?.let{ QuizUtils.computeCorrectAnswers(it) } ?: 0
            mWrongAnswers = mQuizHistory?.let{ QuizUtils.computeWrongAnswers(it) } ?: 0

            val percentage =
                ((mCorrectAnswers / mTotalAnswers.toDouble()) * Constants.PERCENTAGE).toFloat()

            mBinding.progressText.visibility = View.VISIBLE
            mBinding.counterProgressBar.setProgressWithAnimation(percentage,
                Constants.QUIZ_SCORE_ANIMATION_DURATION)
            mBinding.counterProgressBar.onProgressChangeListener = { progress ->
                mBinding.progressText.text =
                    getString(R.string.str_percentage).format(progress.toInt().toString())
            }
        } else {
            mTotalAnswers = 0
            mCorrectAnswers = 0
            mWrongAnswers = 0
            mBinding.progressText.visibility = View.GONE
            mBinding.counterProgressBar.setProgressWithAnimation(Constants.ZERO_QUIZ_SCORE)
        }
    }

    /**
     * Metodo utilizzato, soprattutto in fase di test, per ottenere la lista di quiz
     * svolti dall'utente.
     * @return lista di quiz svolti dall'utente. Se la lista è nulla viene restituita una
     * lista vuota.
     */
    fun getQuizHistory(): List<AnsweredQuiz> {
        return mQuizViewModel.getQuizHistory().value.orEmpty()
    }

    /**
     * Metodo utilizzato per rimuovere un quiz dallo storico dell'utente.
     * @param quizHistoryId Id del quiz svolto nello storico dei quiz dell'utente.
     */
    fun removeQuizFromHistory(quizHistoryId: String) {
        mQuizViewModel.removeQuizFromHistory(quizHistoryId)
    }

    /**
     * Metodo per rimuovere dallo storico tutti i quiz svolti dall'utente corrente.
     */
    fun removeAllQuizzesFromHistory() {
        mQuizViewModel.removeAllQuizzesFromHistory()
    }

}
