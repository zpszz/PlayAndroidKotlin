package com.jpc.playandroidkotlin.ui.login

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import com.jpc.library_base.ext.hideLoading
import com.jpc.library_base.ext.showLoading
import com.jpc.library_base.utils.ToastUtil
import com.jpc.playandroidkotlin.R
import com.jpc.playandroidkotlin.base.BaseActivity
import com.jpc.playandroidkotlin.databinding.ActivityLoginBinding
import com.jpc.playandroidkotlin.ui.login.register.RegisterActivity

/**
 * 登录页面
 */
class LoginActivity : BaseActivity<LoginViewModel, ActivityLoginBinding>(R.layout.activity_login) {
    companion object{
        fun launch(context: Context){
            context.startActivity(Intent(context, LoginActivity::class.java))
        }
    }

    override fun initView(savedInstanceState: Bundle?) {
        // Activity Result需要先注册
        val registerForActivityResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
            if (it.resultCode == RESULT_OK){
                mViewModel.userName.set(it.data?.getStringExtra(RegisterActivity.EXTRA_RESULT_USER_NAME))
            }
        }
        mBinding.apply {
            tvToRegister.setOnClickListener{
                registerForActivityResult.launch(RegisterActivity.newIntent(this@LoginActivity))
            }
            btnLogin.setOnClickListener{
                if (!mViewModel.loginBtnEnable.get()){
                    ToastUtil.showShort(this@LoginActivity, "请输入完整的用户名、密码")
                    return@setOnClickListener
                }
                showLoading("登录中...")
                mViewModel.login(mViewModel.userName.get()!!, mViewModel.password.get()!!){
                    hideLoading()
                    onBackPressedDispatcher.onBackPressed()
                }
            }
        }
    }
}