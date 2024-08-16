package com.jpc.playandroidkotlin.ui.main.square.system

import androidx.lifecycle.MutableLiveData
import com.jpc.library_base.base.BaseViewModel
import com.jpc.library_base.ext.handleRequest
import com.jpc.library_base.ext.launch
import com.jpc.playandroidkotlin.data.DataRepository
import com.jpc.playandroidkotlin.data.bean.Structure

class SystemViewModel : BaseViewModel() {

    /** 体系列表LiveData */
    val structureListLiveData = MutableLiveData<List<Structure>>()

    override fun start() {
    }

    /** 请求体系列表 */
    fun fetchSystemList() {
        launch({
            handleRequest(DataRepository.getTreeList(), { structureListLiveData.value = it.data })
        })
    }
}