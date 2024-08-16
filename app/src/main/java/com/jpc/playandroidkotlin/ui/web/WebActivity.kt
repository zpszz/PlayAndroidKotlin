package com.jpc.playandroidkotlin.ui.web

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.webkit.WebView
import android.widget.FrameLayout
import androidx.core.content.ContextCompat
import com.jpc.library_base.ext.initClose
import com.jpc.library_base.ext.initTitle
import com.jpc.playandroidkotlin.R
import com.jpc.playandroidkotlin.base.BaseActivity
import com.jpc.playandroidkotlin.base.MyApplication
import com.jpc.playandroidkotlin.data.bean.Article
import com.jpc.playandroidkotlin.data.bean.Banner
import com.jpc.playandroidkotlin.data.bean.CollectArticle
import com.jpc.playandroidkotlin.data.bean.CollectData
import com.jpc.playandroidkotlin.data.bean.CollectUrl
import com.jpc.playandroidkotlin.databinding.ActivityWebBinding
import com.just.agentweb.AgentWeb
import com.just.agentweb.WebChromeClient

class WebActivity : BaseActivity<WebViewModel, ActivityWebBinding>(R.layout.activity_web) {
    private lateinit var mAgentWeb: AgentWeb
    private var mArticle: Article? = null
    private var mCollectArticle: CollectArticle? = null
    private var mCollectUrl: CollectUrl? = null
    private var mBanner: Banner? = null
    private lateinit var mUrl: String
    private var mCollect = false
    private lateinit var mTitle: String

    companion object {
        private const val EXTRA_ARTICLE = "extra_article"
        private const val EXTRA_COLLECT_ARTICLE = "extra_collect_article"
        private const val EXTRA_COLLECT_URL = "extra_collect_url"
        private const val EXTRA_BANNER = "extra_banner"

        /**
         * 页面跳转
         *
         * @param context Context
         * @param article Article
         */
        fun launch(context: Context, article: Article) {
            context.startActivity(Intent(context, WebActivity::class.java).apply {
                putExtra(EXTRA_ARTICLE, article)
            })
        }

        /**
         * 页面跳转
         *
         * @param context Context
         * @param collectArticle CollectArticle
         */
        fun launch(context: Context, collectArticle: CollectArticle) {
            context.startActivity(Intent(context, WebActivity::class.java).apply {
                putExtra(EXTRA_COLLECT_ARTICLE, collectArticle)
            })
        }

        /**
         * 页面跳转
         *
         * @param context Context
         * @param collectUrl CollectUrl
         */
        fun launch(context: Context, collectUrl: CollectUrl) {
            context.startActivity(Intent(context, WebActivity::class.java).apply {
                putExtra(EXTRA_COLLECT_URL, collectUrl)
            })
        }

        /**
         * 页面跳转
         *
         * @param context Context
         * @param banner Banner
         */
        fun launch(context: Context, banner: Banner) {
            context.startActivity(Intent(context, WebActivity::class.java).apply {
                putExtra(EXTRA_BANNER, banner)
            })
        }
    }

