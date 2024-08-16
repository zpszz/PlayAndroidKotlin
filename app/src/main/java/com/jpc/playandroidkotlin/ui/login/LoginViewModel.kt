package com.jpc.playandroidkotlin.ui.login

import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import com.jpc.library_base.base.BaseViewModel
import com.jpc.library_base.ext.handleRequest
import com.jpc.library_base.ext.launch
import com.jpc.playandroidkotlin.base.MyApplication
import com.jpc.playandroidkotlin.data.DataRepository
import com.jpc.playandroidkotlin.data.local.UserManager

class LoginViewModel : BaseViewModel() {
    val userName = ObservableField("")
    val password = ObservableField("")

    val loginBtnEnable = object : ObservableBoolean(userName, password) {
        override fun get(): Boolean {
            return !userName.get()?.trim().isNullOrEmpty() && !password.get().isNullOrEmpty()
        }
    }

    override fun start() {
        userName.set(UserManager.getLastUserName())
    }

    fun login(userName: String, password: String, successCallback: () -> Any? = {}) {
        launch({
            handleRequest(DataRepository.login(userName, password),
                {
                    UserManager.saveLastUserName(userName)
                    UserManager.saveUser(it.data)
                    MyApplication.appViewModel.userEvent.value = it.data
                    successCallback.invoke()
                })
        })
    }
}