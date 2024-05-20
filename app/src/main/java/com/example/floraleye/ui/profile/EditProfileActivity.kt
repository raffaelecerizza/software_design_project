package com.example.floraleye.ui.profile

import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.floraleye.R
import com.example.floraleye.databinding.ActivityEditProfileBinding
import com.example.floraleye.utils.GeneralUtils
import com.example.floraleye.utils.OnboardUtils
import com.example.floraleye.viewmodels.UserViewModel
import com.example.floraleye.viewmodels.UserViewModelFactory


/**
 * Activity per la gestione della modifica del Profilo.
 */
class EditProfileActivity : AppCompatActivity() {

    private lateinit var mBinding: ActivityEditProfileBinding

    private lateinit var userViewModel: UserViewModel

    companion object {

        private val TAG: String = EditProfileActivity::class.java.simpleName
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mBinding = ActivityEditProfileBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        userViewModel = ViewModelProvider(
            this,
            UserViewModelFactory(application)
        )[UserViewModel::class.java]

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
            ?: Log.d(TAG, "onCreate: action bar is null.")
        supportActionBar?.setTitle(R.string.str_edit_profile)
            ?: Log.d(TAG, "onCreate: action bar is null.")

        mBinding.editButton.setOnClickListener {
            onChangePasswordButtonClicked()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                finish()
                true
            }
            else -> false
        }
    }

    private fun onChangePasswordButtonClicked() {
        val currentPassword: String = mBinding.inputEditTextOldPassword.text.toString()
        val newPassword: String = mBinding.inputEditTextNewPassword.text.toString()
        val repeatNewPassword: String = mBinding.inputEditTextRepeatNewPassword.text.toString()

        val currentPasswordError = GeneralUtils.getPasswordErrorMessage(currentPassword,
            null, resources)
        val newPasswordError = GeneralUtils.getPasswordErrorMessage(newPassword,
            repeatNewPassword, resources)

        mBinding.inputLayoutOldPassword.error = null
        mBinding.inputLayoutNewPassword.error = null

        if (currentPasswordError != null) {
            mBinding.inputLayoutOldPassword.error = currentPasswordError
            if (currentPasswordError == resources.getString(R.string.str_password_weak_error)) {
                GeneralUtils.showPasswordSuggestionMessage(this)
            }
        } else if (newPasswordError != null) {
            mBinding.inputLayoutNewPassword.error = newPasswordError
            if (newPasswordError == resources.getString(R.string.str_password_weak_error)) {
                GeneralUtils.showPasswordSuggestionMessage(this)
            }
        } else if (currentPassword == newPassword) {
            GeneralUtils.showGenericMessageOnView(
                message = resources.getString(R.string.str_change_password_equal),
                context = this,
                root = mBinding.root,
                anchorView = mBinding.editButton
            )
        }

        if (currentPasswordError != null ||
            newPasswordError != null ||
            currentPassword == newPassword) {
            return
        }

        mBinding.isLoading = true

        userViewModel.authenticateCurrentUser(currentPassword).observe(this) { result ->
            val error = OnboardUtils.getOnboardResultErrorMessage(result, resources)
            Log.d(TAG, "onChangePasswordButtonClicked: errorMessage authenticate -> $error")

            if (error != null) {
                GeneralUtils.showGenericMessageOnView(
                    message = error,
                    context = this,
                    root = mBinding.root,
                    anchorView = mBinding.editButton
                )
                mBinding.isLoading = false
                return@observe
            }

            updateCurrentUserPassword(newPassword)
        }
    }

    private fun updateCurrentUserPassword(newPassword: String) {
        userViewModel.updatePassword(newPassword).observe(this) { resultCode ->
            val errorMsg = OnboardUtils.getOnboardResultErrorMessage(resultCode, resources)
            Log.d(TAG, "onChangePasswordButtonClicked: errorMessage update password" +
                    " -> $errorMsg")

            mBinding.isLoading = false

            if (errorMsg != null) {
                GeneralUtils.showGenericMessageOnView(
                    message = errorMsg,
                    context = this,
                    root = mBinding.root,
                    anchorView = mBinding.editButton
                )
            } else {
                userViewModel.logout()
                finish()
            }
        }
    }
}
