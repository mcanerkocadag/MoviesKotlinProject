package com.example.movies.utility

import com.example.movies.base.BaseViewModel

class DialogViewModel : BaseViewModel() {

    fun postDialogModel(message: Message) {
        _status.value = message
    }
}
