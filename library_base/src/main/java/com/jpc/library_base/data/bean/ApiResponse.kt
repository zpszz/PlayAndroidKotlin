package com.jpc.library_base.data.bean

/**
 * 服务器返回数据的基类
 */
data class ApiResponse<T>(
    val data: T,
    val errorCode: Int,
    val errorMsg: String
)