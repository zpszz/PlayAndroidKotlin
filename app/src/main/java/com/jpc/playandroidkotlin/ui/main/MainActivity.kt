package com.jpc.playandroidkotlin.ui.main

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import com.jpc.library_base.utils.ToastUtil
import com.jpc.playandroidkotlin.R
import com.jpc.playandroidkotlin.base.BaseActivity
import com.jpc.playandroidkotlin.databinding.ActivityMainBinding
import com.jpc.playandroidkotlin.ext.clearLongClickToast
import com.jpc.playandroidkotlin.ui.main.home.HomeFragment
import com.jpc.playandroidkotlin.ui.main.mine.MineFragment
import com.jpc.playandroidkotlin.ui.main.project.ProjectFragment
import com.jpc.playandroidkotlin.ui.main.square.SquareFragment
import com.jpc.playandroidkotlin.ui.main.wechat.WechatFragment

// 主Activity
class MainActivity : BaseActivity<MainViewModel, ActivityMainBinding>(R.layout.activity_main) {
    companion object {
        // 记录修改配置（如页面旋转）前navBar的位置的常量
        private const val CURRENT_NAV_POSITION = "currentNavPosition"
        fun launch(context: Context) {
            context.startActivity(Intent(context, MainActivity::class.java))
        }
    }

    private val mHomeFragment: HomeFragment by lazy { HomeFragment.newInstance() }
    private val mProjectFragment: ProjectFragment by lazy { ProjectFragment.newInstance() }
    private val mSquareFragment: SquareFragment by lazy { SquareFragment.newInstance() }
    private val mWeChatFragment: WechatFragment by lazy { WechatFragment.newInstance() }
    private val mMineFragment: MineFragment by lazy { MineFragment.newInstance() }

    // 当前显示的Fragment
    private var mCurrentFragment: Fragment = mHomeFragment
    private var mCurrentNavPosition = 0

    override fun initView(savedInstanceState: Bundle?) {
        supportFragmentManager.beginTransaction()
            .add(R.id.fl_container, mHomeFragment, "HomeFragment")
            .commitAllowingStateLoss()

        // 修改配置时(例如页面旋转、深浅模式切换等)的页面恢复
        savedInstanceState?.let { state ->
            when (state.getInt(CURRENT_NAV_POSITION)) {
                0 -> R.id.menu_home
                1 -> R.id.menu_project
                2 -> R.id.menu_square
                3 -> R.id.menu_wechat
                else -> R.id.menu_mine
            }.let {
                mBinding.bottomNavigationView.selectedItemId = it
                onNavBarItemSelected(it)
            }
        }

        // 导航Tab
        mBinding.bottomNavigationView.apply {
            // 处理BottomNavigationView的Item长按出现Toast的问题
            clearLongClickToast(
                mutableListOf(
                    R.id.menu_home,
                    R.id.menu_project,
                    R.id.menu_square,
                    R.id.menu_wechat,
                    R.id.menu_mine
                )
            )
            setOnItemSelectedListener {
                return@setOnItemSelectedListener onNavBarItemSelected(it.itemId)
            }
        }

        // 返回键处理
        onBackPressedDispatcher.addCallback(this, mBackPressedCallback)
    }

    /**
     * bottomNavigationView切换时调用的方法
     *
     * @param itemId 切换Item的id
     */
    private fun onNavBarItemSelected(itemId: Int): Boolean {
        when (itemId) {
            R.id.menu_home -> {
                mCurrentNavPosition = 0
                switchFragment(mHomeFragment)
                return true
            }

            R.id.menu_project -> {
                mCurrentNavPosition = 1
                switchFragment(mProjectFragment)
                return true
            }

            R.id.menu_square -> {
                mCurrentNavPosition = 2
                switchFragment(mSquareFragment)
                return true
            }

            R.id.menu_wechat -> {
                mCurrentNavPosition = 3
                switchFragment(mWeChatFragment)
                return true
            }

            else -> {
                mCurrentNavPosition = 4
                switchFragment(mMineFragment)
                return true
            }
        }
    }

    /**
     * 切换Fragment
     * @param fragment 要切换到的Fragment
     */
    private fun switchFragment(fragment: Fragment) {
        if (fragment != mCurrentFragment) {
            val fragmentTransaction = supportFragmentManager.beginTransaction()
            // 隐藏当前的Fragment
            fragmentTransaction.hide(mCurrentFragment)
            if (!fragment.isAdded) {
                // 存入Tag，便于获取
                fragmentTransaction.add(R.id.fl_container, fragment, fragment.javaClass.simpleName)
                    .show(fragment)
            } else {
                fragmentTransaction.show(fragment)
            }
            // 执行提交
            fragmentTransaction.commitAllowingStateLoss()
            // 将当前的Fragment赋值为切换后的Fragment
            mCurrentFragment = fragment
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        // 为了解决Fragment界面重叠问题，可以注释掉本行代码或者传递一个空的Bundle
        super.onSaveInstanceState(Bundle())
        // 记录当前的Fragment位置，在屏幕旋转等操作后可以恢复
        outState.putInt(CURRENT_NAV_POSITION, mCurrentNavPosition)
    }

    // 记录上一次点击返回按键的时间
    private var lastBackMills: Long = 0

    // 返回监听回调
    private val mBackPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            if (System.currentTimeMillis() - lastBackMills > 2000) {
                lastBackMills = System.currentTimeMillis()
                ToastUtil.showShort(
                    this@MainActivity,
                    getString(R.string.toast_double_back_exit)
                )
            } else {
                finish()
            }
        }
    }
}