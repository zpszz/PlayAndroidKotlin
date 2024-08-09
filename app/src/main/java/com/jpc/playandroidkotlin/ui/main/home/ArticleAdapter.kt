package com.jpc.playandroidkotlin.ui.main.home

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.module.LoadMoreModule
import com.chad.library.adapter.base.viewholder.BaseDataBindingHolder
import com.jpc.playandroidkotlin.R
import com.jpc.playandroidkotlin.data.bean.Article
import com.jpc.playandroidkotlin.databinding.ListItemArticleBinding

/**
 * 文章列表的Adapter
 */
class ArticleAdapter:  BaseQuickAdapter<Article, BaseDataBindingHolder<ListItemArticleBinding>>(R.layout.list_item_article), LoadMoreModule{
    init {
        // 加载时的动画
        setAnimationWithDefault(AnimationType.ScaleIn)
    }
    override fun convert(holder: BaseDataBindingHolder<ListItemArticleBinding>, item: Article) {
        holder.dataBinding?.apply {
            article = item
            executePendingBindings()
            clItem.setOnClickListener{
                // 点击跳转到Web文章详情页面
            }
        }
    }
}