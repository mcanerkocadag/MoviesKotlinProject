
package com.example.movies.network

import com.example.movies.network.movies.Data
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import kotlinx.coroutines.Deferred
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.*

private const val BASE_URL = "https://api.themoviedb.org/3/movie/"
private const val API_KEY = "4839a2c8466e1f12b28d5a4ecd201bf2"

private val retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create())
    .addCallAdapterFactory(CoroutineCallAdapterFactory())
    .baseUrl(BASE_URL)
    .build()

interface ApiService {

    @GET("popular?api_key=$API_KEY&language=en-US")
    fun getPopularMovies(
        @Query("page") page:Int
        ):Deferred<Data>
}

object MovieApi {
    val retrofitService: ApiService by lazy {
        retrofit.create(ApiService::class.java)
    }
}
