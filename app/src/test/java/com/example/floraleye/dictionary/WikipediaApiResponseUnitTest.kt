package com.example.floraleye.dictionary

import com.example.floraleye.models.Page
import com.example.floraleye.models.Klass
import com.example.floraleye.models.Thumbnail
import com.example.floraleye.models.WikipediaApiResponse
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class WikipediaApiResponseUnitTest {

    private lateinit var apiResponse: WikipediaApiResponse

    /**
     * Creazione della classe per le risposte delle richeste effettuate all'API Wikipedia.
     */
    @Before fun createWikipediaApiResponse() {
        apiResponse = WikipediaApiResponse()
    }

    /**
     * Test per le nested classes dell'oggetto WikipediaApiResponse.
     */
    @Test fun testGetAndSetQueryPage(){

        val entry = Page()

        entry.extract = "Testo di Prova"
        entry.title = "Titolo"
        entry.thumbnail = Thumbnail("URL", 0,0)

        Assert.assertEquals("Testo di Prova", entry.extract)
        Assert.assertEquals("Titolo", entry.title)
        Assert.assertEquals("URL", entry.thumbnail.source)
        Assert.assertEquals(0, entry.thumbnail.width)
        Assert.assertEquals(0, entry.thumbnail.height)

        val query = Klass()
        query.pages = mapOf("Key1" to entry)
        apiResponse.query = query
        Assert.assertEquals(
            apiResponse.query.pages.entries.iterator().next().value.extract,
            entry.extract
        )
    }
}

