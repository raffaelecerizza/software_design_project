package com.example.floraleye.ui.quiz

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RadioButton
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.floraleye.R
import com.example.floraleye.databinding.FragmentNewQuizBinding
import com.example.floraleye.models.Answer
import com.example.floraleye.models.Quiz
import com.example.floraleye.repositories.QuizHistoryRepository
import com.example.floraleye.repositories.QuizRepository
import com.example.floraleye.utils.Constants
import com.example.floraleye.utils.QuizUtils
import com.example.floraleye.viewmodels.QuizViewModel
import com.example.floraleye.viewmodels.QuizViewModelFactory
import java.time.ZoneId
import java.time.ZonedDateTime

/**
 * Fragment per la gestione dei nuovi quiz da svolgere.
 */
class NewQuizFragment : Fragment() {

    private lateinit var mBinding: FragmentNewQuizBinding

    private lateinit var mQuizViewModel: QuizViewModel

    private var mSolution: String = ""

    private var mQuizzes: List<Quiz>? = null

    private var mQuizIndex = 0

    private var isSubmitButtonClicked = false

    private lateinit var mTime: ZonedDateTime

    /**
     * Array di valori booleani che specificano se un radio button è stato selezionato.
     */
    private val radioButtonStates = booleanArrayOf(false, false, false, false)

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(Constants.QUIZ_INDEX, mQuizIndex)
        outState.putString(Constants.QUIZ_SOLUTION, mSolution)
        outState.putBoolean(Constants.SUBMIT_BUTTON_CLICKED, isSubmitButtonClicked)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        mBinding = DataBindingUtil
            .inflate(inflater, R.layout.fragment_new_quiz, container, false)

        mBinding.areElementsVisible = false

        mQuizViewModel = ViewModelProvider(
            this,
            QuizViewModelFactory(
                requireActivity().application,
                QuizRepository(),
                QuizHistoryRepository())
        )[QuizViewModel::class.java]
        if (savedInstanceState != null) {
            mQuizIndex = savedInstanceState.getInt(Constants.QUIZ_INDEX)
            mSolution = savedInstanceState.getString(Constants.QUIZ_SOLUTION).toString()
            isSubmitButtonClicked = savedInstanceState.getBoolean(Constants.SUBMIT_BUTTON_CLICKED)
        }

        mQuizViewModel.getQuizzes().observe(viewLifecycleOwner) { quizzes ->
            mQuizzes = quizzes
            displayQuiz()
            mBinding.areElementsVisible = true
        }

        mBinding.radioButtonAnswer1.setOnClickListener {
            onRadioButtonClicked(mBinding.radioButtonAnswer1)
        }

        mBinding.radioButtonAnswer2.setOnClickListener {
            onRadioButtonClicked(mBinding.radioButtonAnswer2)
        }

        mBinding.radioButtonAnswer3.setOnClickListener {
            onRadioButtonClicked(mBinding.radioButtonAnswer3)
        }

        mBinding.radioButtonAnswer4.setOnClickListener {
            onRadioButtonClicked(mBinding.radioButtonAnswer4)
        }

        mBinding.submitButton.setOnClickListener {
            isSubmitButtonClicked = true
            onSubmitButtonClicked()
            saveUserAnswer()
        }

        mBinding.nextButton.setOnClickListener {
            onNextButtonClicked()
        }

        mBinding.swipeRefreshQuiz.setOnRefreshListener {
            onRefresh()
            mBinding.swipeRefreshQuiz.isRefreshing = false
        }

