package com.jpc.playandroidkotlin.ui.login.register

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import com.jpc.library_base.ext.hideLoading
import com.jpc.library_base.ext.showLoading
import com.jpc.library_base.utils.ToastUtil
import com.jpc.playandroidkotlin.R
import com.jpc.playandroidkotlin.base.BaseActivity
import com.jpc.playandroidkotlin.databinding.ActivityRegisterBinding


class RegisterActivity : BaseActivity<RegisterViewModel, ActivityRegisterBinding>(R.layout.activity_register) {
    companion object{
        const val EXTRA_RESULT_USER_NAME = "user_name"
        fun newIntent(context: Context): Intent{
            return Intent(context, RegisterActivity::class.java)
        }
    }
    override fun initView(savedInstanceState: Bundle?) {
        mBinding.apply {
            toolbar.setOnClickListener {
                finish()
            }
            btnRegister.setOnClickListener {
                when{
                    mViewModel.password.get()!!.length < 6 ->{
                        ToastUtil.showLong(this@RegisterActivity, "密码最少6位")
                    }
                    mViewModel.rePassword.get() != mViewModel.password.get() ->
                        ToastUtil.showLong(this@RegisterActivity, "密码不一致")
                    else -> {
                        showLoading("注册中...")
                        mViewModel.register(
                            mViewModel.userName.get()!!,
                            mViewModel.password.get()!!,
                            mViewModel.rePassword.get()!!
                        ){
                            hideLoading()
                            setResult(
                                RESULT_OK,
                                Intent().apply {
                                    putExtra(
                                        EXTRA_RESULT_USER_NAME,
                                        mViewModel.userName.get()
                                    )
                                }
                            )
                            finish()
                        }
                    }
                }
            }
        }
    }

}