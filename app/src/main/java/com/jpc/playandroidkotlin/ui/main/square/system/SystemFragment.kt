package com.jpc.playandroidkotlin.ui.main.square.system

import androidx.recyclerview.widget.LinearLayoutManager
import com.jpc.library_base.ext.getEmptyView
import com.jpc.library_base.ext.initColors
import com.jpc.playandroidkotlin.R
import com.jpc.playandroidkotlin.base.BaseFragment
import com.jpc.playandroidkotlin.databinding.IncludeSwiperefreshRecyclerviewBinding

/**
 * 广场tab下的体系
 */
class SystemFragment : BaseFragment<SystemViewModel, IncludeSwiperefreshRecyclerviewBinding>(R.layout.include_swiperefresh_recyclerview){
    private val mAdapter by lazy { SystemAdapter() }

    companion object{
        fun newInstance() = SystemFragment()
    }

    override fun initView() {
        mBinding.apply {
            recyclerView.apply {
                layoutManager = LinearLayoutManager(context)
                adapter = mAdapter
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

    override fun createObserve() {
        super.createObserve()
        mViewModel.structureListLiveData.observe(viewLifecycleOwner) {
            mBinding.swipeRefreshLayout.isRefreshing = false
            mAdapter.apply {
                if (it.isEmpty()) {
                    setEmptyView(mBinding.recyclerView.getEmptyView())
                } else {
                    setList(it)
                }
            }
        }
    }

    /**下拉刷新 */
    private fun onRefresh() {
        mBinding.swipeRefreshLayout.isRefreshing = true
        mViewModel.fetchSystemList()
    }

    override fun requestError(msg: String?) {
        super.requestError(msg)
        mBinding.swipeRefreshLayout.isRefreshing = false
    }
}