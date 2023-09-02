package app.mbl.hcmute.chatApp.ui.features.setting

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import app.mbl.hcmute.base.common.BaseVmDbFragment
import app.mbl.hcmute.base.ext.observe
import app.mbl.hcmute.chatApp.R
import app.mbl.hcmute.chatApp.base.preference.AppSettings
import app.mbl.hcmute.chatApp.data.local.datastore.DataStoreManager
import app.mbl.hcmute.chatApp.databinding.FragmentSettingBinding
import app.mbl.hcmute.chatApp.di.module.navigationModule.AppNavigator
import com.jaygoo.widget.OnRangeChangedListener
import com.jaygoo.widget.RangeSeekBar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject


@AndroidEntryPoint
class SettingFragment : BaseVmDbFragment<SettingFragmentViewModel, FragmentSettingBinding>() {
    override fun getLayoutId() = R.layout.fragment_setting

    @Inject
    lateinit var dataStore: DataStoreManager

    @Inject
    lateinit var navigator: AppNavigator

    override val viewModel: SettingFragmentViewModel by viewModels()

    override fun setUpViews(savedInstanceState: Bundle?) {
        super.setUpViews(savedInstanceState)
        binding.vm = viewModel
        lifecycleScope.launch {
            initDarkModeBar()
        }
    }

    private suspend fun initDarkModeBar() {
        binding.seekBar.apply {
            val currentValue = (dataStore.getSetting(AppSettings.NIGHT_MODE) ?: 0).mapNightModeToSeekBar()
            setProgress(currentValue.toFloat())
            setOnRangeChangedListener(object : OnRangeChangedListener {
                override fun onRangeChanged(view: RangeSeekBar?, leftValue: Float, rightValue: Float, isFromUser: Boolean) {}
                override fun onStartTrackingTouch(view: RangeSeekBar?, isLeft: Boolean) {}
                override fun onStopTrackingTouch(view: RangeSeekBar?, isLeft: Boolean) {
                    val currentProgress = view?.leftSeekBar?.progress?.toInt() ?: 0
//                    showAds({
                    dataStore.setThemeMode(lifecycleScope, currentProgress.mapSeekbarToNightMode())
//                    }, {
//                        showToast(getString(R.string.can_not_preview_ads))
////                            setProgress(currentValue.toFloat()) todo: temp allow user to change dark mode when can not see ads.
//                        dataStore.setThemeMode(lifecycleScope, currentProgress.mapSeekbarToNightMode())
//                    })
                }
            })
        }
    }

    override fun setUpObservers() {
        super.setUpObservers()
        observe(viewModel.uiSingleEvent) {
            when (it) {
                is SettingUiState.TermClicked -> {
                    Timber.d("Start Term screen")
                    Toast.makeText(context, "Start Term screen", Toast.LENGTH_SHORT).show()
                    val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(SettingConst.TERM_URL.settingValue))
                    startActivity(browserIntent)
                }

                is SettingUiState.PolicyClicked -> {
                    Timber.d("Start Policy screen")
                    Toast.makeText(context, "Start Policy screen", Toast.LENGTH_SHORT).show()
                    val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(SettingConst.POLICY_URL.settingValue))
                    startActivity(browserIntent)
                }
            }
        }
    }
//
//    private fun showAds(successAction: () -> Unit, failAction: () -> Unit = { showToast(getString(R.string.can_not_preview_ads)) }) {
//        showRewardedVideoAd({ successAction.invoke() }, failAction)
//    }
//
//    override fun onAttach(context: Context) {
//        super.onAttach(context)
//        configureRewardedVideoAd(requireActivity())
//    }

}