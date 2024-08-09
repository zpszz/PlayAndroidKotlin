package com.jpc.playandroidkotlin.ui.main.wechat

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.jpc.playandroidkotlin.R
import com.jpc.playandroidkotlin.base.BaseFragment
import com.jpc.playandroidkotlin.databinding.FragmentWeChatBinding

/**
 * 微信公众号 Fragment
 */
class WeChatFragment : BaseFragment<WeChatViewModel, FragmentWeChatBinding>(R.layout.fragment_we_chat) {

    override fun initView() {

    }

    companion object {
        @JvmStatic
        fun newInstance() = WeChatFragment()
    }
}