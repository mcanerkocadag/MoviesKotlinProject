package com.example.movies.network.movies

import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize

@Parcelize
class Result(
    @Json(name = "poster_path")
    var poster_path: String,
    @Json(name = "adult")
    var adult: Boolean,
    @Json(name = "overview")
    var overview: String,
    @Json(name = "release_date")
    var releaseDate: String,
    @Json(name = "genre_ids")
    var genreIds: List<Int>? = null,
    @Json(name = "id")
    var id: Int,
    @Json(name = "original_title")
    var originalTitle: String,
    @Json(name = "original_language")
    var originalLanguage: String,
    @Json(name = "title")
    var title: String,
    @Json(name = "backdrop_path")
    var backdropPath: String,
    @Json(name = "popularity")
    var popularity: Double,
    @Json(name = "vote_count")
    var voteCount: Int,
    @Json(name = "video")
    var video: Boolean,
    @Json(name = "vote_average")
    var voteAverage: Double
) : Parcelable