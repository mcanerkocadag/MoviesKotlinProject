package com.example.movies.utility

import com.example.movies.enums.ApiStatus

class Message {
    var text: String = ""
    var apiStatus: ApiStatus? = null
    var data: Any? = null

    override fun toString(): String {
        return "Status: $apiStatus text: $text"
    }
}

