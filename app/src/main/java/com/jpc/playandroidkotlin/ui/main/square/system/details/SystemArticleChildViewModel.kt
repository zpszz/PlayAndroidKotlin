package com.jpc.playandroidkotlin.ui.main.square.system.details

import androidx.lifecycle.MutableLiveData
import com.jpc.library_base.base.BaseViewModel
import com.jpc.library_base.data.bean.PageResponse
import com.jpc.library_base.ext.handleRequest
import com.jpc.library_base.ext.launch
import com.jpc.playandroidkotlin.data.DataRepository
import com.jpc.playandroidkotlin.data.bean.Article

class SystemArticleChildViewModel : BaseViewModel() {

    companion object {
        /** 每页显示的条目大小 */
        const val PAGE_SIZE = 10
    }

    /** 体系列表LiveData */
    val articlePageListLiveData = MutableLiveData<PageResponse<Article>>()

    override fun start() {

    }

    /** 请求体系下的文章列表 */
    fun fetchArticlePageList(pageNo: Int = 0, categoryId: Int) {
        launch({
            handleRequest(
                DataRepository.getArticlePageList(pageNo, PAGE_SIZE, categoryId),
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