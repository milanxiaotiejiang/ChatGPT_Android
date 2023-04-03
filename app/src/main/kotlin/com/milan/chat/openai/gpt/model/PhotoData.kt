package com.milan.chat.openai.gpt.model

import com.github.iielse.imageviewer.adapter.ItemType
import com.github.iielse.imageviewer.core.Photo

/**
 * User: milan
 * Time: 2023/4/3 16:17
 * Des:
 */
class PhotoData(
    val id: Long,
    val url: String,
    val subsampling: Boolean = false,
    val desc: String = "[$id] Caption or other information for this picture [$id]"
) : Photo {

    override fun id(): Long = id

    override fun itemType(): Int {
        return ItemType.PHOTO
    }
}