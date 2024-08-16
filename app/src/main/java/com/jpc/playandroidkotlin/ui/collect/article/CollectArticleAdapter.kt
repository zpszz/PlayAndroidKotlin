package com.jpc.playandroidkotlin.ui.collect.article

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.module.LoadMoreModule
import com.chad.library.adapter.base.viewholder.BaseDataBindingHolder
import com.jpc.playandroidkotlin.R
import com.jpc.playandroidkotlin.data.bean.CollectArticle
import com.jpc.playandroidkotlin.databinding.ListItemCollectArticleBinding
import com.jpc.playandroidkotlin.ui.web.WebActivity

/**
 * 收藏文章列表的Adapter
 */
class CollectArticleAdapter :
    BaseQuickAdapter<CollectArticle, BaseDataBindingHolder<ListItemCollectArticleBinding>>(
        layoutResId = R.layout.list_item_collect_article
    ), LoadMoreModule {

    init {
        setAnimationWithDefault(AnimationType.ScaleIn)
    }

    override fun convert(
        holder: BaseDataBindingHolder<ListItemCollectArticleBinding>,
        item: CollectArticle
    ) {
        holder.dataBinding?.apply {
            collectArticle = item
            executePendingBindings()

            clItem.setOnClickListener { WebActivity.launch(context, item) }
        }
    }
}