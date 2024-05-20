package com.example.floraleye.viewmodels

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.floraleye.models.DictionaryFlower
import com.example.floraleye.models.Taxonomy
import com.example.floraleye.repositories.DictionaryRepository
import com.example.floraleye.repositories.FiltersRepository
import com.example.floraleye.utils.Constants
import java.io.File


/**
 * View Model per la gestione del Dizionario.
 */
class DictionaryViewModel(
    application: Application,
    private val dictionaryRepository: DictionaryRepository,
    private val filtersRepository: FiltersRepository
) : AndroidViewModel(application) {

    /**
     * Filtri correntemente considerati.
     */
    val currentFilters: Taxonomy
        get() = filtersRepository.currentFilters

    /**
     * Variabile che controlla se i fiori sono stati inizializzati nel Dizionario.
     */
    val bIsFlowerListInitialized: MutableLiveData<Boolean>
        get() = dictionaryRepository.bIsFlowerListInitialized

    /**
     *  Contatore per il download dei fiori.
     */
    val repoDownloadCounter: MutableLiveData<String>
        get() = dictionaryRepository.counter

    /**
     * Variabile che controlla la presenza di errori durante il download dei dati dei fiori.
     */
    val bShowErrorMessage: MutableLiveData<Boolean>
        get() = dictionaryRepository.bShowErrorMessage

    /**
     * Metodo per ottenere la lista dei fiori inizializzati nel Dizionario.
     * @return lista di fiori del Dizionario
     */
    fun getDictionaryList(): MutableLiveData<MutableList<DictionaryFlower>> {
        return dictionaryRepository.flowersList
    }

    /**
     * Metodo per l'inizializzazione dei dati nel Dizionario.
     */
    fun initializeFlowersList(){
        dictionaryRepository.initializeFlowersList()
    }

    /**
     * Metodo che ordina la lista di fiori del Dizionario in base al loro nome comune dalla A alla Z.
     */
    fun sortDictionaryEntries(){
        dictionaryRepository.sortDictionaryEntries()
    }

    /**
     * Metodo per controllare se il thread che ottiene i dati per il Dizionario è partito.
     * @return Boolean indica se il thread per il parsing dei dati del dizionario è partito o no
     */
    fun isLoadingDictionary() : Boolean {
        return dictionaryRepository.isThreadStarted
    }

    /**
     * Metodo per effettuare il clean della repository.
     */
    fun clean(context: Context) {
        dictionaryRepository.cleanRepository()
        File(context.cacheDir, Constants.DICTIONARY_CACHE_FILE).delete()
        filtersRepository.deleteCurrentFilters()
    }

    /**
     * Metodo per caricare il dizionario da un file JSON contenente oggetti DictionaryFlower.
     */
    fun loadFromJSON(context: Context){
        dictionaryRepository.loadDictionaryFromJSON(context)
    }

    /**
     * Metodo per ottenere la dimensione della lista dei fiori da ricercare per il dizionario.
     */
    fun getFlowersNameListSize() : Int {
        return dictionaryRepository.flowersName.size
    }

    /**
     * Metodo per controllare se il dizionario è stato salvato nella cache.
     * @return Boolean indica se il dizionario è stato salvato nella cache.
     */
    fun isDictionaryCached(context : Context) : Boolean {
        return dictionaryRepository.isDictionaryCached(context)
    }

    /**
     * Metodo per effettuare la cache del dizionario.
     * Return Boolean indica se è stata effettuata la cache del dizionario.
     */
    fun cacheDictionary(context : Context): Boolean {
        return dictionaryRepository.cacheDictionary(context)
    }

    /**
     * Metodo utilizzato per settare i nuovi filtri inseriti dall'utente.
     * @param newFilters nuovi filtri
     */
    fun setNewFilters(newFilters: Taxonomy) {
        filtersRepository.setCurrentFilters(newFilters)
    }

    /**
     * Metodo per aggiornare lo stato della variabile preferiti per i fiori del dizionario.
     * @param flower fiore che deve essere aggiornato.
     */
    fun updateFavourites(flower: DictionaryFlower) {

        val flowers = getDictionaryList().value

        if (flowers != null){
            for (entry in flowers){
                if (entry.commonName == flower.commonName){
                    entry.isFavourite = flower.isFavourite
                    break
                }
            }
            getDictionaryList().value = flowers
        }
    }
}
