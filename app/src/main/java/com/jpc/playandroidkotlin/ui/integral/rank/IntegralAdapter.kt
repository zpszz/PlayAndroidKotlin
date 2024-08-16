package com.jpc.playandroidkotlin.ui.integral.rank

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.module.LoadMoreModule
import com.chad.library.adapter.base.viewholder.BaseDataBindingHolder
import com.jpc.playandroidkotlin.R
import com.jpc.playandroidkotlin.data.bean.CoinInfo
import com.jpc.playandroidkotlin.databinding.ListItemIntegralBinding

/**
 * 积分排行列表的Adapter
 */
class IntegralAdapter :
    BaseQuickAdapter<CoinInfo, BaseDataBindingHolder<ListItemIntegralBinding>>(layoutResId = R.layout.list_item_integral),
    LoadMoreModule {

    init {
        setAnimationWithDefault(AnimationType.ScaleIn)
    }

    override fun convert(holder: BaseDataBindingHolder<ListItemIntegralBinding>, item: CoinInfo) {
        holder.dataBinding?.apply {
            integral = item
            executePendingBindings()
        }
    }
}