package com.jpc.library_base.data.bean

data class ApiResponse<T>(
    val data: T,
    val errorCode: Int,
    val errorMsg: String
)