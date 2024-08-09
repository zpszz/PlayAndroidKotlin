package com.jpc.playandroidkotlin.data.bean

import com.jpc.library_base.data.bean.PageResponse

/**
 * 我的分享
 */
data class Share(
    var coinInfo: CoinInfo,
    var shareArticles: PageResponse<Article>
)