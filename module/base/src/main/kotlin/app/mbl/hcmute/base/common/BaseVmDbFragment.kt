package app.mbl.hcmute.base.common


import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModel
import app.mbl.hcmute.base.ext.logD

abstract class BaseVmDbFragment<VM : ViewModel, DB : ViewDataBinding> : BaseVmFragment<VM>() {

    private var _binding: DB? = null

    val binding get() = _binding!!

    private var mRootView: View? = null
    private var hasInitializedRootView = false

    @LayoutRes
    abstract fun getLayoutId(): Int

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        logD("onCreateView")
        initViewBinding(inflater, container)
        return mRootView
    }

    private fun initViewBinding(inflater: LayoutInflater, container: ViewGroup?) {
        _binding = DataBindingUtil.inflate(inflater, getLayoutId(), container, false)
        logD("initViewBinding")
        mRootView = binding.root
        binding.apply {
            lifecycleOwner = lifecycleBinding()
            executePendingBindings()
        }
    }

    protected open fun lifecycleBinding() = viewLifecycleOwner

    override fun onDestroyView() {
        super.onDestroyView()
        logD("onDestroyView")
        _binding = null
        if (viewModel is BaseViewModel) {
            (viewModel as BaseViewModel).onDestroyView()
        } else {
            (viewModel as BaseAndroidViewModel).onDestroyView()
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        logD("onAttach", "context $context")
    }

    override fun onPause() {
        super.onPause()
        logD("onPause")
    }

    override fun onStop() {
        super.onStop()
        logD("onStop")
    }

    override fun onDetach() {
        super.onDetach()
        logD("onDetach", "Fragment is detached")
    }

    override fun onDestroy() {
        super.onDestroy()
        logD("onDestroy", "Fragment is destroy")
    }
}