package com.jpc.playandroidkotlin.ui.share.add

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.bottomsheets.BottomSheet
import com.afollestad.materialdialogs.customview.customView
import com.afollestad.materialdialogs.lifecycle.lifecycleOwner
import com.jpc.library_base.ext.hideLoading
import com.jpc.library_base.ext.showLoading
import com.jpc.library_base.utils.ToastUtil
import com.jpc.playandroidkotlin.R
import com.jpc.playandroidkotlin.base.BaseActivity
import com.jpc.playandroidkotlin.base.MyApplication
import com.jpc.playandroidkotlin.databinding.ActivityAddArticleBinding


/**
 * 添加分享的文章
 */
class AddArticleActivity :
    BaseActivity<AddArticleViewModel, ActivityAddArticleBinding>(R.layout.activity_add_article) {

    companion object {

        /**
         * 页面启动
         * @param context Context
         */
        fun launch(context: Context) {
            context.startActivity(Intent(context, AddArticleActivity::class.java))
        }
    }

    override fun initView(savedInstanceState: Bundle?) {
        mBinding.apply {
            titleLayout.setRightView(R.drawable.ic_integral_rule) {
                MaterialDialog(this@AddArticleActivity, BottomSheet())
                    .lifecycleOwner(this@AddArticleActivity)
                    .show {
                        title(text = "温馨提示")
                        customView(
                            R.layout.dialog_share_article_tip,
                            scrollable = true,
                            horizontalPadding = true
                        )
                        positiveButton(text = "知道了")
                        cornerRadius(16f)
                    }
            }

            btnShare.setOnClickListener {
                when {
                    mViewModel.title.get().isNullOrEmpty() -> {
                        ToastUtil.showShort(this@AddArticleActivity, "请填写文章标题")
                    }

                    mViewModel.articleLink.get().isNullOrEmpty() -> {
                        ToastUtil.showShort(this@AddArticleActivity, "请填写文章链接")
                    }

                    else -> {
                        showLoading("分享中...")
                        mViewModel.apply {
                            addArticle(title.get()!!, articleLink.get()!!) {
                                MyApplication.appViewModel.shareArticleEvent.value = true
                                hideLoading()
                                onBackPressed()
                            }
                        }
                    }
                }
            }
        }
    }
}