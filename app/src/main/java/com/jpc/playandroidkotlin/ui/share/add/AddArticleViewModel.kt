package com.jpc.playandroidkotlin.ui.share.add

import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import com.jpc.library_base.base.BaseViewModel
import com.jpc.library_base.ext.handleRequest
import com.jpc.library_base.ext.launch
import com.jpc.playandroidkotlin.data.DataRepository
import com.jpc.playandroidkotlin.data.local.UserManager

class AddArticleViewModel : BaseViewModel() {

    val title = ObservableField("")
    val articleLink = ObservableField("")
    val shareUserName = ObservableField("")


    /** 分享按键是否可点击(这样可避免在dataBinding中写较复杂的逻辑) */
    val shareBtnEnable = object : ObservableBoolean(title, articleLink) {
        override fun get(): Boolean {
            return !title.get()?.trim().isNullOrEmpty() && !articleLink.get()?.trim()
                .isNullOrEmpty()
        }
    }

    override fun start() {
        shareUserName.set(UserManager.getUser()?.nickname ?: "")
    }

    /**
     * 添加分享的文章
     *
     * @param title 标题
     * @param link 文章链接
     */
    fun addArticle(title: String, link: String, successAction: () -> Any? = {}) {
        launch({
            val response = DataRepository.addArticle(title, link)
            handleRequest(response, {
                successAction.invoke()
            })
        })
    }
}