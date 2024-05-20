package com.example.floraleye.ui.onboard

import android.os.Bundle
import android.text.method.LinkMovementMethod
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.example.floraleye.R
import com.example.floraleye.databinding.FragmentOnboardBinding
import com.example.floraleye.utils.GeneralUtils
import com.example.floraleye.utils.OnboardUtils
import com.example.floraleye.viewmodels.UserViewModel
import com.example.floraleye.viewmodels.UserViewModelFactory
import com.google.android.material.dialog.MaterialAlertDialogBuilder


/**
 * Fragment per la gestione del onboard.
 */
class LoginFragment : Fragment() {

    private lateinit var mBinding: FragmentOnboardBinding

    private lateinit var userViewModel: UserViewModel

    companion object {

        private val TAG: String = LoginFragment::class.java.simpleName
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBinding = FragmentOnboardBinding.inflate(inflater, container, false)

        mBinding.isLogin = true

        userViewModel = ViewModelProvider(
            this,
            UserViewModelFactory(requireActivity().application)
        )[UserViewModel::class.java]

        val currentActivity = activity
        if (currentActivity is OnboardActivity) {
            mBinding.switchModeTextView.text = OnboardUtils
                .setUpSwitchTextWithButton(resources.getString(R.string.str_signup_text),
                    resources.getString(R.string.str_signup_button),
                    currentActivity::toggleDisplayedFragment)
            mBinding.switchModeTextView.movementMethod = LinkMovementMethod.getInstance()
        } else {
            Log.d(TAG, "onCreateView: impossible to access activity.")
        }

        mBinding.onboardButton.setOnClickListener {
            onLoginButtonClicked()
        }

        mBinding.forgotPasswordTextView.setOnClickListener {
            onSendResetPasswordMail()
        }

        userViewModel.logout()

        return mBinding.root
    }

    private fun onSendResetPasswordMail() {
        val mail: String = mBinding.inputEditTextMail.text.toString()

        val emailError = OnboardUtils.getEmailErrorMessage(mail, resources)

        mBinding.inputLayoutMail.error = null

        if (emailError != null) {
            mBinding.inputLayoutMail.error = emailError
            return
        }

        mBinding.forgotPasswordTextView.isClickable = false
        mBinding.onboardProgressBar.visibility = View.VISIBLE

        userViewModel.sendResetPasswordMail(mail).observe(viewLifecycleOwner) {result ->
            val errorMessage = OnboardUtils.getOnboardResultErrorMessage(result, resources)
            Log.d(TAG, "onSendResetPasswordMail: send reset mail executed with error " +
                    "$errorMessage")

            mBinding.forgotPasswordTextView.isClickable = true
            mBinding.onboardProgressBar.visibility = View.INVISIBLE

            if (errorMessage == null) {
                return@observe
            }

            GeneralUtils.showGenericMessageOnView(message = errorMessage,
                context = requireContext(), root = mBinding.root,
                anchorView = mBinding.onboardButton)
        }
    }

    private fun onLoginButtonClicked() {
        val mail: String = mBinding.inputEditTextMail.text.toString()
        val password: String = mBinding.inputEditTextPassword.text.toString()

        val emailError = OnboardUtils.getEmailErrorMessage(mail, resources)
        val passwordError = GeneralUtils.getPasswordErrorMessage(password = password,
            repeatedPassword = null,
            resources = resources)

        mBinding.inputLayoutMail.error = null
        mBinding.inputLayoutPassword.error = null

        if (emailError != null) {
            mBinding.inputLayoutMail.error = emailError
            return
        }
        if (passwordError != null) {
            mBinding.inputLayoutPassword.error = passwordError
            if (passwordError == resources.getString(R.string.str_password_weak_error)) {
                GeneralUtils.showPasswordSuggestionMessage(requireContext())
            }
            return
        }

        mBinding.onboardButton.isEnabled = false
        mBinding.onboardProgressBar.visibility = View.VISIBLE

        userViewModel.loginWithMailAndPassword(mail, password)
            .observe(viewLifecycleOwner) { result ->
            val errorMessage = OnboardUtils.getOnboardResultErrorMessage(result, resources)
            Log.d(TAG, "onSignUpButtonClicked: sign up executed with error $errorMessage")

            mBinding.onboardButton.isEnabled = true
            mBinding.onboardProgressBar.visibility = View.INVISIBLE

            if (errorMessage == null) {
                return@observe
            }

            if (errorMessage == getString(R.string.str_mail_not_verified)) {
                MaterialAlertDialogBuilder(requireContext())
                    .setTitle(R.string.str_warning)
                    .setMessage(errorMessage)
                    .setPositiveButton(R.string.str_ok) { _, _ ->
                        mBinding.onboardProgressBar.visibility = View.VISIBLE
                        resendVerificationEmail()
                    }
                    .setNeutralButton(R.string.str_cancel) { dialog, _ ->
                        dialog.dismiss()
                    }
                    .setOnDismissListener {
                        userViewModel.logout()
                    }
                    .setCancelable(true)
                    .show()
                return@observe
            }

                GeneralUtils.showGenericMessageOnView(message = errorMessage,
                context = requireContext(), root = mBinding.root,
                anchorView = mBinding.onboardButton)
        }
    }

    private fun resendVerificationEmail() {
        userViewModel.sendVerificationEmail().observe(viewLifecycleOwner) {result ->
            val errorMessage = OnboardUtils.getOnboardResultErrorMessage(result, resources)
            Log.d(TAG, "onSignUpButtonClicked: sign up executed with error $errorMessage")

            mBinding.onboardProgressBar.visibility = View.INVISIBLE

            if (errorMessage == null) {
                GeneralUtils.showGenericMessageOnView(
                    message = getString(R.string.str_send_verify_mail_ok),
                    context = requireContext(), root = mBinding.root,
                    anchorView = mBinding.onboardButton)
                return@observe
            }

            GeneralUtils.showGenericMessageOnView(
                message = errorMessage, context = requireContext(), root = mBinding.root,
                anchorView = mBinding.onboardButton)
        }
    }
}
