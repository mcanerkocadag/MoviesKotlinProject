package com.example.movies.base

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.movies.enums.ApiStatus
import com.example.movies.utility.Message

open class BaseAndroidViewModel(application: Application) : AndroidViewModel(application) {

    val _status = MutableLiveData<Message>()

    val status: LiveData<Message>
        get() = _status

    fun createMessageModel(mesaj: String, apiStatus: ApiStatus): Message {

        return Message().let {
            it.apiStatus = apiStatus
            it.text = mesaj
            it
        }
    }

    fun onStatusTransactionComplated() {
        _status.value = null
    }
}