package com.example.floraleye.utils

import android.content.ContentValues.TAG
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import com.example.floraleye.databinding.FragmentPhotoBinding
import com.example.floraleye.ml.ModelGeneral
import org.json.JSONException
import org.json.JSONObject
import org.tensorflow.lite.support.image.TensorImage
import org.tensorflow.lite.support.label.Category
import java.io.IOException
import java.nio.charset.StandardCharsets
import java.util.*
import kotlin.collections.ArrayList

/**
 * Classe contenente metodi di utilità per quanto concerne il riconoscimento automatico
 * dei fiori in immagini.
 */
object PhotoUtils {

    /**
     * Metodo per rendere invisibili le stringhe di testo del PhotoFragment.
     * @param binding ViewBinding del PhotoFragment.
     */
    fun setAllTextsInvisible(binding: FragmentPhotoBinding) {
        binding.textViewFirstResult.visibility = View.INVISIBLE
        binding.textViewFirstConfidence.visibility = View.INVISIBLE
        binding.textViewSecondResult.visibility = View.INVISIBLE
        binding.textViewSecondConfidence.visibility = View.INVISIBLE
        binding.textViewThirdResult.visibility = View.INVISIBLE
        binding.textViewThirdConfidence.visibility = View.INVISIBLE
    }

    /**
     * Metodo per rendere visibili le stringhe di testo del PhotoFragment.
     * @param binding ViewBinding del PhotoFragment.
     */
    fun setAllTextsVisible(binding: FragmentPhotoBinding) {
        binding.textViewFirstResult.visibility = View.VISIBLE
        binding.textViewFirstConfidence.visibility = View.VISIBLE
        binding.textViewSecondResult.visibility = View.VISIBLE
        binding.textViewSecondConfidence.visibility = View.VISIBLE
        binding.textViewThirdResult.visibility = View.VISIBLE
        binding.textViewThirdConfidence.visibility = View.VISIBLE
    }

    /**
     * Metodo per impostare la visibilità delle stringhe di testo del PhotoFragment nel caso
     * in cui l'immagine da identificare non contenga un fiore.
     * @param binding ViewBinding del PhotoFragment.
     */
    fun setTextsNotFlower(binding: FragmentPhotoBinding) {
        binding.textViewFirstResult.visibility = View.VISIBLE
        binding.textViewFirstConfidence.visibility = View.INVISIBLE
        binding.textViewSecondResult.visibility = View.INVISIBLE
        binding.textViewSecondConfidence.visibility = View.INVISIBLE
        binding.textViewThirdResult.visibility = View.INVISIBLE
        binding.textViewThirdConfidence.visibility = View.INVISIBLE
    }

    /**
     * Metodo per convertire un numero in virgola mobile in una stringa rappresentante
     * una percentuale.
     * @param number Numero che si vuole convertire in percentuale.
     * @return Stringa rappresentante la percentuale.
     */
    fun convertToPercentage(number: Float): String {
        val percentage = number * Constants.PERCENTAGE
        return "%.2f%%".format(percentage)
    }

    /**
     * Metodo per salvare lo stato del PhotoFragment al cambio di configurazione.
     * @param binding ViewBinding del PhotoFragment.
     * @param outState Bundle in cui salvare i dati.
     */
    fun saveInstanceState(binding: FragmentPhotoBinding, outState: Bundle) {
        outState.putString(Constants.FIRST_RESULT_TEXT,
            binding.textViewFirstResult.text.toString())
        outState.putInt(Constants.FIRST_RESULT_VISIBILITY,
            binding.textViewFirstResult.visibility)
        outState.putString(Constants.FIRST_CONFIDENCE_TEXT,
            binding.textViewFirstConfidence.text.toString())
        outState.putInt(Constants.FIRST_CONFIDENCE_VISIBILITY,
            binding.textViewFirstConfidence.visibility)

        outState.putString(Constants.SECOND_RESULT_TEXT,
            binding.textViewSecondResult.text.toString())
        outState.putInt(Constants.SECOND_RESULT_VISIBILITY,
            binding.textViewSecondResult.visibility)
        outState.putString(Constants.SECOND_CONFIDENCE_TEXT,
            binding.textViewSecondConfidence.text.toString())
        outState.putInt(Constants.SECOND_CONFIDENCE_VISIBILITY,
            binding.textViewSecondConfidence.visibility)

        outState.putString(Constants.THIRD_RESULT_TEXT,
            binding.textViewThirdResult.text.toString())
        outState.putInt(Constants.THIRD_RESULT_VISIBILITY,
            binding.textViewThirdResult.visibility)
        outState.putString(Constants.THIRD_CONFIDENCE_TEXT,
            binding.textViewThirdConfidence.text.toString())
        outState.putInt(Constants.THIRD_CONFIDENCE_VISIBILITY,
            binding.textViewThirdConfidence.visibility)
    }

