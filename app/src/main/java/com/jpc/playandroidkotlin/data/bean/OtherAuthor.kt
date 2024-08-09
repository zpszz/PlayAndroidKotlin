package com.jpc.playandroidkotlin.data.bean

import com.jpc.library_base.data.bean.PageResponse

/**
 * 其他文章作者信息实体
 */
data class OtherAuthor(
    val coinInfo: CoinInfo,
    val shareArticles: PageResponse<Article>
)