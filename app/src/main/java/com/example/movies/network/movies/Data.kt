package com.example.movies.network.movies

import com.squareup.moshi.Json

class Data (
    @Json(name = "page")
    var page:Int,
    @Json(name = "results")
    var results:List<Result>? = null,
    @Json(name = "total_results")
    var totalResults:Int,
    @Json(name = "total_pages")
    var totalPages:Int
)