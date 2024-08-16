package com.jpc.playandroidkotlin.ui.collect.url

import androidx.lifecycle.MutableLiveData
import com.jpc.library_base.base.BaseViewModel
import com.jpc.library_base.ext.handleRequest
import com.jpc.library_base.ext.launch
import com.jpc.playandroidkotlin.data.DataRepository
import com.jpc.playandroidkotlin.data.bean.CollectUrl

class CollectUrlViewModel : BaseViewModel() {

    /** 收藏网址列表LiveData */
    val collectUrlList = MutableLiveData<List<CollectUrl>>()

    override fun start() {}

    /** 请求收藏网址列表 */
    fun fetchCollectUrlList() {
        launch({
            handleRequest(DataRepository.getCollectUrlList(), { collectUrlList.value = it.data })
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