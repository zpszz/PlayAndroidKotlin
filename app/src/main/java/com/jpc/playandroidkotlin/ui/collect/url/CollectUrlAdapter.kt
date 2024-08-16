package com.jpc.playandroidkotlin.ui.collect.url

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseDataBindingHolder
import com.jpc.playandroidkotlin.R
import com.jpc.playandroidkotlin.data.bean.CollectUrl
import com.jpc.playandroidkotlin.databinding.ListItemCollectUrlBinding
import com.jpc.playandroidkotlin.ui.web.WebActivity

/**
 * 收藏网址列表的Adapter
 */
class CollectUrlAdapter :
    BaseQuickAdapter<CollectUrl, BaseDataBindingHolder<ListItemCollectUrlBinding>>(layoutResId = R.layout.list_item_collect_url) {

    init {
        setAnimationWithDefault(AnimationType.ScaleIn)
    }

    override fun convert(holder: BaseDataBindingHolder<ListItemCollectUrlBinding>, item: CollectUrl) {
        holder.dataBinding?.apply {
            collectUrl = item
            executePendingBindings()

            clItem.setOnClickListener { WebActivity.launch(context, item) }
        }
    }
}