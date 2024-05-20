package com.example.floraleye.models

import android.os.Parcel
import android.os.Parcelable
import com.example.floraleye.utils.Constants.Generated

/**
 * Classe per la gestione della tassonomia di un Fiore.
 */
class Taxonomy(
    kingdom: String,
    phylum: String,
    order: String,
    family: String,
    genus: String,
    species: String
): Parcelable {
    /**
     * Regno di un fiore.
     */
    var kingdom: String
        private set
    /**
     * Phylum di un fiore.
     */
    var phylum: String
        private set
    /**
     * Ordine di un fiore.
     */
    var order: String
        private set
    /**
     * Famiglia di un fiore.
     */
    var family: String
        private set
    /**
     * Genere di un fiore.
     */
    var genus: String
        private set
    /**
     * Specie di un fiore.
     */
    var species: String
        private set

    @Generated
    constructor(parcel: Parcel) : this(
        kingdom = parcel.readString().toString(),
        phylum = parcel.readString().toString(),
        order = parcel.readString().toString(),
        family = parcel.readString().toString(),
        genus = parcel.readString().toString(),
        species = parcel.readString().toString()
    )

    init{
        this.kingdom = kingdom
        this.phylum = phylum
        this.order = order
        this.family = family
        this.genus = genus
        this.species = species
    }

    @Generated
    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(kingdom)
        parcel.writeString(phylum)
        parcel.writeString(order)
        parcel.writeString(family)
        parcel.writeString(genus)
        parcel.writeString(species)
    }

    override fun describeContents(): Int {
        return 0
    }

    /**
     * Metodo utilizzato per verificare se la tassonomia corrente si compone di tutti campi vuoti.
     * @return true se la tassonia è completamente vuota, false altrimenti
     */
    fun isEmpty(): Boolean {
        return kingdom == EMPTY_FIELD &&
                phylum == EMPTY_FIELD &&
                order == EMPTY_FIELD &&
                family == EMPTY_FIELD &&
                genus == EMPTY_FIELD &&
                species == EMPTY_FIELD
    }

    /**
     * Parcelable CREATOR della classe Taxonomy.
     */
    @Generated
    companion object CREATOR : Parcelable.Creator<Taxonomy> {

        /**
         * Valore da considerare per un campo della tassonia per dire che è vuoto.
         */
        const val EMPTY_FIELD = ""

        override fun createFromParcel(parcel: Parcel): Taxonomy {
            return Taxonomy(parcel)
        }

        override fun newArray(size: Int): Array<Taxonomy?> {
            return arrayOfNulls(size)
        }

        /**
         * Metodo utilizzare per ottenere una istanzia di Taxonomy completamente vuota.
         * @return istanza di Taxonomy con i campi tutti vuoti
         */
        fun getEmptyTaxonomy(): Taxonomy {
            return Taxonomy(
                kingdom = EMPTY_FIELD,
                phylum = EMPTY_FIELD,
                order = EMPTY_FIELD,
                family = EMPTY_FIELD,
                genus = EMPTY_FIELD,
                species = EMPTY_FIELD)
        }
    }
}
