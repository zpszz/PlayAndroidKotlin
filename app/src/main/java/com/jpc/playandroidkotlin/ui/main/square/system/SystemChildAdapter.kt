package com.jpc.playandroidkotlin.ui.main.square.system

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.module.LoadMoreModule
import com.chad.library.adapter.base.viewholder.BaseDataBindingHolder
import com.jpc.playandroidkotlin.R
import com.jpc.playandroidkotlin.data.bean.Classify
import com.jpc.playandroidkotlin.databinding.ListItemTvBinding

/**
 * 体系分类列表的Adapter
 */
class SystemChildAdapter :
    BaseQuickAdapter<Classify, BaseDataBindingHolder<ListItemTvBinding>>(layoutResId = R.layout.list_item_tv),
    LoadMoreModule {

    init {
        setAnimationWithDefault(AnimationType.ScaleIn)
    }

    override fun convert(holder: BaseDataBindingHolder<ListItemTvBinding>, item: Classify) {
        holder.dataBinding?.apply {
            text = item.name
            executePendingBindings()
        }
    }
}