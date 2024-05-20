package com.example.floraleye.utils

import android.os.Parcel
import com.example.floraleye.models.DictionaryFlower
import com.example.floraleye.models.Taxonomy
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class DictionaryParcelTest {

    private lateinit var flower: DictionaryFlower

    /**
     * Inizializzazione di un oggetto di tipo DictionaryFlower.
     */
    @Before fun createFlower() {
        flower = DictionaryFlower(
            scientificName = "Eryngium alpinum L.",
            commonName = "Alpine Sea Holly",
            canonicalName = "Eryngium alpinum",
            imageURL = "imageURL",
            description = "description",
            taxonomy = Taxonomy(
                kingdom = "Plantae",
                phylum = "Tracheophyta",
                order = "Apiales",
                family = "Apiaceae",
                genus = "Eryngium",
                species = "Eryngium alpinum"
            )
        )
    }

    /**
     * Test per confronto di un DictionaryFlower inizializzato tramite Parcelable.
     */
    @Test fun parcelFlower_assertEqual(){
        val parcel : Parcel = Parcel.obtain()
        flower.writeToParcel(parcel, 0)
        parcel.setDataPosition(0)
        val parceledFlower: DictionaryFlower = DictionaryFlower.createFromParcel(parcel)

        Assert.assertEquals(parceledFlower.scientificName, flower.scientificName);
        Assert.assertEquals(parceledFlower.commonName, flower.commonName);
        Assert.assertEquals(parceledFlower.canonicalName, flower.canonicalName);
        Assert.assertEquals(parceledFlower.imageURL, flower.imageURL);
        Assert.assertEquals(parceledFlower.description, flower.description);
        Assert.assertEquals(parceledFlower.taxonomy.kingdom, flower.taxonomy.kingdom);
        Assert.assertEquals(parceledFlower.taxonomy.phylum, flower.taxonomy.phylum);
        Assert.assertEquals(parceledFlower.taxonomy.order, flower.taxonomy.order);
        Assert.assertEquals(parceledFlower.taxonomy.family, flower.taxonomy.family);
        Assert.assertEquals(parceledFlower.taxonomy.genus, flower.taxonomy.genus);
        Assert.assertEquals(parceledFlower.taxonomy.species, flower.taxonomy.species);
    }

    /**
     * Test per la gestione di array di DictionaryFlower tramite Parcelable.
     */
    @Test fun parcel_creatorNewArray(){

        val flowerArray = DictionaryFlower.newArray(0)
        Assert.assertEquals(0, flowerArray.size)

        val taxonomyArray = Taxonomy.newArray(0)
        Assert.assertEquals(0, taxonomyArray.size)
    }
}
