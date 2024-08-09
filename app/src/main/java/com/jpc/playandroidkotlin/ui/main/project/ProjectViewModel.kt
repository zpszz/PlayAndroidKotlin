package com.jpc.playandroidkotlin.ui.main.project

import androidx.lifecycle.MutableLiveData
import com.jpc.library_base.base.BaseViewModel
import com.jpc.library_base.ext.handleRequest
import com.jpc.library_base.ext.launch
import com.jpc.playandroidkotlin.data.DataRepository
import com.jpc.playandroidkotlin.data.bean.ProjectTitle

class ProjectViewModel: BaseViewModel(){
    // 项目分类标题列表LiveData
    val projectTitleLiveData = MutableLiveData<List<ProjectTitle>?>()
    override fun start() {
        // 创建Fragment好后就调用
        fetchProjectTitleList()
    }

    // 获取项目分类的标题
    private fun fetchProjectTitleList(){
        launch({
            handleRequest(
                DataRepository.getProjectTitleList(),
                { projectTitleLiveData.value = it.data }
            )
        })
    }
}