package com.jpc.playandroidkotlin.ui.main.mine

import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import com.jpc.library_base.base.BaseViewModel
import com.jpc.library_base.ext.handleRequest
import com.jpc.library_base.ext.launch
import com.jpc.playandroidkotlin.data.DataRepository
import com.jpc.playandroidkotlin.data.bean.CoinInfo
import com.jpc.playandroidkotlin.data.bean.User
import com.jpc.playandroidkotlin.data.local.UserManager

class MineViewModel: BaseViewModel(){
    val user = ObservableField<User?>()
    val integral = MutableLiveData<CoinInfo?>()
    val userName = object : ObservableField<String>(user){
        override fun get(): String {
            return if (UserManager.isLogin()) user.get()!!.nickname else "请登录"
        }
    }
    override fun start() {
        if (UserManager.isLogin()){
            user.set(UserManager.getUser())
        }
    }
    // 获取个人积分
    fun fetchPoints(){
        launch({
            handleRequest(DataRepository.getUserIntegral(), { integral.value = it.data })
        })
    }
}