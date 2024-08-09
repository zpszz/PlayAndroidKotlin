package com.jpc.playandroidkotlin.ui.main.project

import android.annotation.SuppressLint
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.google.android.material.tabs.TabLayoutMediator
import com.jpc.library_base.ext.toHtml
import com.jpc.playandroidkotlin.R
import com.jpc.playandroidkotlin.base.BaseFragment
import com.jpc.playandroidkotlin.data.bean.ProjectTitle
import com.jpc.playandroidkotlin.databinding.FragmentViewpagerBinding

/**
 * 项目 Fragment
 */
class ProjectFragment :
    BaseFragment<ProjectViewModel, FragmentViewpagerBinding>(R.layout.fragment_viewpager) {
    // TabLayout的标题列表
    private val mProjectTitleList = ArrayList<ProjectTitle>()
    private val mTitleList = ArrayList<String>()

    // TabLayoutMediator是TabLayout和ViewPager2的中介者
    private lateinit var mTabLayoutMediator: TabLayoutMediator

    // 这是ViewPager2的适配器
    private lateinit var mFragmentStateAdapter: FragmentStateAdapter

    override fun initView() {
        mFragmentStateAdapter = object : FragmentStateAdapter(parentFragmentManager, lifecycle) {
            override fun getItemCount(): Int {
                return mTitleList.size
            }

            override fun createFragment(position: Int): Fragment {
                return if (position == 0) ProjectChildFragment.newInstance(true)
                else ProjectChildFragment.newInstance(categoryId = mProjectTitleList[position - 1].id)
            }

        }
        mBinding.apply {
            // 由于标题也需要请求（只有请求完标题后才会加载Fragment从而显示swipeRefreshLayout），
            // 所以在请求标题之前也需要一个loading
            showLoading = true
            viewPager2.apply {
                adapter = mFragmentStateAdapter
            }
            // 将TabLayout与ViewPager2联动
            mTabLayoutMediator = TabLayoutMediator(tabLayout, viewPager2) { tab, position ->
                tab.apply {
                    // 处理长按tab出现Toast的问题
                    view.setOnLongClickListener { true }
                    text = mTitleList[position]
                }
            }.apply { attach() }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun createObserve() {
        super.createObserve()
        mViewModel.apply {
            projectTitleLiveData.observe(viewLifecycleOwner) { list ->
                mBinding.showLoading = false
                mProjectTitleList.apply {
                    clear()
                    list?.let { addAll(it) }
                }
                mTitleList.apply {
                    clear()
                    mTitleList.add("最新项目")
                }
                list?.forEach { projectTitle ->
                    // 为什么要调用toHtml()方法？ 因为项目标题中可能包含html标签，需要转义
                    mTitleList.add(projectTitle.name.toHtml().toString())
                }
                mFragmentStateAdapter.notifyDataSetChanged()
                // 这里的方案是直接缓存所有的子Fragment，然后让子Fragment懒加载数据
                mBinding.viewPager2.offscreenPageLimit = mTitleList.size
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        // 解除TabLayout与ViewPager2的联动，避免内存泄漏
        // 一般来说，只要在Fragment中使用了TabLayoutMediator，就需要在Fragment销毁时调用detach()方法
        mTabLayoutMediator.detach()
    }

    companion object {
        @JvmStatic // 作用是告诉编译器，这个方法是静态方法，可以通过类名直接调用
        fun newInstance() = ProjectFragment()
    }
}