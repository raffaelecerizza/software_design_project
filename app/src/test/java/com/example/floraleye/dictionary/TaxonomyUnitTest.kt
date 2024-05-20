package com.example.floraleye.dictionary

import com.example.floraleye.models.Taxonomy
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class TaxonomyUnitTest {

    private lateinit var taxonomy: Taxonomy

    /**
     * Inizializzazione di un oggetto Taxonomy.
     */
    @Before fun createTaxonomy() {
        taxonomy = Taxonomy(
            kingdom = "Plantae",
            phylum = "Tracheophyta",
            order = "Apiales",
            family = "Apiaceae",
            genus = "Eryngium",
            species = "Eryngium alpinum"
        )
    }

    /**
     * Test valori delle variabili di un oggetto Taxonomy.
     */
    @Test fun taxonomy_assertEqualValues() {
        Assert.assertEquals("Plantae", taxonomy.kingdom)
        Assert.assertEquals("Tracheophyta", taxonomy.phylum)
        Assert.assertEquals("Apiales", taxonomy.order)
        Assert.assertEquals("Apiaceae", taxonomy.family)
        Assert.assertEquals("Eryngium", taxonomy.genus)
        Assert.assertEquals("Eryngium alpinum", taxonomy.species)
    }

    @Test fun taxonomy_describeContents() {
        Assert.assertEquals(taxonomy.describeContents(), 0)
    }

    @Test fun taxonomy_isEmpty() {
        var taxonomyCopy = taxonomy

        Assert.assertFalse(taxonomyCopy.isEmpty())

        taxonomyCopy = Taxonomy(
            kingdom = "",
            phylum = "Tracheophyta",
            order = "Apiales",
            family = "Apiaceae",
            genus = "Eryngium",
            species = "Eryngium alpinum"
        )
        Assert.assertFalse(taxonomyCopy.isEmpty())

        taxonomyCopy = Taxonomy(
            kingdom = "",
            phylum = "",
            order = "Apiales",
            family = "Apiaceae",
            genus = "Eryngium",
            species = "Eryngium alpinum"
        )
        Assert.assertFalse(taxonomyCopy.isEmpty())

        taxonomyCopy = Taxonomy(
            kingdom = "",
            phylum = "",
            order = "",
            family = "Apiaceae",
            genus = "Eryngium",
            species = "Eryngium alpinum"
        )
        Assert.assertFalse(taxonomyCopy.isEmpty())

        taxonomyCopy = Taxonomy(
            kingdom = "",
            phylum = "",
            order = "",
            family = "",
            genus = "Eryngium",
            species = "Eryngium alpinum"
        )
        Assert.assertFalse(taxonomyCopy.isEmpty())

        taxonomyCopy = Taxonomy(
            kingdom = "",
            phylum = "",
            order = "",
            family = "",
            genus = "",
            species = "Eryngium alpinum"
        )
        Assert.assertFalse(taxonomyCopy.isEmpty())

        taxonomyCopy = Taxonomy(
            kingdom = "",
            phylum = "",
            order = "",
            family = "",
            genus = "",
            species = ""
        )
        Assert.assertTrue(taxonomyCopy.isEmpty())
    }
}
