package com.example.movies.ui.moviedetail

import androidx.lifecycle.*
import com.example.movies.base.BaseViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class MovieDetailViewModel : BaseViewModel() {

    private val _movie = MutableLiveData<com.example.movies.network.movies.Result>()
    val movie: LiveData<com.example.movies.network.movies.Result>
        get() = _movie

    private var viewModelJob = Job()
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    init {
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

    fun resetMovie() {
        _movie.value = null
    }

    fun postMovie(movie: com.example.movies.network.movies.Result?) {
        _movie.value = movie
    }
}
