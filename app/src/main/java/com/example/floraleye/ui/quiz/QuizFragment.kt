package com.example.floraleye.ui.quiz

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.example.floraleye.R
import com.example.floraleye.databinding.FragmentQuizBinding
import com.example.floraleye.utils.Constants
import com.google.android.material.tabs.TabLayoutMediator

/**
 * Fragment per la gestione dei quiz da svolgere e dei quiz svolti.
 */
class QuizFragment : Fragment() {

    private lateinit var mBinding: FragmentQuizBinding

    private var mTabShown: Int = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBinding = FragmentQuizBinding.inflate(inflater, container, false)
        mTabShown = arguments?.getInt(Constants.ARG_PARAM_QUIZ_TAB) ?: 0
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val fragmentList = arrayListOf(
            NewQuizFragment(),
            OldQuizzesFragment()
        )

        val fragmentManager: FragmentManager = childFragmentManager
        val adapter = QuizPagerAdapter(fragmentList, fragmentManager, lifecycle)
        mBinding.quizViewPager.adapter = adapter

        TabLayoutMediator(mBinding.quizTabLayout, mBinding.quizViewPager) { tab, position ->
            when (position) {
                0 -> tab.text = getString(R.string.str_new_quiz)
                1 -> tab.text = getString(R.string.str_old_quizzes)
            }
        }.attach()

        mBinding.quizViewPager.setCurrentItem(mTabShown, false)

    }

}
