package com.jpc.playandroidkotlin.ui.main.home

import androidx.lifecycle.MutableLiveData
import com.jpc.library_base.BaseApplication.Companion.context
import com.jpc.library_base.base.BaseViewModel
import com.jpc.library_base.data.bean.PageResponse
import com.jpc.library_base.ext.handleRequest
import com.jpc.library_base.ext.launch
import com.jpc.library_base.utils.ToastUtil
import com.jpc.playandroidkotlin.data.DataRepository
import com.jpc.playandroidkotlin.data.bean.Article
import com.jpc.playandroidkotlin.data.bean.Banner
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow

private const val TAG = "HomeViewModel"
class HomeViewModel: BaseViewModel(){
    companion object{
        // 每页显示的数据条数
        const val PAGE_SIZE = 10
    }
    // Banner列表
    private val _bannerListStateFlow = MutableStateFlow<List<Banner>>(emptyList())
    val bannerListStateFlow = _bannerListStateFlow

    // 文章列表
    val articlePageListLiveData = MutableLiveData<PageResponse<Article>?>()

    override fun start() {
    }

    // 请求轮播图数据
    fun fetchBanners(){
        launch({
            handleRequest(DataRepository.getBanner(), {
                _bannerListStateFlow.value = it.data
            })
        })
    }

    /**
     * 请求文章列表
     * @param pageNo 当前页码，后端规定从0开始
     */
    fun fetchArticlePageList(pageNo: Int = 0){
        launch({
            if (pageNo == 0){
                // 第一页需要同时请求置顶文章接口、分页文章列表接口
                // 使用async进行并行请求速度更快
                // 需要加SupervisorJob()来自行处理协程（https://juejin.cn/post/7130132604568731655）
                val job1 = async(Dispatchers.IO + SupervisorJob()) {
                    // 获取分页文章
                    DataRepository.getArticlePageList(pageNo, PAGE_SIZE)
                }
                val job2 = async(Dispatchers.IO + SupervisorJob()) {
                    // 获取置顶文章
                    DataRepository.getArticleTopList()
                }
                try {
                    val response1 = job1.await()
                    val response2 = job2.await()
                    val result1 = handleRequest(response1)
                    val result2 = handleRequest(response2)
                    if (result1.isSuccess && result2.isSuccess){
                        // 添加顺序
                        (response1.data.datas as ArrayList<Article>).addAll(0, response2.data)
                        articlePageListLiveData.value = response1.data
                    }
                }catch (e: Exception){
                    e.printStackTrace()
                }
            }else{
                handleRequest(
                    DataRepository.getArticlePageList(pageNo, PAGE_SIZE),
                    {
                        articlePageListLiveData.value = it.data
                    }
                )
            }
        })
    }

    /**
     * 收藏文章
     * @param id 文章id
     */
    fun collectArticle(id: Int, successCallback: () -> Any? = {}){
        launch({
            handleRequest(DataRepository.collectArticle(id), { successCallback.invoke() })
        })
    }

    /**
     * 收藏文章
     * @param id 文章id
     */
    fun unCollectArticle(id: Int, successCallback: () -> Any? = {}){
        launch({
            handleRequest(DataRepository.unCollectArticle(id), {
                successCallback.invoke()
                ToastUtil.showShort(context, "收藏成功，记得时常看看哦")
            })
        })
    }
}