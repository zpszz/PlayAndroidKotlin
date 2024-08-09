package com.jpc.library_base.data.local

import com.tencent.mmkv.MMKV

/**
 * 服务器地址管理
 */
object IpManager {

    /** MMKV独有的mmapId */
    private const val MMKV_MAP_ID = "ip"

    /** 常用的IP */
    private const val DEFAULT_IP_ADDRESS_REMOTE = "https://www.wanandroid.com"     // 外网
    private const val DEFAULT_IP_ADDRESS_CHEN = "http://localhost:8095"
    private const val DEFAULT_IP_ADDRESS_LOCAL = "http://172.168.111.60:8095"     // ltp

    private const val KEY_IP_SET = "data_ip_set"
    private const val KEY_DEFAULT_IP_AND_PORT = "data_default_ip_and_port"

    private val mmkv by lazy { MMKV.mmkvWithID(MMKV_MAP_ID) }

    /**
     * 保存默认IP
     *
     * @param ip 要保存为默认的IP
     */
    fun saveDefaultIP(ip: String) {
        mmkv.encode(KEY_DEFAULT_IP_AND_PORT, ip)
    }

    /** 获取默认IP */
    fun getDefaultIP(): String {
        return mmkv.decodeString(KEY_DEFAULT_IP_AND_PORT, DEFAULT_IP_ADDRESS_REMOTE)!!
    }

    /** 存储使用过的IP集 */
    fun saveIPSet(ipSet: MutableSet<String>) {
        mmkv.encode(KEY_IP_SET, ipSet)
    }

    /** 获取使用过的IP集 */
    fun getIPSet(): MutableSet<String> {
        return mmkv.decodeStringSet(
            KEY_IP_SET, setOf(
                DEFAULT_IP_ADDRESS_REMOTE,
                DEFAULT_IP_ADDRESS_CHEN,
                DEFAULT_IP_ADDRESS_LOCAL
            )
        )!!
    }
}