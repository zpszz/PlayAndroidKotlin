package com.jpc.playandroidkotlin.ui.share.list

import android.annotation.SuppressLint
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.module.LoadMoreModule
import com.chad.library.adapter.base.viewholder.BaseDataBindingHolder
import com.jpc.playandroidkotlin.R
import com.jpc.playandroidkotlin.data.bean.Article
import com.jpc.playandroidkotlin.databinding.ListItemShareArticleBinding
import com.jpc.playandroidkotlin.ui.web.WebActivity

/**
 * 积分记录列表的Adapter
 */
class MyArticleAdapter :
    BaseQuickAdapter<Article, BaseDataBindingHolder<ListItemShareArticleBinding>>(
        layoutResId = R.layout.list_item_share_article
    ), LoadMoreModule {

    init {
        setAnimationWithDefault(AnimationType.ScaleIn)
    }

    @SuppressLint("SetTextI18n")
    override fun convert(
        holder: BaseDataBindingHolder<ListItemShareArticleBinding>,
        item: Article
    ) {
        holder.dataBinding?.apply {
            article = item
            executePendingBindings()
            clItem.setOnClickListener { WebActivity.launch(context, item) }
        }
    }
}