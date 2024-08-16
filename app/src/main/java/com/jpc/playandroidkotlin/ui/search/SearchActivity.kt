package com.jpc.playandroidkotlin.ui.search

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.flexbox.FlexboxLayoutManager
import com.jpc.library_base.utils.ToastUtil
import com.jpc.playandroidkotlin.R
import com.jpc.playandroidkotlin.base.BaseActivity
import com.jpc.playandroidkotlin.data.local.CacheManager
import com.jpc.playandroidkotlin.databinding.ActivitySearchBinding
import com.jpc.playandroidkotlin.ui.search.result.SearchResultActivity

class SearchActivity : BaseActivity<SearchViewModel, ActivitySearchBinding>(R.layout.activity_search) {
    private val mSearchHistoryAdapter by lazy { SearchHistoryAdapter() }
    private val mHotSearchAdapter by lazy { HotSearchAdapter() }

    companion object{
        fun launch(context: Context){
            context.startActivity(Intent(context, SearchActivity::class.java))
        }
    }
    override fun initView(savedInstanceState: Bundle?) {
        mBinding.apply {
            titleLayout.setRightView(R.drawable.ic_search){
                handleSearch()
            }
            rvHotSearch.apply {
                isNestedScrollingEnabled = false // RecyclerView在NestedScrollView中不允许自己滑动
                layoutManager = FlexboxLayoutManager(this@SearchActivity)
                adapter = mHotSearchAdapter.apply {
                    setOnItemClickListener{ _, _, position ->
                        SearchResultActivity.launch(this@SearchActivity, this.data[position].name)
                    }
                }
            }
            rvSearchHistory.apply {
                isNestedScrollingEnabled = false
                layoutManager = LinearLayoutManager(this@SearchActivity)
                adapter = mSearchHistoryAdapter.apply {
                    setOnItemClickListener{_, _, position ->
                        SearchResultActivity.launch(this@SearchActivity, this.data[position])
                    }
                    addChildClickViewIds(R.id.iv_delete)
                    setOnItemChildClickListener{_, _, position ->
                        mViewModel.searchHistoryData.value?.removeAt(position)
                        removeAt(position) // 从RecyclerView的Item项中删除
                        if (mViewModel.searchHistoryData.value?.size == 0)
                            tvClear.visibility = View.GONE
                    }
                }
            }
            // 监听软键盘的搜索键
            etSearch.setOnEditorActionListener { _, _, _ ->
                handleSearch()
                return@setOnEditorActionListener true
            }
        }
    }

    // 处理搜索事件
    private fun handleSearch() {
        mViewModel.searchKeyFlow.value.let {
            val key = it.trim()
            if (key.isNotEmpty()) {
                Log.d("searchKey", key)
                updateSearchKey(key)
                SearchResultActivity.launch(this@SearchActivity, key)
            } else {
                Log.d("searchKey", key)
                ToastUtil.showShort(this@SearchActivity, "请输入搜索关键词")
            }
        }
    }

    override fun createObserve() {
        super.createObserve()
        mViewModel.apply {
            hotSearchList.observe(this@SearchActivity){
                mHotSearchAdapter.setList(it)
            }
            searchHistoryData.observe(this@SearchActivity){
                mSearchHistoryAdapter.setList(it)
                CacheManager.saveSearchHistoryData(it)
            }
        }
    }

    /**
     * 更新搜索关键词
     * @param searchKey 最近的搜索关键词
     */
    private fun updateSearchKey(searchKey: String){
        mViewModel.searchHistoryData.value?.apply {
            if (contains(searchKey)){
                // 当搜索历史中包含该关键词时，先将旧的删除
                remove(searchKey)
            }else if(size >= 10){
                // 如果历史条数有10个了就删除最后一个
                removeLast()
            }
            // 添加到队列头部，作为最新的搜索记录
            addFirst(searchKey)
            mViewModel.searchHistoryData.value = this
        }
    }
}