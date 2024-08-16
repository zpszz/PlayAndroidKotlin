package com.jpc.playandroidkotlin.ui.setting

import androidx.databinding.ObservableBoolean
import com.jpc.library_base.BaseApplication.Companion.context
import com.jpc.library_base.base.BaseViewModel
import com.jpc.library_base.utils.ToastUtil
import com.jpc.playandroidkotlin.data.local.UserManager
import com.tencent.bugly.beta.Beta

class SettingViewModel : BaseViewModel() {
    val showLogoutBtn = ObservableBoolean(false)
    val haveNewVersion = ObservableBoolean(false)

    override fun start() {
        showLogoutBtn.set(UserManager.isLogin())
        checkAppUpdate()
    }

    /**
     * APP更新检查
     * @param isManual 是否手动检查，默认为false
     */
    fun checkAppUpdate(isManual: Boolean = false) {
        Beta.checkUpgrade(isManual, !isManual)
        // 获取升级信息
        val upgradeInfo = Beta.getUpgradeInfo()
        if (upgradeInfo == null) {
            haveNewVersion.set(false)
            if (isManual) {
                ToastUtil.showShort(context, "你已经是最新版本")
            }
        } else {
            haveNewVersion.set(true)
        }
    }
}