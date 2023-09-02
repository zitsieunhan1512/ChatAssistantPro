package app.mbl.hcmute.base.common

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModel
import androidx.viewbinding.ViewBinding
import app.mbl.hcmute.base.ext.logD

typealias Inflate<T> = (LayoutInflater, ViewGroup?, Boolean) -> T

abstract class BaseVbVmFragment<VB : ViewBinding, VM : ViewModel>(private val inflate: Inflate<VB>) : BaseVmFragment<VM>() {

    private var _binding: VB? = null

    val binding get() = _binding!!

    private var mRootView: View? = null
    private var hasInitializedRootView = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        logD("onCreateView")
        initViewBinding(inflater, container)
        return mRootView
    }

    private fun initViewBinding(inflater: LayoutInflater, container: ViewGroup?) {
        _binding = inflate.invoke(inflater, container, false)
        logD("initViewBinding")
        mRootView = binding.root
    }

    override fun setUpObservers() {
        super.setUpObservers()
        //
//        val progressBar: SingleLiveEvent<Boolean> = if (viewModel is BaseViewModel) {
//            (viewModel as BaseViewModel).showCommonProgressBar
//        } else {
//            (viewModel as BaseAndroidViewModel).showCommonProgressBar
//        }
//        observe(progressBar) { show -> if (show) commonProgressBar.apply { if (show) show() else hide() } }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        if (viewModel is BaseViewModel) {
            (viewModel as BaseViewModel).onDestroyView()
        } else {
            (viewModel as BaseAndroidViewModel).onDestroyView()
        }
    }
//
//    protected fun hideLoading() {
//        _binding?.let {
//            with(it.root) {
//                findViewById<ProgressBar>(R.id.progress_bar)?.visibility = View.GONE
//                if (showLoadingWithAlpha()) {
//                    alpha = 1f
//                }
//            }
//        }
//    }
//
//    protected fun showLoading() {
//        _binding?.let {
//            with(it.root) {
//                findViewById<ProgressBar>(R.id.progress_bar)?.visibility = View.VISIBLE
//                alpha = if (showLoadingWithAlpha()) 0.3f else 1.0f
//            }
//        }
//    }
//
//    open fun showLoadingWithAlpha() : Boolean = false

    override fun onAttach(context: Context) {
        super.onAttach(context)
        logD("onAttach", "context $context")
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