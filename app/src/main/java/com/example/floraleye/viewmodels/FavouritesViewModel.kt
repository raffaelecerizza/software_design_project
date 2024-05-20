package com.example.floraleye.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.floraleye.models.DictionaryFlower
import com.example.floraleye.repositories.DictionaryFavouritesRepository

/**
 * View Model per la gestione dei fiori preferiti.
 */
class FavouritesViewModel(
    private val repository: DictionaryFavouritesRepository
) : ViewModel() {

    /**
     * Variabile che controlla se la lista dei preferiti è stata inizializzata.
     */
    val bIsFavouritesInitialized: MutableLiveData<Boolean>
        get() = repository.bIsFavouritesInitialized

    /**
     * Metodo per ottenere la lista dei fiori preferiti.
     * @return MutableLiveData<MutableList<DictionaryFlower>> lista di fiori preferiti del Dizionario
     */
    fun getFavourites(): MutableLiveData<MutableList<DictionaryFlower>> {
        return repository.favouritesFlowersList
    }

    /**
     * Metodo per inizializzare la lista dei fiori preferiti.
     * @param dictionary lista di fiori del dizionario
     */
    fun initFavourites(dictionary: MutableList<DictionaryFlower>){
        repository.getFavouriteFlowers(dictionary)
    }

    /**
     * Metodo per aggiungere un fiore alla lista dei preferiti.
     * @param flower fiore che deve essere aggiunto ai preferiti
     */
    fun addFlowerToFavourites(flower: DictionaryFlower){
        val favouritesArray = getFavourites().value
        if (favouritesArray != null) {
            favouritesArray.add(flower)
            getFavourites().value = favouritesArray
            repository.updateFirebaseFavourites()
        }
    }

    /**
     * Metodo per rimuovere un fiore alla lista dei preferiti.
     * @param flower fiore che deve essere rimosso dai preferiti
     */
    fun removeFlowerToFavourites(flower: DictionaryFlower){
        val favouritesArray = getFavourites().value
        if (favouritesArray != null) {
            favouritesArray.remove(flower)
            getFavourites().value = favouritesArray
            repository.updateFirebaseFavourites()
        }
    }

    /**
     * Metodo per cancellare la lista dei preferiti dell'utente da Firebase.
     */
    fun cleanStored(){
        repository.cleanFirebaseFavourites()
    }
}
