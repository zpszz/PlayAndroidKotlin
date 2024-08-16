package com.jpc.playandroidkotlin.ui.main.mine

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.jpc.library_base.ext.initColors
import com.jpc.playandroidkotlin.R
import com.jpc.playandroidkotlin.base.BaseFragment
import com.jpc.playandroidkotlin.base.MyApplication
import com.jpc.playandroidkotlin.data.bean.Banner
import com.jpc.playandroidkotlin.data.local.UserManager
import com.jpc.playandroidkotlin.databinding.FragmentMineBinding
import com.jpc.playandroidkotlin.ext.launchCheckLogin
import com.jpc.playandroidkotlin.ui.collect.CollectActivity
import com.jpc.playandroidkotlin.ui.integral.rank.IntegralRankActivity
import com.jpc.playandroidkotlin.ui.login.LoginActivity
import com.jpc.playandroidkotlin.ui.setting.SettingActivity
import com.jpc.playandroidkotlin.ui.share.list.MyArticleActivity
import com.jpc.playandroidkotlin.ui.web.WebActivity

/**
 * 我的 Fragment
 */
class MineFragment : BaseFragment<MineViewModel, FragmentMineBinding>(R.layout.fragment_mine) {

    override fun initView() {
        mBinding.apply {
            swipeRefreshLayout.apply {
                initColors()
                setOnRefreshListener{onRefresh()}
            }
            clUser.setOnClickListener {
                if (!UserManager.isLogin()){
                    LoginActivity.launch(requireContext())
                }
            }
            // 我的积分
            tvPoints.setOnClickListener {
                IntegralRankActivity.launch(requireContext())
            }

            // 我的收藏（需要登录）
            tvCollect.setOnClickListener {
                requireContext().launchCheckLogin { CollectActivity.launch(it) }
            }

            // 我分享的文章（需要登录）
            tvArticle.setOnClickListener {
                requireContext().launchCheckLogin { MyArticleActivity.launch(it) }
            }

            // 开源网站
            tvWeb.setOnClickListener {
                WebActivity.launch(
                    requireContext(),
                    Banner(title = "玩Android网站", url = "https://www.wanandroid.com/")
                )
            }

            // 设置
            tvSettings.setOnClickListener { SettingActivity.launch(requireContext()) }
        }
        onRefresh()
    }

    @SuppressLint("SetTextI18n")
    override fun createObserve() {
        super.createObserve()
        MyApplication.appViewModel.userEvent.observe(this) {
            mViewModel.user.set(it)
            if (it == null) {
                mViewModel.integral.value = null
            }
            onRefresh()
        }

        mViewModel.integral.observe(this) {
            mBinding.apply {
                swipeRefreshLayout.isRefreshing = false
                tvInfo.text = "id：${it?.userId ?: '—'}　排名：${it?.rank ?: '—'}"
                tvPointsNum.text = "${it?.coinCount ?: '—'}"
            }
        }
    }

    override fun requestError(msg: String?) {
        super.requestError(msg)
        mBinding.swipeRefreshLayout.isRefreshing = false
    }

    private fun onRefresh(){
        if (UserManager.isLogin()){
            mBinding.swipeRefreshLayout.apply {
                isEnabled = true
                isRefreshing = true
            }
            mViewModel.fetchPoints()
        }else{
            mBinding.swipeRefreshLayout.isEnabled = false
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() = MineFragment()
    }
}