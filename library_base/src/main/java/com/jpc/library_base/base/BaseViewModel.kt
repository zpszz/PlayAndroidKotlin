package com.jpc.library_base.base

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.jpc.library_base.data.bean.ApiResponse

abstract class BaseViewModel: ViewModel(){
    // 请求异常（请求失败、服务器连接超时等）
    val exception = MutableLiveData<Exception>()
    // 请求服务器返回错误（请求成功但是status错误，如登录过期）
    val errorResponse = MutableLiveData<ApiResponse<*>?>()
    // 界面启动时要进行的初始化操作，如网络请求、数据初始化等
    abstract fun start()

}