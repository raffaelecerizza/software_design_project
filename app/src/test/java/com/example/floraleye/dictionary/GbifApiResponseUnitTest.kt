package com.example.floraleye.dictionary

import com.example.floraleye.models.GbifApiResponse
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class GbifApiResponseUnitTest {

    private lateinit var apiResponse: GbifApiResponse

    /**
     * Creazione della classe per le risposte delle richeste effettuate all'API GBIF.
     */
    @Before
    fun createGbifApiResponse() {
        apiResponse = GbifApiResponse(
            scientificName = "Eryngium alpinum L.",
            canonicalName = "Eryngium alpinum",
            kingdom = "Plantae",
            phylum = "Tracheophyta",
            order = "Apiales",
            family = "Apiaceae",
            genus = "Eryngium",
            species = "Eryngium"
        )
    }

    /**
     * Test per le nested classes dell'oggetto WikipediaApiResponse.
     */
    @Test
    fun testGbif_Equals(){

        Assert.assertEquals("Eryngium alpinum L.", apiResponse.scientificName)
        Assert.assertEquals("Eryngium alpinum", apiResponse.canonicalName)
        Assert.assertEquals("Plantae", apiResponse.kingdom)
        Assert.assertEquals("Tracheophyta", apiResponse.phylum)
        Assert.assertEquals("Apiales", apiResponse.order)
        Assert.assertEquals("Apiaceae", apiResponse.family)
        Assert.assertEquals("Eryngium", apiResponse.genus)
        Assert.assertEquals("Eryngium", apiResponse.species)
    }
}
