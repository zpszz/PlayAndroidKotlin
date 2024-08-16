package com.jpc.playandroidkotlin.ui.search

import androidx.lifecycle.MutableLiveData
import com.jpc.library_base.base.BaseViewModel
import com.jpc.library_base.ext.handleRequest
import com.jpc.library_base.ext.launch
import com.jpc.playandroidkotlin.data.DataRepository
import com.jpc.playandroidkotlin.data.bean.HotSearch
import com.jpc.playandroidkotlin.data.local.CacheManager
import kotlinx.coroutines.flow.MutableStateFlow

class SearchViewModel: BaseViewModel(){
    // 搜索关键词
    val searchKeyFlow = MutableStateFlow("")
    // 搜索历史
    val searchHistoryData = MutableLiveData<ArrayDeque<String>>()
    // 热门搜索
    val hotSearchList = MutableLiveData<List<HotSearch>?>()
    override fun start() {
        fetchHotSearchList()
        fetchSearchHistoryData()
    }

    // 获取热门搜索
    private fun fetchHotSearchList(){
        launch({
            handleRequest(DataRepository.getHotSearchList(), { hotSearchList.value = it.data })
        })
    }

    // 获取搜索历史记录
    private fun fetchSearchHistoryData(){
        launch({
            searchHistoryData.value = CacheManager.getSearchHistoryData()
        })
    }

    // 清空搜索记录
    fun clearSearchHistoryData(){
        searchHistoryData.value = ArrayDeque()
    }
}