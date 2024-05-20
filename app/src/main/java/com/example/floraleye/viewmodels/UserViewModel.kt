package com.example.floraleye.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.floraleye.repositories.UserRepository

/**
 * View Model per la gestione delle funzionalità utente, come onboard e registrazione.
 */
class UserViewModel(
    application: Application,
    private val repository: UserRepository
) : AndroidViewModel(application) {

    /**
     * Variabile utilizzabile per sapere se l'utente ha già eseguito l'onboard in precedenza,
     * oppure deve ancora farlo.
     */
    val userOnboarded: LiveData<Boolean>
        get() = repository.userOnboarded()

    /**
     * Indirizzo E-Mail dell'utente correntemente autenticato.
     */
    val userMail: String?
        get() = repository.currentUserMail

    /**
     * Metodo utilizzabile per eseguire la registrazione.
     * @param mail Mail specificata dall'utente
     * @param password Password specificata dall'utente
     * @return LiveData da osservare per conoscere il risultato del processo di registrazione.
     */
    fun registerWithMailAndPassword(
        mail: String,
        password: String
    ): LiveData<Int> {
        return repository.registerWithMailAndPassword(mail, password)
    }

    /**
     * Metodo per eseguire il login dell'utente tramite mail e password specificate in fase di
     * registrazione.
     * @param mail indirizzo e-mail specificato in fase di registrazione
     * @param password password associata all'indirizzo e-mail
     * @return LiveData da osservare per conoscere il risultato dell'operazione
     */
    fun loginWithMailAndPassword(mail: String, password: String): LiveData<Int> {
        return repository.loginWithMailAndPassword(mail, password)
    }

    /**
     * Metodo per inviare una mail per il reset della password all'indirizzo e-mail specificato.
     * @param mail indirizzo e-mail a cui inviare la mail di ripristino della password
     * @return LiveData da osservare per conoscere il risultato dell'operazione
     */
    fun sendResetPasswordMail(mail: String): LiveData<Int> {
        return repository.sendResetPasswordMail(mail)
    }

    /**
     * Metodo per inviare una mail di verifica dell'indirizzo e-mail alla casella di posta
     * dell'utente correntemente loggato.
     * @return LiveData da osservare per venire a conoscenza del risultato
     */
    fun sendVerificationEmail(): LiveData<Int> {
        val result = MutableLiveData<Int>()
        repository.sendVerificationMail(result)
        return result
    }

    /**
     * Metodo per eseguire il logout.
     */
    fun logout() {
        repository.logoutCurrentUser()
    }

    /**
     * Metodo per eliminare l'account dell'utente correntemente loggato.
     * @return LiveData da osservare per conoscere il risultato dell'operazione
     */
    fun deleteUser() : LiveData<Int> {
        return repository.deleteCurrentUser()
    }

    /**
     * Metodo per autenticare o riautenticare l'utente corrente.
     * @param password Password associata all'acccount
     * @return LiveData da osservare per venire a conoscenza del risultato
     */
    fun authenticateCurrentUser(password: String): LiveData<Int> {
        return repository.authenticateCurrentUser(password)
    }

    /**
     * Metodo per aggiornare la password associata all'account dell'utente correntemente
     * autenticato.
     * @param newPassword Nuova password per l'account
     * @return LiveData da osservare per venire a conoscenza del risultato
     */
    fun updatePassword(newPassword: String): LiveData<Int> {
        return repository.updatePassword(newPassword)
    }
}
