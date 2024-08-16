package com.jpc.playandroidkotlin.ui.search

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.module.LoadMoreModule
import com.chad.library.adapter.base.viewholder.BaseDataBindingHolder
import com.jpc.playandroidkotlin.R
import com.jpc.playandroidkotlin.data.bean.HotSearch
import com.jpc.playandroidkotlin.databinding.ListItemTvBinding

/**
 * 热门搜索列表的Adapter
 */
class HotSearchAdapter :
    BaseQuickAdapter<HotSearch, BaseDataBindingHolder<ListItemTvBinding>>(layoutResId = R.layout.list_item_tv),
    LoadMoreModule {

    init {
        setAnimationWithDefault(AnimationType.ScaleIn)
    }

    override fun convert(holder: BaseDataBindingHolder<ListItemTvBinding>, item: HotSearch) {
        holder.dataBinding?.apply {
            text = item.name
            executePendingBindings()
        }
    }
}