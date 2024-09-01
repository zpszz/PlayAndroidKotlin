package com.jpc.playandroidkotlin.base

import com.jpc.library_base.BaseApplication
import com.tencent.bugly.Bugly

/**
 * 应用的Application，主要是为了创建AppViewModel的实例
 */
class MyApplication : BaseApplication(){
    companion object{
        lateinit var appViewModel: AppViewModel
    }

    override fun onCreate() {
        super.onCreate()
        appViewModel = getAppViewModelProvider()[AppViewModel::class.java]
        // Bugly初始化
        Bugly.init(applicationContext, "99ff7c64d9", false)
    }
}