package com.example.floraleye.utils

/**
 * Classe per la gestione delle espressioni regolari usate in tutta l'app.
 */
object RegexManager {

    private val emailRegex: Regex = android.util.Patterns.EMAIL_ADDRESS.toRegex()

    private val passwordRegex: Regex =
        "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$".toRegex()

    private val searchDictionaryRegex: Regex =
        "^[a-zA-Z\\s]+\$".toRegex()

    /**
     * Metodo per verificare se la e-mail inserita è nel formato corretto.
     * @param email E-mail da verificare
     * @return true se la email è nel formato corretto, false altrimenti
     */
    fun isEmailValid(email: String): Boolean {
        return emailRegex matches email
    }

    /**
     * Metodo per verificare se la password rispetta i criteri di sicurezza minimi. In particolare,
     * deve contenere almeno 8 caratteri, una minuscola, una maiuscola, un numero ed un carattere
     * speciale.
     * @param password Password da verificare
     * @return true se la password rispetta i criteri di sicurezza, false altrimenti
     */
    fun isPasswordValid(password: String): Boolean {
        return passwordRegex matches password
    }

    /**
     * Metodo per verificare se il testo inserito nella funzionalità di ricerca del Dizionario
     * risulta essere nel formato corretto.
     * @param query Testo da verificare
     * @return Boolean che indica se il testo inserito nella ricerca del Dizionario rispetta il formato corretto.
     */
    fun isDictionarySearchValid(query: String): Boolean {
        return searchDictionaryRegex matches query
    }
}
