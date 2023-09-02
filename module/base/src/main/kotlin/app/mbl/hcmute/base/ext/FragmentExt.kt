package app.mbl.hcmute.base.ext

import android.os.Bundle
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import app.mbl.hcmute.base.common.BaseViewModel
import app.mbl.hcmute.base.common.BaseVmDbFragment
import app.mbl.hcmute.base.common.ViewModelProviderFactory
import kotlin.reflect.KClass

inline fun <FRAGMENT : Fragment> FRAGMENT.putArgs(argsBuilder: Bundle.() -> Unit): FRAGMENT = this.apply { arguments = Bundle().apply(argsBuilder) }

inline fun FragmentManager.inTransactionAllowingStateLoss(func: FragmentTransaction.() -> FragmentTransaction) {
    beginTransaction().func().commitAllowingStateLoss()
}

fun <F> AppCompatActivity.replaceFragment(
    fragment: F,
    id: Int,
    tag: String? = null,
) where F : Fragment {
    supportFragmentManager.inTransactionAllowingStateLoss {
        addToBackStack(tag)
        replace(id, fragment, tag)
    }
}

fun <F> BaseVmDbFragment<*, *>.replaceFragment(
    fragment: F,
    id: Int,
    tag: String? = null,
    isAddToBackStack: Boolean = true,
) where F : Fragment {
    requireActivity().supportFragmentManager.inTransactionAllowingStateLoss {
        if (isAddToBackStack) {
            addToBackStack(tag)
        }
        replace(id, fragment, tag)
    }
}

fun <F> BaseVmDbFragment<*, *>.replaceFragment(
    fragmentManager: FragmentManager?,
    fragment: F,
    id: Int,
    tag: String? = null,
    isAddToBackStack: Boolean = true,
) where F : Fragment {
    fragmentManager?.inTransactionAllowingStateLoss {
        if (isAddToBackStack) {
            addToBackStack(tag)
        }
        replace(id, fragment, tag)
    }
}

fun <F> AppCompatActivity.addFragment(
    fragment: F,
    id: Int,
    tag: String? = null,
) where F : Fragment {
    supportFragmentManager.inTransactionAllowingStateLoss {
        addToBackStack(tag)
        add(id, fragment, tag)
    }
}

fun <F> FragmentManager.addFragment(id: Int, fragment: F, allowBackStack: Boolean = false) where F : Fragment {
    val tag = fragment::class.simpleName
    inTransactionAllowingStateLoss {
        if (allowBackStack) addToBackStack(tag)
        add(id, fragment, tag)
    }
}

fun <T : Fragment> FragmentManager.removeFragment(frag: KClass<T>) {
    val fragment = findFragmentByTag(frag.simpleName)
    inTransactionAllowingStateLoss {
        remove(fragment!!)
    }
}

fun Fragment.recreate(fragmentManager: FragmentManager? = null) {
    fragmentManager ?: parentFragmentManager.let {
        val detachTransaction = it.beginTransaction()
        val attachTransaction = it.beginTransaction()
        detachTransaction.detach(this)
        attachTransaction.attach(this)
        detachTransaction.commitAllowingStateLoss()
        attachTransaction.commitAllowingStateLoss()
    }
}

fun Fragment.navigateUp() {
    requireActivity().supportFragmentManager.popBackStack()
}

fun <T : Fragment> Fragment.navigateUpInclusive(fragment: KClass<T>) {
    requireActivity().supportFragmentManager.popBackStack(fragment.simpleName, FragmentManager.POP_BACK_STACK_INCLUSIVE)
}

fun Fragment.navigateUpInclusive(tag: String) {
    requireActivity().supportFragmentManager.popBackStack(tag, FragmentManager.POP_BACK_STACK_INCLUSIVE)
}

inline fun <reified T : ViewModel> BaseVmDbFragment<*, *>.getNormalViewModel(vm: ViewModel, owner: ViewModelStoreOwner = this) =
    ViewModelProvider(owner, ViewModelProviderFactory(vm))[T::class.java]

inline fun <reified T : ViewModel> BaseVmDbFragment<*, *>.getSharedNormalViewModel(vm: BaseViewModel) =
    ViewModelProvider(this.requireActivity(), ViewModelProviderFactory(vm))[T::class.java]

fun Fragment.showToast(message: String?) {
    Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
}

fun Fragment.formatResId(@StringRes firstResID: Int, @StringRes secondResID: Int): String {
    return String.format(requireContext().getString(firstResID), requireContext().getString(secondResID))
}

fun Fragment.formatResId(@StringRes resID: Int, value: String): String {
    return String.format(requireContext().getString(resID), value)
}
