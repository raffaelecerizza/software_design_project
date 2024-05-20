package com.example.floraleye.ui.onboard

import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.floraleye.R
import com.example.floraleye.databinding.ActivityOnboardBinding
import com.example.floraleye.viewmodels.UserViewModel
import com.example.floraleye.viewmodels.UserViewModelFactory


private const val ARG_ONBOARD_BEHAVIORS = "arg_onboard_behaviors"
private const val ARG_LOADING_BEHAVIORS = "arg_loading_behaviors"

/**
 * Activity per la gestione dell'onboarding dell'utente, che può avvenire tramite registrazione di
 * un nuovo account, oppure onboard con un account creato in precedenza.
 */
class OnboardActivity : AppCompatActivity() {

    private enum class OnboardBehaviours {
        LOGIN,
        SIGNUP
    }

    private lateinit var mBinding: ActivityOnboardBinding

    private lateinit var userViewModel: UserViewModel

    private lateinit var currentOnboardBehaviours: OnboardBehaviours

    private lateinit var currentLoadingBehaviours: LoadingFragment.LoadingBehaviours

    @Suppress("DEPRECATION")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Ripristino valori da instance state.
        if (savedInstanceState == null) {
            currentOnboardBehaviours = OnboardBehaviours.SIGNUP
            currentLoadingBehaviours = LoadingFragment.LoadingBehaviours.CHECK_ONBOARD
        } else {
            currentOnboardBehaviours = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                savedInstanceState.getSerializable(
                    ARG_ONBOARD_BEHAVIORS,
                    OnboardBehaviours::class.java
                ) ?: OnboardBehaviours.SIGNUP
            } else {
                (savedInstanceState.getSerializable(
                    ARG_ONBOARD_BEHAVIORS
                ) ?: OnboardBehaviours.SIGNUP) as OnboardBehaviours
            }
            currentLoadingBehaviours = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                savedInstanceState.getSerializable(
                    ARG_LOADING_BEHAVIORS,
                    LoadingFragment.LoadingBehaviours::class.java
                ) ?: LoadingFragment.LoadingBehaviours.CHECK_ONBOARD
            } else {
                (savedInstanceState.getSerializable(
                    ARG_LOADING_BEHAVIORS
                ) ?: LoadingFragment.LoadingBehaviours.CHECK_ONBOARD) as
                        LoadingFragment.LoadingBehaviours
            }
        }

        mBinding = ActivityOnboardBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        userViewModel = ViewModelProvider(
            this,
            UserViewModelFactory(application)
        )[UserViewModel::class.java]

        supportActionBar?.hide()

        userViewModel.userOnboarded.observe(this) { isUserOnboarded ->
            if (isUserOnboarded) {
                currentLoadingBehaviours = LoadingFragment.LoadingBehaviours.ONBOARD_SUCCESS
                changeDisplayedFragment(LoadingFragment.newInstance(currentLoadingBehaviours),
                    currentLoadingBehaviours.name)
            } else {
                showOnboardFragment(currentOnboardBehaviours)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        userViewModel.userOnboarded.removeObservers(this)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putSerializable(ARG_ONBOARD_BEHAVIORS, currentOnboardBehaviours)
        outState.putSerializable(ARG_LOADING_BEHAVIORS, currentLoadingBehaviours)
        super.onSaveInstanceState(outState)
    }

    /**
     * Metodo da utilizzare per cambiare la pagina mostrata, da pagina di onboard a registrazione, o
     * viceversa.
     */
    fun toggleDisplayedFragment() {
        showOnboardFragment(when (currentOnboardBehaviours) {
            OnboardBehaviours.SIGNUP -> OnboardBehaviours.LOGIN
            OnboardBehaviours.LOGIN -> OnboardBehaviours.SIGNUP
        })
    }

    /**
     * Metodo utilizzato, soprattutto in fase di test, per venire a conoscenza
     * del fragment correntemente visualizzato.
     * @return fragment correntemente visualizzato
     */
    fun getDisplayedFragment() : Fragment {
        return mBinding.fragmentContainerView.getFragment()
    }

    private fun showOnboardFragment(newOnboardBehaviours: OnboardBehaviours) {
        currentOnboardBehaviours = newOnboardBehaviours

        val newFragment = when (newOnboardBehaviours) {
            OnboardBehaviours.SIGNUP -> SignUpFragment()
            OnboardBehaviours.LOGIN -> LoginFragment()
        }

        changeDisplayedFragment(newFragment, currentOnboardBehaviours.name)
    }

    private fun changeDisplayedFragment(newFragment: Fragment, newTag: String) {
        /*
            Se il fragment attualmente mostrato ha stesso tag di quello che si vuole mostrare,
            non viene fatto niente, perché vuol dire che il fragmente che si desidera mostrare in
            realtà è già presente.
         */
        if (getDisplayedFragment().tag == newTag) {
            return
        }

        supportFragmentManager.beginTransaction()
            .setCustomAnimations(
                androidx.appcompat.R.anim.abc_grow_fade_in_from_bottom,
                androidx.appcompat.R.anim.abc_shrink_fade_out_from_bottom)
            .replace(R.id.fragmentContainerView, newFragment, newTag)
            .commit()
    }
}
