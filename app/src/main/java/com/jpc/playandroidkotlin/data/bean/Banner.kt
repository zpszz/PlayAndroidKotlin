package com.jpc.playandroidkotlin.data.bean

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * 轮播图实体
 */
@Parcelize
data class Banner(
    var desc: String = "",
    var id: Int = 0,
    var imagePath: String = "",
    var isVisible: Int = 0,
    var order: Int = 0,
    var title: String = "",
    var type: Int = 0,
    var url: String = ""
) : Parcelable