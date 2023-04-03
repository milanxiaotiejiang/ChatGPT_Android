package com.milan.chat.openai.gpt.holders

import android.net.Uri
import android.os.Looper
import android.util.LruCache
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.davemorrissey.labs.subscaleview.ImageSource
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView
import com.github.iielse.imageviewer.ImageViewerActionViewModel
import com.github.iielse.imageviewer.ImageViewerBuilder
import com.github.iielse.imageviewer.core.ImageLoader
import com.github.iielse.imageviewer.core.Photo
import com.github.iielse.imageviewer.core.Transformer
import com.github.iielse.imageviewer.core.VHCustomizer
import com.github.iielse.imageviewer.viewholders.SubsamplingViewHolder
import com.milan.chat.openai.gpt.R
import com.milan.chat.openai.gpt.model.PhotoData
import com.seabreeze.robot.base.ext.tool.getItemView
import com.seabreeze.robot.base.ext.tool.toast
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.io.File

/**
 * User: milan
 * Time: 2023/4/3 16:21
 * Des:
 */
class SimpleImageLoader(private val activity: FragmentActivity) : ImageLoader {
    /**
     * 根据自身photo数据加载图片.可以使用其它图片加载框架.
     */
    override fun load(view: ImageView, data: Photo, viewHolder: RecyclerView.ViewHolder) {
        val it = (data as? PhotoData?)?.url ?: return
        Glide.with(view).load(it).placeholder(view.drawable).into(view)
    }

    /**
     * 根据自身photo数据加载超大图.subsamplingView数据源需要先将内容完整下载到本地.需要注意生命周期
     */
    override fun load(
        subsamplingView: SubsamplingScaleImageView, data: Photo, viewHolder: RecyclerView.ViewHolder
    ) {
        val url = (data as? PhotoData?)?.url ?: return
        val cache = subsamplingCache.get(url)
        if (cache != null) subsamplingView.setImage(cache)
        else {
            activity.lifecycleScope.launch {
                subsamplingDownloadRequest(url)
                    .onStart {
                        findLoadingView(viewHolder)?.visibility = View.VISIBLE
                    }.catch { e ->
                        e.message?.let { toast { it } }

                    }.onEach { file ->
                        subsamplingView.setImage(
                            ImageSource.uri(Uri.fromFile(file))
                                .also { source -> subsamplingCache.put(url, source) })
                    }.onCompletion {
                        findLoadingView(viewHolder)?.visibility = View.GONE
                    }.collect()
            }
        }
    }

    private fun subsamplingDownloadRequest(url: String): Flow<File> {
        return flow {
            try {
                emit(Glide.with(activity).downloadOnly().load(url).submit().get())
            } catch (e: Throwable) {
                throw e
            }
        }.flowOn(Dispatchers.IO)
    }

    private fun findLoadingView(viewHolder: RecyclerView.ViewHolder): View? {
        return viewHolder.itemView.findViewById<ProgressBar>(R.id.loadingView)
    }

    companion object {
        val subsamplingCache = LruCache<String, ImageSource>(3)
    }
}

class SimpleTransformer : Transformer {
    override fun getView(key: Long): ImageView? = provide(key)

    companion object {
        private val transition = HashMap<ImageView, Long>()
        fun put(photoId: Long, imageView: ImageView) {
            require(Looper.myLooper() == Looper.getMainLooper())
            if (!imageView.isAttachedToWindow) return
            imageView.addOnAttachStateChangeListener(object : View.OnAttachStateChangeListener {
                override fun onViewAttachedToWindow(p0: View) = Unit

                override fun onViewDetachedFromWindow(p0: View) {
                    transition.remove(imageView)
                    imageView.removeOnAttachStateChangeListener(this)
                }
            })
            transition[imageView] = photoId
        }

        private fun provide(photoId: Long): ImageView? {
            transition.keys.forEach {
                if (transition[it] == photoId)
                    return it
            }
            return null
        }
    }
}

class SimpleViewerCustomizer : VHCustomizer {

    private var activity: FragmentActivity? = null
    private var viewerViewModel: ImageViewerActionViewModel? = null

    fun process(activity: FragmentActivity, builder: ImageViewerBuilder) {
        this.activity = activity
        viewerViewModel = ViewModelProvider(activity)[ImageViewerActionViewModel::class.java]
        builder.setVHCustomizer(this)
    }

    override fun initialize(type: Int, viewHolder: RecyclerView.ViewHolder) {
        (viewHolder.itemView as? ViewGroup?)?.let {
            it.addView(it.getItemView(R.layout.item_photo_custom_layout))
        }
        if (viewHolder is SubsamplingViewHolder) {
            viewHolder.binding.subsamplingView.setOnClickCallback { viewerViewModel?.dismiss() }
        }

    }

    override fun bind(type: Int, data: Photo, viewHolder: RecyclerView.ViewHolder) {
        val photoData = data as PhotoData
        viewHolder.itemView.findViewById<TextView>(R.id.exText).text = photoData.desc
    }

    private fun View.setOnClickCallback(interval: Long = 500L, callback: (View) -> Unit) {
        if (!isClickable) isClickable = true
        if (!isFocusable) isFocusable = true
        setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                v ?: return
                val lastClickedTimestamp =
                    v.getTag(R.id.view_last_click_timestamp)?.toString()?.toLongOrNull() ?: 0L
                val currTimestamp = System.currentTimeMillis()
                if (currTimestamp - lastClickedTimestamp < interval) return
                v.setTag(R.id.view_last_click_timestamp, currTimestamp)
                callback(v)
            }
        })
    }
}



