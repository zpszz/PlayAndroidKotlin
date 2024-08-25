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
    /**
     * 这是使用MutableStateFlow来实现的，MutableStateFlow是一个可变的StateFlow，可以通过value属性来修改它的值
     * StateFlow是一个只读的数据流，它可以通过collect方法来监听数据的变化，为什么不使用LiveData呢？
     * 因为LiveData是一个Lifecycle-aware的组件，它会在Activity或Fragment的生命周期结束时自动取消订阅，而StateFlow不会
     * 但是StateFlow是一个Kotlin的协程库，它需要在协程的作用域内使用，所以在ViewModel中使用StateFlow是一个不错的选择
     * 通过MutableStateFlow来实现LiveData的功能，这样可以更好的与协程结合，也可以更好的与Jetpack Compose结合
     */
    private val _bannerListStateFlow = MutableStateFlow<List<Banner>>(emptyList())
    val bannerListStateFlow = _bannerListStateFlow

    // 文章列表
    /**
     * 这里使用MutableLiveData来实现，LiveData是一个具有生命周期感知能力的数据持有类，它可以感知Activity或Fragment的生命周期
     * 当Activity或Fragment的生命周期结束时，LiveData会自动取消订阅，这样可以避免内存泄漏
     * 为什么不使用StateFlow呢？因为StateFlow是一个Kotlin的协程库，它需要在协程的作用域内使用
     */
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