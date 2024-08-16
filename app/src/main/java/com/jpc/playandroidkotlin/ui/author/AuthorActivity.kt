package com.jpc.playandroidkotlin.ui.author

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.OnBackPressedDispatcher
import androidx.recyclerview.widget.LinearLayoutManager
import com.jpc.library_base.data.bean.PageResponse
import com.jpc.library_base.ext.getEmptyView
import com.jpc.library_base.ext.initClose
import com.jpc.library_base.ext.initColors
import com.jpc.playandroidkotlin.R
import com.jpc.playandroidkotlin.base.BaseActivity
import com.jpc.playandroidkotlin.base.MyApplication
import com.jpc.playandroidkotlin.data.bean.Article
import com.jpc.playandroidkotlin.data.bean.CollectData
import com.jpc.playandroidkotlin.databinding.ActivityAuthorBinding
import com.jpc.playandroidkotlin.ui.main.home.ArticleAdapter

class AuthorActivity : BaseActivity<AuthorViewModel, ActivityAuthorBinding>(R.layout.activity_author) {
    /** 作者Id */
    private var mAuthorId = 0

    /** 页数 */
    private var mPageNo: Int = 0
    private val mAdapter by lazy { ArticleAdapter() }

    companion object {
        private const val EXTRA_AUTHOR_ID: String = "authorId"

        /**
         * 页面启动
         * @param context Context
         * @param authorId 作者Id
         */
        fun launch(context: Context, authorId: Int) {
            context.startActivity(Intent(context, AuthorActivity::class.java).apply {
                putExtra(EXTRA_AUTHOR_ID, authorId)
            })
        }
    }

    override fun initView(savedInstanceState: Bundle?) {
        mAuthorId = intent.getIntExtra(EXTRA_AUTHOR_ID, 0)

        mBinding.apply {
            toolbar.initClose { onBackPressedDispatcher.onBackPressed() }

            recyclerView.apply {
                layoutManager = LinearLayoutManager(context)
                adapter = mAdapter.apply {
                    loadMoreModule.setOnLoadMoreListener { loadMoreData() }
                    addChildClickViewIds(R.id.iv_collect)
                    setOnItemChildClickListener { _, _, position ->
                        if (mAdapter.getItem(position).collect) {
                            mViewModel.unCollectArticle(mAdapter.getItem(position).id) {
                                // 取消收藏成功后,手动更改避免刷新整个列表
                                mAdapter.getItem(position).collect = false
                                mAdapter.notifyItemChanged(position)
                                MyApplication.appViewModel.collectEvent.setValue(
                                    CollectData(
                                        mAdapter.getItem(
                                            position
                                        ).id, collect = false
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
                                        mAdapter.getItem(
                                            position
                                        ).id, collect = true
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
        onRefresh()
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun createObserve() {
        super.createObserve()
        mViewModel.apply {
            articlePageList.observe(this@AuthorActivity) {
                it?.let { handleArticleData(it) }
            }
        }
    }

    /**
     * 文章分页数据处理
     *
     * @param pageResponse
     */
    private fun handleArticleData(pageResponse: PageResponse<Article>) {
        mPageNo = pageResponse.curPage
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


    /**下拉刷新 */
    private fun onRefresh() {
        mBinding.swipeRefreshLayout.isRefreshing = true
        // 这里的作用是防止下拉刷新的时候还可以上拉加载
        mAdapter.loadMoreModule.isEnableLoadMore = false
        mViewModel.apply {
            fetchShareArticlePageList(mAuthorId) {
                mAdapter.setEmptyView(
                    mBinding.recyclerView.getEmptyView("该用户不存在")
                )
                mBinding.swipeRefreshLayout.isRefreshing = false
                true
            }
        }
    }

    /** 下拉加载更多 */
    private fun loadMoreData() {
        // 上拉加载时禁止下拉刷新
        mBinding.swipeRefreshLayout.isEnabled = false
        mViewModel.fetchShareArticlePageList(mAuthorId, ++mPageNo)
    }
}