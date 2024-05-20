package com.example.floraleye.ui.onboard

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.floraleye.MainActivity
import com.example.floraleye.databinding.FragmentLoadingBinding


private const val ARG_LOADING_BEHAVIORS = "arg_loading_behaviors"

private const val LOADING_TIME = 1000L

/**
 * Fragment mostrato in fase di caricamento delle funzionalità dell'applicazione.
 */
class LoadingFragment : Fragment() {

    /**
     * Possibili comportamenti per il loading, che può avvenire in fase di controllo dell'
     * utente autenticato, o in fase di caricamento della Dashboard.
     */
    enum class LoadingBehaviours {
        ONBOARD_SUCCESS,
        CHECK_ONBOARD
    }

    private var loadingBehaviours: LoadingBehaviours = LoadingBehaviours.CHECK_ONBOARD

    private lateinit var mBinding: FragmentLoadingBinding

    @Suppress("DEPRECATION")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            loadingBehaviours = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                it.getSerializable(ARG_LOADING_BEHAVIORS, LoadingBehaviours::class.java)
                        as LoadingBehaviours
            } else {
                it.getSerializable(ARG_LOADING_BEHAVIORS) as LoadingBehaviours
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBinding = FragmentLoadingBinding.inflate(inflater, container, false)

        mBinding.loadingBehaviours = loadingBehaviours

        Log.d(TAG, "onCreateView: loading behaviours is ${loadingBehaviours.name}")

        if (loadingBehaviours == LoadingBehaviours.ONBOARD_SUCCESS) {
            Handler(Looper.getMainLooper()).postDelayed({

                // ... qui possono essere eseguiti task di pre loading ...

                val onboardActivity = activity
                if (onboardActivity is OnboardActivity) {
                    val intent = Intent(
                        onboardActivity,
                        MainActivity::class.java
                    )
                    startActivity(intent)
                    onboardActivity.finish()
                } else Log.d(TAG, "onCreateView: activity error.")
            }, LOADING_TIME)
        }

        return mBinding.root
    }

    companion object {

        /**
         * Metodo statico utilizzabile per creare una istanza del LoadingFragment.
         * @param loadingBehaviours specificare se il loading avviene in fase di controllo dei dati
         * o di passaggio alla Dashboard
         * @return istanza del LoadingFragment
         */
        @JvmStatic
        fun newInstance(loadingBehaviours: LoadingBehaviours) =
            LoadingFragment().apply {
                arguments = Bundle().apply {
                    putSerializable(ARG_LOADING_BEHAVIORS, loadingBehaviours)
                }
            }

        private val TAG: String = LoadingFragment::class.java.simpleName
    }
}
