package com.example.floraleye.utils

import android.content.res.Resources
import android.text.SpannableString
import android.text.Spanned
import android.text.TextPaint
import android.text.style.ClickableSpan
import android.util.Log
import android.view.View
import androidx.lifecycle.MutableLiveData
import com.example.floraleye.R
import com.example.floraleye.repositories.UserRepository
import com.google.android.gms.tasks.Task
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.FirebaseTooManyRequestsException
import java.util.Locale
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseAuthRecentLoginRequiredException


/**
 * Classe contenente metodo di utilità per quanto concerne l'Onboard dell'utente.
 */
object OnboardUtils {

    private val TAG = UserRepository::class.java.simpleName

    private const val SWITCH_ONBOARD_VIEW_TEXT_FORMAT = "%s %s"

    /**
     * Metodo per eseguire il setup del testo con bottone per passare la pagina di onboard a pagina
     * di registrazione, e viceversa.
     * @param textValue Testo da mostrare
     * @param buttonValue Testo del bottone, inserito in coda a textValue
     * @param onButtonClicked Funzione da chiamare quando si clicca il bottone
     */
    fun setUpSwitchTextWithButton(textValue: String,
                                  buttonValue: String,
                                  onButtonClicked: () -> Unit): SpannableString {
        val fullText = SpannableString(String.format(
            Locale.US, SWITCH_ONBOARD_VIEW_TEXT_FORMAT,
            textValue,
            buttonValue))
        val buttonStartPosition = fullText.indexOf(buttonValue, ignoreCase = true)
        val buttonEndPosition = buttonStartPosition + buttonValue.length

        val clickableSpan = object : ClickableSpan() {

            override fun updateDrawState(ds: TextPaint) {
                super.updateDrawState(ds)
                ds.isUnderlineText = true
            }

            override fun onClick(textView: View) {
                onButtonClicked()
            }
        }
        fullText.setSpan(clickableSpan, buttonStartPosition, buttonEndPosition,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        return fullText
    }

    /**
     * Metodo utilizzare per verificare la correttezza della mail.
     * @param email Mail inserita dall'utente
     * @param resources Risorse da cui ottenere le stringhe di errore
     * @return null se la mail è valida, il messaggio di errore da mostrare altrimenti
     */
    fun getEmailErrorMessage(email: String?,
                             resources: Resources): String? {
        var mailError: String? = null

        if (email.isNullOrEmpty()) {
            mailError = resources.getString(R.string.str_no_mail)
        } else if (!RegexManager.isEmailValid(email)) {
            mailError = resources.getString(R.string.str_credential_not_valid)
        }

        return mailError
    }

    /**
     * Metodo utilizzare per verificare il risultato del processo di onboard.
     * @param resultCode codice di stato ottenuto come output dell'onboard
     * @param resources Risorse da cui ottenere le stringhe di errore
     * @return null se il processo è terminato con successo, il messaggio di errore da mostrare
     * altrimenti
     */
    fun getOnboardResultErrorMessage(resultCode: Int, resources: Resources): String? {
        var errorMessage: String? = null

        when (resultCode) {
            Constants.FIREBASE_AUTH_OK -> {
                // il messagio di errore rimane null
            }
            Constants.FIREBASE_AUTH_MAIL_NOT_VERIFIED ->
                errorMessage = resources.getString(R.string.str_mail_not_verified)
            Constants.FIREBASE_AUTH_SEND_RESET_PASSWORD_OK ->
                errorMessage = resources.getString(R.string.str_send_reset_password_mail_ok)
            Constants.FIREBASE_SIGNUP_USER_EXISTS_ERROR ->
                errorMessage = resources.getString(R.string.str_mail_already_in_use_error)
            Constants.FIREBASE_AUTH_WEAK_PASSWORD_ERROR ->
                errorMessage = resources.getString(R.string.str_password_weak_error)
            Constants.FIREBASE_AUTH_CREDENTIAL_ERROR ->
                errorMessage = resources.getString(R.string.str_credential_not_valid)
            Constants.FIREBASE_AUTH_NETWORK_ERROR ->
                errorMessage = resources.getString(R.string.str_signup_network_error)
            Constants.FIREBASE_AUTH_INVALID_USER_ERROR ->
                errorMessage = resources.getString(R.string.str_invalid_user_error)
            Constants.FIREBASE_AUTH_TOO_MANY_REQUEST ->
                errorMessage = resources.getString(R.string.str_too_may_request)
            Constants.FIREBASE_AUTH_LOGIN_TOO_OLD ->
                errorMessage = resources.getString(R.string.str_login_too_old)
            Constants.FIREBASE_AUTH_GENERIC_ERROR ->
                errorMessage = resources.getString(R.string.str_generic_backend_error)
            else ->
                errorMessage = resources.getString(R.string.str_generic_error)
        }

        return errorMessage
    }

    /**
     * Metodo per gestire una risposta da Firebase.
     * @param task task generato dalla richiesta Firebase
     * @param liveData LiveData di riferimento
     * @param callerName nome della funzione chiamante
     * @param okCode codice di stato da ritornare nel caso di successo
     */
    @Suppress("ForbiddenVoid")
    fun handleAuthResponse(task: Task<Void>,
                           liveData: MutableLiveData<Int>,
                           callerName: String,
                           okCode: Int = Constants.FIREBASE_AUTH_OK) {
        if (task.isSuccessful) {
            Log.d(TAG, "$callerName: OK.")
            liveData.postValue(okCode)
        } else {
            Log.d(TAG, "$callerName: error -> ${task.exception?.message}.")
            liveData.postValue(checkFirebaseException(task.exception))
        }
    }

    /**
     * Metodo per verificare la tipologia di eccezione Firebase.
     * @param exception eccezione da verificare
     * @return codice di stato associato all'eccezione
     */
    fun checkFirebaseException(exception: Exception?): Int {
        return when (exception) {
            is FirebaseAuthUserCollisionException ->
                Constants.FIREBASE_SIGNUP_USER_EXISTS_ERROR
            is FirebaseAuthWeakPasswordException ->
                Constants.FIREBASE_AUTH_WEAK_PASSWORD_ERROR
            is FirebaseAuthInvalidCredentialsException ->
                Constants.FIREBASE_AUTH_CREDENTIAL_ERROR
            is FirebaseNetworkException ->
                Constants.FIREBASE_AUTH_NETWORK_ERROR
            is FirebaseAuthInvalidUserException ->
                Constants.FIREBASE_AUTH_INVALID_USER_ERROR
            is FirebaseAuthRecentLoginRequiredException ->
                Constants.FIREBASE_AUTH_LOGIN_TOO_OLD
            is FirebaseTooManyRequestsException ->
                Constants.FIREBASE_AUTH_TOO_MANY_REQUEST
            else ->
                Constants.FIREBASE_AUTH_GENERIC_ERROR
        }
    }
}
