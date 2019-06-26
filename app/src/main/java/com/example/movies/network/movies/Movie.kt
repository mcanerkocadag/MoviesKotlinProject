package com.example.movies.network.movies

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class Movie(
    var poster_path: String,
    var adult: Boolean,
    var overview: String,
    var release_date: String,
    var genreIds: List<Int>? = null,
    var id: Int,
    var originalTitle: String,
    var originalLanguage: String,
    var title: String,
    var backdropPath: String,
    var popularity: Double,
    var vote_count: Int,
    var video: Boolean,
    var vote_average: Double
) : Parcelable