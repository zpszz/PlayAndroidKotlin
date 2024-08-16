package com.jpc.playandroidkotlin.ui.login.register

import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import com.jpc.library_base.base.BaseViewModel
import com.jpc.library_base.ext.handleRequest
import com.jpc.library_base.ext.launch
import com.jpc.playandroidkotlin.data.DataRepository
import com.jpc.playandroidkotlin.data.local.UserManager

class RegisterViewModel : BaseViewModel() {
    val userName = ObservableField("")
    val password = ObservableField("")
    val rePassword = ObservableField("")

    val registerBtnEnable = object : ObservableBoolean(userName, password, rePassword) {
        override fun get(): Boolean {
            return !userName.get()?.trim().isNullOrEmpty() && password.get()
                .isNullOrEmpty() && !rePassword.get().isNullOrEmpty()
        }
    }

    override fun start() {
    }

    /**
     * 注册
     * @param userName 用户名
     * @param pwd 密码
     * @param pwdSure 确认密码
     * @param successCall 成功回调函数
     */
    fun register(userName: String, pwd: String, pwdSure: String, successCall: () -> Any? = {}) {
        launch({
            handleRequest(DataRepository.register(userName, pwd, pwdSure), successBlock = {
                UserManager.saveLastUserName(userName)
                successCall.invoke()
            })
        })
    }
}