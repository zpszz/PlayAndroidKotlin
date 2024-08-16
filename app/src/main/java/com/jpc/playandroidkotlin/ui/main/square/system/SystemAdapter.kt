package com.jpc.playandroidkotlin.ui.main.square.system

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.module.LoadMoreModule
import com.chad.library.adapter.base.viewholder.BaseDataBindingHolder
import com.google.android.flexbox.FlexboxLayoutManager
import com.jpc.playandroidkotlin.R
import com.jpc.playandroidkotlin.data.bean.Structure
import com.jpc.playandroidkotlin.databinding.ListItemSystemBinding
import com.jpc.playandroidkotlin.ui.main.square.system.details.SystemArticleListActivity

/**
 * 体系列表的Adapter
 */
class SystemAdapter :
    BaseQuickAdapter<Structure, BaseDataBindingHolder<ListItemSystemBinding>>(layoutResId = R.layout.list_item_system),
    LoadMoreModule {

    init {
        setAnimationWithDefault(AnimationType.ScaleIn)
    }

    override fun convert(holder: BaseDataBindingHolder<ListItemSystemBinding>, item: Structure) {
        holder.dataBinding?.apply {
            text = item.name
            executePendingBindings()

            recyclerView.apply {
                layoutManager = FlexboxLayoutManager(context)
                setHasFixedSize(true) // 当确定Item的改变不会影响RecyclerView的宽高的时候可设置以提升性能
                setItemViewCacheSize(200) // 设置缓存大小为200，默认为2
                adapter = SystemChildAdapter().apply {
                    setList(item.children)
                    setOnItemClickListener { _, _, position ->
                        SystemArticleListActivity.launch(context, item, position)
                    }
                }
            }
        }
    }
}