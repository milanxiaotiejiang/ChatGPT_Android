package com.milan.chat.openai.gpt.holders

import androidx.fragment.app.FragmentActivity
import com.github.iielse.imageviewer.ImageViewerBuilder
import com.github.iielse.imageviewer.core.Photo
import com.github.iielse.imageviewer.core.SimpleDataProvider
import com.milan.chat.openai.gpt.model.PhotoData
import com.stfalcon.chatkit.messages.MessagesListAdapter
import com.stfalcon.chatkit.sample.common.data.model.Message

/**
 * User: milan
 * Time: 2023/4/3 18:04
 * Des:
 */
object ViewerHelper {
    fun provideImageViewerBuilder(
        context: FragmentActivity,
        messagesAdapter: MessagesListAdapter<Message>,
        message: Message
    ): ImageViewerBuilder {
        val dataList = mutableListOf<Photo>()
        val messagePosition = messagesAdapter.getMessagePositionById(message.id)
        val photoData = PhotoData(messagePosition.toLong(), message.imageUrl!!)
        dataList.add(photoData)
        val builder = ImageViewerBuilder(
            context = context,
            dataProvider = SimpleDataProvider(
                photoData,
                dataList
            ), // 一次性全量加载 // 实现DataProvider接口支持分页加载
            imageLoader = SimpleImageLoader(context),
            transformer = SimpleTransformer(),
        )
        SimpleViewerCustomizer().process(context, builder)
        return builder
    }
}