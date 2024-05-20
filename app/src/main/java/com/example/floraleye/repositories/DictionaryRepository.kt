package com.example.floraleye.repositories

import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.floraleye.utils.Constants.GBIF_API_URL
import com.example.floraleye.utils.Constants.WIKI_API_URL
import com.example.floraleye.utils.Constants.DICTIONARY_CACHE_FILE
import com.example.floraleye.models.DictionaryFlower
import com.example.floraleye.models.WikiApiResponseInterface
import com.example.floraleye.models.GbifApiResponseInterface
import com.example.floraleye.models.WikipediaApiResponse
import com.example.floraleye.models.GbifApiResponse
import com.example.floraleye.models.Taxonomy
import com.example.floraleye.utils.Constants.DICTIONARY_COUNTER_START
import com.example.floraleye.utils.GeneralUtils.searchValueInMapArray
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File

/**
 * Repository del Dizionario.
 */
class DictionaryRepository {

    /**
     * Lista dei fiori del dizionario.
     */
    val flowersList = MutableLiveData<MutableList<DictionaryFlower>>()

    /**
     * Boolean che controlla se il dizionario è inizializzato.
     */
    val bIsFlowerListInitialized: MutableLiveData<Boolean> = MutableLiveData()

    /**
     * Lista di nomi di fiori da ricercare per comporre il dizionario.
     */
    val flowersName = mutableListOf(
        "Eryngium alpinum", //alpine sea holly
        "Anthurium", //anthurium
        "Cynara cardunculus", //artichoke
        "Azalea", //azalea
        "Tillandsia recurvata", //ballmoss
        "Platycodon grandiflorus", //balloon flower
        "Gerbera jamesonii", //barbeton daisy
        "Iris × germanica", //bearded iris
        "Monarda didyma L.", //bee balm
        "Strelitzia", //bird of paradise
        "Dahlia Cav.", //bishop of llandaff
        "Rudbeckia hirta", //black-eyed susan
        "Iris domestica", //blackberry lily
        "Gaillardia Foug.", //blanket flower
        "Eustoma russellianum", //bolero deep blue
        "Bougainvillea", //bougainvillea
        "Guzmania lingulata", //bromelia ????????
        "Protea cynaroides", //king protea
        "Helleborus orientalis", //lenten rose
        "Nelumbo nucifera", //lotus
        "Nigella damascena", //love in the mist
        "Magnolia × soulangeana", //magnolia
        "Malva", //mallow
        "Tagetes", //marigold
        "Cosmos bipinnatus", //mexican aster
        "Ruellia simplex", //mexican petunia
        "Aconitum", //monkshood
        "Phalaenopsis", //moon orchid
        "Ipomoea", //morning glory
        "Dahlia pinnata", //orange dahlia
        "osteospermum", //osteospermum
        "Leucanthemum vulgare", //oxeye daisy
        "Passiflora", //passion flowers
        "Pelargonium", //pelargonium

        "Ranunculus",
        "Eschscholzia californica",
        "Camellia",
        "Canna (plant)",
        "Campanula medium",
        "Nerine bowdenii", //cape flower
        "Dianthus caryophyllus",
        "Cautleya spicata",
        "Clematis",
        "Tussilago farfara",
        "Aquilegia",
        "Taraxacum officinale",
        "Papaver rhoeas",
        "Cyclamen",
        "Narcissus (plant)",
        "Adenium obesum",
        "Calendula officinalis",
        "Alstroemeria",
        "Petunia",
        "Scabiosa",
        "Primula polyantha",
        //"pink-yellow dahlia", //pink-yellow dahlia ??
        "Euphorbia pulcherrima",
        "Primula",
        "Amaranthus hypochondriacus", //"Prince-of-Wales feather"
        "Echinacea purpurea",
        "Alpinia purpurata",
        "Rose L.",
        "Cattleya labiata",
        "Curcuma alismatifolia",
        "Convolvulus cneorum",
        "Antirrhinum",
        "Cirsium vulgare",
        "Crocus vernus",

        "Gloriosa_(plant)",
        "Digitalis",
        "Plumeria L.",
        "Fritillaria L.",
        "Phlox paniculata",
        "Gaura L.",
        "Gazania Gaertn.",
        "Geranium L.",
        "Zantedeschia aethiopica",
        "Echinops L.",
        "Trollius",
        "Muscari",
        "Astrantia major",
        "Paphiopedilum micranthum",
        "Hibiscus",
        "Hippeastrum",
        "Eriocapitella japonica",
        "Gentiana acaulis",
        "Helianthus annuus",
        "Lathyrus odoratus",
        "Dianthus barbatus",
        "Gladiolus",
        "Datura stramonium",
        "Lilium lancifolium",
        "Tricyrtis hirta",
        "Malva arborea",
        "Dendromecon rigida",
        "Campsis radicans",
        "Erysimum",
        "Nymphaea",
        "Nasturtium officinale",
        "Viola tricolor",
        "Anemone",
        "Iris pseudacorus"
    )

