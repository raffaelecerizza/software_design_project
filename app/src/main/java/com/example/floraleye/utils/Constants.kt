package com.example.floraleye.utils


/**
 * Oggetto contenente tutte le costanti utilizzate.
 */
object Constants {

    // *** inizio regione - firebase

    /**
     * URL del Realtime Database di Firebase utilizzato per l'applicazione.
     */
    const val FIREBASE_REALTIME_DB =
        "https://floraleye-default-rtdb.europe-west1.firebasedatabase.app/"

    /**
     * Codice di stato ritornato nel caso di registrazione/onboard concluso con successo.
     */
    const val FIREBASE_AUTH_OK = 200

    /**
     * Codice di stato ritornato se la mail per il ripristino della password è stata inviata con
     * successo all'indirizzo e-mail specificato dall'utente.
     */
    const val FIREBASE_AUTH_SEND_RESET_PASSWORD_OK = 201

    /**
     * Codice di stato ritornato nel caso di errore generico in fase di registrazione/onboard.
     */
    const val FIREBASE_AUTH_GENERIC_ERROR = 400

    /**
     * Codice di stato ritornato nel caso di tentativo di registrazione con mail già in uso da
     * un altro utente.
     */
    const val FIREBASE_SIGNUP_USER_EXISTS_ERROR = 401

    /**
     * Codice di stato ritornato nel caso di tentativo di registrazione/onboard con password troppo debole
     * per i criteri di sicurezza di Firebase (NON quelli dell'app).
     */
    const val FIREBASE_AUTH_WEAK_PASSWORD_ERROR = 402

    /**
     * Codice di stato ritornato nel caso di tentativo di registrazione/onboard con e-mail malformata.
     */
    const val FIREBASE_AUTH_CREDENTIAL_ERROR = 403

    /**
     * Codice di stato ritornato nel caso di tentativo di registrazione/onboard con connessione
     * assente o lenta.
     */
    const val FIREBASE_AUTH_NETWORK_ERROR = 404

    /**
     * Codice di stato ritornato nel caso di login con indirizzo e-mail non associato ad alcun
     * utente precedentemente registrato.
     */
    const val FIREBASE_AUTH_INVALID_USER_ERROR = 405

    /**
     * Codice di stato ritornato nel caso di tentativo di login con indirizzo e-mail non ancora
     * verificato.
     */
    const val FIREBASE_AUTH_MAIL_NOT_VERIFIED = 406

    /**
     * Codice di stato ritornato nel caso si facciano troppe richieste a Firebase Auth in un tempo
     * limitato.
     */
    const val FIREBASE_AUTH_TOO_MANY_REQUEST = 407

    /**
     * Codice di stato ritornato nel caso di tentativo di eliminazione dell'account di un utente
     * con una autenticazione troppo vecchia.
     */
    const val FIREBASE_AUTH_LOGIN_TOO_OLD = 408

    // *** fine regione - firebase

    // *** inizio regione - quiz

    /**
     * Nome del database utilizzato per memorizzare la lista di singoli quiz
     * da sottoporre agli utenti.
     */
    const val QUIZZES = "quizzes"

    /**
     * Stringa rappresentante l'identificatore di un quiz.
     */
    const val IDENTIFIER = "id"

    /**
     * Stringa rappresentante la domanda di un quiz.
     */
    const val QUESTION = "question"

    /**
     * Stringa rappresentante l'immagine di un quiz.
     */
    const val IMAGE = "image"

    /**
     * Stringa rappresentante la soluzione di un quiz.
     */
    const val SOLUTION = "solution"

    /**
     * Stringa rappresentante la prima risposta disponibile per un quiz.
     */
    const val ANSWER1 = "answer1"

    /**
     * Stringa rappresentante la seconda risposta disponibile per un quiz.
     */
    const val ANSWER2 = "answer2"

    /**
     * Stringa rappresentante la terza risposta disponibile per un quiz.
     */
    const val ANSWER3 = "answer3"

    /**
     * Stringa rappresentante la quarta risposta disponibile per un quiz.
     */
    const val ANSWER4 = "answer4"

    /**
     * Messaggio di errore per il mancato recupero dei quiz da Firebase Realtime Database.
     */
    const val FAILED_READ = "Failed to read value."

    /**
     * Stringa utilizzata per poter recuperare il quiz corretto
     * in caso di cambio di configurazione.
     */
    const val QUIZ_INDEX = "QUIZ_INDEX"

    /**
     * Stringa utilizzata per poter recuperare la soluzione di un quiz
     * in caso di cambio di configurazione.
     */
    const val QUIZ_SOLUTION = "QUIZ_SOLUTION"

    /**
     * Stringa utilizzata per poter memorizzare il fatto che la risposta al quiz
     * sia già stata sottomessa e tenerne conto in caso di cambio di configurazione.
     */
    const val SUBMIT_BUTTON_CLICKED = "SUBMIT_BUTTON_CLICKED"

