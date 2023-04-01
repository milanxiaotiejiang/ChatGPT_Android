package com.milan.chat.openai.gpt.ui.chat

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.milan.chat.openai.gpt.api.ConciseData
import com.milan.chat.openai.gpt.api.OpenAiError
import com.milan.chat.openai.gpt.api.OpenAiRespiratory
import com.milan.chat.openai.gpt.api.RequestData
import com.milan.chat.openai.gpt.ext.gToBean
import com.milan.chat.openai.gpt.model.BaseThrowable
import com.milan.chat.openai.gpt.ui.BaseMessagesActivity
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
class ChatViewModel : ViewModel() {

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

    private val _createLiveData = MutableLiveData<Message>()
    val createLiveData: LiveData<Message>
        get() = _createLiveData

    private val _streamLiveData = MutableLiveData<Message>()
    val streamLiveData: LiveData<Message>
        get() = _streamLiveData

    private val _messageLiveData = MutableLiveData<Message>()
    val messageLiveData: LiveData<Message>
        get() = _messageLiveData

    private val _errorMessage = MutableLiveData<BaseThrowable>()
    val errorMessage: LiveData<BaseThrowable>
        get() = _errorMessage

//    fun sendMessage(sendContent: String) {
//        val messageStringBuilder = StringBuilder()
//        var currentId: String = UUID.randomUUID().leastSignificantBits.toString()
//
//        val senderMessage = sendContent.senderMessage()
//
//        val selfUser = User(USER_ID, senderMessage.role, null, true)
//        val selfId = UUID.randomUUID().leastSignificantBits.toString()
//        val selfMessage = Message(selfId, selfUser, senderMessage.content)
//        _inputLiveData.value = selfMessage
//
//
//        viewModelScope.launch {
//
//            try {
//                val messageMutableList = mutableListOf<RequestData.Message>()
//                getMessageQueue().forEach {
//                    val requestMessage = RequestData.Message(role = it.user.name, content = it.text)
//                    messageMutableList.add(requestMessage)
//                }
//                messageMutableList.add(senderMessage)
//                val requestData = RequestData(messages = messageMutableList)
//
//                OpenAiRespiratory.INSTANCE.getCompletion(requestData) { conciseData ->
//                    currentId = conciseData.id
//                    if (conciseData.finish_reason != "stop") {
//                        conciseData.role?.let { role ->
//                            val user = User(ASSISTANT_ID, ASSISTANT_ROLE, null, true)
//                            val message = Message(currentId, user, "")
//                            _createLiveData.postValue(message)
//                        }
//                        conciseData.content?.let { content ->
//                            messageStringBuilder.append(content)
//
//                            val user = User(ASSISTANT_ID, ASSISTANT_ROLE, null, true)
//                            val message = Message(currentId, user, messageStringBuilder.toString())
//                            _streamLiveData.postValue(message)
//                        }
//                    }
//                }
//                withContext(Dispatchers.Main) {
//                    val user = User(ASSISTANT_ID, ASSISTANT_ROLE, null, true)
//                    val message = Message(currentId, user, messageStringBuilder.toString())
//                    _messageLiveData.value = message
//
//                    addMessageToQueue(selfMessage)
//                    addMessageToQueue(message)
//
//                }
//            } catch (e: Exception) {
//                e.printStackTrace()
//                when (e) {
//                    is HttpException -> {
//                        val errorBody = e.response()?.errorBody()?.string()
//                        val errorContent =
//                            errorBody?.gToBean<OpenAiError>()?.error?.message ?: "Unknown error"
//                        _errorMessage.value = BaseThrowable.InsideThrowable(e.code(), errorContent)
//                    }
//                    else -> _errorMessage.value = BaseThrowable.ExternalThrowable(e)
//                }
//            }
//        }
//    }


    fun sendMessage(sendContent: String) {
        val selfMessage = selfMessage(sendContent)
        val requestMessage = selfMessage.requestMessage()

        viewModelScope.launch {

            try {
                val messageMutableList = mutableListOf<RequestData.Message>()
                getMessageQueue().forEach {
                    val queueMessage = RequestData.Message(role = it.user.name, content = it.text)
                    messageMutableList.add(queueMessage)
                }
                messageMutableList.add(requestMessage)
                val requestData = RequestData(messages = messageMutableList)

                OpenAiRespiratory.INSTANCE.getCompletion(requestData).onStart {
                        _inputLiveData.value = selfMessage
                    }.catch { e ->
                        throw BaseThrowable.ExternalThrowable(e)
                    }.collect { conciseData ->
                        if (conciseData.done) {
                            _messageLiveData.value = conciseData.conciseToMessage()

                            addMessageToQueue(selfMessage)
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
                        val errorContent =
                            errorBody?.gToBean<OpenAiError>()?.error?.message ?: "Unknown error"
                        _errorMessage.value = BaseThrowable.InsideThrowable(e.code(), errorContent)
                    }
                    else -> _errorMessage.value = BaseThrowable.ExternalThrowable(e)
                }
            }
        }
    }

    private fun ConciseData.conciseToMessage(): Message {
        val user = User(BaseMessagesActivity.ASSISTANT_ID, ASSISTANT_ROLE,
            "https://img0.baidu.com/it/u=664228060,2961772095&fm=253&fmt=auto&app=120&f=PNG?w=192&h=192", true)
        return Message(id, user, content)
    }

    private fun selfMessage(sendContent: String): Message {
        val selfUser = User(BaseMessagesActivity.USER_ID, "user", null, true)
        val selfId = UUID.randomUUID().leastSignificantBits.toString()
        return Message(selfId, selfUser, sendContent)
    }

    private fun Message.requestMessage(): RequestData.Message {
        return RequestData.Message(role = user.name, content = text)
    }

    companion object {
        private const val QUEUE_CAPACITY = 10
        private const val ASSISTANT_ROLE = "assistant"
    }
}