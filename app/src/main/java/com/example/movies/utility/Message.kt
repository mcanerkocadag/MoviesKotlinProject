package com.example.movies.utility

import com.example.movies.enums.ApiStatus

class Message {
    var text: String = ""
    var apiStatus: ApiStatus? = null
    var possitiveButtonisVisible: Boolean? = null
    var negativeButtonisVisible: Boolean? = null
    var possitiveButtonText: String? = null
    var negativeButtonText: String? = null
    var data: Any? = null

    override fun toString(): String {
        return "Status: $apiStatus text: $text"
    }
}

