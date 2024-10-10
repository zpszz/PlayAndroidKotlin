package com.jpc.playandroidkotlin.ui.main.wechat

import androidx.lifecycle.MutableLiveData
import com.jpc.library_base.base.BaseViewModel
import com.jpc.library_base.ext.handleRequest
import com.jpc.library_base.ext.launch
import com.jpc.playandroidkotlin.data.DataRepository
import com.jpc.playandroidkotlin.data.bean.Classify

class WechatViewModel : BaseViewModel() {

    /** 项目标题列表LiveData */
    val authorTitleListLiveData = MutableLiveData<List<Classify>>()

    override fun start() {
        fetchAuthorTitleList()
    }

    /** 请求公众号作者标题列表 */
    private fun fetchAuthorTitleList() {
        launch({
            handleRequest(
                DataRepository.getAuthorTitleList(),
                { authorTitleListLiveData.value = it.data })
        })
    }
}