package com.jpc.playandroidkotlin.base

import androidx.lifecycle.MutableLiveData
import com.jpc.library_base.base.BaseViewModel
import com.jpc.playandroidkotlin.data.bean.CollectData
import com.jpc.playandroidkotlin.data.bean.User

/**
 * App全局ViewModel，可以直接替代EventBus
 */
class AppViewModel: BaseViewModel(){
    override fun start() {
    }

    // 全局用户
    val userEvent = MutableLiveData<User?>()

    // 分享添加文章
    val shareArticleEvent = MutableLiveData<Boolean>()

    // 文章收藏
    val collectEvent = MutableLiveData<CollectData>()
}