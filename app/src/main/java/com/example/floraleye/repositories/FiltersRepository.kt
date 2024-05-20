package com.example.floraleye.repositories

import android.app.Application
import com.example.floraleye.models.Taxonomy
import com.example.floraleye.utils.Constants
import com.example.floraleye.utils.EncryptedSharedPreferencesManager


/**
 * Repository per la gestione dei filtri.
 */
class FiltersRepository(application: Application) {

    private val sharedPref =
        EncryptedSharedPreferencesManager(application, Constants.FILTERS_FILE)

    /**
     * Filtri correntemente considerati.
     */
    var currentFilters: Taxonomy = getCurrentFiltersSharedPref()
        private set

    /**
     * Metodo utilizzato per salvare i nuovi filtri in shared preferences.
     * @param newFilters nuovi filtri
     */
    fun setCurrentFilters(newFilters: Taxonomy) {
        currentFilters = newFilters
        sharedPref.writeSecretString(Constants.FILTERS_KINGDOM_KEY, newFilters.kingdom)
        sharedPref.writeSecretString(Constants.FILTERS_PHYLUM_KEY, newFilters.phylum)
        sharedPref.writeSecretString(Constants.FILTERS_FAMILY_KEY, newFilters.family)
        sharedPref.writeSecretString(Constants.FILTERS_GENUS_KEY, newFilters.genus)
        sharedPref.writeSecretString(Constants.FILTERS_SPECIES_KEY, newFilters.species)
        sharedPref.writeSecretString(Constants.FILTERS_ORDER_KEY, newFilters.order)
    }

    /**
     * Metodo utilizzato per eliminare i filtri correnti da shared preferences.
     */
    fun deleteCurrentFilters() {
        currentFilters = Taxonomy.getEmptyTaxonomy()
        sharedPref.deleteSecretData(Constants.FILTERS_KINGDOM_KEY)
        sharedPref.deleteSecretData(Constants.FILTERS_PHYLUM_KEY)
        sharedPref.deleteSecretData(Constants.FILTERS_FAMILY_KEY)
        sharedPref.deleteSecretData(Constants.FILTERS_GENUS_KEY)
        sharedPref.deleteSecretData(Constants.FILTERS_SPECIES_KEY)
        sharedPref.deleteSecretData(Constants.FILTERS_ORDER_KEY)
    }

    private fun getCurrentFiltersSharedPref(): Taxonomy {
        return Taxonomy(
            kingdom = sharedPref
                .readSecretString(Constants.FILTERS_KINGDOM_KEY, Taxonomy.EMPTY_FIELD),
            phylum = sharedPref
                .readSecretString(Constants.FILTERS_PHYLUM_KEY, Taxonomy.EMPTY_FIELD),
            family = sharedPref
                .readSecretString(Constants.FILTERS_FAMILY_KEY, Taxonomy.EMPTY_FIELD),
            genus = sharedPref
                .readSecretString(Constants.FILTERS_GENUS_KEY, Taxonomy.EMPTY_FIELD),
            species = sharedPref
                .readSecretString(Constants.FILTERS_SPECIES_KEY, Taxonomy.EMPTY_FIELD),
            order = sharedPref
                .readSecretString(Constants.FILTERS_ORDER_KEY, Taxonomy.EMPTY_FIELD)
        )
    }
}
