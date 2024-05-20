package com.example.floraleye.ui.onboard

import android.os.Bundle
import android.text.method.LinkMovementMethod
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.floraleye.R
import com.example.floraleye.databinding.FragmentOnboardBinding
import com.example.floraleye.utils.GeneralUtils
import com.example.floraleye.utils.OnboardUtils
import com.example.floraleye.utils.GeneralUtils.showGenericMessageOnView
import com.example.floraleye.viewmodels.UserViewModel
import com.example.floraleye.viewmodels.UserViewModelFactory
import com.google.android.material.dialog.MaterialAlertDialogBuilder


/**
 * Fragment per la gestione della registrazione.
 */
class SignUpFragment : Fragment() {

    private lateinit var mBinding: FragmentOnboardBinding

    private lateinit var userViewModel: UserViewModel

    companion object {

        private val TAG: String = SignUpFragment::class.java.simpleName
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBinding = FragmentOnboardBinding.inflate(inflater, container, false)

        mBinding.isLogin = false

        userViewModel = ViewModelProvider(
            this,
            UserViewModelFactory(requireActivity().application)
        )[UserViewModel::class.java]

        val currentActivity = activity
        if (currentActivity is OnboardActivity) {
            mBinding.switchModeTextView.text =
                OnboardUtils.setUpSwitchTextWithButton(resources.getString(R.string.str_login_text),
                    resources.getString(R.string.str_login_button),
                    currentActivity::toggleDisplayedFragment)
            mBinding.switchModeTextView.movementMethod = LinkMovementMethod.getInstance()
        } else {
            Log.d(TAG, "onCreateView: impossible to access activity.")
        }

        mBinding.onboardButton.setOnClickListener {
            onSignUpButtonClicked()
        }

        return mBinding.root
    }

    private fun onSignUpButtonClicked() {
        val mail: String = mBinding.inputEditTextMail.text.toString()
        val password: String = mBinding.inputEditTextPassword.text.toString()
        val repeatPassword: String = mBinding.inputEditTextRepeatPassword.text.toString()

        val emailError = OnboardUtils.getEmailErrorMessage(mail, resources)
        val passwordError = GeneralUtils.getPasswordErrorMessage(password = password,
            repeatedPassword = repeatPassword,
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

        userViewModel.registerWithMailAndPassword(mail, password)
            .observe(viewLifecycleOwner) { result ->
            val errorMessage = OnboardUtils.getOnboardResultErrorMessage(result, resources)
            Log.d(TAG, "onSignUpButtonClicked: sign up executed with error $errorMessage")

            mBinding.onboardButton.isEnabled = true
            mBinding.onboardProgressBar.visibility = View.INVISIBLE

            if (errorMessage == null) {
                MaterialAlertDialogBuilder(requireContext())
                    .setMessage(getString(R.string.str_signup_ok))
                    .setPositiveButton(R.string.str_ok) { dialog, _ ->
                        dialog.dismiss()
                        val currentActivity = activity
                        if (currentActivity is OnboardActivity) {
                            currentActivity.toggleDisplayedFragment()
                        } else Log.d(TAG, "onSignUpButtonClicked: activity error.")
                    }
                    .setCancelable(false)
                    .show()
                return@observe
            }

            showGenericMessageOnView(message = errorMessage, context = requireContext(),
                root = mBinding.root, anchorView = mBinding.onboardButton)
        }
    }
}
