package com.jpc.playandroidkotlin.ui.main.square

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.jpc.playandroidkotlin.R
import com.jpc.playandroidkotlin.base.BaseFragment
import com.jpc.playandroidkotlin.databinding.FragmentSquareBinding

/**
 * 广场 Fragment
 */
class SquareFragment : BaseFragment<SquareViewModel, FragmentSquareBinding>(R.layout.fragment_square) {
    override fun initView() {
    }

    companion object {
        @JvmStatic
        fun newInstance() = SquareFragment()
    }
}