package app.mbl.hcmute.chatApp.ui.firstScreen

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import app.mbl.hcmute.chatApp.ui.features.bookmark.BookMarkFragment
import app.mbl.hcmute.chatApp.ui.features.conversation.ConversationFragment
import app.mbl.hcmute.chatApp.ui.features.setting.SettingFragment

class ViewPagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int = 3

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> ConversationFragment()
            1 -> BookMarkFragment()
            2 -> SettingFragment()
            else -> Fragment()
        }
    }
}