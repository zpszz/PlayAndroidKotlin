package com.jpc.playandroidkotlin.ui.main.square.navigation

import androidx.lifecycle.MutableLiveData
import com.jpc.library_base.base.BaseViewModel
import com.jpc.library_base.ext.handleRequest
import com.jpc.library_base.ext.launch
import com.jpc.playandroidkotlin.data.DataRepository
import com.jpc.playandroidkotlin.data.bean.Navigation

class NavigationViewModel : BaseViewModel() {

    /** 导航列表LiveData */
    val navigationListLiveData = MutableLiveData<List<Navigation>>()

    override fun start() {}

    /** 请求导航列表 */
    fun fetchNavigationList() {
        launch({
            handleRequest(
                DataRepository.getNavigationList(),
                { navigationListLiveData.value = it.data })
        })
    }
}