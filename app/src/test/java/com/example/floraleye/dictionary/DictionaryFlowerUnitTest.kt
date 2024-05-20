package com.example.floraleye.dictionary

import com.example.floraleye.models.DictionaryFlower
import com.example.floraleye.models.Taxonomy
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class DictionaryFlowerUnitTest {

    private lateinit var flower: DictionaryFlower

    private val emptyFilters = Taxonomy.getEmptyTaxonomy()

    private val matchedFilters = Taxonomy(
        kingdom = "Plantae",
        phylum = "Tracheophyta",
        order = "Apiales",
        family = "Apiaceae",
        genus = "Eryngium",
        species = "Eryngium alpinum"
    )

    /**
     * Inizializzazione di un oggetto DictionaryFlower.
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
     * Test valori delle variabili di un DictionaryFlower.
     */
    @Test fun flower_assertEqualValues() {
        Assert.assertEquals("Eryngium alpinum L.", flower.scientificName)
        Assert.assertEquals("Alpine Sea Holly", flower.commonName)
        Assert.assertEquals("Eryngium alpinum", flower.canonicalName)
        Assert.assertEquals("imageURL", flower.imageURL)
        Assert.assertEquals("description", flower.description)
        Assert.assertEquals("Plantae", flower.taxonomy.kingdom)
        Assert.assertEquals("Tracheophyta", flower.taxonomy.phylum)
        Assert.assertEquals("Apiales", flower.taxonomy.order)
        Assert.assertEquals("Apiaceae", flower.taxonomy.family)
        Assert.assertEquals("Eryngium", flower.taxonomy.genus)
        Assert.assertEquals("Eryngium alpinum", flower.taxonomy.species)
    }

    @Test fun flower_describeContents() {
        Assert.assertEquals(flower.describeContents(), 0)
    }

    @Test fun flower_areFiltersMatched() {
        Assert.assertTrue(flower.areFilterMatched(emptyFilters))
        Assert.assertTrue(flower.areFilterMatched(matchedFilters))
    }
}
