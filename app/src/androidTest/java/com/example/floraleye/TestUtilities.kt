package com.example.floraleye

import android.app.Activity
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.text.SpannableString
import android.text.style.ClickableSpan
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.annotation.StringRes
import androidx.test.espresso.NoMatchingViewException
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.ext.junit.rules.ActivityScenarioRule
import com.google.android.material.textfield.TextInputLayout
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.Matchers
import org.hamcrest.TypeSafeMatcher

/**
 * Classe contenente metodo di utility per eseguire i test.
 */
object TestUtilities {

    private val TAG = TestUtilities.javaClass.simpleName

    private const val CLICKABLE_SPAN_DESCRIPTION = "Clicking on a ClickableSpan"

    /**
     * Metodo per ottenere una stringa dalla risorse.
     * @param T activity della regola corrente
     * @param resId id della stringa
     * @param activityRule regola dello scenario corrente
     * @return stringa associata all'id richiesto, se esiste, una stringa vuota altrimenti
     */
    fun <T : Activity> getString(@StringRes resId: Int,
                                 activityRule: ActivityScenarioRule<T>): String {
        var str = ""
        activityRule.scenario.onActivity { activity ->
            str = activity.getString(resId)
        }
        return str
    }

    /**
     * Metodo utilizzabile per verificare se un edit text presenta un certo errore.
     * @param expectedErrorText messaggio di errore che ci si aspetta
     */
    fun hasTextInputLayoutHintText(expectedErrorText: String): Matcher<View> = object :
        TypeSafeMatcher<View>() {

        override fun matchesSafely(item: View?): Boolean {
            if (item is TextInputLayout) {
                val error = item.error
                if (error != null) {
                    val hint = error.toString()
                    return expectedErrorText == hint
                }
            }
            return false
        }

        override fun describeTo(description: Description?) {
            return
        }
    }

    /**
     * Metodo per cliccare su un bottone presente all'interno di una text view.
     * @param textToClick testo da cliccare
     */
    fun clickClickableSpan(textToClick: CharSequence): ViewAction {
        return object : ViewAction {
            override fun getConstraints(): Matcher<View> {
                return Matchers.instanceOf(TextView::class.java)
            }

            override fun getDescription(): String {
                return CLICKABLE_SPAN_DESCRIPTION
            }

            override fun perform(uiController: UiController?, view: View) {
                val textView = view as TextView
                val spannableString = textView.text as SpannableString
                if (spannableString.isEmpty()) {
                    // TextView vuota, non può essere fatto alcun controllo
                    throw NoMatchingViewException.Builder()
                        .includeViewHierarchy(true)
                        .withRootView(textView)
                        .build()
                }

                // Ottenimento links dentro la TextView e controllo se si trova la stringa cercata
                val spans = spannableString.getSpans(
                    0, spannableString.length,
                    ClickableSpan::class.java
                )
                if (spans.isNotEmpty()) {
                    var spanCandidate: ClickableSpan?
                    for (span in spans) {
                        spanCandidate = span
                        val start = spannableString.getSpanStart(spanCandidate)
                        val end = spannableString.getSpanEnd(spanCandidate)
                        val sequence = spannableString.subSequence(start, end)
                        if (textToClick.toString() == sequence.toString()) {
                            span.onClick(textView)
                            return
                        }
                    }
                }
                throw NoMatchingViewException.Builder()
                    .includeViewHierarchy(true)
                    .withRootView(textView)
                    .build()
            }
        }
    }

    /**
     * Metodo per eseguire un controllo ciclico.
     * @param totalCheck numero totale di controlli
     * @param timeBetweenCheck tempo che intercorre tra ogni controllo (in millisecondi)
     * @param function funzione di controllo
     * @return true se il test si conclude con successo, false altrimenti
     */
    fun makeCyclicViewTest(
        totalCheck: Int = 10,
        timeBetweenCheck: Long = 1000,
        function: () -> Unit): Boolean {
        var isSuccessful = false
        run repeatedBlock@ {
            repeat(totalCheck) {
                try {
                    function()
                    isSuccessful = true
                    return@repeatedBlock
                } catch (exception: Throwable) {
                    Log.d(TAG, "makeCyclicViewTest: ${exception.message}")
                    Thread.sleep(timeBetweenCheck)
                }
            }
        }
        return isSuccessful
    }

    /**
     * Metodo utilizzato per creare una immagine Bitmap delle dimensioni volute e di colore unico.
     * @param width ampiezza
     * @param height altezza
     * @param color colore
     * @return immagine come Bitmap
     */
    fun createImage(width: Int, height: Int, color: Int): Bitmap? {
        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        val paint = Paint()
        paint.color = color
        canvas.drawRect(0f, 0f, width.toFloat(), height.toFloat(), paint)
        return bitmap
    }

}
