package com.milan.chat.openai.gpt.ui.chat

import android.content.DialogInterface
import android.os.Bundle
import androidx.activity.viewModels
import com.milan.chat.openai.gpt.R
import com.milan.chat.openai.gpt.holders.IncomingVoiceMessageViewHolder
import com.milan.chat.openai.gpt.holders.OutcomingVoiceMessageViewHolder
import com.milan.chat.openai.gpt.model.BaseThrowable
import com.milan.chat.openai.gpt.ui.BaseMessagesActivity
import com.stfalcon.chatkit.messages.MessageHolders
import com.stfalcon.chatkit.messages.MessageInput
import com.stfalcon.chatkit.messages.MessagesList
import com.stfalcon.chatkit.messages.MessagesListAdapter
import com.stfalcon.chatkit.sample.common.data.fixtures.MessagesFixtures
import com.stfalcon.chatkit.sample.common.data.model.Message
import com.tapadoo.alerter.Alerter

/**
 * https://github.com/stfalcon-studio/ChatKit
 */
class ChatMessagesActivity : BaseMessagesActivity(),
    MessageHolders.ContentChecker<Message>, DialogInterface.OnClickListener {

    private lateinit var messageInput: MessageInput
    private lateinit var messagesList: MessagesList
    private val chatViewModel: ChatViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_messages)

        messageInput = findViewById<MessageInput>(R.id.input)
        messageInput.setInputListener { input ->
            messageInput.button.isEnabled = false
            showProgressDialog()
            chatViewModel.sendMessage(input.toString())
            true
        }

        messagesList = findViewById(R.id.messagesList)
        val holders = MessageHolders().registerContentType(
            CONTENT_TYPE_VOICE,
            IncomingVoiceMessageViewHolder::class.java,
            R.layout.item_custom_incoming_voice_message,
            OutcomingVoiceMessageViewHolder::class.java,
            R.layout.item_custom_outcoming_voice_message,
            this
        )
        messagesAdapter = MessagesListAdapter(USER_ID, holders, imageLoader)
        messagesAdapter.enableSelectionMode(this)
        messagesAdapter.setLoadMoreListener(this)
        messagesList.setAdapter(messagesAdapter)

        with(chatViewModel) {
            inputLiveData.observe(this@ChatMessagesActivity) { message ->
                if (message.user.id == USER_ID) {
                    messagesAdapter.addToStart(message, true)
                }
            }
            createLiveData.observe(this@ChatMessagesActivity) { message ->
                messagesAdapter.addToStart(message, true)
                dismissProgressDialog()
            }
            streamLiveData.observe(this@ChatMessagesActivity) { message ->
                messagesAdapter.update(message)
            }
            messageLiveData.observe(this@ChatMessagesActivity) { message ->
                messageInput.button.isEnabled = true
                dismissProgressDialog()
                messagesAdapter.update(message)
            }
            errorMessage.observe(this@ChatMessagesActivity) { throwable ->
                messageInput.button.isEnabled = true
                dismissProgressDialog()
                when {
                    throwable.isExternal() -> {
                        val externalThrowable = this as BaseThrowable.ExternalThrowable
                        externalThrowable.cause?.apply {
                            message?.apply {
                                Alerter.create(this@ChatMessagesActivity)
                                    .setText(this)
                                    .show()
                            }
                        }
                    }
                    throwable.isInside() -> {
                        val insideThrowable = this as BaseThrowable.InsideThrowable
                        Alerter.create(this@ChatMessagesActivity)
                            .setText("${insideThrowable.errorCode} ${insideThrowable.errorMessage}")
                            .show()
                    }
                }
            }
        }

    }

    override fun hasContentFor(message: Message, type: Byte): Boolean {
        return if (type == CONTENT_TYPE_VOICE) {
            message.voice != null && message.voice.url != null && message.voice.url.isNotEmpty()
        } else false
    }

    override fun onClick(dialogInterface: DialogInterface, i: Int) {
        when (i) {
            0 -> messagesAdapter.addToStart(MessagesFixtures.getImageMessage(), true)
            1 -> messagesAdapter.addToStart(MessagesFixtures.getVoiceMessage(), true)
        }
    }

    companion object {
        private const val CONTENT_TYPE_VOICE: Byte = 1
    }
}