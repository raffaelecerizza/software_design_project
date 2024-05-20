package com.example.floraleye.utils

import android.content.Context
import android.content.res.Resources
import android.view.View
import androidx.annotation.StringRes
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import com.example.floraleye.R
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar


/**
 * Funzioni di utilità di uso generale.
 */
object GeneralUtils {

    /**
     * Metodo per mostrare un messaggio generico.
     * @param message messaggio da mostrare
     * @param context contesto di riferimento
     * @param root root della view di riferimento
     * @param anchorView view a cui ancorare la snackbar
     */
    fun showGenericMessageOnView(message: String,
                                 context: Context,
                                 root: View,
                                 anchorView: View
    )  {
        if (message.length > Constants.MESSAGE_LENGTH_FOR_SNACKBAR_LIMIT) {
            MaterialAlertDialogBuilder(context)
                .setMessage(message)
                .setNeutralButton(R.string.str_ok) { dialog, _ ->
                    dialog.dismiss()
                }
                .setCancelable(true)
                .show()
        } else {
            Snackbar.make(root,
                message,
                Snackbar.LENGTH_LONG)
                .setAnchorView(anchorView)
                .show()
        }
    }

    /**
     * Metodo utilizzato per mostrare un messaggio di errore nel caso di password
     * specificata non sicura.
     * @param context Contesto di riferimento
     */
    fun showPasswordSuggestionMessage(context: Context) {
        MaterialAlertDialogBuilder(context)
            .setTitle(R.string.str_warning)
            .setMessage(R.string.str_password_not_valid)
            .setNeutralButton(R.string.str_ok) { dialog, _ ->
                dialog.dismiss()
            }
            .setCancelable(true)
            .show()
    }

    /**
     * Metodo utilizzato per verificare la correttezza della password.
     * @param password Password inserita dall'utente
     * @param repeatedPassword Ripetizione della password
     * @param resources Risorse da cui ottenere le stringhe di errore
     * @return null se la password è valida, il messaggio di errore da mostrare altrimenti
     */
    fun getPasswordErrorMessage(password: String?,
                                repeatedPassword: String?,
                                resources: Resources
    ): String? {
        var passwordError: String? = null

        if (password.isNullOrEmpty()) {
            passwordError = resources.getString(R.string.str_no_password)
        } else if (repeatedPassword != null && repeatedPassword.isEmpty()) {
            passwordError = resources.getString(R.string.str_no_repeat_password)
        } else if (!RegexManager.isPasswordValid(password)) {
            passwordError = resources.getString(R.string.str_password_weak_error)
        } else if (repeatedPassword != null && password != repeatedPassword) {
            passwordError = resources.getString(R.string.str_password_no_match)
        }

        return passwordError
    }

    /**
     * Metodo utilizzato per cercare un valore all'interno di un Map Array.
     * @param T Classe corrispondente agli oggetti nell'Array
     * @param firstKey Key da ricercare nell'Array
     * @param secondKey Key alternativa da ricercare nell'Array
     * @param array Map Array in cui effettuare la ricerca
     * @return valore corrispondente alla key ricercata, se non viene trovato allora viene restituita la firstKey
     */
     fun <T> searchValueInMapArray(
        firstKey: T,
        secondKey: T,
        array: Map<T,T>
     ): T {

        for (entry in array) {
            if (entry.key == firstKey ||
                entry.key == secondKey
            ){
                return entry.value
            }
        }
        return firstKey
    }

    /**
     * Metodo utilizzato per mostrare un messaggio tramite un alert dialog.
     * @param context contesto di riferimento
     * @param title titolo da inserire nel pop up
     * @param message messaggio da inserire nel pop up
     * @param onNegativeButtonClicked funzione da invocare se si clicca sul bottone negativo
     * @param onPositiveButtonClicked funzione da invocare se si clicca sul bottone positivo
     * @return AlertDialog creato
     */
    fun showGenericMessageInDialog(context: Context,
                                   @StringRes title: Int,
                                   @StringRes message: Int,
                                   onNegativeButtonClicked: () -> Unit,
                                   onPositiveButtonClicked: () -> Unit):
    AlertDialog = MaterialAlertDialogBuilder(context)
        .setTitle(title)
        .setMessage(message)
        .setNegativeButton(R.string.str_cancel) { _, _ ->
            onNegativeButtonClicked()
        }
        .setPositiveButton(R.string.str_ok) { _, _ ->
            onPositiveButtonClicked()
        }
        .setCancelable(false)
        .show()

    /**
     * Metodo utilizzato la creazione di un observer che termina dopo la prima esecuzione.
     * @param T Classe del LiveData
     * @param owner LifecycleOwner
     * @param observer observer
     */
    fun <T> LiveData<T>.observeOnce(owner: LifecycleOwner, observer: (T) -> Unit) {
        observe(owner, object: Observer<T> {
            override fun onChanged(value: T) {
                removeObserver(this)
                observer(value)
            }
        })
    }
}
