package com.jpc.playandroidkotlin.ui.main.home

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.jpc.library_base.data.bean.PageResponse
import com.jpc.library_base.ext.getEmptyView
import com.jpc.library_base.ext.initColors
import com.jpc.playandroidkotlin.R
import com.jpc.playandroidkotlin.base.BaseFragment
import com.jpc.playandroidkotlin.base.MyApplication
import com.jpc.playandroidkotlin.data.bean.Article
import com.jpc.playandroidkotlin.data.bean.Banner
import com.jpc.playandroidkotlin.data.bean.CollectData
import com.jpc.playandroidkotlin.databinding.FragmentHomeBinding
import com.jpc.playandroidkotlin.databinding.HeaderBannerBinding
import com.jpc.playandroidkotlin.ui.author.AuthorActivity
import com.jpc.playandroidkotlin.ui.search.SearchActivity
import com.youth.banner.indicator.CircleIndicator
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.launch

/**
 * 首页Fragment
 */
class HomeFragment : BaseFragment<HomeViewModel, FragmentHomeBinding>(R.layout.fragment_home) {
    // 列表总数
    private var mTotalCount: Int = 0

    // 页数
    private var mPageNumber: Int = 0

    // 当前列表数量
    private var mCurrentCount: Int = 0

    // 轮播图
    private val mBannerList = ArrayList<Banner>()
    private val mBannerAdapter = HomeBannerAdapter(mBannerList)
    private val mArticleAdapter by lazy { ArticleAdapter() }

    @SuppressLint("InflateParams")
    override fun initView() {
        val headerBannerBinding = DataBindingUtil.inflate<HeaderBannerBinding>(
            layoutInflater,
            R.layout.header_banner,
            null,
            false
        ).apply {
            banner.apply {
                setAdapter(mBannerAdapter)
                indicator = CircleIndicator(context)
                addBannerLifecycleObserver(viewLifecycleOwner)
            }
        }

        mBinding.apply {
            titleLayout.setRightView(R.drawable.ic_search) {
                // 跳转搜索界面
                SearchActivity.launch(requireContext())
            }
            includeList.apply {
                recyclerView.apply {
                    layoutManager = LinearLayoutManager(context)
                    adapter = mArticleAdapter.apply {
                        loadMoreModule.setOnLoadMoreListener { loadMoreData() }
                        addHeaderView(headerBannerBinding.root)
                        addChildClickViewIds(R.id.tv_author, R.id.iv_collect)
                        setOnItemChildClickListener{ _, view, position ->
                            when (view.id) {
                                // 查看作者的文章列表
                                R.id.tv_author -> {
                                    AuthorActivity.launch(requireContext(), mArticleAdapter.getItem(position).userId)
                                }
                                // 收藏与取消收藏
                                R.id.iv_collect -> {
                                    if (mArticleAdapter.getItem(position).collect) {
                                        // 已收藏，再次点击就是取消收藏
                                        mViewModel.unCollectArticle(mArticleAdapter.getItem(position).id) {
                                            mArticleAdapter.getItem(position).collect = false
                                            // 这里position需要+1，因为0位置是轮播图的位置HeaderView
                                            mArticleAdapter.notifyItemChanged(position + 1)
                                            MyApplication.appViewModel.collectEvent.setValue(
                                                CollectData(
                                                    mArticleAdapter.getItem(position).id,
                                                    collect = false
                                                )
                                            )
                                        }
                                    } else {
                                        mViewModel.collectArticle(mArticleAdapter.getItem(position).id) {
                                            // 手动更改可以避免刷新整个列表
                                            mArticleAdapter.getItem(position).collect = true
                                            mArticleAdapter.notifyItemChanged(position + 1)
                                            MyApplication.appViewModel.collectEvent.setValue(
                                                CollectData(
                                                    mArticleAdapter.getItem(position).id,
                                                    collect = true
                                                )
                                            )
                                        }
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
        // 每次切换到首页，主动刷新一下
        onRefresh()
    }


    @SuppressLint("NotifyDataSetChanged")
    override fun createObserve() {
        super.createObserve()
        mViewModel.apply {
            lifecycleScope.launch {
                bannerListStateFlow.flowWithLifecycle(lifecycle).drop(1).collect {
                    it.let {
                        mBannerList.apply {
                            clear()
                            addAll(it)
                        }
                    }
                    mBannerAdapter.notifyDataSetChanged()
                }
            }
            articlePageListLiveData.observe(viewLifecycleOwner) {
                it?.let { handleArticleData(it) }
            }
        }
        // 全局监听
        MyApplication.appViewModel.apply {
            // 收藏监听
            collectEvent.observe(viewLifecycleOwner) {
                for (position in mArticleAdapter.data.indices) {
                    if (mArticleAdapter.data[position].id == it.id) {
                        mArticleAdapter.data[position].collect = it.collect
                        mArticleAdapter.notifyItemChanged(position + 1)
                        break
                    }
                }
            }
            // 用户退出App后，收藏应该全部为false，登录时再获取已收藏记录
            userEvent.observe(this@HomeFragment) {
                if (it != null) {
                    it.collectIds.forEach { id ->
                        for (item in mArticleAdapter.data) {
                            if (id.toInt() == item.id) {
                                item.collect = false
                                break
                            }
                        }
                    }
                } else {
                    for (item in mArticleAdapter.data) {
                        item.collect = false
                    }
                }
                mArticleAdapter.notifyDataSetChanged()
            }
        }
    }

    /**
     * 文章分页数据处理
     *
     */
    private fun handleArticleData(pageResponse: PageResponse<Article>) {
        mPageNumber = pageResponse.curPage
        mTotalCount = pageResponse.pageCount
        val list = pageResponse.datas
        mArticleAdapter.apply {
            if (mPageNumber == 1) {
                if (list.isEmpty()) {
                    // 数据列表为空，就设置空列表布局视图
                    setEmptyView(recyclerView.getEmptyView())
                }
                // 加载第一页数据
                setList(list)
            } else {
                // 不是第一页就累加
                addData(list)
            }
            mCurrentCount = data.size
            loadMoreModule.apply {
                isEnableLoadMore = true
                if (list.size < HomeViewModel.PAGE_SIZE || mCurrentCount == mTotalCount) {
                    // 如果加载到的数据不够一页或都已加载完,显示没有更多数据布局,
                    // 当然后台接口不同分页方式判断方法不同,这个是比较通用的（通常都有TotalCount）
                    loadMoreEnd()
                } else {
                    loadMoreComplete()
                }
            }
            mBinding.includeList.swipeRefreshLayout.isEnabled = true
        }
        mBinding.includeList.swipeRefreshLayout.isRefreshing = false
    }

    // 下拉刷新
    private fun onRefresh() {
        mBinding.includeList.swipeRefreshLayout.isRefreshing = true
        // 防止下拉刷新时上拉加载
        mArticleAdapter.loadMoreModule.isEnableLoadMore = false
        mViewModel.apply {
            fetchBanners()
            fetchArticlePageList()
        }
    }

    // 下拉加载更多
    private fun loadMoreData() {
        // 禁止下拉刷新
        mBinding.includeList.swipeRefreshLayout.isEnabled = false
        // 当前页码+1
        mViewModel.fetchArticlePageList(++mPageNumber)
    }

    override fun requestError(msg: String?) {
        super.requestError(msg)
        mBinding.includeList.swipeRefreshLayout.isRefreshing = false
    }

    companion object {
        @JvmStatic
        fun newInstance() = HomeFragment()
    }
}