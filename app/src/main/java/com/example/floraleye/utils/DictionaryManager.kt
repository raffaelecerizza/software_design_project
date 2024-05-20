package com.example.floraleye.utils

import android.content.Context
import android.view.View
import androidx.fragment.app.Fragment
import com.example.floraleye.R
import com.example.floraleye.databinding.FragmentDictionaryBinding
import com.example.floraleye.utils.GeneralUtils.observeOnce
import com.example.floraleye.viewmodels.DictionaryViewModel
import com.example.floraleye.viewmodels.FavouritesViewModel
import com.google.android.material.snackbar.Snackbar

/**
 * Classe di utility per la gestione del Dizionario.
 */
class DictionaryManager(
    private val dictionaryViewModel: DictionaryViewModel,
    private val favouritesViewModel: FavouritesViewModel,
    private val fragment: Fragment,
    private val mBinding: FragmentDictionaryBinding,
    private val context: Context?
) {

    /**
     * Metodo per inizializzare il Dizionario.
     */
    fun initDictionary(){

        initializeDictionaryObservers()

        if (dictionaryViewModel.bIsFlowerListInitialized.value == false){

            if (dictionaryViewModel.isDictionaryCached(context as Context)){
                dictionaryViewModel.loadFromJSON(context)
            }
            else {
                if (!dictionaryViewModel.isLoadingDictionary()) {
                    dictionaryViewModel.initializeFlowersList()
                }
            }
        }
    }

    private fun initializeDictionaryObservers(){

        favouritesViewModel.bIsFavouritesInitialized.observeOnce(fragment) { isLoaded ->
            if (isLoaded) {
                mBinding.rvFlowerList.adapter?.notifyDataSetChanged()
            }
        }

        dictionaryViewModel.bIsFlowerListInitialized.observe(fragment){ isLoaded ->

            mBinding.isDictionaryLoaded = isLoaded

            if (isLoaded){
                dictionaryViewModel.sortDictionaryEntries()

                mBinding.dictionaryProgressBar.visibility = View.INVISIBLE
                mBinding.dictionaryDownloadIndicator.visibility = View.INVISIBLE
                mBinding.rvFlowerList.visibility = View.VISIBLE
                mBinding.rvFlowerList.adapter?.notifyDataSetChanged()

                if (!dictionaryViewModel.isDictionaryCached(context as Context))
                    dictionaryViewModel.cacheDictionary(context)
            }
            else {
                mBinding.dictionaryProgressBar.visibility = View.VISIBLE
                mBinding.dictionaryDownloadIndicator.visibility = View.VISIBLE
                mBinding.rvFlowerList.visibility = View.INVISIBLE
            }
        }

        dictionaryViewModel.repoDownloadCounter.observe(fragment){ counter ->
            mBinding.dictionaryCount = counter
            mBinding.dictionaryMax = dictionaryViewModel.getFlowersNameListSize().toString()
        }

        dictionaryViewModel.bShowErrorMessage.observe(fragment){ error ->
            if (error){
                Snackbar.make(mBinding.root,
                    R.string.str_snack_notification_dictionary,
                    Snackbar.LENGTH_LONG)
                    .setAnchorView(mBinding.dictionaryDownloadIndicator)
                    .show()
                dictionaryViewModel.bShowErrorMessage.value = false
            }
        }

        dictionaryViewModel.getDictionaryList().observe(fragment){
            favouritesViewModel.getFavourites().value?.clear()
            updateFavourites()
        }
    }

    private fun updateFavourites(){

        dictionaryViewModel.getDictionaryList().value?.let {
            favouritesViewModel.initFavourites(it)
        }

        favouritesViewModel.getFavourites().value.let { list ->
            if (list != null && list.size > 0){
                for (entry in list) {
                    dictionaryViewModel.updateFavourites(entry)
                }
            }
        }
    }
}