        return mBinding.root
    }

    /**
     * Metodo per visualizzare il contenuto di uno specifico quiz.
     */
    private fun displayQuiz() {
        /*
            Per la visualizzazione degli elementi del quiz vengono effettuate chiamate
            sicure attraverso l'operatore ?. In questo modo le proprietà vengono accedute
            solo se i valori non sono nulli.
         */
        mBinding.questionTextView.text = mQuizzes?.get(mQuizIndex)?.question
        mBinding.radioButtonAnswer1.text =
            mQuizzes?.get(mQuizIndex)?.answers?.get(Constants.INDEX_RADIO_BUTTON1)?.text
        mBinding.radioButtonAnswer2.text =
            mQuizzes?.get(mQuizIndex)?.answers?.get(Constants.INDEX_RADIO_BUTTON2)?.text
        mBinding.radioButtonAnswer3.text =
            mQuizzes?.get(mQuizIndex)?.answers?.get(Constants.INDEX_RADIO_BUTTON3)?.text
        mBinding.radioButtonAnswer4.text =
            mQuizzes?.get(mQuizIndex)?.answers?.get(Constants.INDEX_RADIO_BUTTON4)?.text
        mSolution = mQuizzes?.get(mQuizIndex)?.solution.toString()
        val imageView: ImageView = mBinding.questionImageView
        Glide.with(requireContext())
            .load(mQuizzes?.get(mQuizIndex)?.image)
            .into(imageView)
        if (isSubmitButtonClicked) {
            onSubmitButtonClicked()
        }
    }

    /**
     * Metodo per la gestione dei click sui radio button. Può essere selezionato
     * un solo radio button alla volta. Inoltre il click su un radio button
     * già selezionato porta alla sua deselezione.
     * @param radioButtonAnswer radio button su cui è stato effettuato un click.
     */
    private fun onRadioButtonClicked(radioButtonAnswer: RadioButton) {
        // Viene creata una mappa che associa un indice a ogni radio button.
        val radioButtonIndexMap = mapOf(
            mBinding.radioButtonAnswer1 to Constants.INDEX_RADIO_BUTTON1,
            mBinding.radioButtonAnswer2 to Constants.INDEX_RADIO_BUTTON2,
            mBinding.radioButtonAnswer3 to Constants.INDEX_RADIO_BUTTON3,
            mBinding.radioButtonAnswer4 to Constants.INDEX_RADIO_BUTTON4
        )

        /*
            Viene creato un indice radioButtonIndex corrispondente al radio button selezionato.
            L'operatore Elvis '?' viene utilizzato per gestire il caso in cui il radio button
            selezionato non sia presente nella mappa. In questo caso viene restituito null.
         */
        val radioButtonIndex = radioButtonIndexMap[radioButtonAnswer] ?: return

        /*
            Se il radio button selezionato è già attivo, allora viene deselezionato
            e il corrispondente elemento dell'array radioButtonStates viene impostato a false.
         */
        if (radioButtonStates[radioButtonIndex]) {
            radioButtonAnswer.isChecked = false
            radioButtonStates[radioButtonIndex] = false
        /*
            Altrimenti l'array radioButtonStates viene riempito con tutti i valori false
            e l'elemento dell'array corrispondente al radio button selezionato
            viene impostato a true.
            Infine lo stato (checked) di ciascun radio button viene impostato
            in base al valore dell'array radioButtonStates.
         */
        } else {
            radioButtonStates.fill(false)
            radioButtonStates[radioButtonIndex] = true
            mBinding.radioButtonAnswer1.isChecked =
                radioButtonIndex == Constants.INDEX_RADIO_BUTTON1
            mBinding.radioButtonAnswer2.isChecked =
                radioButtonIndex == Constants.INDEX_RADIO_BUTTON2
            mBinding.radioButtonAnswer3.isChecked =
                radioButtonIndex == Constants.INDEX_RADIO_BUTTON3
            mBinding.radioButtonAnswer4.isChecked =
                radioButtonIndex == Constants.INDEX_RADIO_BUTTON4
        }
    }

    /**
     * Metodo per la gestione dei click sul bottone Submit.
     */
    private fun onSubmitButtonClicked() {
        // Viene creata una lista di radio button.
        val radioButtons = listOf(
            mBinding.radioButtonAnswer1,
            mBinding.radioButtonAnswer2,
            mBinding.radioButtonAnswer3,
            mBinding.radioButtonAnswer4
        )

        // Viene creata una lista di CardViews contenenti i radio button.
        val cardViews = listOf(
            mBinding.cardViewRadioButtonAnswer1,
            mBinding.cardViewRadioButtonAnswer2,
            mBinding.cardViewRadioButtonAnswer3,
            mBinding.cardViewRadioButtonAnswer4
        )

        /*
            Questo ciclo evidenzia la risposta corretta in verde. Se la risposta data
            è errata, questa viene evidenziata in rosso. L'evidenziatura avviene sulla
            CardView. Infine i radio button vengono disabilitati.
         */
        for ((index, radioButton) in radioButtons.withIndex()) {
            val answer = radioButton.text.toString()
            val isCorrectAnswer = answer == mSolution
            if (isCorrectAnswer) {
                cardViews[index].strokeColor =
                    ContextCompat.getColor(this.requireContext(), R.color.green)
                QuizUtils.playAnimationAnswer(cardViews[index])
                cardViews[index].strokeWidth = Constants.ANSWER_STROKE_WIDTH
            } else if (radioButton.isChecked) {
                cardViews[index].strokeColor =
                    ContextCompat.getColor(this.requireContext(), R.color.red)
                QuizUtils.playAnimationAnswer(cardViews[index])
                cardViews[index].strokeWidth = Constants.ANSWER_STROKE_WIDTH
            }
            radioButton.isClickable = false
        }

        // Anche il bottone Submit viene disabilitato.
        mBinding.submitButton.isClickable = false
    }

    /**
     * Metodo per la gestione dei click sul bottone Next.
     */
    private fun onNextButtonClicked() {
        // Resetta lo stato dei radio button e del bottone Submit.
        val radioButtons = arrayOf(mBinding.radioButtonAnswer1,
            mBinding.radioButtonAnswer2,
            mBinding.radioButtonAnswer3,
            mBinding.radioButtonAnswer4)
        val cardViews = arrayOf(mBinding.cardViewRadioButtonAnswer1,
            mBinding.cardViewRadioButtonAnswer2,
            mBinding.cardViewRadioButtonAnswer3,
            mBinding.cardViewRadioButtonAnswer4)
        for (i in radioButtons.indices) {
            radioButtons[i].isClickable = true
            radioButtons[i].isChecked = false
            cardViews[i].strokeWidth = 0
            cardViews[i].strokeColor = ContextCompat.getColor(this.requireContext(), R.color.white)
        }
        mBinding.submitButton.isClickable = true
        isSubmitButtonClicked = false

        // Avanza al quiz successivo.
        if (mQuizIndex + 1 < (mQuizzes?.size ?: 0))
            mQuizIndex += 1
        else {
            // Se sono già stati svolti tutti i quiz, vengono rimescolati e si riparte da capo.
            mQuizzes = mQuizzes?.shuffled()
            mQuizIndex = 0
        }

        QuizUtils.playAnimationNextButton(mBinding.scrollViewQuizFragment)
        displayQuiz()
    }

    /**
     * Metodo utilizzato per effettuare un refresh del quiz tramite swipe.
     */
    private fun onRefresh() {
        if (!mQuizViewModel.isLoadingQuizzes())
            onNextButtonClicked()
    }

    /**
     * Metodo per memorizzare la risposta dell'utente al quiz.
     */
    private fun saveUserAnswer() {
        // Viene creata una lista di radio button.
        val radioButtons = listOf(
            mBinding.radioButtonAnswer1,
            mBinding.radioButtonAnswer2,
            mBinding.radioButtonAnswer3,
            mBinding.radioButtonAnswer4
        )

        val quiz: Quiz? = mQuizzes?.get(mQuizIndex)

        var userAnswer = Answer(getString(R.string.str_no_answer))
        for (radioButton in radioButtons) {
            if (radioButton.isChecked) {
                userAnswer = Answer(radioButton.text.toString())
                break
            }
        }

        val italyZone = ZoneId.of(Constants.ZONE_ID)

        mTime = ZonedDateTime.now(italyZone)

        if (quiz != null) {
            mQuizViewModel.addQuizToHistory(quiz, userAnswer, mTime)
        }
    }

    /**
     * Metodo utilizzato, soprattutto in fase di test, per ottenere la lista di quiz
     * utilizzata da NewQuizFragment.
     * @return lista di quiz utilizzata da NewQuizFragment. Se la lista è nulla viene restituita una
     * lista vuota.
     */
    fun getQuizzes(): List<Quiz> {
        return mQuizViewModel.getQuizzes().value.orEmpty()
    }

}
