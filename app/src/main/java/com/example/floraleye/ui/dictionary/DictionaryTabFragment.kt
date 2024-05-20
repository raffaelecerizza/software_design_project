package com.example.floraleye.ui.dictionary

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.floraleye.R
import com.example.floraleye.databinding.FragmentDictionaryTabBinding
import com.example.floraleye.repositories.DictionaryFavouritesRepository
import com.example.floraleye.viewmodels.DictionaryViewModel
import com.example.floraleye.viewmodels.FavouritesViewModel
import com.example.floraleye.viewmodels.DictionaryViewModelFactory
import com.example.floraleye.viewmodels.FavouritesViewModelFactory
import com.google.android.material.tabs.TabLayoutMediator

/**
 * Fragment per la gestione del TabLayout contenente il dizionario e i preferiti.
 */
class DictionaryTabFragment : Fragment() {

    private lateinit var mBinding: FragmentDictionaryTabBinding
    private lateinit var tabAdapter: DictionaryTabAdapter
    private lateinit var dictionaryViewModel: DictionaryViewModel
    private lateinit var favouritesViewModel: FavouritesViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        mBinding = FragmentDictionaryTabBinding.inflate(inflater, container, false)

        dictionaryViewModel = ViewModelProvider(
            this,
            DictionaryViewModelFactory(requireActivity().application)
        )[DictionaryViewModel::class.java]

        favouritesViewModel = ViewModelProvider(
            this,
            FavouritesViewModelFactory(DictionaryFavouritesRepository())
        )[FavouritesViewModel::class.java]

        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        tabAdapter = DictionaryTabAdapter(this)
        mBinding.pager.adapter = tabAdapter

        TabLayoutMediator(mBinding.tabLayout, mBinding.pager) { tab, position ->
            when(position){
                0 -> tab.text = getString(R.string.title_dictionary)
                1 -> tab.text = getString(R.string.title_favourites)
            }
        }.attach()
    }
}
