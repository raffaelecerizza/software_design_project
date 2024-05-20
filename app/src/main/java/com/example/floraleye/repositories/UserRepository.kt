package com.example.floraleye.repositories

import android.app.Application
import android.util.Log
import androidx.core.content.ContextCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.floraleye.utils.Constants
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.EmailAuthProvider
import com.example.floraleye.utils.OnboardUtils.handleAuthResponse
import com.example.floraleye.utils.OnboardUtils.checkFirebaseException


/**
 * Repository relativo all'utente. Utilizzato principalmente per gestire onboard, registrazione
 * ed eventuali modifiche dei dati salvati.
 */
class UserRepository(application: Application){

    /**
     * Indirizzo E-Mail dell'utente correntemente autenticato.
     */
    val currentUserMail: String?
        get() = auth.currentUser?.email

    private val application: Application

    private val auth: FirebaseAuth

    private val userOnboarded: MutableLiveData<Boolean> = MutableLiveData()

    private val authStateListener: FirebaseAuth.AuthStateListener =
        FirebaseAuth.AuthStateListener { auth ->
            Log.d(TAG, "authStateListener: current user is not null: "
                    + (auth.currentUser != null))
            if (auth.currentUser != null) {
                val isMailVerified = auth.currentUser?.isEmailVerified ?: false
                Log.d(TAG, "authStateListener: is mail verified: $isMailVerified")
                if (isMailVerified) {
                    userOnboarded.postValue(true)
                } else {
                    userOnboarded.postValue(false)
                }
            } else {
                userOnboarded.postValue(false)
            }
        }

    companion object {

        private val TAG: String = UserRepository::class.java.simpleName
    }

    init {
        this.application = application
        auth = FirebaseAuth.getInstance()
        auth.addAuthStateListener(authStateListener)
        auth.currentUser?.reload() ?: Log.d(TAG, "UserRepository init reload: " +
                "current user is null.")
    }

    /**
     * Metodo utilizzato per eseguire la registrazione dell'utente tramite i servizi di
     * Firebase Authenticator.
     * @param mail Mail specificata dall'utente
     * @param password Password specifica dall'utente
     * @return LiveData da osservare per conoscere il risultato del processo di registrazione.
     */
    fun registerWithMailAndPassword(
        mail: String,
        password: String
    ): LiveData<Int> {
        val result = MutableLiveData<Int>()

        auth.createUserWithEmailAndPassword(
            mail,
            password
        ).addOnCompleteListener(
            ContextCompat.getMainExecutor(application)
        ) { task ->
            if (task.isSuccessful) {
                val user = auth.currentUser
                if (user != null) {
                    sendVerificationMail(result)
                    logoutCurrentUser()
                } else result.postValue(Constants.FIREBASE_AUTH_GENERIC_ERROR)
            } else {
                result.postValue(checkFirebaseException(task.exception))
            }
        }

        return result
    }

    /**
     * Metodo utilizzato per eseguire il login dell'utente tramite mail e password.
     * @param mail indirizzo e-mail specificato dall'utente in fase di registrazione
     * @param password password associata all'indirizzo e-mail
     * @return LiveData osservabile per conoscere l'esito dell'operazione
     */
    fun loginWithMailAndPassword(mail: String, password: String): LiveData<Int> {
        val result = MutableLiveData<Int>()

        auth.signInWithEmailAndPassword(
            mail,
            password
        ).addOnCompleteListener(
            ContextCompat.getMainExecutor(application)
        ) { task ->
            if (task.isSuccessful) {
                val user = auth.currentUser
                if (user != null) {
                    result.postValue(when (user.isEmailVerified) {
                        true -> Constants.FIREBASE_AUTH_OK
                        false -> Constants.FIREBASE_AUTH_MAIL_NOT_VERIFIED
                    })
                } else {
                    result.postValue(Constants.FIREBASE_AUTH_GENERIC_ERROR)
                }
            } else {
                result.postValue(checkFirebaseException(task.exception))
            }
        }

        return result
    }