    /**
     * Metodo per ripristinare lo stato del PhotoFragment dopo il cambio di configurazione.
     * @param binding ViewBinding del PhotoFragment.
     * @param savedInstanceState Bundle in cui sono salvati i dati.
     */
    fun restoreInstanceState(binding: FragmentPhotoBinding, savedInstanceState: Bundle?) {
        if (savedInstanceState != null) {
            binding.textViewFirstResult.text =
                savedInstanceState.getString(Constants.FIRST_RESULT_TEXT)
            binding.textViewFirstResult.visibility =
                savedInstanceState.getInt(Constants.FIRST_RESULT_VISIBILITY)
            binding.textViewFirstConfidence.text =
                savedInstanceState.getString(Constants.FIRST_CONFIDENCE_TEXT)
            binding.textViewFirstConfidence.visibility =
                savedInstanceState.getInt(Constants.FIRST_CONFIDENCE_VISIBILITY)

            binding.textViewSecondResult.text =
                savedInstanceState.getString(Constants.SECOND_RESULT_TEXT)
            binding.textViewSecondResult.visibility =
                savedInstanceState.getInt(Constants.SECOND_RESULT_VISIBILITY)
            binding.textViewSecondConfidence.text =
                savedInstanceState.getString(Constants.SECOND_CONFIDENCE_TEXT)
            binding.textViewSecondConfidence.visibility =
                savedInstanceState.getInt(Constants.SECOND_CONFIDENCE_VISIBILITY)

            binding.textViewThirdResult.text =
                savedInstanceState.getString(Constants.THIRD_RESULT_TEXT)
            binding.textViewThirdResult.visibility =
                savedInstanceState.getInt(Constants.THIRD_RESULT_VISIBILITY)
            binding.textViewThirdConfidence.text =
                savedInstanceState.getString(Constants.THIRD_CONFIDENCE_TEXT)
            binding.textViewThirdConfidence.visibility =
                savedInstanceState.getInt(Constants.THIRD_CONFIDENCE_VISIBILITY)
        }
    }

    /**
     * Metodo per determinare se un'immagine rappresenta o no un fiore.
     * @param tensorImage Immagine considerata.
     * @param modelGeneral Modello di Machin Learning utilizzato per classificare l'immagine.
     * @return true se l'immagine rappresenta un fiore, false altrimenti.
     */
    fun isImageFlower(tensorImage: TensorImage, modelGeneral: ModelGeneral): Boolean {
        val isFlower: Boolean

        val outputs = modelGeneral.process(tensorImage)
        val scores: List<Category> = outputs.scoreAsCategoryList

        // Ordina in ordine decrescente.
        val sortedScores = scores.sortedWith(compareByDescending { it.score })
        val category = sortedScores[0]

        // Verifica se l'immagine rappresenta un fiore in base all'etichetta predetta.
        val predictedClass = category.label.toInt()

        isFlower = predictedClass == 0

        // Rilascia le risorse del modello.
        modelGeneral.close()

        return isFlower
    }

    /**
     * Metodo utilizzato per ottenere la lista dei nomi dei fiori del dataset Oxford 102.
     * @param context contesto di riferimento.
     * @return la lista dei nomi dei fiori del dataset Oxford 102.
     */
    fun getFlowerNames(context: Context): List<String> {
        val flowerNames = ArrayList<String>()

        try {
            val json = loadJSONFromAsset(context, Constants.OXFORD_FLOWERS_JSON_CLASS_LIST)
            val jsonObject = json?.let { JSONObject(it) }

            // Recupera le chiavi (numeri) dall'oggetto JSON.
            val keys: MutableList<String> = ArrayList()
            val jsonArray = jsonObject?.names()
            if (jsonArray != null) {
                for (i in 0 until jsonArray.length()) {
                    keys.add(jsonArray.getString(i))
                }
            }

            // Ordina le chiavi in ordine crescente.
            keys.sortWith { key1, key2 ->
                val num1 = key1.toInt()
                val num2 = key2.toInt()
                num1.compareTo(num2)
            }

            // Aggiunge i nomi dei fiori alla lista in base alle chiavi.
            for (key in keys) {
                val flowerName = jsonObject?.getString(key)
                if (flowerName != null) {
                    val capitalizedFlowerName = flowerName.lowercase(Locale.ROOT)
                        .replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.ROOT)
                        else it.toString() }
                    flowerNames.add(capitalizedFlowerName)
                }
            }

        } catch (e: JSONException) {
            Log.d(TAG, e.toString())
        }

        return flowerNames
    }

    /**
     * Metodo per leggere il file JSON contenente la lista dei nomi dei fiori
     * del dataset Oxford 102.
     * @param context contesto di riferimento.
     * @param fileName nome del file JSON da leggere.
     * @return stringa contenente la lista lista dei nomi dei fiori del dataset Oxford 102.
     */
    private fun loadJSONFromAsset(context: Context, fileName: String): String? {
        var json: String? = null
        try {
            val inputStream = context.assets.open(fileName)
            val size = inputStream.available()
            val buffer = ByteArray(size)
            inputStream.read(buffer)
            inputStream.close()
            json = String(buffer, StandardCharsets.UTF_8)
        } catch (e: IOException) {
            Log.d(TAG, e.toString())
        }
        return json
    }

}
