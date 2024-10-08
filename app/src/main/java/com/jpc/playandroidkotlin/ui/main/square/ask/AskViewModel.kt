package com.jpc.playandroidkotlin.ui.main.square.ask

import androidx.lifecycle.MutableLiveData
import com.jpc.library_base.base.BaseViewModel
import com.jpc.library_base.data.bean.PageResponse
import com.jpc.library_base.ext.handleRequest
import com.jpc.library_base.ext.launch
import com.jpc.playandroidkotlin.data.DataRepository
import com.jpc.playandroidkotlin.data.bean.Article

class AskViewModel : BaseViewModel() {

    /** 每日一问分页列表LiveData */
    val articlePageListLiveData = MutableLiveData<PageResponse<Article>?>()

    override fun start() {}

    /** 请求每日一问分页列表 */
    fun fetchAskPageList(pageNo: Int = 1) {
        launch({
            handleRequest(
                DataRepository.getAskPageList(pageNo),
                { articlePageListLiveData.value = it.data })
        })
    }

    /**
     * 收藏文章
     * @param id 文章id
     */
    fun collectArticle(id: Int, successCallBack: () -> Any? = {}) {
        launch({
            handleRequest(DataRepository.collectArticle(id), {
                successCallBack.invoke()
            })
        })
    }

    /**
     * 取消收藏文章
     * @param id 文章id
     */
    fun unCollectArticle(id: Int, successCallBack: () -> Any? = {}) {
        launch({
            handleRequest(DataRepository.unCollectArticle(id), {
                successCallBack.invoke()
            })
        })
    }
}