    /**
     * Metodo utilizzato per inviare all'indirizzo e-mail specificato una mail per il rispristino
     * della password.
     * @param mail indirizzo e-mail a cui inviare la mail di ripristino della password
     * @return LiveData da osservare per conoscere l'esito dell'operazione di invio della mail di
     * ripristino della password
     */
    fun sendResetPasswordMail(mail: String): LiveData<Int> {
        val result = MutableLiveData<Int>()

        auth.sendPasswordResetEmail(
            mail
        ).addOnCompleteListener(
            ContextCompat.getMainExecutor(application)
        ) { task ->
            handleAuthResponse(
                task = task,
                liveData = result,
                callerName = "sendResetPasswordMail",
                okCode = Constants.FIREBASE_AUTH_SEND_RESET_PASSWORD_OK
            )
        }

        return result
    }

    /**
     * Metodo utilizzato per eliminare l'utente correntemente loggato. Per motivi di sicurezza,
     * Firebase permette l'eliminazione solo dell'utente attualmente loggato.
     * @return LiveData da osservare per venire a conoscenza dell'esito dell'operazione
     */
    fun deleteCurrentUser() : LiveData<Int> {
        val result = MutableLiveData<Int>()

        val user = auth.currentUser

        if (user != null) {
            user.delete().addOnCompleteListener { task ->
                handleAuthResponse(task, result, "deleteCurrentUser")
            }
        } else {
            Log.d(TAG, "deleteCurrentUser: current user is null.")
            result.value = Constants.FIREBASE_AUTH_GENERIC_ERROR
        }

        return result
    }

    /**
     * Metodo per eseguire il logout dell'utente. In particolare, questo metodo elimina lo
     * user token salvato, permettendo all'utente in seguito di rieseguire onboard o registrazione.
     */
    fun logoutCurrentUser() {
        auth.signOut()
    }

    /**
     * Metodo utilizzabile per capire se l'utente ha già eseguito l'onboard in
     * precedenza, oppure deve ancora farlo.
     * @return LiveData da osservare per sapere se abbiamo un utente già loggato oppure no
     */
    fun userOnboarded(): LiveData<Boolean> {
        return userOnboarded
    }

    /**
     * Metodo per l'invio della mail di verifica dell'indirizzo e-mail.
     * @param result LiveData da passare e osservare per conoscere il risultato
     */
    fun sendVerificationMail(result: MutableLiveData<Int>) {
        val user = auth.currentUser
        if (user != null) {
            user.sendEmailVerification().addOnCompleteListener { task ->
                handleAuthResponse(task, result, "sendVerificationMail")
            }
        } else {
            Log.d(TAG, "sendVerificationMail: current user is null.")
            result.value = Constants.FIREBASE_AUTH_GENERIC_ERROR
        }
    }

    /**
     * Metodo utilizzato per autenticare o riautenticare l'utente attualmente loggato.
     * @param password password associata all'account dell utente corrente
     * @return LiveData da osservare per conoscere il risultato dell'operazione
     */
    fun authenticateCurrentUser(password: String): LiveData<Int> {
        val result = MutableLiveData<Int>()

        val user = auth.currentUser

        if (user != null) {
            val credential = user.email?.let { email ->
                EmailAuthProvider.getCredential(email, password)
            }
            if (credential != null) {
                user.reauthenticate(credential).addOnCompleteListener { task ->
                    handleAuthResponse(task, result, "authenticateCurrentUser")
                }
            } else {
                Log.d(TAG, "authenticateCurrentUser: current user email is null.")
                result.value = Constants.FIREBASE_AUTH_GENERIC_ERROR
            }
        } else {
            Log.d(TAG, "authenticateCurrentUser: current user is null.")
            result.value = Constants.FIREBASE_AUTH_GENERIC_ERROR
        }

        return result
    }

    /**
     * Metodo utilizzato per aggiornare la password dell'utente attualmente autenticato. Prima di
     * utilizzare questo metodo è bene riautenticare l'utente, per motivi di sicurezza.
     * @param newPassword nuova password
     * @return LiveData da osservare per venire a conoscenza del risulta dell'operazione
     */
    fun updatePassword(newPassword: String): LiveData<Int> {
        val result = MutableLiveData<Int>()

        val user = auth.currentUser

        if (user != null) {
            user.updatePassword(newPassword).addOnCompleteListener { task ->
                handleAuthResponse(task, result, "updatePassword")
            }
        } else {
            Log.d(TAG, "updatePassword: user is null.")
            result.value = Constants.FIREBASE_AUTH_GENERIC_ERROR
        }

        return result
    }
}
