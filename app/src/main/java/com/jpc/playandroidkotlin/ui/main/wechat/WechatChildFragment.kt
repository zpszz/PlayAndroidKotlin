package com.jpc.playandroidkotlin.ui.main.wechat

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.jpc.library_base.data.bean.PageResponse
import com.jpc.library_base.ext.getEmptyView
import com.jpc.library_base.ext.initColors
import com.jpc.playandroidkotlin.R
import com.jpc.playandroidkotlin.base.BaseFragment
import com.jpc.playandroidkotlin.base.MyApplication
import com.jpc.playandroidkotlin.data.bean.Article
import com.jpc.playandroidkotlin.data.bean.CollectData
import com.jpc.playandroidkotlin.databinding.IncludeSwiperefreshRecyclerviewBinding
import com.jpc.playandroidkotlin.ui.author.AuthorActivity
import com.jpc.playandroidkotlin.ui.main.home.ArticleAdapter

/**
 * 公众号Tab下的子Fragment
 */
class WechatChildFragment :
    BaseFragment<WechatChildViewModel, IncludeSwiperefreshRecyclerviewBinding>(R.layout.include_swiperefresh_recyclerview) {

    /** 列表总数 */
    private var mTotalCount: Int = 0

    /** 页数 */
    private var mPageNo: Int = 0

    /** 当前列表的数量 */
    private var mCurrentCount: Int = 0

    private val mAdapter by lazy { ArticleAdapter() }

    companion object {
        private const val AUTHOR_ID = "authorId"

        /**
         * 创建实例
         *
         * @param authorId 公众号作者Id
         */
        fun newInstance(authorId: Int) =
            WechatChildFragment().apply {
                arguments = Bundle().apply {
                    putInt(AUTHOR_ID, authorId)
                }
            }
    }

    override fun initView() {
        mBinding.apply {
            recyclerView.apply {
                layoutManager = LinearLayoutManager(context)
                adapter = mAdapter.apply {
                    loadMoreModule.setOnLoadMoreListener { loadMoreData() }
                    addChildClickViewIds(R.id.tv_author, R.id.iv_collect)
                    setOnItemChildClickListener { _, view, position ->
                        when (view.id) {
                            // 查看作者文章列表
                            R.id.tv_author ->
                                AuthorActivity.launch(
                                    requireContext(),
                                    mAdapter.getItem(position).userId
                                )
                            // 收藏与取消收藏
                            R.id.iv_collect ->
                                if (mAdapter.getItem(position).collect) {
                                    mViewModel.unCollectArticle(mAdapter.getItem(position).id) {
                                        // 取消收藏成功后,手动更改避免刷新整个列表
                                        mAdapter.getItem(position).collect = false
                                        mAdapter.notifyItemChanged(position)
                                        MyApplication.appViewModel.collectEvent.setValue(
                                            CollectData(
                                                mAdapter.getItem(position).id,
                                                collect = false
                                            )
                                        )
                                    }
                                } else {
                                    mViewModel.collectArticle(mAdapter.getItem(position).id) {
                                        // 收藏成功后,手动更改避免刷新整个列表
                                        mAdapter.getItem(position).collect = true
                                        mAdapter.notifyItemChanged(position)
                                        MyApplication.appViewModel.collectEvent.setValue(
                                            CollectData(
                                                mAdapter.getItem(position).id,
                                                collect = true
                                            )
                                        )
                                    }
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
    }

    override fun lazyLoadData() {
        super.lazyLoadData()
        onRefresh()
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun createObserve() {
        super.createObserve()
        mViewModel.apply {
            articlePageListLiveData.observe(viewLifecycleOwner) {
                it?.let { handleArticleData(it) }
            }
        }

        MyApplication.appViewModel.apply {
            collectEvent.observe(viewLifecycleOwner) {
                for (position in mAdapter.data.indices) {
                    if (mAdapter.data[position].id == it.id) {
                        mAdapter.data[position].collect = it.collect
                        mAdapter.notifyItemChanged(position)
                        break
                    }
                }
            }

            // 用户退出时，收藏应全为false，登录时获取collectIds
            userEvent.observe(viewLifecycleOwner) {
                if (it != null) {
                    it.collectIds.forEach { id ->
                        for (item in mAdapter.data) {
                            if (id.toInt() == item.id) {
                                item.collect = true
                                break
                            }
                        }
                    }
                } else {
                    for (item in mAdapter.data) {
                        item.collect = false
                    }
                }
                mAdapter.notifyDataSetChanged()
            }
        }
    }

    /**
     * 文章分页数据处理
     *
     * @param pageResponse PageResponse
     */
    private fun handleArticleData(pageResponse: PageResponse<Article>) {
        mPageNo = pageResponse.curPage
        mTotalCount = pageResponse.pageCount
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
            mCurrentCount = data.size
            loadMoreModule.apply {
                isEnableLoadMore = true
                if (pageResponse.over) {
                    loadMoreEnd()
                } else {
                    loadMoreComplete()
                }
            }
            mBinding.swipeRefreshLayout.isEnabled = true
        }
        mBinding.swipeRefreshLayout.isRefreshing = false
    }

    /**
     * 获取公众号作者文章分页列表
     */
    private fun fetchArticlePageList(pageNo: Int = 1) {
        arguments?.getInt(AUTHOR_ID)?.let { mViewModel.fetchAuthorArticlePageList(it, pageNo) }
    }

    /**下拉刷新 */
    private fun onRefresh() {
        mBinding.swipeRefreshLayout.isRefreshing = true
        // 这里的作用是防止下拉刷新的时候还可以上拉加载
        mAdapter.loadMoreModule.isEnableLoadMore = false
        fetchArticlePageList()
    }

    /** 上拉加载更多 */
    private fun loadMoreData() {
        // 上拉加载时禁止下拉刷新
        mBinding.swipeRefreshLayout.isEnabled = false
        fetchArticlePageList(++mPageNo)
    }
}