    /**
     * Indice del primo radio button.
     */
    const val INDEX_RADIO_BUTTON1 = 0

    /**
     * Indice del secondo radio button.
     */
    const val INDEX_RADIO_BUTTON2 = 1

    /**
     * Indice del terzo radio button.
     */
    const val INDEX_RADIO_BUTTON3 = 2

    /**
     * Indice del quarto radio button.
     */
    const val INDEX_RADIO_BUTTON4 = 3

    /**
     * Valore iniziale dell'animazione dell'opacità del bordo delle risposte.
     */
    const val START_OPACITY = 0.0F

    /**
     * Valore finale dell'animazione dell'opacità del bordo delle risposte.
     */
    const val END_OPACITY = 1.0F

    /**
     * Durata dell'animazione di opacità del bordo delle risposte in millisecondi.
     */
    const val ANSWER_ANIMATION_DURATION = 500

    /**
     * Durata dell'animazione per il passaggio al quiz successivo in millisecondi.
     */
    const val NEXT_BUTTON_ANIMATION_DURATION = 1000

    /**
     * Ampiezza della colorazione del bordo della risposta corretta e della eventuale
     * risposta errata.
     */
    const val ANSWER_STROKE_WIDTH = 8

    /**
     * Stringa utilizzata nel QuizFragment per determinare quale tab debba essere mostrato.
     */
    const val ARG_PARAM_QUIZ_TAB = "selected tab"

    // *** fine regione - quiz


    // *** inizio regione - storico quiz

    /**
     * Nome del database utilizzato per memorizzare lo storico dei quiz
     * degli utenti.
     */
    const val QUIZ_HISTORY = "quizHistory"

    /**
     * Stringa rappresentante l'identificatore nello storico di un quiz svolto.
     */
    const val QUIZ_HISTORY_ID = "quizHistoryId"

    /**
     * Stringa rappresentante l'identificatore di un modello di quiz.
     */
    const val QUIZ_ID = "quizId"

    /**
     * Stringa rappresentante la risposta di un utente a un quiz.
     */
    const val USER_ANSWER = "userAnswer"

    /**
     * Stringa rappresentante l'istante di tempo in cui un utente ha sottomesso la risposta
     * di un quiz.
     */
    const val TIME = "time"

    /**
     * Indice della prima risposta di un quiz.
     */
    const val INDEX_ANSWER1 = 0

    /**
     * Indice della seconda risposta di un quiz.
     */
    const val INDEX_ANSWER2 = 1

    /**
     * Indice della terza risposta di un quiz.
     */
    const val INDEX_ANSWER3 = 2

    /**
     * Indice della quarta risposta di un quiz.
     */
    const val INDEX_ANSWER4 = 3

    /**
     * Riferimento locale da utilizzare per ottenere l'ora corrente.
     */
    const val ZONE_ID = "Europe/Rome"

    /**
     * Messaggio di errore per il caso in cui lo storico dei quiz dell'utente corrente sia vuoto.
     */
    const val EMPTY_QUIZ_HISTORY = "There isn't any quiz in the history of the current user."

    /**
     * Valore utilizzato per ottenere una percentuale.
     */
    const val PERCENTAGE = 100

    /**
     * Quiz score di un utente che non ha sottomesso alcuna risposta ai quiz.
     */
    const val ZERO_QUIZ_SCORE = 0.0F

    /**
     * Durata dell'animazione del quiz score dell'utente.
     */
    const val QUIZ_SCORE_ANIMATION_DURATION = 2000L

    /**
     * Carattere iniziale utilizzato per evidenziare a quale località
     * fa riferimento il tempo calcolato.
     */
    const val TIME_ZONE_START = "["

    /**
     * Pattern utilizzato per specificare l'anno, il mese e il giorno di un riferimento temporale.
     */
    const val YEAR_MONTH_DAY_PATTERN = "yyyy-MM-dd"

    /**
     * Pattern utilizzato per specificare l'ora, il minuto e il secondo di un riferimento temporale.
     */
    const val HOUR_MINUTE_SECOND_PATTERN = "HH:mm:ss"

    // *** fine regione - storico quiz


    // *** inizio regione - generale

    /**
     * Lunghezza massima per i messaggi presentabili tramite Snackbar. Se i messaggio eccede questa
     * lunghezza dovrà essere mostrato in altro modo, ad esempio con un pop up.
     */
    const val MESSAGE_LENGTH_FOR_SNACKBAR_LIMIT = 60

    /**
     * Messaggio di eccezione per il tentativo di creare un ViewModel di una classe sconosciuta.
     */
    const val UNKNOWN_VIEWMODEL = "Unknown ViewModel class"

    // *** fine regione - generale


    // *** inizio regione - dizionario

    /**
     * Endpoint per le richieste all'API GBIF.
     */
    const val GBIF_ENDPOINT = "/v1/species/match"

    /**
     * Endpoint per le richieste all'API Wikipedia.
     */
    const val WIKI_ENDPOINT = "/w/api.php"

