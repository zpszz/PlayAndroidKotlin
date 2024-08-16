package com.jpc.playandroidkotlin.ui.main.project

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
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

/**
 * 项目Tab下的子Fragment
 */
class ProjectChildFragment :
    BaseFragment<ProjectChildViewModel, IncludeSwiperefreshRecyclerviewBinding>(
        R.layout.include_swiperefresh_recyclerview
    ) {
    // 列表项总条数
    private var mTotalCount: Int = 0

    // 页码
    private var mPageNo: Int = 0

    // 当前列表项条数
    private var mCurrentCount: Int = 0

    private val mAdapter by lazy { ImageArticleAdapter() }

    companion object {
        // 最新项目模块
        private const val IS_NEW = "isNew"

        // 项目分类id
        private const val CATEGORY_ID = "category_id"

        /**
         * 创建实例
         * @param isNew 是否是最新项目tab
         * @param categoryId 项目分类id
         */
        fun newInstance(isNew: Boolean = false, categoryId: Int = 0): Fragment{
            return ProjectChildFragment().apply {
                arguments = Bundle().apply {
                    putBoolean(IS_NEW, isNew)
                    putInt(CATEGORY_ID, categoryId)
                }
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
                            R.id.tv_author -> AuthorActivity.launch(
                                requireContext(),
                                mAdapter.getItem(position).userId
                            )
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
        // 收藏监听
        MyApplication.appViewModel.collectEvent.observe(viewLifecycleOwner) {
            for (position in mAdapter.data.indices) {
                if (mAdapter.data[position].id == it.id) {
                    mAdapter.data[position].collect = it.collect
                    mAdapter.notifyItemChanged(position)
                    break
                }
            }
        }

        // 用户退出时，收藏应全为false，登录时获取collectIds
        MyApplication.appViewModel.userEvent.observe(this) {
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
                if (list.size < ProjectChildViewModel.PAGE_SIZE || mCurrentCount == mTotalCount) {
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

    // 根据板块获取文章分页列表
    private fun fetchArticlePageList(pageNo: Int = if (arguments?.getBoolean(IS_NEW) == true) 0 else 1) {
        if (arguments?.getBoolean(IS_NEW) == true) {
            mViewModel.fetchNewProjectPageList(pageNo)
        } else {
            arguments?.getInt(CATEGORY_ID)?.let { mViewModel.fetchProjectPageList(pageNo, it) }
        }
    }

    // 下拉刷新
    private fun onRefresh() {
        mBinding.swipeRefreshLayout.isRefreshing = true
        mAdapter.loadMoreModule.isEnableLoadMore = false
        fetchArticlePageList()
    }

    // 上拉加载更多
    private fun loadMoreData() {
        // 上拉加载时禁止下拉刷新
        mBinding.swipeRefreshLayout.isEnabled = false
        fetchArticlePageList(++mPageNo)
    }
}