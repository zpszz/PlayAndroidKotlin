package com.jpc.playandroidkotlin.ui.main.square.navigation

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseDataBindingHolder
import com.google.android.flexbox.FlexboxLayoutManager
import com.jpc.playandroidkotlin.R
import com.jpc.playandroidkotlin.data.bean.Navigation
import com.jpc.playandroidkotlin.databinding.ListItemSystemBinding

/**
 * 导航模块的Adapter
 */
class NavigationAdapter :
    BaseQuickAdapter<Navigation, BaseDataBindingHolder<ListItemSystemBinding>>(layoutResId = R.layout.list_item_system) {
    init {
        setAnimationWithDefault(AnimationType.AlphaIn)
    }

    override fun convert(holder: BaseDataBindingHolder<ListItemSystemBinding>, item: Navigation) {
        holder.dataBinding?.apply {
            text = item.name
            executePendingBindings()
            recyclerView.apply {
                layoutManager = FlexboxLayoutManager(context)
                // 当确定Item的改变不会影响RecyclerView的宽高时可以设置为true以提高性能
                setHasFixedSize(true)
                // 设置缓存大小为200，默认的为2
                setItemViewCacheSize(200)
                adapter = NavigationChildAdapter().apply {
                    setList(item.articles)
                    setOnItemClickListener { _, _, position ->
                        // 点击跳转到Web
                    }
                }
            }
        }
    }

}