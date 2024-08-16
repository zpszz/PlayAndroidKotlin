package com.jpc.playandroidkotlin.ui.author

import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import com.jpc.library_base.base.BaseViewModel
import com.jpc.library_base.data.bean.PageResponse
import com.jpc.library_base.ext.handleRequest
import com.jpc.library_base.ext.launch
import com.jpc.playandroidkotlin.data.DataRepository
import com.jpc.playandroidkotlin.data.bean.Article
import com.jpc.playandroidkotlin.data.bean.CoinInfo

class AuthorViewModel : BaseViewModel() {
    val coinInfo = ObservableField<CoinInfo>()
    val articlePageList = MutableLiveData<PageResponse<Article>>()
    val name = object : ObservableField<String>(coinInfo) {
        override fun get(): String {
            return coinInfo.get()?.nickname ?: "——"
        }
    }
    val info = object : ObservableField<String>(coinInfo) {
        override fun get(): String {
            return "积分：${coinInfo.get()?.coinCount ?: "——"}\t\t排名：${coinInfo.get()?.rank ?: "——"}"
        }
    }

    override fun start() {
    }

    /**
     * 分页获取其他作者分享的文章分页列表
     *
     * @param id 作者Id
     * @param pageNo 页码
     * @param errorCallback 请求失败的回调函数，函数返回值表示是否拦截统一的错误提示
     */
    fun fetchShareArticlePageList(
        id: Int,
        pageNo: Int = 1,
        errorCallback: () -> Boolean = { false }
    ) {
        launch({
            handleRequest(
                DataRepository.getOtherAuthorArticlePageList(id, pageNo),
                {
                    if (pageNo == 1) {
                        // 作者信息没必要在每次上拉加载时都更新，在获取第一页数据时设置就行
                        coinInfo.set(it.data.coinInfo)
                    }
                    articlePageList.value = it.data.shareArticles
                }, { errorCallback.invoke() }
            )
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