    /**
     * Boolean che controlla se il download per i dati del dizionario è inziato.
     */
    var isThreadStarted : Boolean = false

    /**
     * Contatore per il download dei fiori del dizionario.
     */
    var counter: MutableLiveData<String> = MutableLiveData()

    /**
     * Boolean che controlla la presenza di errori durante il download dei dati del dizionario.
     */
    var bShowErrorMessage: MutableLiveData<Boolean> = MutableLiveData()

    private val checkFlowersName = mutableListOf(
        "Monarda",
        "Dahlia 'Bishop of Llandaff'",
        "Gaillardia",
        "Rose",
        "Fritillaria",
        "Plumeria",
        "Gaura",
        "Gazania",
        "Geranium",
        "Echinops"
    )

    private val commonNameFlowers = mapOf(
        "Eryngium alpinum" to "Alpine Sea Holly",
        "Cynara cardunculus" to "Artichoke",
        "Ericaceae" to "Azalea",
        "Tillandsia recurvata" to "Ball moss",
        "Platycodon grandiflorus" to "Balloon Flower",
        "Gerbera jamesonii" to "Barbeton Daisy",
        "Iris germanica" to "Bearded Iris",
        "Monarda didyma" to "Bee balm",
        "Strelitzia" to "Bird Of Paradise",
        "Dahlia Cav." to "Bishop of llandaff",
        "Rudbeckia hirta" to "Black-eyed Susan",
        "Iris domestica" to "Blackberry lily",
        "Gaillardia Foug." to "Blanket flower",
        "Eustoma russellianum" to "Bolero Deep Blue",
        "Guzmania lingulata" to "Bromelia",
        "Protea cynaroides" to "King Protea",
        "Helleborus orientalis" to "Lenten rose",
        "Nelumbo nucifera" to "Lotus",
        "Nigella damascena" to "Love In The Mist",
        "Magnolia soulangeana" to "Magnolia",
        "Malva" to "Mallow",
        "Tagetes" to "Marigold",
        "Cosmos bipinnatus" to "Mexican Aster",
        "Ruellia simplex" to "Mexican Petunia",
        "Aconitum" to "Monkshood",
        "Phalaenopsis" to "Moon Orchid",
        "Ipomoea" to "Morning Glory",
        "Dahlia pinnata" to "Orange Dahlia",
        "Leucanthemum vulgare" to "Oxeye Daisy",
        "Passiflora" to "Passion Flowers",

        "Ranunculus" to "Buttercup",
        "Eschscholzia californica" to "Californian Poppy",
        "Canna" to "Canna lily",
        "Campanula medium" to "Canterbury Bells",
        "Nerine bowdenii" to "Cape Flower",
        "Dianthus caryophyllus" to "Carnation",
        "Tussilago farfara" to "Colt's foot",
        "Aquilegia" to "Columbine",
        "Taraxacum officinale" to "Common Dandelion",
        "Papaver rhoeas" to "Corn poppy",
        "Narcissus" to "Daffodil",
        "Adenium obesum" to "Desert Rose",
        "Calendula officinalis" to "English Marigold",
        "Alstroemeria" to "Peruvian Lily",
        "Scabiosa" to "Pincushion Flower",
        "Primula polyantha" to "Pink Primrose",
        //"pink-yellow dahlia", //pink-yellow dahlia ??
        "Euphorbia pulcherrima" to "Poinsettia",
        "Amaranthus hypochondriacus" to "Prince of Wales Feather",
        "Echinacea purpurea" to "Purple Coneflower",
        "Alpinia purpurata" to "Red Ginger",
        "Rosa" to "Rose",
        "Cattleya labiata" to "Ruby Lipped Cattleya",
        "Curcuma alismatifolia" to "Siam Tulip",
        "Convolvulus cneorum" to "Silverbush",
        "Antirrhinum" to "Snapdragon",
        "Cirsium vulgare" to "Spear Thistle",
        "Crocus vernus" to "Spring Crocus",

        "Gloriosa" to "Fire lily",
        "Digitalis" to "Foxglove",
        "Plumeria" to "Frangipani",
        "Fritillaria" to "Fritillary",
        "Phlox paniculata" to "Garden phlox",
        "Zantedeschia aethiopica" to "Giant White Arum Lily",
        "Echinops L." to "Globe Thistle",
        "Trollius" to "Globe Flower",
        "Muscari" to "Grape Hyacinth",
        "Astrantia major" to "Great Masterwort",
        "Paphiopedilum micranthum" to "Hard Leaved Pocket Orchid",
        "Eriocapitella japonica" to "Japanese Anemone",
        "Gentiana acaulis" to "Stemless Gentian",
        "Helianthus annuus" to "Sunflower",
        "Lathyrus odoratus" to "Sweet Pea",
        "Dianthus barbatus" to "Sweet William",
        "Gladiolus" to "Sword Lily",
        "Datura stramonium" to "Thorn Apple",
        "Lilium lancifolium" to "Tiger Lily",
        "Tricyrtis hirta" to "Toad Lily",
        "Malva arborea" to "Tree Mallow",
        "Dendromecon rigida" to "Tree Poppy",
        "Campsis radicans" to "Trumpet Creeper",
        "Erysimum" to "Wallflower",
        "Nymphaea" to "Water Lily",
        "Nasturtium officinale" to "Watercress",
        "Viola tricolor" to "Wild Pansy",
        "Anemone" to "Windflower",
        "Iris pseudacorus" to "Yellow Iris"
    )

