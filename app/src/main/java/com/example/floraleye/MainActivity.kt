package com.example.floraleye

import android.content.Intent
import android.os.Bundle
import androidx.activity.addCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.floraleye.databinding.ActivityMainBinding
import com.example.floraleye.ui.onboard.OnboardActivity
import com.example.floraleye.viewmodels.UserViewModel
import com.example.floraleye.viewmodels.UserViewModelFactory
import com.google.android.material.bottomnavigation.BottomNavigationView


/**
 * Activity principale del progetto.
 */
class MainActivity : AppCompatActivity() {

    private lateinit var mBinding: ActivityMainBinding

    private lateinit var userViewModel: UserViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        userViewModel = ViewModelProvider(
            this,
            UserViewModelFactory(application)
        )[UserViewModel::class.java]

        val navView: BottomNavigationView = mBinding.navView

        val navController = findNavController(R.id.nav_host_fragment_activity_main)

        val appBarConfiguration = AppBarConfiguration(setOf(
            R.id.navigation_quiz,
            R.id.navigation_dictionary,
            R.id.navigation_photo,
            R.id.navigation_profile))
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        onBackPressedDispatcher.addCallback(this) {
            finish()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        userViewModel.userOnboarded.removeObservers(this)
    }

    override fun onPause() {
        super.onPause()
        userViewModel.userOnboarded.removeObservers(this)
    }

    override fun onResume() {
        super.onResume()
        userViewModel.userOnboarded.observe(this) { isUserOnboarded ->
            if (!isUserOnboarded) {
                val intent = Intent(
                    this,
                    OnboardActivity::class.java
                )
                startActivity(intent)
                finish()
            }
        }
    }

    /**
     * Metodo utilizzato, soprattutto in fase di test, per venire a conoscenza
     * del fragment correntemente visualizzato.
     * @return fragment correntemente visualizzato.
     */
    fun getCurrentFragment(): Fragment? {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment_activity_main)
        return navHostFragment?.childFragmentManager?.fragments?.first()
    }
}
