package com.jpc.library_base

import android.app.Application
import android.content.Context
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStore
import androidx.lifecycle.ViewModelStoreOwner
import com.tencent.mmkv.MMKV
import java.lang.ref.WeakReference

/**
 * 这是一个基类Application，用于初始化一些全局配置
 */
open class BaseApplication: Application(), ViewModelStoreOwner{
    private var mFactory: ViewModelProvider.Factory? = null
    private val appViewModelStore: ViewModelStore by lazy { ViewModelStore() }
    companion object{
        private var weakContext: WeakReference<Context>? = null
        val context: Context
            get() = weakContext?.get() ?: throw IllegalStateException("context is null")
    }

    override fun onCreate() {
        super.onCreate()
        weakContext = WeakReference(applicationContext)
        // 初始化MMKV
        MMKV.initialize(this)
    }

    private fun getAppViewModelFactory(): ViewModelProvider.Factory{
        if (mFactory == null){
            mFactory = ViewModelProvider.AndroidViewModelFactory.getInstance(this)
        }
        return mFactory as ViewModelProvider.Factory
    }

    fun getAppViewModelProvider(): ViewModelProvider{
        return ViewModelProvider(this, getAppViewModelFactory())
    }
    // 重写接口中的成员变量
    override val viewModelStore: ViewModelStore
        get() = appViewModelStore
}