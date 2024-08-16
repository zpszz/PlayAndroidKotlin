package com.jpc.playandroidkotlin.ui.search

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.module.LoadMoreModule
import com.chad.library.adapter.base.viewholder.BaseDataBindingHolder
import com.jpc.playandroidkotlin.R
import com.jpc.playandroidkotlin.databinding.ListItemRemoveBinding

class SearchHistoryAdapter :
    BaseQuickAdapter<String, BaseDataBindingHolder<ListItemRemoveBinding>>(R.layout.list_item_remove),
    LoadMoreModule {
    init {
        setAnimationWithDefault(AnimationType.AlphaIn)
    }

    override fun convert(holder: BaseDataBindingHolder<ListItemRemoveBinding>, item: String) {
        holder.dataBinding?.apply {
            this.item = item
            executePendingBindings()
        }
    }

}