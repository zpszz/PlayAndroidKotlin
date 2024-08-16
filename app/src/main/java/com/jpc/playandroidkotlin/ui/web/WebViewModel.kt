package com.jpc.playandroidkotlin.ui.web

import com.jpc.library_base.base.BaseViewModel
import com.jpc.library_base.ext.handleRequest
import com.jpc.library_base.ext.launch
import com.jpc.playandroidkotlin.data.DataRepository
import com.jpc.playandroidkotlin.data.bean.CollectUrl

class WebViewModel: BaseViewModel(){
    override fun start() {

    }
    fun collectArticle(id: Int, successCallback: () -> Any? = {}){
        launch({
            handleRequest(DataRepository.collectArticle(id), {
                // invoke()方法是Kotlin中的一个函数，它的作用是调用函数类型的对象，
                // 这里的successCallback是一个函数类型的参数，所以可以使用invoke()方法调用它
                successCallback.invoke()
            })
        })
    }
    fun unCollectArticle(id: Int, successCallBack: () -> Any? = {}) {
        launch({
            handleRequest(DataRepository.unCollectArticle(id), {
                successCallBack.invoke()
            })
        })
    }
    /**
     * 收藏网址
     * @param name 网址名
     * @param link 网址链接
     */
    fun collectUrl(
        name: String,
        link: String,
        successCallBack: (collectUrl: CollectUrl?) -> Any? = {}
    ) {
        launch({
            handleRequest(DataRepository.collectUrl(name, link), {
                successCallBack.invoke(it.data)
            })
        })
    }

    /** 取消收藏网址*/
    fun unCollectUrl(id: Int, successCallBack: () -> Any? = {}) {
        launch({
            handleRequest(DataRepository.unCollectUrl(id), {
                successCallBack.invoke()
            })
        })
    }
}