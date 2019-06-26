package com.example.movies.ui.movies.pagelist

import androidx.lifecycle.MutableLiveData
import androidx.paging.PageKeyedDataSource
import com.example.movies.enums.ApiStatus
import com.example.movies.network.MovieApi
import com.example.movies.network.movies.Movie
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class MoviePageList(private val coroutineScope: CoroutineScope) : PageKeyedDataSource<Int, Movie>() {
    var state: MutableLiveData<ApiStatus> = MutableLiveData()


    override fun loadInitial(params: LoadInitialParams<Int>, callback: LoadInitialCallback<Int, Movie>) {

        updateState(ApiStatus.LOADING)
        coroutineScope.launch {

            var response = MovieApi.retrofitService.getPopularMovies(1)

            try {
                val popularMovies = response.await()
                if (popularMovies.results?.size!! > 0) {

                    updateState(ApiStatus.DONE)
                    callback.onResult(
                        popularMovies.results!!,
                        null,
                        2
                    )
                } else {
                    updateState(ApiStatus.ERROR)
                }

            } catch (t: Throwable) {
                updateState(ApiStatus.ERROR)
            }
        }
    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, Movie>) {

        updateState(ApiStatus.LOADING)
        coroutineScope.launch {

            var page: Int = params.key
            var response = MovieApi.retrofitService.getPopularMovies(page = page)

            try {
                val popularMovies = response.await()
                if (popularMovies.results?.size!! > 0) {

                    updateState(ApiStatus.LOADING)
                    callback.onResult(
                        popularMovies.results!!,
                        params.key + 1
                    )
                } else {
                    updateState(ApiStatus.ERROR)
                }

            } catch (t: Throwable) {
                updateState(ApiStatus.ERROR)
            }
        }

    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, Movie>) {
    }

    private fun updateState(state: ApiStatus) {
        this.state.postValue(state)
    }

    fun retry() {
        invalidate()
    }

}