package com.jpc.playandroidkotlin.ui.main.home

import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.jpc.library_base.ext.load
import com.jpc.playandroidkotlin.data.bean.Banner
import com.jpc.playandroidkotlin.ui.web.WebActivity
import com.youth.banner.adapter.BannerAdapter

/**
 * 首页轮播图Adapter
 */
class HomeBannerAdapter(dataList: ArrayList<Banner>): BannerAdapter<Banner, HomeBannerAdapter.BannerViewHolder>(dataList){
    override fun onCreateHolder(parent: ViewGroup?, viewType: Int): BannerViewHolder {
        val imageView = ImageView(parent?.context)
        // 必须设置为match_parent，这是ViewPager2强制要求的
        imageView.layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT,
        )
        imageView.scaleType = ImageView.ScaleType.CENTER_CROP
        return BannerViewHolder(imageView)
    }

    override fun onBindView(holder: BannerViewHolder, data: Banner, position: Int, size: Int) {
        holder.imageView.apply {
            load(data.imagePath)
            setOnClickListener { WebActivity.launch(context, data) }
        }
    }

    inner class BannerViewHolder(var imageView: ImageView): RecyclerView.ViewHolder(imageView)
}