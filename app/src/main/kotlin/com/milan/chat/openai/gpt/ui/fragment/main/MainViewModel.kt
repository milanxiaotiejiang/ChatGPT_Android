package com.milan.chat.openai.gpt.ui.fragment.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MainViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is main Fragment"
    }
    val text: LiveData<String> = _text
}