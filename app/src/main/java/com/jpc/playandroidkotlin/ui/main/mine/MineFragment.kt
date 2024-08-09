package com.jpc.playandroidkotlin.ui.main.mine

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.jpc.playandroidkotlin.R
import com.jpc.playandroidkotlin.base.BaseFragment
import com.jpc.playandroidkotlin.databinding.FragmentMineBinding

/**
 * 我的 Fragment
 */
class MineFragment : BaseFragment<MineViewModel, FragmentMineBinding>(R.layout.fragment_mine) {

    override fun initView() {

    }

    companion object {
        @JvmStatic
        fun newInstance() = MineFragment()
    }
}