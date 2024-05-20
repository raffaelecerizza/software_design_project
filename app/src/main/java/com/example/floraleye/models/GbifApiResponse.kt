package com.example.floraleye.models

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query
import com.example.floraleye.utils.Constants.GBIF_ENDPOINT
import com.example.floraleye.utils.Constants.Generated

/**
 * Interfaccia per query all'API GBIF.
 */
interface GbifApiResponseInterface {

    /**
     * Metodo per ottenere informazioni relative a un fiore dal sito GBIF.
     * @param name nome di un Fiore
     */
    @GET(GBIF_ENDPOINT)
    fun getGbifData(@Query("name") name: String): Call<GbifApiResponse>
}

/**
 * Classe per lo storing dei dati ottenuti tramite query all'API GBIF.
 */
@Generated
data class GbifApiResponse(
    /**
     * Nome Scientifico di un fiore.
     */
    val scientificName: String = "",
    /**
     * Nome Canonico di un fiore.
     */
    val canonicalName: String = "",
    /**
     * Regno di un fiore.
     */
    val kingdom: String = "",
    /**
     * Phylum di un fiore.
     */
    val phylum: String = "",
    /**
     * Ordine di un fiore.
     */
    val order: String = "",
    /**
     * Famiglia di un fiore.
     */
    val family: String = "",
    /**
     * Genere di un fiore.
     */
    val genus: String = "",
    /**
     * Specie di un fiore.
     */
    val species: String = "",
)
