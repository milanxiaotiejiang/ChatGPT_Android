package com.milan.chat.openai.gpt.ui.chat

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.milan.chat.openai.gpt.ui.BaseMessagesActivity
import com.seabreeze.robot.base.ext.foundation.BaseThrowable
import com.seabreeze.robot.base.ext.tool.gToBean
import com.seabreeze.robot.base.framework.mvvm.BaseViewModel
import com.seabreeze.robot.data.net.DataRepository
import com.seabreeze.robot.data.net.bean.request.RequestChat
import com.seabreeze.robot.data.net.bean.response.ChatMajor
import com.seabreeze.robot.data.net.bean.response.OpenAiError
import com.stfalcon.chatkit.sample.common.data.model.Message
import com.stfalcon.chatkit.sample.common.data.model.User
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.util.*

/**
 * User: milan
 * Time: 2023/3/30 21:03
 * Des:
 */
class ChatViewModel : BaseViewModel() {

    private val messageQueue: LinkedList<Message> = LinkedList()

    private fun addMessageToQueue(message: Message) {
        messageQueue.offer(message)
        if (messageQueue.size > QUEUE_CAPACITY) {
            messageQueue.poll()
        }
    }

    private fun getMessageQueue(): List<Message> {
        return messageQueue.toList()
    }

    private val _inputLiveData = MutableLiveData<Message>()
    val inputLiveData: LiveData<Message>
        get() = _inputLiveData

    private val _deleteMessage = MutableLiveData<Message>()
    val deleteMessage: LiveData<Message>
        get() = _deleteMessage

    private val _createLiveData = MutableLiveData<Message>()
    val createLiveData: LiveData<Message>
        get() = _createLiveData

    private val _streamLiveData = MutableLiveData<Message>()
    val streamLiveData: LiveData<Message>
        get() = _streamLiveData

    private val _resultLiveData = MutableLiveData<Message>()
    val resultLiveData: LiveData<Message>
        get() = _resultLiveData

    private val _errorMessage = MutableLiveData<BaseThrowable>()
    val errorMessage: LiveData<BaseThrowable>
        get() = _errorMessage

    private val _imageLiveData = MutableLiveData<Message>()
    val imageLiveData: LiveData<Message>
        get() = _imageLiveData

    fun sendMessage(sendContent: String) {

        val selfMessage = selfMessage(sendContent)

        if (selfMessage.text.startsWith("image ")) {
            imagesGenerations(selfMessage)
        } else {
            chatCompletions(selfMessage)
        }
    }

    private fun imagesGenerations(contentMessage: Message) {
        val imagePrompt = contentMessage.text.substringAfter("image ")
        viewModelScope.launch {
            try {
                DataRepository.INSTANCE.generateImage(imagePrompt).onStart {
                    onStart(contentMessage)
                }.catch { e ->
                    onCatch(contentMessage, BaseThrowable.ExternalThrowable(e))
                }.collect { url ->
                    _imageLiveData.postValue(url.urlToMessage(imagePrompt))
                }

            } catch (e: Exception) {
                e.printStackTrace()
                when (e) {
                    is HttpException -> {
                        val errorBody = e.response()?.errorBody()?.string()
                        val errorContent = errorBody?.gToBean<OpenAiError>()?.error?.message
                            ?: "Unknown error"
                        onCatch(
                            contentMessage,
                            BaseThrowable.InsideThrowable(e.code(), errorContent)
                        )
                    }
                    else -> {
                        onCatch(contentMessage, BaseThrowable.ExternalThrowable(e))
                    }
                }
            }
        }
    }

    private fun chatCompletions(contentMessage: Message) {
        val requestMessage = contentMessage.requestMessage()

        viewModelScope.launch {

            try {
                val messageMutableList = mutableListOf<RequestChat.Message>()
                getMessageQueue().forEach {
                    val queueMessage = RequestChat.Message(role = it.user.name, content = it.text)
                    messageMutableList.add(queueMessage)
                }
                messageMutableList.add(requestMessage)
                val requestChat = RequestChat(messages = messageMutableList)

                DataRepository.INSTANCE.getCompletion(requestChat).onStart {
                    onStart(contentMessage)
                }.catch { e ->
                    onCatch(contentMessage, BaseThrowable.ExternalThrowable(e))
                }.collect { conciseData ->
                    if (conciseData.done) {
                        _resultLiveData.postValue(conciseData.conciseToMessage())

                        addMessageToQueue(contentMessage)
                        addMessageToQueue(conciseData.conciseToMessage())
                    } else {
                        if (conciseData.index == 0) {
                            _createLiveData.postValue(conciseData.conciseToMessage())
                        } else {
                            _streamLiveData.postValue(conciseData.conciseToMessage())
                        }
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                when (e) {
                    is HttpException -> {
                        val errorBody = e.response()?.errorBody()?.string()
                        val errorContent = errorBody?.gToBean<OpenAiError>()?.error?.message
                            ?: "Unknown error"
                        onCatch(
                            contentMessage,
                            BaseThrowable.InsideThrowable(e.code(), errorContent)
                        )
                    }
                    else -> {
                        onCatch(contentMessage, BaseThrowable.ExternalThrowable(e))
                    }
                }
            }
        }
    }

    private fun onCatch(message: Message, throwable: BaseThrowable) {
        _deleteMessage.postValue(message)
        _errorMessage.postValue(throwable)
    }

    private fun onStart(selfMessage: Message) {
        _inputLiveData.postValue(selfMessage)
    }

    private fun String.urlToMessage(content: String): Message {
        val user = User(
            BaseMessagesActivity.ASSISTANT_ID,
            ASSISTANT_ROLE,
            "https://img0.baidu.com/it/u=664228060,2961772095&fm=253&fmt=auto&app=120&f=PNG?w=192&h=192",
            true
        )

        val message = Message(UUID.randomUUID().leastSignificantBits.toString(), user, content)
        message.setImage(Message.Image(this))
        return message
    }

    private fun ChatMajor.conciseToMessage(): Message {
        val user = User(
            BaseMessagesActivity.ASSISTANT_ID,
            ASSISTANT_ROLE,
            "https://img0.baidu.com/it/u=664228060,2961772095&fm=253&fmt=auto&app=120&f=PNG?w=192&h=192",
            true
        )
        return Message(id, user, content)
    }

    private fun selfMessage(sendContent: String): Message {
        val selfUser = User(BaseMessagesActivity.USER_ID, "user", null, true)
        val selfId = UUID.randomUUID().leastSignificantBits.toString()
        return Message(selfId, selfUser, sendContent)
    }

    private fun Message.requestMessage(): RequestChat.Message {
        return RequestChat.Message(role = user.name, content = text)
    }

    companion object {
        private const val QUEUE_CAPACITY = 10
        private const val ASSISTANT_ROLE = "assistant"
    }
}