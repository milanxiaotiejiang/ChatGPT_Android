package com.milan.chat.openai.gpt.ui.api

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.seabreeze.robot.base.ext.tool.gToBean
import com.seabreeze.robot.data.net.DataRepository
import com.seabreeze.robot.data.net.bean.response.OpenAiError
import kotlinx.coroutines.launch
import retrofit2.HttpException

/**
 * User: milan
 * Time: 2023/4/6 17:29
 * Des:
 */
class ApiViewModel : ViewModel() {

    private val _resultLiveData = MutableLiveData<Boolean>()
    val resultLiveData: LiveData<Boolean>
        get() = _resultLiveData


    fun tryRequest() {
        viewModelScope.launch {
            try {
                val models = DataRepository.INSTANCE.models()
                if (models.data.isNotEmpty()) {
                    _resultLiveData.postValue(true)
                } else {
                    _resultLiveData.postValue(false)
                }
            } catch (e: Exception) {
                e.printStackTrace()
                when (e) {
                    is HttpException -> {
                        val errorBody = e.response()?.errorBody()?.string()
                        val errorContent = errorBody?.gToBean<OpenAiError>()?.error?.message
                            ?: "Unknown error"
                        _resultLiveData.postValue(false)
                    }
                    else -> {
                        _resultLiveData.postValue(false)
                    }
                }
            }

        }
    }
}