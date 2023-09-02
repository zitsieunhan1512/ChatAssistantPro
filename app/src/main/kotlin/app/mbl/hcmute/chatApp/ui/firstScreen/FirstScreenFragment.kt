package app.mbl.hcmute.chatApp.ui.firstScreen

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import app.mbl.hcmute.base.common.BaseVmDbFragment
import app.mbl.hcmute.chatApp.R
import app.mbl.hcmute.chatApp.databinding.FragmentFirstScreenBinding
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FirstScreenFragment : BaseVmDbFragment<FirstScreenViewModel, FragmentFirstScreenBinding>() {
    private val firstScreenTabItems = arrayListOf(R.string.conversation, R.string.favorite, R.string.setting)

    override fun getLayoutId() = R.layout.fragment_first_screen
    override val viewModel: FirstScreenViewModel by viewModels()

    override fun setUpViews(savedInstanceState: Bundle?) {
        super.setUpViews(savedInstanceState)
        binding.apply {
            viewPager.adapter = ViewPagerAdapter(this@FirstScreenFragment)
            viewPager.isUserInputEnabled = false
            val tabIconColors = context?.let { ContextCompat.getColorStateList(it, R.color.tab_icon_colors) }
            TabLayoutMediator(tabLayout, viewPager) { tab, position ->
                val tabView = layoutInflater.inflate(R.layout.tab_item_layout, null)
                val tabIcon = tabView.findViewById<ImageView>(R.id.tab_icon)
                val tabText = tabView.findViewById<TextView>(R.id.tab_text)
                tabIcon.setImageResource(getTabIcon(position))
                tabText.text = getString(firstScreenTabItems[position])
                tabIcon.imageTintList = tabIconColors
                tab.customView = tabView
            }.attach()

        }
    }

    private fun getTabIcon(position: Int): Int {
        return when (position) {
            0 -> R.drawable.baseline_view_list_24
            1 -> R.drawable.baseline_favorite_24
            2 -> R.drawable.baseline_settings_24
            else -> R.drawable.ic_launcher_foreground
        }
    }

    override fun onDestroyView() {
        binding.viewPager.addOnAttachStateChangeListener(object : View.OnAttachStateChangeListener {
            override fun onViewAttachedToWindow(v: View) {}
            override fun onViewDetachedFromWindow(v: View) {
                binding.viewPager.adapter = null
            }
        })
        super.onDestroyView()
    }
}