package com.jpc.playandroidkotlin.data.bean

/**
 * 热门搜索
 */
data class HotSearch(
    var id: Int,
    var link: String,
    var name: String,
    var order: Int,
    var visible: Int
)