package com.jpc.playandroidkotlin.ui.main.project

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.module.LoadMoreModule
import com.chad.library.adapter.base.viewholder.BaseDataBindingHolder
import com.jpc.playandroidkotlin.R
import com.jpc.playandroidkotlin.data.bean.Article
import com.jpc.playandroidkotlin.databinding.ListItemArticleImageBinding
import com.jpc.playandroidkotlin.ui.web.WebActivity

/**
 * 带图片的文章列表的Adapter
 */
class ImageArticleAdapter: BaseQuickAdapter<Article, BaseDataBindingHolder<ListItemArticleImageBinding>>(
    R.layout.list_item_article_image), LoadMoreModule{
    init {
        setAnimationWithDefault(AnimationType.ScaleIn)
    }

    override fun convert(
        holder: BaseDataBindingHolder<ListItemArticleImageBinding>,
        item: Article
    ) {
        holder.dataBinding?.apply {
            article = item
            executePendingBindings()
            clItem.setOnClickListener {
                // 跳转到文章详情页面
                WebActivity.launch(context, item)
            }
        }
    }
}