package com.example.floraleye.utils

import android.app.Application
import android.content.SharedPreferences
import java.io.IOException
import java.security.GeneralSecurityException
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey

/**
 * Classe per la gestione dei file SharedPreferences in modalità criptata con AES256.
 */
class EncryptedSharedPreferencesManager(
    private val application: Application,
    private val sharedPreferencesFileName: String) {

    /**
     * Scrittura di una stringa utilizzando EncryptedSharedPreferences. Sia chiave che valore
     * saranno criptati.
     * @param key Chiave associata al valore
     * @param value Stringa da scrivere
     * @throws GeneralSecurityException
     * @throws IOException
     */
    @Throws(GeneralSecurityException::class, IOException::class)
    fun writeSecretString(
        key: String,
        value: String
    ) {
        val mainKey: MasterKey = MasterKey.Builder(application)
            .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
            .build()

        val sharedPreferences: SharedPreferences = EncryptedSharedPreferences.create(
            application,
            sharedPreferencesFileName,
            mainKey,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )

        val editor = sharedPreferences.edit()
        editor.putString(key, value)
        editor.apply()
    }

    /**
     * Lettura di una stringa criptato utilizzando EncryptedSharedPreferences.
     * @param key La chiave associata al valore che si vuole leggere
     * @param defaultValue Stringa di default, da considerare se la chiave cercata non è presente
     * @return la stringa decriptata
     * @throws GeneralSecurityException
     * @throws IOException
     */
    @Throws(GeneralSecurityException::class, IOException::class)
    fun readSecretString(
        key: String,
        defaultValue: String?
    ): String {
        val mainKey: MasterKey = MasterKey.Builder(application)
            .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
            .build()
        val sharedPreferences: SharedPreferences = EncryptedSharedPreferences.create(
            application,
            sharedPreferencesFileName,
            mainKey,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
        return sharedPreferences.getString(key, defaultValue).toString()
    }

    /**
     * Eliminazione di un valore utilizzando EncryptedSharedPreferences.
     * @param key Chiave associata al valore da eliminare
     * @throws GeneralSecurityException
     * @throws IOException
     */
    @Throws(GeneralSecurityException::class, IOException::class)
    fun deleteSecretData(
        key: String
    ) {
        val mainKey: MasterKey = MasterKey.Builder(application)
            .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
            .build()

        val sharedPreferences: SharedPreferences = EncryptedSharedPreferences.create(
            application,
            sharedPreferencesFileName,
            mainKey,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )

        val editor = sharedPreferences.edit()
        editor.remove(key)
        editor.apply()
    }
}