    override fun initView(savedInstanceState: Bundle?) {
        mBinding.toolbar.apply {
            setSupportActionBar(this)
            initTitle("加载中...")
            initClose { onBackPressedDispatcher.onBackPressed() }
        }
        intent.apply {
            mArticle = getParcelableExtra(EXTRA_ARTICLE)
            mCollectArticle = getParcelableExtra(EXTRA_COLLECT_ARTICLE)
            mCollectUrl = getParcelableExtra(EXTRA_COLLECT_URL)
            mBanner = getParcelableExtra(EXTRA_BANNER)
        }
        when {
            mArticle != null -> {
                mUrl = mArticle!!.link
                mCollect = mArticle!!.collect
            }
            mCollectArticle != null -> {
                mUrl = mCollectArticle!!.link
                mCollect = true
            }
            mCollectUrl != null -> {
                mUrl = mCollectUrl!!.link
                mCollect = true
            }
            else -> {
                mUrl = mBanner!!.url
                mCollect = false // 无法得知是否收藏直接置为false
            }
        }
        // 初始化AgentWeb
        mAgentWeb = AgentWeb.with(this)
            // 设置AgentWeb的父布局，-1表示填充满父布局
            .setAgentWebParent(mBinding.flWeb, FrameLayout.LayoutParams(-1, -1))
            // 使用默认的进度条
            .useDefaultIndicator()
            // 设置WebChromeClient，用于获取网页标题
            .setWebChromeClient(object : WebChromeClient(){
                // 获取网页标题
                override fun onReceivedTitle(view: WebView?, title: String?) {
                    super.onReceivedTitle(view, title)
                    mTitle = title ?: ""
                    mBinding.toolbar.title = mTitle
                }
            })
            // 准备就绪，加载网页
            .createAgentWeb()
            .ready()
            .go(mUrl)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.item_collect -> {
                handleCollect()
            }
            R.id.item_share -> {
                startActivity(Intent.createChooser(Intent().apply {
                    action = Intent.ACTION_SEND
                    putExtra(Intent.EXTRA_TEXT, "${mTitle}:${mUrl}")
                    type = "text/plain"
                }, "分享到"))
            }
            R.id.item_refresh -> {
                mAgentWeb.urlLoader.reload()
            }
            R.id.item_openByBrowser -> {
                // 使用默认浏览器打开
                startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(mUrl)))
            }
        }
        return super.onOptionsItemSelected(item)
    }

    // 收藏与取消收藏处理
    private fun handleCollect() {
        when {
            mArticle != null -> {
                if (mCollect) {
                    mViewModel.unCollectArticle(mArticle!!.id) {
                        // 重置mCollect为false然后强制刷新menu即可
                        mCollect = !mCollect
                        invalidateOptionsMenu()
                        MyApplication.appViewModel.collectEvent.setValue(
                            CollectData(mArticle!!.id, collect = false)
                        )
                    }
                } else {
                    mViewModel.collectArticle(mArticle!!.id) {
                        mCollect = !mCollect
                        invalidateOptionsMenu()
                        MyApplication.appViewModel.collectEvent.setValue(
                            CollectData(mArticle!!.id, collect = true)
                        )
                    }
                }
            }
            mCollectArticle != null -> {
                if (mCollect) {
                    mViewModel.unCollectArticle(mCollectArticle!!.originId) {
                        mCollect = !mCollect
                        invalidateOptionsMenu()
                        MyApplication.appViewModel.collectEvent.setValue(
                            CollectData(mCollectArticle!!.id, collect = false)
                        )
                    }
                } else {
                    mViewModel.collectArticle(mCollectArticle!!.originId) {
                        mCollect = !mCollect
                        invalidateOptionsMenu()
                        MyApplication.appViewModel.collectEvent.setValue(
                            CollectData(mCollectArticle!!.id, collect = true)
                        )
                    }
                }
            }
            mCollectUrl != null -> {
                if (mCollect) {
                    mViewModel.unCollectUrl(mCollectUrl!!.id) {
                        mCollect = !mCollect
                        invalidateOptionsMenu()
                        // 连续收藏取消收藏，id会变这里直接以link为唯一标志
                        MyApplication.appViewModel.collectEvent.setValue(
                            CollectData(mCollectUrl!!.id, mCollectUrl!!.link, false)
                        )
                    }
                } else {
                    mViewModel.collectUrl(mCollectUrl!!.name, mCollectUrl!!.link) {
                        mCollect = !mCollect
                        invalidateOptionsMenu()
                        // 取消后再重新收藏id会变，故重新赋值
                        mCollectUrl = it
                        MyApplication.appViewModel.collectEvent.setValue(
                            CollectData(mCollectUrl!!.id, mCollectUrl!!.link, true)
                        )
                    }
                }
            }
            else -> {
                if (mCollect) {
                    mViewModel.unCollectUrl(mBanner!!.id) {
                        // 重置mCollect为false然后强制刷新menu即可
                        mCollect = !mCollect
                        invalidateOptionsMenu()
                    }
                } else {
                    mViewModel.collectUrl(mBanner!!.title, mBanner!!.url) {
                        mCollect = !mCollect
                        invalidateOptionsMenu()
                    }
                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_web, menu)
        return super.onCreateOptionsMenu(menu)
    }
    // 判断是否收藏过，显示对应的收藏图标
    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        menu?.findItem(R.id.item_collect)?.icon = ContextCompat.getDrawable(
            this,
            if (mCollect) R.drawable.ic_collect else R.drawable.ic_un_collect
        )
        return super.onPrepareOptionsMenu(menu)
    }

    override fun onResume() {
        mAgentWeb.webLifeCycle.onResume()
        super.onResume()
    }

    override fun onPause() {
        mAgentWeb.webLifeCycle.onPause()
        super.onPause()
    }

    override fun onDestroy() {
        mAgentWeb.webLifeCycle.onDestroy()
        super.onDestroy()
    }
}