package com.jpc.playandroidkotlin.data.bean

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 *
 */
@Parcelize
data class Tag(
    val name: String,
    val url: String
) : Parcelable