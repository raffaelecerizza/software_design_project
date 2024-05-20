package com.example.floraleye.repositories

import androidx.test.platform.app.InstrumentationRegistry.getInstrumentation
import com.example.floraleye.TestConstants
import com.example.floraleye.TestConstants.DICTIONARY_TEST_CONTROL_NUMBER
import com.example.floraleye.TestUtilities.makeCyclicViewTest
import com.example.floraleye.models.DictionaryFlower
import com.example.floraleye.models.GbifApiResponse
import com.example.floraleye.models.Taxonomy
import org.junit.Assert
import org.junit.Before
import org.junit.Ignore
import org.junit.Test

@Ignore class DictionaryRepositoryTest {

    private lateinit var repository: DictionaryRepository

    /**
     * Creazione della repository DictionaryRepository.
     */
    @Before fun createDictionaryRepository() {
        getInstrumentation().runOnMainSync {
            repository = DictionaryRepository()
        }
    }

    /**
     * Test inizializzazione del dizionario tramite repository.
     */
    @Test fun testDictionaryInitAndCleaning(){
        Assert.assertNotEquals(0, repository.flowersName.size)
        getInstrumentation().runOnMainSync {
            repository.initializeFlowersList()
        }

        makeCyclicViewTest(60,1000) {
            repository.bIsFlowerListInitialized.value?.let { Assert.assertTrue(it) }
        }

        Assert.assertEquals(repository.flowersName.size.toString(), repository.counter.value)

        getInstrumentation().runOnMainSync {
            Assert.assertTrue(repository.sortDictionaryEntries())
        }

        getInstrumentation().runOnMainSync {
            repository.cleanRepository()
        }

        getInstrumentation().runOnMainSync {
            Assert.assertFalse(repository.sortDictionaryEntries())
        }

        repository.bIsFlowerListInitialized.value?.let { Assert.assertFalse(it) }
    }

    /**
     * Test per controllare funzionamento del controllo sul Thread di inizializzazione del Dizionario.
     */
    @Test fun isLoadingDictionary(){
        Assert.assertFalse(repository.isThreadStarted)
        getInstrumentation().runOnMainSync {
            repository.initializeFlowersList()
        }

        Assert.assertTrue(makeCyclicViewTest {
            Assert.assertTrue(repository.isThreadStarted)
        })
    }

    /**
     * Test sulla gestione delle eccezioni durante l'inizializzazione del Dizionario.
     */
    @Ignore @Test fun exceptionHandlingDictionary(){
        getInstrumentation().uiAutomation.executeShellCommand(TestConstants.WIFI_DISABLE)
        getInstrumentation().uiAutomation.executeShellCommand(TestConstants.DATA_DISABLE)

        getInstrumentation().runOnMainSync {
            repository.initializeFlowersList()
        }

        makeCyclicViewTest(100,100) {
            repository.bShowErrorMessage.value?.let { Assert.assertTrue(it) }
        }

        getInstrumentation().uiAutomation.executeShellCommand(TestConstants.WIFI_ENABLE)
        getInstrumentation().uiAutomation.executeShellCommand(TestConstants.DATA_ENABLE)

        makeCyclicViewTest(100,100) {
            repository.bIsFlowerListInitialized.value?.let { Assert.assertFalse(it) }
        }
    }

    /**
     * Test con richieste non valide alle API.
     */
    @Test fun testNotValidRequestToAPIs(){
        val testFlower = "NotValid"
        val gbifResult = repository.getGbifQueryResult(testFlower)
        val wikiResult = repository.getWikiQueryResult(testFlower)

        val emptyResult = GbifApiResponse()
        Assert.assertEquals(gbifResult.kingdom, emptyResult.kingdom)
        Assert.assertEquals("", repository.getWikiExtract(wikiResult))
    }

    /**
     * Test aggiunta di un oggetto DictionaryFlower al Dizionario.
     */
    @Test fun addFlowerToDictionary(){
        val flower  = DictionaryFlower(
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

        repository.addFlowerPost(flower)
        val listFlower = repository.flowersList.value?.get(0)

        if (listFlower != null) {
            Assert.assertEquals(listFlower.scientificName, flower.scientificName)
            Assert.assertEquals(listFlower.commonName, flower.commonName)
            Assert.assertEquals(listFlower.canonicalName, flower.canonicalName)
            Assert.assertEquals(listFlower.imageURL, flower.imageURL)
            Assert.assertEquals(listFlower.description, flower.description)
            Assert.assertEquals(listFlower.taxonomy.kingdom, flower.taxonomy.kingdom)
            Assert.assertEquals(listFlower.taxonomy.phylum, flower.taxonomy.phylum)
            Assert.assertEquals(listFlower.taxonomy.order, flower.taxonomy.order)
            Assert.assertEquals(listFlower.taxonomy.family, flower.taxonomy.family)
            Assert.assertEquals(listFlower.taxonomy.genus, flower.taxonomy.genus)
            Assert.assertEquals(listFlower.taxonomy.species, flower.taxonomy.species)

            getInstrumentation().runOnMainSync {
                repository.flowersList.value = null
            }
            Assert.assertFalse(repository.addFlowerPost(listFlower))
        }
    }

    /**
     * Test per verificare il corretto funzionamento del caching del Dizionario.
     */
    @Test fun testDictionaryCaching(){
        getInstrumentation().runOnMainSync {
            repository.initializeFlowersList()
        }

        makeCyclicViewTest(DICTIONARY_TEST_CONTROL_NUMBER,1000) {
            repository.bIsFlowerListInitialized.value?.let { Assert.assertTrue(it) }
        }

        val context = getInstrumentation().targetContext
        repository.cacheDictionary(context)

        Assert.assertTrue(repository.isDictionaryCached(context))

        getInstrumentation().runOnMainSync {
            repository.cleanRepository()
        }

        getInstrumentation().runOnMainSync {
            repository.loadDictionaryFromJSON(context)
        }

        Assert.assertTrue(repository.isDictionaryCached(context))
        repository.bIsFlowerListInitialized.value?.let { Assert.assertTrue(it) }
    }

    /**
     * Test sull'ordinamento dei fiori del Dizionario.
     */
    @Test fun testSortNullDictionary(){
        getInstrumentation().runOnMainSync {
            repository.flowersList.value = null
        }
        Assert.assertFalse(repository.sortDictionaryEntries())
    }

    /**
     * Test per controllare che non sia possibile effettuare il caching di un Dizionario non valido.
     */
    @Test fun testCachingNotValidDictionary(){
        val context = getInstrumentation().targetContext
        Assert.assertFalse(repository.cacheDictionary(context))

        getInstrumentation().runOnMainSync {
            repository.flowersList.value = null
            repository.bIsFlowerListInitialized.value = true
        }

        Assert.assertFalse(repository.cacheDictionary(context))
    }

    /**
     * Test per assegnamento valore counter download Dizionario.
     */
    @Test fun testSetCounter(){
        getInstrumentation().runOnMainSync {
            repository.counter.value = "100"
        }

        Assert.assertEquals("100", repository.counter.value)
    }

    /**
     * Test per assegnamento Boolean messaggio di errore Dizionario.
     */
    @Test fun testSetErrorMessage(){
        getInstrumentation().runOnMainSync {
            repository.bShowErrorMessage.value = true
        }

        repository.bShowErrorMessage.value?.let { Assert.assertTrue(it) }
    }
}
