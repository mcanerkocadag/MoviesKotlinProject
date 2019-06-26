package com.example.movies.network.movies

class Data(
    var page: Int,
    var results: List<Movie>? = null,
    var totalResults: Int,
    var totalPages: Int
)