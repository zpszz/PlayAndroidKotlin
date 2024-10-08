package com.jpc.playandroidkotlin.ui.main.square.system.details

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
 * 广场Tab下的体系
 */
class SystemArticleChildFragment :
    BaseFragment<SystemArticleChildViewModel, IncludeSwiperefreshRecyclerviewBinding>(R.layout.include_swiperefresh_recyclerview) {

    /** 列表总数 */
    private var mTotalCount: Int = 0

    /** 页数 */
    private var mPageNo: Int = 0

    /** 当前列表的数量 */
    private var mCurrentCount: Int = 0

    private val mCategoryId by lazy { arguments?.getInt(EXTRA_CATEGORY_ID) ?: 0 }

    private val mAdapter by lazy { ArticleAdapter() }

    companion object {
        private const val EXTRA_CATEGORY_ID = "extra_category_id"

        fun newInstance(categoryId: Int) = SystemArticleChildFragment().apply {
            arguments = Bundle().apply { putInt(EXTRA_CATEGORY_ID, categoryId) }
        }
    }

    override fun initView() {
        mBinding.apply {
            recyclerView.apply {
                layoutManager = LinearLayoutManager(context)
                adapter = mAdapter.apply {
                    loadMoreModule.setOnLoadMoreListener { loadMoreData() }
                    addChildClickViewIds(
                        R.id.tv_author,
                        R.id.iv_collect
                    )
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
                                        // 注意:这里position需要+1,因为0位置属于轮播图HeaderView
                                        mAdapter.notifyItemChanged(position + 1)
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
                                        mAdapter.notifyItemChanged(position + 1)
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
            }

            swipeRefreshLayout.apply {
                initColors()
                setOnRefreshListener { onRefresh() }
            }

        }
    }

    override fun lazyLoadData() {
        super.lazyLoadData()
        onRefresh()
    }

    override fun createObserve() {
        super.createObserve()
        mViewModel.articlePageListLiveData.observe(viewLifecycleOwner) {
            it?.let { handleArticleData(it) }
        }
    }

    /**
     * 文章分页数据处理
     *
     * @param pageResponse
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
                // 如果是加载的第一页数据，用 setData()
                setList(list)
            } else {
                // 不是第一页，则用add
                addData(list)
            }
            mCurrentCount = data.size
            loadMoreModule.apply {
                isEnableLoadMore = true
                if (list.size < SystemArticleChildViewModel.PAGE_SIZE || mCurrentCount == mTotalCount) {
                    // 如果加载到的数据不够一页或都已加载完,显示没有更多数据布局,
                    // 当然后台接口不同分页方式判断方法不同,这个是比较通用的（通常都有TotalCount）
                    loadMoreEnd()
                } else {
                    loadMoreComplete()
                }
            }
            mBinding.swipeRefreshLayout.isEnabled = true
        }
        mBinding.swipeRefreshLayout.isRefreshing = false
    }


    /**下拉刷新 */
    private fun onRefresh() {
        mBinding.swipeRefreshLayout.isRefreshing = true
        // 这里的作用是防止下拉刷新的时候还可以上拉加载
        mAdapter.loadMoreModule.isEnableLoadMore = false
        mViewModel.apply { fetchArticlePageList(categoryId = mCategoryId) }
    }

    /** 上拉加载更多 */
    private fun loadMoreData() {
        // 上拉加载时禁止下拉刷新
        mBinding.swipeRefreshLayout.isEnabled = false
        mViewModel.fetchArticlePageList(++mPageNo, mCategoryId)
    }

    override fun requestError(msg: String?) {
        super.requestError(msg)
        mBinding.swipeRefreshLayout.isRefreshing = false
    }
}