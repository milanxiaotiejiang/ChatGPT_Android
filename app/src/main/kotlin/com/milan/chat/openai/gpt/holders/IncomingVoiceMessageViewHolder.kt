package com.milan.chat.openai.gpt.holders

import android.view.View
import android.widget.TextView
import com.milan.chat.openai.gpt.R
import com.stfalcon.chatkit.messages.MessageHolders.IncomingTextMessageViewHolder
import com.stfalcon.chatkit.sample.common.data.model.Message
import com.stfalcon.chatkit.sample.utils.FormatUtils
import com.stfalcon.chatkit.utils.DateFormatter

/*
 * Created by troy379 on 05.04.17.
 */
class IncomingVoiceMessageViewHolder(itemView: View, payload: Any) :
    IncomingTextMessageViewHolder<Message>(itemView, payload) {

    private val tvDuration: TextView
    private val tvTime: TextView

    init {
        tvDuration = itemView.findViewById(R.id.duration)
        tvTime = itemView.findViewById(R.id.time)
    }

    override fun onBind(message: Message) {
        super.onBind(message)
        tvDuration.text = FormatUtils.getDurationString(
            message.voice.duration
        )
        tvTime.text = DateFormatter.format(
            message.createdAt,
            DateFormatter.Template.TIME
        )
    }
}