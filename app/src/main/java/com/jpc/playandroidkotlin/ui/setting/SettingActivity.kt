package com.jpc.playandroidkotlin.ui.setting

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.OnBackPressedDispatcher
import com.jpc.library_base.ext.showDialog
import com.jpc.library_base.http.RetrofitManager
import com.jpc.library_base.utils.AppUtil
import com.jpc.library_base.utils.CacheUtil
import com.jpc.playandroidkotlin.R
import com.jpc.playandroidkotlin.base.BaseActivity
import com.jpc.playandroidkotlin.base.MyApplication
import com.jpc.playandroidkotlin.data.bean.Banner
import com.jpc.playandroidkotlin.data.local.UserManager
import com.jpc.playandroidkotlin.databinding.ActivitySettingBinding
import com.jpc.playandroidkotlin.ui.web.WebActivity

/**
 * 设置界面
 */
class SettingActivity :
    BaseActivity<SettingViewModel, ActivitySettingBinding>(R.layout.activity_setting) {

    companion object {
        /**
         * 页面启动
         * @param context Context
         */
        fun launch(context: Context) {
            context.startActivity(Intent(context, SettingActivity::class.java))
        }
    }

    override fun initView(savedInstanceState: Bundle?) {
        mBinding.apply {
            tvCacheNum.text = CacheUtil.getTotalCacheSize(this@SettingActivity)
            // 清理缓存
            layerClearCache.setOnClickListener {
                showDialog("确定清理缓存吗?", positiveButtonText = "清理", positiveAction = {
                    CacheUtil.clearAllCache(this@SettingActivity)
                    tvCacheNum.text = CacheUtil.getTotalCacheSize(this@SettingActivity)
                })
            }

            tvVersionName.text = AppUtil.getAppVersionName(this@SettingActivity)
            layerVersion.setOnClickListener { mViewModel.checkAppUpdate(true) }

            // 作者
            layerAuthor.setOnClickListener {
                showDialog(
                    "Q\tQ：1069663473\n\n微信：J1314\n\n邮箱：106911354373@qq.com",
                    "联系作者", negativeButtonText = ""
                )
            }

            // 项目源码
            tvSourceCode.setOnClickListener {
                WebActivity.launch(
                    this@SettingActivity,
                    Banner(title = "项目源码", url = "https://gitee.com/programming-xiaopeng/PlayAndroidKotlin")
                )
            }

            // 退出登录
            btnLogout.setOnClickListener {
                showDialog("确定退出登录吗?", positiveButtonText = "退出", positiveAction = {
                    // 手动清除cookie
                    RetrofitManager.cookieJar.clear()
                    UserManager.logout()
                    MyApplication.appViewModel.userEvent.value = null
                    onBackPressedDispatcher.onBackPressed()
                })
            }
        }
    }

}