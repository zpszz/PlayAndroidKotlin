package com.jpc.playandroidkotlin.data


import com.jpc.library_base.data.bean.ApiResponse
import com.jpc.library_base.data.bean.PageResponse
import com.jpc.library_base.http.RetrofitManager
import com.jpc.playandroidkotlin.data.bean.*
import com.jpc.playandroidkotlin.data.http.Api

/**
 * 数据仓库
 */
object DataRepository : Api {

    private val service by lazy { RetrofitManager.getService(Api::class.java) }

    override suspend fun login(username: String, password: String): ApiResponse<User> {
        return service.login(username, password)
    }

    override suspend fun register(
        username: String,
        password: String,
        pwdSure: String
    ): ApiResponse<Any?> {
        return service.register(username, password, pwdSure)
    }

    override suspend fun getBanner(): ApiResponse<List<Banner>> {
        return service.getBanner()
    }

    override suspend fun getArticleTopList(): ApiResponse<List<Article>> {
        return service.getArticleTopList()
    }

    override suspend fun getArticlePageList(
        pageNo: Int,
        pageSize: Int,
        cid: Int?
    ): ApiResponse<PageResponse<Article>> {
        return service.getArticlePageList(pageNo, pageSize, cid)
    }

    override suspend fun collectArticle(id: Int): ApiResponse<Any?> {
        return service.collectArticle(id)
    }

    override suspend fun unCollectArticle(id: Int): ApiResponse<Any?> {
        return service.unCollectArticle(id)
    }

    override suspend fun getProjectTitleList(): ApiResponse<List<ProjectTitle>> {
        return service.getProjectTitleList()
    }

    override suspend fun getNewProjectPageList(
        pageNo: Int,
        pageSize: Int
    ): ApiResponse<PageResponse<Article>> {
        return service.getNewProjectPageList(pageNo, pageSize)
    }

    override suspend fun getProjectPageList(
        pageNo: Int,
        pageSize: Int,
        categoryId: Int
    ): ApiResponse<PageResponse<Article>> {
        return service.getProjectPageList(pageNo, pageSize, categoryId)
    }

    override suspend fun getSquarePageList(
        pageNo: Int,
        pageSize: Int
    ): ApiResponse<PageResponse<Article>> {
        return service.getSquarePageList(pageNo, pageSize)
    }

    override suspend fun getAskPageList(
        pageNo: Int
    ): ApiResponse<PageResponse<Article>> {
        return service.getAskPageList(pageNo)
    }

    override suspend fun getTreeList(): ApiResponse<List<Structure>> {
        return service.getTreeList()
    }

    override suspend fun getNavigationList(): ApiResponse<List<Navigation>> {
        return service.getNavigationList()
    }

    override suspend fun getAuthorTitleList(): ApiResponse<List<Classify>> {
        return service.getAuthorTitleList()
    }

    override suspend fun getAuthorArticlePageList(
        authorId: Int,
        pageNo: Int,
        pageSize: Int
    ): ApiResponse<PageResponse<Article>> {
        return service.getAuthorArticlePageList(authorId, pageNo, pageSize)
    }

    override suspend fun getUserIntegral(): ApiResponse<CoinInfo> {
        return service.getUserIntegral()
    }

    override suspend fun getIntegralRankPageList(pageNo: Int): ApiResponse<PageResponse<CoinInfo>> {
        return service.getIntegralRankPageList(pageNo)
    }

    override suspend fun getIntegralRecordPageList(pageNo: Int): ApiResponse<PageResponse<IntegralRecord>> {
        return service.getIntegralRecordPageList(pageNo)
    }

    override suspend fun getCollectArticlePageList(pageNo: Int): ApiResponse<PageResponse<CollectArticle>> {
        return service.getCollectArticlePageList(pageNo)
    }

    override suspend fun getCollectUrlList(): ApiResponse<List<CollectUrl>> {
        return service.getCollectUrlList()
    }

    override suspend fun getMyShareArticlePageList(pageNo: Int): ApiResponse<Share> {
        return service.getMyShareArticlePageList(pageNo)
    }

    override suspend fun addArticle(title: String, link: String): ApiResponse<Any?> {
        return service.addArticle(title, link)
    }

    override suspend fun deleteShareArticle(id: Int): ApiResponse<Any?> {
        return service.deleteShareArticle(id)
    }

    override suspend fun collectUrl(name: String, link: String): ApiResponse<CollectUrl?> {
        return service.collectUrl(name, link)
    }

    override suspend fun unCollectUrl(id: Int): ApiResponse<Any?> {
        return service.unCollectUrl(id)
    }

    override suspend fun getOtherAuthorArticlePageList(
        id: Int,
        page: Int
    ): ApiResponse<OtherAuthor> {
        return service.getOtherAuthorArticlePageList(id, page)
    }

    override suspend fun getHotSearchList(): ApiResponse<List<HotSearch>> {
        return service.getHotSearchList()
    }

    override suspend fun getSearchDataByKey(
        pageNo: Int,
        searchKey: String
    ): ApiResponse<PageResponse<Article>> {
        return service.getSearchDataByKey(pageNo, searchKey)
    }
}