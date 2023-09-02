package app.mbl.hcmute.chatApp.ui.main

import android.content.res.Configuration
import android.os.Bundle
import android.os.PersistableBundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.lifecycleScope
import app.mbl.hcmute.chatApp.base.preference.AppSettings
import app.mbl.hcmute.chatApp.databinding.ActivityMainBinding
import app.mbl.hcmute.chatApp.di.module.navigationModule.AppNavigator
import app.mbl.hcmute.chatApp.util.extension.collectIn
import app.mbl.hcmute.chatApp.util.extension.viewInflateBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val binding by viewInflateBinding(ActivityMainBinding::inflate)

    @Inject
    lateinit var navigator: AppNavigator

    @Inject
    lateinit var dataStoreManager: app.mbl.hcmute.chatApp.data.local.datastore.DataStoreManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        configureSplashScreen()
        setContentView(binding.root)
        setupUI()
//        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
//            override fun handleOnBackPressed() {
//                finish()
//            }
//        })
    }

    private fun configureSplashScreen() {
        val splashScreen = installSplashScreen()
        splashScreen.apply {
            setKeepOnScreenCondition {
                val condition = (application as MainApplication).isOpenAppAdLoading.value
                Timber.d("splash screen keep on screen condition: $condition")
                condition
            }

            setOnExitAnimationListener { splashScreenView ->
                Timber.d("splash screen animation is about to end")
                splashScreenView.remove()
            }
        }
    }

    override fun onSupportNavigateUp() = navigator.getNavController().navigateUp()

    override fun onDestroy() {
        if (isTaskRoot && isFinishing) {
            finishAfterTransition()
        }
        super.onDestroy()
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        Timber.d("onConfigurationChanged")
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onRestoreInstanceState(savedInstanceState, persistentState)
        Timber.d("onRestoreInstanceState")
    }

    private fun setupUI() {
        lifecycleScope.launch {
            dataStoreManager.getSettingStream(AppSettings.NIGHT_MODE).collectIn(this@MainActivity) { mode ->
                if (mode != null) setNightMode(mode)
            }
        }
    }

    private fun setNightMode(mode: Int) {
        if (AppCompatDelegate.getDefaultNightMode() != mode) AppCompatDelegate.setDefaultNightMode(mode)
    }
}