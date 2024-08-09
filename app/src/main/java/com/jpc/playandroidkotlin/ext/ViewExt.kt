package com.jpc.playandroidkotlin.ext

import android.content.Context
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.jpc.playandroidkotlin.data.local.UserManager
import com.jpc.playandroidkotlin.ui.login.LoginActivity

/**
 * 一些扩展函数
 */

/**
 * 需要验证登录状态的操作
 *
 * @param action 函数参数，已登录状态时的处理
 */
fun Context.launchCheckLogin(action: (context: Context) -> Unit) {
    if (UserManager.isLogin()) {
        action.invoke(this)
    } else {
        LoginActivity.launch(this)
    }
}

/**
 * 处理BottomNavigationView中的tab长按出现toast的问题
 *
 * @param ids tab项的id集
 */
fun BottomNavigationView.clearLongClickToast(ids: MutableList<Int>) {
    val bottomNavigationView: ViewGroup = getChildAt(0) as ViewGroup
    for (position in 0 until ids.size) {
        bottomNavigationView.getChildAt(position).findViewById<View>(ids[position])
            .setOnLongClickListener { true }
    }
}