package com.jpc.playandroidkotlin.data.bean

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * 积分
 */
@Parcelize
data class CoinInfo(
    val coinCount: Int, // 当前积分
    val rank: String,
    val level: Int,
    val userId: Int,
    val nickname: String,
    val username: String
) : Parcelable