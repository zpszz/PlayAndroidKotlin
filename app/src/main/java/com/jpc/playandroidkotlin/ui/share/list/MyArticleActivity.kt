package com.jpc.playandroidkotlin.ui.share.list

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.jpc.library_base.data.bean.PageResponse
import com.jpc.library_base.ext.getEmptyView
import com.jpc.library_base.ext.initColors
import com.jpc.library_base.ext.showDialog
import com.jpc.playandroidkotlin.R
import com.jpc.playandroidkotlin.base.BaseActivity
import com.jpc.playandroidkotlin.base.MyApplication
import com.jpc.playandroidkotlin.data.bean.Article
import com.jpc.playandroidkotlin.databinding.ActivityIntegralRecordBinding
import com.jpc.playandroidkotlin.ui.share.add.AddArticleActivity

/**
 * 我分享的文章列表
 */
class MyArticleActivity :
    BaseActivity<MyArticleViewModel, ActivityIntegralRecordBinding>(R.layout.activity_integral_record) {

    /** 页数 */
    private var mPageNo: Int = 0
    private val mAdapter by lazy { MyArticleAdapter() }

    companion object {

        /**
         * 页面启动
         * @param context Context
         */
        fun launch(context: Context) {
            context.startActivity(Intent(context, MyArticleActivity::class.java))
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun initView(savedInstanceState: Bundle?) {
        mBinding.apply {
            titleLayout.setTitleText("我分享的文章")
                .setRightView(R.drawable.ic_add) { AddArticleActivity.launch(this@MyArticleActivity) }

            includeList.apply {
                recyclerView.apply {
                    layoutManager = LinearLayoutManager(context)
                    adapter = mAdapter.apply {
                        loadMoreModule.setOnLoadMoreListener { loadMoreData() }
                        addChildClickViewIds(R.id.iv_delete)
                        setOnItemChildClickListener { _, view, position ->
                            when (view.id) {
                                R.id.iv_delete ->
                                    showDialog(
                                        "确定删除该文章吗？",
                                        positiveButtonText = "删除",
                                        positiveAction = {
                                            mViewModel.deleteMyShareArticle(getItem(position).id) {
                                                if (data.size == 1) { // 只剩一个再删除就空了
                                                    setEmptyView(recyclerView.getEmptyView())
                                                }
                                                removeAt(position)
                                            }
                                        }
                                    )
                            }
                        }
                    }
                }

                swipeRefreshLayout.apply {
                    initColors()
                    setOnRefreshListener { onRefresh() }
                }
            }
        }

        onRefresh()
    }

    /**下拉刷新 */
    private fun onRefresh() {
        mBinding.includeList.swipeRefreshLayout.isRefreshing = true
        // 这里的作用是防止下拉刷新的时候还可以上拉加载
        mAdapter.loadMoreModule.isEnableLoadMore = false
        mViewModel.fetchMyShareArticlePageList()
    }

    /** 上拉加载更多 */
    private fun loadMoreData() {
        // 上拉加载时禁止下拉刷新
        mBinding.includeList.swipeRefreshLayout.isEnabled = false
        mViewModel.fetchMyShareArticlePageList(++mPageNo)
    }

    override fun createObserve() {
        super.createObserve()
        mViewModel.articlePageList.observe(this) {
            handleData(it)
        }
        MyApplication.appViewModel.shareArticleEvent.observe(this) {
            onRefresh()
        }
    }

    /**
     * 分页数据处理
     *
     * @param pageResponse
     */
    private fun handleData(pageResponse: PageResponse<Article>) {
        mPageNo = pageResponse.curPage
        val list = pageResponse.datas
        mAdapter.apply {
            if (mPageNo == 1) {
                if (list.isEmpty()) {
                    setEmptyView(recyclerView.getEmptyView())
                }
                // 如果是加载的第一页数据，用setList()
                setList(list)
            } else {
                // 不是第一页，则用add
                addData(list)
            }
            loadMoreModule.apply {
                isEnableLoadMore = true
                if (pageResponse.over) {
                    loadMoreEnd()
                } else {
                    loadMoreComplete()
                }
            }
            mBinding.includeList.swipeRefreshLayout.isEnabled = true
        }
        mBinding.includeList.swipeRefreshLayout.isRefreshing = false
    }
}