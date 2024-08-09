package com.jpc.playandroidkotlin.data.bean

/**
 * 收藏实体，主要用于全局收藏事件Event
 */
data class CollectData(
    var id: Int = 0,
    var link: String = "",
    var collect: Boolean
)