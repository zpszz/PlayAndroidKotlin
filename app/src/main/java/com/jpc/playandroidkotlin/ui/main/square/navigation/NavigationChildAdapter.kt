package com.jpc.playandroidkotlin.ui.main.square.navigation

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.module.LoadMoreModule
import com.chad.library.adapter.base.viewholder.BaseDataBindingHolder
import com.jpc.playandroidkotlin.R
import com.jpc.playandroidkotlin.data.bean.Article
import com.jpc.playandroidkotlin.databinding.ListItemTvBinding

/**
 * 导航列表项的Adapter
 */
class NavigationChildAdapter :
    BaseQuickAdapter<Article, BaseDataBindingHolder<ListItemTvBinding>>(R.layout.list_item_tv),
    LoadMoreModule {
    init {
        setAnimationWithDefault(AnimationType.AlphaIn)
    }

    override fun convert(holder: BaseDataBindingHolder<ListItemTvBinding>, item: Article) {
        holder.dataBinding?.apply {
            text = item.title
            executePendingBindings()
        }
    }

}