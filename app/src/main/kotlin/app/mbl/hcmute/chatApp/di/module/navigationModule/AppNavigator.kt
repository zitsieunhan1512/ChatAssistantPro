package app.mbl.hcmute.chatApp.di.module.navigationModule

import androidx.navigation.NavController
import androidx.navigation.NavDirections

interface AppNavigator {

    fun getNavController(): NavController
    fun navigateTo(direction: NavDirections): Unit?

}