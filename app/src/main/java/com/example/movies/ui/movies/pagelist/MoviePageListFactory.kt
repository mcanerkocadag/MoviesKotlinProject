package com.example.movies.ui.movies.pagelist

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import com.example.movies.network.movies.Movie
import kotlinx.coroutines.CoroutineScope


class MoviePageListFactory(val coroutineScope: CoroutineScope) : DataSource.Factory<Int, Movie>() {

    val newsDataSourceLiveData = MutableLiveData<MoviePageList>()

    override fun create(): DataSource<Int, Movie> {
        val newsDataSource = MoviePageList(coroutineScope)
        newsDataSourceLiveData.postValue(newsDataSource)
        return newsDataSource
    }
}