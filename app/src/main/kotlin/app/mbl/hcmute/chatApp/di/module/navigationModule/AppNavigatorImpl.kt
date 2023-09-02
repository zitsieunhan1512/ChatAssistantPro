package app.mbl.hcmute.chatApp.di.module.navigationModule

import androidx.fragment.app.FragmentActivity
import androidx.navigation.NavController
import androidx.navigation.NavDirections
import androidx.navigation.fragment.NavHostFragment
import app.mbl.hcmute.chatApp.R
import javax.inject.Inject

class AppNavigatorImpl @Inject constructor(private val activity: FragmentActivity) : AppNavigator {

    override fun getNavController(): NavController {
        val navHostFragment = activity.supportFragmentManager.findFragmentById(R.id.activityMainChooseHostFragment) as NavHostFragment
        return navHostFragment.navController
    }

    override fun navigateTo(direction: NavDirections) = with(getNavController()) {
        currentDestination?.getAction(direction.actionId)?.let { navigate(direction) }
    }

}