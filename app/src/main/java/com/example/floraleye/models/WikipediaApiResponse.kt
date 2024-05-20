package com.example.floraleye.models

import com.example.floraleye.utils.Constants.WIKI_ENDPOINT
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Interfaccia per query all'API Wikipedia.
 */
interface WikiApiResponseInterface {

    /**
     * Metodo per ottenere informazioni relative a un fiore dal sito Wikipedia.
     * @param action azione da eseguire
     * @param prop proprietà dell'articolo di Wikipedia da recuperare
     * @param titles titolo dell'articolo di Wikipedia da recuperare
     * @param redirects parametro per seguire eventuali reindirizzamenti
     * @param format formato della risposta della query
     * @param formatversion versione del formato
     * @param exintro parametro per ottenere solo il testo introduttivo
     * @param explaintext parametro per ottenere l'estratto dell'articolo come testo semplice
     * @param pithumbsize parametro che indica la dimensione dell'immagine
     * @return Root del JSON ottenuto effettuando la query
     */
    @Suppress("LongParameterList")
    @GET(WIKI_ENDPOINT)
    fun getData(@Query("action") action: String, @Query("prop") prop: String,
                @Query("titles") titles: String, @Query("redirects") redirects: Int,
                @Query("format") format: String, @Query("formatversion") formatversion: Int,
                @Query("exintro") exintro: Int, @Query("explaintext") explaintext: Int,
                @Query("pithumbsize") pithumbsize: Int
    ): Call<WikipediaApiResponse>
}

/**
 * Classe per lo storing della Root del JSON ottenuta tramite query all'API Wikipedia.
 */
class WikipediaApiResponse {
    /**
     * Classe query del JSON.
     */
    var query: Klass = Klass()
}

/**
 * Classe per lo storing delle Pages del JSON ottenute tramite query all'API Wikipedia.
 */
class Klass {
    /**
     * Pages di un JSON.
     */
    var pages: Map<String, Page> = emptyMap<String, Page>()
}

/**
 * Classe per lo storing dei parametri relativi ai fiori ottenuti tramite query all'API Wikipedia.
 */
class Page {
    /**
     * Titolo della pagina Wikipedia.
     */
    var title: String = ""
    /**
     * Estratto della pagina Wikipedia.
     */
    var extract: String = ""
    /**
     * Immagine.
     */
    var thumbnail : Thumbnail = Thumbnail("",0,0)
}

/**
 * Classe per lo storing dell'immagine di un fiore ottenuta tramite query all'API Wikipedia.
 */
data class Thumbnail(
    /**
     * URL dell'immagine.
     */
    val source : String,
    /**
     * Larghezza dell'immagine.
     */
    val width : Int,
    /**
     * Altezza dell'immagine.
     */
    val height : Int,
)
