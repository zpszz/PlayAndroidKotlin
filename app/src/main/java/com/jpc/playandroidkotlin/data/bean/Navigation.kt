package com.jpc.playandroidkotlin.data.bean

/**
 * 导航实体
 */
data class Navigation(
    var articles: List<Article>,
    var cid: Int,
    var name: String
)