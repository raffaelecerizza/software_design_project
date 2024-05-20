package com.example.floraleye.models

import android.os.Build
import android.os.Parcel
import android.os.Parcelable
import com.example.floraleye.utils.Constants.Generated

/**
 * Classe per i fiori del Dizionario.
 */
class DictionaryFlower(scientificName: String,
                       canonicalName: String,
                       commonName: String,
                       imageURL: String,
                       description: String,
                       taxonomy: Taxonomy
): Parcelable {
    /**
     * Nome Scientifico di un Fiore.
     */
    var scientificName: String
        private set

    /**
     * Nome Comune di un Fiore.
     */
    var commonName: String
        private set

    /**
     * Nome Canonico di un fiore.
     */
    var canonicalName: String
        private set
    /**
     * URL dell'immagine di un fiore.
     */
    var imageURL : String
        private set
    /**
     * Descrizione di un fiore.
     */
    var description : String
        private set

    /**
     * Tassonomia di un fiore.
     */
    var taxonomy: Taxonomy
        private set

    /**
     * Variabile che indica se un fiore è aggiunto ai preferiti.
     */
    var isFavourite: Boolean = false

    /**
     * Metodo utilizzato per verificare se il DictionaryFlower rispetta certi filtri. Si noti che,
     * nel caso in cui un campo del filtro è vuoto, questo viene considerato rispettato di default.
     * @param filter filtri da comparare con il DictionaryFlower corrente
     * @return true se i filtri sono rispettati, false altrimenti
     */
    fun areFilterMatched(filter: Taxonomy): Boolean {
        return (filter.kingdom.isEmpty() ||
                taxonomy.kingdom.equals(filter.kingdom, ignoreCase = true)) &&
                (filter.phylum.isEmpty() ||
                        taxonomy.phylum.equals(filter.phylum, ignoreCase = true)) &&
                (filter.genus.isEmpty() ||
                        taxonomy.genus.equals(filter.genus, ignoreCase = true)) &&
                (filter.family.isEmpty() ||
                        taxonomy.family.equals(filter.family, ignoreCase = true)) &&
                (filter.species.isEmpty() ||
                        taxonomy.species.equals(filter.species, ignoreCase = true)) &&
                (filter.order.isEmpty() ||
                        taxonomy.order.equals(filter.order, ignoreCase = true))
    }

    @Generated
    @Suppress("DEPRECATION")
    constructor(parcel: Parcel) : this(
        scientificName = parcel.readString().toString(),
        commonName = parcel.readString().toString(),
        canonicalName = parcel.readString().toString(),
        imageURL = parcel.readString().toString(),
        description = parcel.readString().toString(),
        taxonomy = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            parcel.readParcelable(Taxonomy::class.java.classLoader, Taxonomy::class.java)
                    as Taxonomy
        } else {
            parcel.readParcelable<Taxonomy>(Taxonomy::class.java.classLoader)
                    as Taxonomy
        }
    )

    init {
        this.scientificName = scientificName
        this.commonName = commonName
        this.canonicalName = canonicalName
        this.imageURL = imageURL
        this.description = description
        this.taxonomy = taxonomy
    }

    @Generated
    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(scientificName)
        parcel.writeString(commonName)
        parcel.writeString(canonicalName)
        parcel.writeString(imageURL)
        parcel.writeString(description)
        parcel.writeParcelable(taxonomy, 0)
    }

    override fun describeContents(): Int {
        return 0
    }

    /**
     * Parcelable CREATOR della classe DictionaryFlower.
     */
    @Generated
    companion object CREATOR : Parcelable.Creator<DictionaryFlower> {
        override fun createFromParcel(parcel: Parcel): DictionaryFlower {
            return DictionaryFlower(parcel)
        }

        override fun newArray(size: Int): Array<DictionaryFlower?> {
            return arrayOfNulls(size)
        }
    }
}
