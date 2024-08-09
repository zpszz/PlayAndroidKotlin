package com.jpc.playandroidkotlin.base

import androidx.databinding.ViewDataBinding
import com.jpc.library_base.base.BaseVMBActivity
import com.jpc.library_base.base.BaseViewModel
import com.jpc.playandroidkotlin.ui.login.LoginActivity

/**
 * 继承自BaseVMBActivity
 * 在这里只做了统一处理跳转登录页面的逻辑
 */
abstract class BaseActivity<VM : BaseViewModel, B : ViewDataBinding>(contentViewResId: Int) :
    BaseVMBActivity<VM, B>(contentViewResId) {

    override fun createObserve() {
        super.createObserve()
        mViewModel.errorResponse.observe(this) {
            if (it?.errorCode == -1001) {
                // 需要登录，这里主要是针对收藏操作，不想每个地方都判断一下
                LoginActivity.launch(this)
            }
        }
    }
}