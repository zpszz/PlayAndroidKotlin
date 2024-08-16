package com.jpc.playandroidkotlin.ui.integral.record

import androidx.lifecycle.MutableLiveData
import com.jpc.library_base.base.BaseViewModel
import com.jpc.library_base.data.bean.PageResponse
import com.jpc.library_base.ext.handleRequest
import com.jpc.library_base.ext.launch
import com.jpc.playandroidkotlin.data.DataRepository
import com.jpc.playandroidkotlin.data.bean.IntegralRecord

class IntegralRecordViewModel : BaseViewModel() {
    val integralRecordPageList = MutableLiveData<PageResponse<IntegralRecord>>()

    override fun start() {}

    /**
     * 获取积分记录分页列表
     *
     * @param pageNo 页码
     */
    fun fetchIntegralRecordPageList(pageNo: Int = 1) {
        launch({
            handleRequest(DataRepository.getIntegralRecordPageList(pageNo), {
                integralRecordPageList.value = it.data
            })
        })
    }
}