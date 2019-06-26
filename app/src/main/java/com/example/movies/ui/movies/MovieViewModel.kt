package com.example.movies.ui.movies

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.example.movies.base.BaseViewModel
import com.example.movies.enums.ApiStatus
import com.example.movies.network.movies.Movie
import com.example.movies.ui.movies.pagelist.MoviePageList
import com.example.movies.ui.movies.pagelist.MoviePageListFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job

class MovieViewModel : BaseViewModel() {

    private var viewModelJob = Job()
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    var newsList: LiveData<PagedList<Movie>>
    private val pageSize = 5
    private val newsDataSourceFactory: MoviePageListFactory =
        MoviePageListFactory(coroutineScope)

    init {
        val config = PagedList.Config.Builder()
            .setPageSize(pageSize)
            .setInitialLoadSizeHint(pageSize * 2)
            .setEnablePlaceholders(false)
            .build()
        newsList = LivePagedListBuilder<Int, Movie>(newsDataSourceFactory, config).build()
    }


    fun getState(): LiveData<ApiStatus> = Transformations.switchMap<MoviePageList,
            ApiStatus>(newsDataSourceFactory.newsDataSourceLiveData, MoviePageList::state)

    fun retry() {
        newsDataSourceFactory.newsDataSourceLiveData.value?.retry()
    }

    fun listIsEmpty(): Boolean {
        return newsList.value?.isEmpty() ?: true
    }
}