    private var gbifRetro : GbifApiResponseInterface

    private var wikiRetro : WikiApiResponseInterface

    private var isDictionaryCached : Boolean

    companion object {

        private val TAG: String = DictionaryRepository::class.java.simpleName
    }

    init {
        bIsFlowerListInitialized.value = false
        isDictionaryCached = false
        counter.value = DICTIONARY_COUNTER_START
        bShowErrorMessage.value = false
        flowersList.value = ArrayList()

        gbifRetro = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(GBIF_API_URL)
            .build()
            .create(GbifApiResponseInterface::class.java)

        wikiRetro = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(WIKI_API_URL)
            .build()
            .create(WikiApiResponseInterface::class.java)
    }

    /**
     * Metodo per aggiungere un fiore alla Lista.
     * @param flower fiore che deve essere aggiunto alla lista
     */
    fun addFlowerPost(flower: DictionaryFlower): Boolean {

        val repoValue = flowersList.value

        if(repoValue != null) {
            repoValue.add(flower)
            flowersList.postValue(flowersList.value)
            return true
        }

        return false
    }

    /**
     * Metodo per ottenere informazioni di un fiore dall'API GBIF.
     * @param flower nome del fiore
     * @return GBifQueryResult? classe che contiene le informazioni ottenute
     */
    fun getGbifQueryResult(flower: String) : GbifApiResponse {
        val gbifRetro = this.gbifRetro
        val retrofitGbifData = gbifRetro.getGbifData(flower)
        return retrofitGbifData.execute().body() as GbifApiResponse
    }

    /**
     * Metodo per ottenere informazioni di un fiore dall'API Wikipedia.
     * @param flower nome del fiore
     * @return Root? classe che contiene le informazioni ottenute
     */
    fun getWikiQueryResult(flower: String) : WikipediaApiResponse {
        val wikiRetro = this.wikiRetro
        var searchName = ""

        for (entry in checkFlowersName) {
            if (flower.contains(".") && flower.contains(entry.substringBefore(" ")))
                searchName = entry
        }

        if (searchName.isEmpty())
            searchName = flower

        val retrofitWikiData = wikiRetro.getData(
            action = "query",
            prop = "extracts|pageimages", titles = searchName,
            redirects = 1, format = "json", formatversion = 1, exintro = 1,
            explaintext = 1, pithumbsize = 1280
        )
        return retrofitWikiData.execute().body() as WikipediaApiResponse
    }

