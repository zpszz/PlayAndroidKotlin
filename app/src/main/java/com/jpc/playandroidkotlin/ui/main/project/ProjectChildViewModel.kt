package com.jpc.playandroidkotlin.ui.main.project

import androidx.lifecycle.MutableLiveData
import com.jpc.library_base.base.BaseViewModel
import com.jpc.library_base.data.bean.PageResponse
import com.jpc.library_base.ext.handleRequest
import com.jpc.library_base.ext.launch
import com.jpc.playandroidkotlin.data.DataRepository
import com.jpc.playandroidkotlin.data.bean.Article

class ProjectChildViewModel : BaseViewModel() {
    // 项目分页列表的LiveData
    val articlePageListLiveData = MutableLiveData<PageResponse<Article>?>()

    companion object {
        const val PAGE_SIZE = 10
    }

    override fun start() {
    }

    /**
     * 请求最新项目分页列表
     * @param pageNo 页码
     */
    fun fetchNewProjectPageList(pageNo: Int = 0) {
        launch({
            handleRequest(
                DataRepository.getNewProjectPageList(pageNo, PAGE_SIZE),
                { articlePageListLiveData.value = it.data }
            )
        })
    }

    /**
     * 请求项目分页列表
     * @param pageNo 页码
     * @param categoryId 项目所属分类id
     */
    fun fetchProjectPageList(pageNo: Int = 1, categoryId: Int) {
        launch({
            handleRequest(
                DataRepository.getProjectPageList(pageNo, PAGE_SIZE, categoryId),
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