    /**
     * URL dell'API GBIF.
     */
    const val GBIF_API_URL = "https://api.gbif.org/"

    /**
    * URL dell'API GBIF.
    */
    const val WIKI_API_URL = "https://en.wikipedia.org/"

    /**
     * Nome del file JSON per il caching del Dizionario.
     */
    const val DICTIONARY_CACHE_FILE = "dictionary.json"

    /**
     * Stringa iniziale per il contatore download Dizionario.
     */
    const val DICTIONARY_COUNTER_START = "0"

    /**
     * Identificatore parcelable di un oggetto DictionaryFlower.
     */
    const val FLOWER_KEY = "flower"

    /**
     * Nome del file shared preferences utilizzato per persistere i filtri.
     */
    const val FILTERS_FILE = "filters_file"

    /**
     * Chiave associata al filtro kingdom.
     */
    const val FILTERS_KINGDOM_KEY = "filters_kingdom_key"

    /**
     * Chiave associata al filtro phylum.
     */
    const val FILTERS_PHYLUM_KEY = "filters_phylum_key"

    /**
     * Chiave associata al filtro order.
     */
    const val FILTERS_ORDER_KEY = "filters_order_key"

    /**
     * Chiave associata al filtro family.
     */
    const val FILTERS_FAMILY_KEY = "filters_family_key"

    /**
     * Chiave associata al filtro genus.
     */
    const val FILTERS_GENUS_KEY = "filters_genus_key"

    /**
     * Chiave associata al filtro species.
     */
    const val FILTERS_SPECIES_KEY = "filters_species_key"

    /**
     * Nome del database utilizzato per memorizzare i fiori preferiti scelti da ogni singolo utente.
     */
    const val DICTIONARY_FAVOURITES = "favourites"

    /**
     * Nome dell'array sul Database utilizzato per memorizzare i fiori preferiti scelti da ogni singolo utente.
     */
    const val DICTIONARY_FLOWERS_ARRAY = "flowers"

    /**
     * Annotazione per escludere Parcelable dagli UnitTest delle classi DictionaryFlower e Taxonomy.
     */
    annotation class Generated

    // *** fine regione - dizionario


    // *** inizio regione - photo

    /**
     * Nome del file JSON contenente la lista delle classi del dataset Oxford 102.
     */
    const val OXFORD_FLOWERS_JSON_CLASS_LIST = "cat_to_name.json"

    /**
     * Stringa utilizzata per salvare il testo del primo risultato della classificazione.
     */
    const val FIRST_RESULT_TEXT = "first_result_text"

    /**
     * Stringa utilizzata per salvare la visibilità del primo risultato della classificazione.
     */
    const val FIRST_RESULT_VISIBILITY = "first_result_visibility"

    /**
     * Stringa utilizzata per salvare il testo della confidenza
     * del primo risultato della classificazione.
     */
    const val FIRST_CONFIDENCE_TEXT = "first_confidence_text"

    /**
     * Stringa utilizzata per salvare la visibilità della confidenza
     * del primo risultato della classificazione.
     */
    const val FIRST_CONFIDENCE_VISIBILITY = "first_confidence_visibility"

    /**
     * Stringa utilizzata per salvare il testo del secondo risultato della classificazione.
     */
    const val SECOND_RESULT_TEXT = "second_result_text"

    /**
     * Stringa utilizzata per salvare la visibilità del secondo risultato della classificazione.
     */
    const val SECOND_RESULT_VISIBILITY = "second_result_visibility"

    /**
     * Stringa utilizzata per salvare il testo della confidenza
     * del secondo risultato della classificazione.
     */
    const val SECOND_CONFIDENCE_TEXT = "second_confidence_text"

    /**
     * Stringa utilizzata per salvare la visibilità della confidenza
     * del secondo risultato della classificazione.
     */
    const val SECOND_CONFIDENCE_VISIBILITY = "second_confidence_visibility"

    /**
     * Stringa utilizzata per salvare il testo del terzo risultato della classificazione.
     */
    const val THIRD_RESULT_TEXT = "third_result_text"

    /**
     * Stringa utilizzata per salvare la visibilità del secondo risultato della classificazione.
     */
    const val THIRD_RESULT_VISIBILITY = "third_result_visibility"

    /**
     * Stringa utilizzata per salvare il testo della confidenza
     * del terzo risultato della classificazione.
     */
    const val THIRD_CONFIDENCE_TEXT = "third_confidence_text"

    /**
     * Stringa utilizzata per salvare la visibilità della confidenza
     * del terzo risultato della classificazione.
     */
    const val THIRD_CONFIDENCE_VISIBILITY = "third_confidence_visibility"

    /**
     * Dimensione da utilizzare per classificare le immagini.
     */
    const val IMAGE_SIZE = 224

    /**
     * Numero dei primi risultati ottenuti dalla classificazione in ordine di confidenza.
     */
    const val NUM_TOP_SCORES = 3

    // *** fine regione - photo

}