    /**
     * Metodo per l'inizializzazione dei dati nel Dizionario.
     */
    fun initializeFlowersList(){

        Thread {

            try {
                isThreadStarted = true
                var count = 0

                for (nameFlower in flowersName) {

                    val gbifResult = getGbifQueryResult(nameFlower)

                    val wikiResult = getWikiQueryResult(nameFlower)

                    val commonName = searchValueInMapArray(
                        gbifResult.scientificName,
                        gbifResult.canonicalName,
                        commonNameFlowers
                    )

                    val flower = DictionaryFlower(
                        scientificName = gbifResult.scientificName,
                        commonName = commonName,
                        canonicalName = gbifResult.canonicalName,
                        imageURL = wikiResult.query.pages.entries.iterator()
                            .next().value.thumbnail.source,
                        description = getWikiExtract(wikiResult).replace("\n", ""),
                        taxonomy = Taxonomy(
                            kingdom = gbifResult.kingdom,
                            phylum = gbifResult.phylum,
                            order = gbifResult.order,
                            family = gbifResult.family,
                            genus = gbifResult.genus,
                            species = gbifResult.species
                        )
                    )
                    count++
                    counter.postValue(count.toString())
                    addFlowerPost(flower)
                }
            }
            catch (e : java.lang.Exception){
                Log.e(TAG, e.toString())
                this.isThreadStarted = false
                this.flowersList.postValue(ArrayList())
                this.counter.postValue(DICTIONARY_COUNTER_START)
                this.bShowErrorMessage.postValue(true)
                return@Thread
            }

            this.bIsFlowerListInitialized.postValue(true)
            this.isThreadStarted = false
        }.start()
    }

    /**
     * Metodo che ordina la lista di fiori del Dizionario in base al loro nome scientifico dalla A alla Z.
     */
    fun sortDictionaryEntries(): Boolean{

        val currentList = flowersList.value

        if (currentList != null) {

            val flowerList: MutableList<DictionaryFlower> = currentList

            if (flowerList.size > 0) {

                val newFlowerList = flowerList.sortedBy { myObject ->
                    myObject.commonName
                } as MutableList<DictionaryFlower>

                flowersList.value = newFlowerList
                return true
            }
        }

        return false
    }

    /**
     * Metodo per ottenere l'estratto di una pagina Wikipedia.
     * @return String testo dell'estratto.
     */
    fun getWikiExtract(result: WikipediaApiResponse) : String {
        return result.query.pages.entries.iterator().next().value.extract
    }

    /**
     * Metodo per caricare il dizionario da un file JSON contenente oggetti DictionaryFlower.
     */
    fun loadDictionaryFromJSON(context: Context) {
        val cacheFile = File(context.cacheDir, DICTIONARY_CACHE_FILE)
        val gson = Gson()
        val itemType = object : TypeToken<MutableList<DictionaryFlower>>() {}.type
        val list = gson.fromJson<MutableList<DictionaryFlower>>(cacheFile.readText(), itemType)

        for (flower : DictionaryFlower in list){
            addFlowerPost(flower)
        }

        bIsFlowerListInitialized.value = true
        isDictionaryCached = true
    }

    /**
     * Metodo per effettuare il clean della repository.
     */
    fun cleanRepository() {
        bIsFlowerListInitialized.value = false
        isDictionaryCached = false
        counter.value = DICTIONARY_COUNTER_START
        bShowErrorMessage.value = false
        flowersList.value = ArrayList()
    }

    /**
     * Metodo per controllare se il dizionario è stato salvato nella cache.
     * @return Boolean indica se il dizionario è stato salvato nella cache.
     */
    fun isDictionaryCached(context : Context) : Boolean {
        isDictionaryCached = File(context.cacheDir, DICTIONARY_CACHE_FILE).exists()
        return isDictionaryCached
    }

    /**
     * Metodo per effettuare la cache del dizionario.
     * Return Boolean indica se è stata effettuata la cache del dizionario.
     */
    fun cacheDictionary(context : Context): Boolean {

        File.createTempFile(DICTIONARY_CACHE_FILE, null, context.cacheDir)

        if (bIsFlowerListInitialized.value == true){

            val repositoryArray = flowersList.value

            if (repositoryArray != null){
                val jsonFlower = Gson().toJson(repositoryArray)
                val cacheFile = File(context.cacheDir, DICTIONARY_CACHE_FILE)
                cacheFile.writeText(jsonFlower)
                isDictionaryCached = true
                return true
            }
        }
        return false
    }
}
