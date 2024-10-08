package com.jpc.playandroidkotlin.ui.collect

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.google.android.material.tabs.TabLayoutMediator
import com.jpc.playandroidkotlin.R
import com.jpc.playandroidkotlin.base.BaseActivity
import com.jpc.playandroidkotlin.databinding.ActivityCollectBinding
import com.jpc.playandroidkotlin.ui.collect.article.CollectArticleFragment
import com.jpc.playandroidkotlin.ui.collect.url.CollectUrlFragment

/**
 * 我的收藏，包含文章和网址两个Tab
 */
class CollectActivity : BaseActivity<CollectViewModel, ActivityCollectBinding>(R.layout.activity_collect) {
    private val mTitleList = arrayListOf("文章", "网址")
    private val mFragmentList: ArrayList<Fragment> = ArrayList()
    private lateinit var mTabLayoutMediator: TabLayoutMediator
    private lateinit var mFragmentStateAdapter: FragmentStateAdapter

    init {
        mFragmentList.add(CollectArticleFragment.newInstance())
        mFragmentList.add(CollectUrlFragment.newInstance())
    }

    companion object{
        fun launch(context: Context){
            context.startActivity(Intent(context, CollectActivity::class.java))
        }
    }

    override fun initView(savedInstanceState: Bundle?) {
        mFragmentStateAdapter = object : FragmentStateAdapter(supportFragmentManager, lifecycle) {
            override fun getItemCount(): Int {
                return mTitleList.size
            }

            override fun createFragment(position: Int): Fragment {
                return mFragmentList[position]
            }
        }
        mBinding.apply {
            viewPager2.apply {
                adapter = mFragmentStateAdapter
            }
            mTabLayoutMediator = TabLayoutMediator(tabLayout, viewPager2){tab, position ->
                tab.text = mTitleList[position]
            }.apply { attach() }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mTabLayoutMediator.detach()
    }
}