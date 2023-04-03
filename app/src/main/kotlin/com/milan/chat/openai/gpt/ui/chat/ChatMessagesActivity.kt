package com.milan.chat.openai.gpt.ui.chat

import android.content.DialogInterface
import android.widget.ImageView
import com.milan.chat.openai.gpt.R
import com.milan.chat.openai.gpt.databinding.ActivityChatMessagesBinding
import com.milan.chat.openai.gpt.holders.IncomingVoiceMessageViewHolder
import com.milan.chat.openai.gpt.holders.OutcomingVoiceMessageViewHolder
import com.milan.chat.openai.gpt.holders.ViewerHelper
import com.milan.chat.openai.gpt.ui.BaseMessagesActivity
import com.seabreeze.robot.base.ext.foundation.onError
import com.squareup.picasso.Picasso
import com.stfalcon.chatkit.commons.ImageLoader
import com.stfalcon.chatkit.messages.MessageHolders
import com.stfalcon.chatkit.messages.MessagesListAdapter
import com.stfalcon.chatkit.sample.common.data.fixtures.MessagesFixtures
import com.stfalcon.chatkit.sample.common.data.model.Message

/**
 * https://github.com/stfalcon-studio/ChatKit
 */
class ChatMessagesActivity :
    BaseMessagesActivity<ChatViewModel, ActivityChatMessagesBinding>(R.layout.activity_chat_messages),
    MessageHolders.ContentChecker<Message>, DialogInterface.OnClickListener {

    override fun onInitDataBinding() {
        with(mDataBinding) {
            viewModel = mViewModel
        }.apply {
            registerErrorEvent()
            registerLoadingProgressBarEvent()
        }
    }

    override fun initViewModelActions() {
        with(mViewModel) {
            inputLiveData.observe(this@ChatMessagesActivity) { message ->
                if (message.user.id == USER_ID) {
                    messagesAdapter.addToStart(message, true)
                }
            }
            deleteMessage.observe(this@ChatMessagesActivity) { message ->
                mDataBinding.messageInput.inputEditText.setText(message.text)
                mDataBinding.messageInput.inputEditText.setSelection(mDataBinding.messageInput.inputEditText.text.length)
                mDataBinding.messageInput.button.isEnabled = message.text.isNotEmpty()
                messagesAdapter.deleteById(message.id)
            }

            createLiveData.observe(this@ChatMessagesActivity) { message ->
                unChatLimit()
                messagesAdapter.addToStart(message, true)
            }
            streamLiveData.observe(this@ChatMessagesActivity) { message ->
                messagesAdapter.update(message)
            }
            resultLiveData.observe(this@ChatMessagesActivity) { message ->
                messagesAdapter.update(message)
            }

            errorMessage.observe(this@ChatMessagesActivity) { throwable ->
                unChatLimit()
                throwable.onError(this@ChatMessagesActivity)
            }

            imageLiveData.observe(this@ChatMessagesActivity) { message ->
                unChatLimit()
                messagesAdapter.addToStart(message, true)
            }
        }
    }

    override fun initData() {

        setSupportActionBar(mDataBinding.toolbar)

        imageLoader = object : ImageLoader {
            override fun loadImage(imageView: ImageView, url: String?, payload: Any?) {
                Picasso.get().load(url).into(imageView)
            }

            override fun loadImage(imageView: ImageView) {
                Picasso.get().load(R.mipmap.ic_avatars_assistant).into(imageView)
            }
        }

        mDataBinding.messageInput.setInputListener { input ->
            onChatLimit()
            mViewModel.sendMessage(input.toString())
            true
        }

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
        mDataBinding.messagesList.setAdapter(messagesAdapter)

//        messagesAdapter.addToStart(MessagesFixtures.getTextMessage(), true)
//        messagesAdapter.addToStart(MessagesFixtures.getTextMessage(), true)
//        messagesAdapter.addToStart(MessagesFixtures.getTextMessage(), true)
//        messagesAdapter.addToStart(MessagesFixtures.getTextMessage(), true)
//        messagesAdapter.addToStart(MessagesFixtures.getTextMessage(), true)
//        messagesAdapter.addToStart(MessagesFixtures.getImageMessage(), true)
//        messagesAdapter.addToStart(MessagesFixtures.getVoiceMessage(), true)

        messagesAdapter.setOnMessageClickListener { message ->
            if (message.imageUrl.isNullOrBlank()) {
                return@setOnMessageClickListener
            }
            ViewerHelper.provideImageViewerBuilder(this, messagesAdapter, message)
                .show()
        }
    }

    private fun onChatLimit() {
        mDataBinding.messageInput.prohibitInput(true)
        showProgressDialog()
    }

    private fun unChatLimit() {
        mDataBinding.messageInput.prohibitInput(false)
        dismissProgressDialog()
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