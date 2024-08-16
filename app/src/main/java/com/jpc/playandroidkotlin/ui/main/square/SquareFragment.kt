package com.jpc.playandroidkotlin.ui.main.square

import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayoutMediator
import com.jpc.library_base.utils.ScreenUtil
import com.jpc.playandroidkotlin.R
import com.jpc.playandroidkotlin.base.BaseFragment
import com.jpc.playandroidkotlin.databinding.FragmentViewpagerBinding
import com.jpc.playandroidkotlin.ext.launchCheckLogin
import com.jpc.playandroidkotlin.ui.main.square.ask.AskFragment
import com.jpc.playandroidkotlin.ui.main.square.navigation.NavigationFragment
import com.jpc.playandroidkotlin.ui.main.square.square.SquareChildFragment
import com.jpc.playandroidkotlin.ui.main.square.system.SystemFragment
import com.jpc.playandroidkotlin.ui.share.add.AddArticleActivity

/**
 * 广场 Fragment
 */
class SquareFragment : BaseFragment<SquareViewModel, FragmentViewpagerBinding>(R.layout.fragment_viewpager) {
    private val mTitleList = arrayListOf("广场", "每日一问", "体系", "导航")
    private val mFragmentList: ArrayList<Fragment> = ArrayList()
    private lateinit var mTabLayoutMediator: TabLayoutMediator
    private lateinit var mFragmentStateAdapter: FragmentStateAdapter

    init {
        mFragmentList.add(SquareChildFragment.newInstance())
        mFragmentList.add(AskFragment.newInstance())
        mFragmentList.add(SystemFragment.newInstance())
        mFragmentList.add(NavigationFragment.newInstance())
    }

    override fun initView() {
        mFragmentStateAdapter = object : FragmentStateAdapter(parentFragmentManager, lifecycle){
            override fun getItemCount(): Int {
                return mTitleList.size
            }

            override fun createFragment(position: Int): Fragment {
                return mFragmentList[position]
            }

        }

        mBinding.apply {
            tabLayout.layoutParams = ConstraintLayout.LayoutParams(tabLayout.layoutParams).apply {
                marginStart = ScreenUtil.dp2px(requireContext(), 40f)
                marginEnd = ScreenUtil.dp2px(requireContext(), 60f)
                topToTop = mBinding.root.top
                topMargin = ScreenUtil.dp2px(requireContext(), 6f)
            }
            viewPager2.apply {
                adapter = mFragmentStateAdapter
                offscreenPageLimit = mFragmentList.size
                registerOnPageChangeCallback(mPageChangeCallback)
            }
            mTabLayoutMediator = TabLayoutMediator(tabLayout, viewPager2){ tab, position ->
                tab.apply {
                    view.setOnLongClickListener{ true }
                    text = mTitleList[position]
                }
            }.apply { attach() }
        }
    }

    private val mPageChangeCallback = object : ViewPager2.OnPageChangeCallback(){
        override fun onPageSelected(position: Int) {
            super.onPageSelected(position)
            if (position == 0){
                mBinding.titleLayout.setRightView(R.drawable.ic_add){
                    // 添加文章分享
                    requireContext().launchCheckLogin { AddArticleActivity.launch(it) }
                }
            }else{
                mBinding.titleLayout.setRightView("")
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mTabLayoutMediator.detach()
        mBinding.viewPager2.unregisterOnPageChangeCallback(mPageChangeCallback)
    }

    companion object {
        @JvmStatic
        fun newInstance() = SquareFragment()
    }
}