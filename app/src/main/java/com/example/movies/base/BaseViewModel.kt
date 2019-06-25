package com.example.movies.base

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.movies.enums.ApiStatus
import com.example.movies.utility.Message

open class BaseViewModel : ViewModel() {

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

    fun createMessageModel(mesaj: String, apiStatus: ApiStatus, data:Any): Message {

        Log.i("Test", "createMessageModel() Data$data")
        return Message().let {
            it.apiStatus = apiStatus
            it.text = mesaj
            it.data = data
            it
        }
    }

    fun onStatusTransactionComplated() {
        _status.value = null
    }

    fun postStatus(message: Message) {

        _status.value = message

    }
}