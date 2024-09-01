package com.jpc.playandroidkotlin.data.bean

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * 标签
 */
@Parcelize
data class Tag(
    val name: String,
    val url: String
) : Parcelable