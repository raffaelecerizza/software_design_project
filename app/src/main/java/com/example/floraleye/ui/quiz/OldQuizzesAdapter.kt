package com.example.floraleye.ui.quiz

import android.content.Context
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.floraleye.R
import com.example.floraleye.databinding.ItemOldQuizzesBinding
import com.example.floraleye.models.AnsweredQuiz
import com.example.floraleye.utils.Constants
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

/**
 * Adapter per la gestione dei quiz già svolti dagli utenti.
 */
class OldQuizzesAdapter(
    private val quizzes: List<AnsweredQuiz>?,
) :
    RecyclerView.Adapter<OldQuizzesAdapter.QuizViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuizViewHolder {
        val mBinding = ItemOldQuizzesBinding.inflate(
            LayoutInflater.from(parent.context), parent, false)
        return QuizViewHolder(mBinding, parent.context)
    }

    override fun onBindViewHolder(holder: QuizViewHolder, position: Int) {
        if (quizzes != null){
            holder.bindQuiz(quizzes[position])
        }
    }

    override fun getItemCount(): Int {
        return quizzes?.size ?: 0
    }

    /**
     * Metodo per ottenere la lista di quiz utilizzati dall'adapter.
     * @return La lista di quiz utilizzati dall'adapter.
     */
    fun getQuizzes(): List<AnsweredQuiz>? {
        return quizzes
    }

    /**
     * ViewHolder per la visualizzazione dei singoli quiz svolti dagli utenti.
     */
    class QuizViewHolder(private val mBinding: ItemOldQuizzesBinding,
                         private val mContext: Context): RecyclerView.ViewHolder(
        mBinding.root
    ) {

        /**
         * Metodo per visualizzare un singolo quiz svolto.
         * @param quiz Quiz svolto di cui si vogliono visualizzare alcuni dati.
         */
        fun bindQuiz(quiz: AnsweredQuiz) {
            val imageView: ImageView = mBinding.imageItemOldQuizzes
            Glide.with(mContext)
                .load(quiz.quiz.image)
                .centerCrop()
                .into(imageView)

            val userAnswerPrefix = mContext.getString(R.string.str_user_answer_prefix)
            val userAnswerText = userAnswerPrefix + quiz.userAnswer
            val userAnswerSpannableString = SpannableString(userAnswerText)
            userAnswerSpannableString.setSpan(
                ForegroundColorSpan(ContextCompat.getColor(mContext,
                    R.color.quiz_user_answer)),
                0,
                userAnswerPrefix.length-1,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )

            val solutionPrefix = mContext.getString(R.string.str_solution_prefix)
            val solutionText = solutionPrefix + quiz.quiz.solution
            val solutionSpannableString = SpannableString(solutionText)
            solutionSpannableString.setSpan(
                ForegroundColorSpan(ContextCompat.getColor(mContext,
                    R.color.quiz_solution)),
                0,
                solutionPrefix.length-1,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )

            mBinding.textViewQuestionItemOldQuizzes.text = quiz.quiz.question
            mBinding.textViewAnswerItemOldQuizzes.text = userAnswerSpannableString
            mBinding.textViewSolutionItemOldQuizzes.text = solutionSpannableString
            mBinding.textViewDateItemOldQuizzes.text = formatDateTime(quiz.time)
        }

        private fun formatDateTime(dateTimeString: String): String {
            val formattedDateTime = dateTimeString.substring(0,
                dateTimeString.indexOf(Constants.TIME_ZONE_START))
            val dateTime = LocalDateTime.parse(formattedDateTime,
                DateTimeFormatter.ISO_OFFSET_DATE_TIME)
            val dateFormatter = DateTimeFormatter.ofPattern(Constants.YEAR_MONTH_DAY_PATTERN)
            val timeFormatter = DateTimeFormatter.ofPattern(Constants.HOUR_MINUTE_SECOND_PATTERN)
            val formattedDate = dateTime.format(dateFormatter)
            val formattedTime = dateTime.format(timeFormatter)
            return "$formattedDate $formattedTime"
        }
    }
}
