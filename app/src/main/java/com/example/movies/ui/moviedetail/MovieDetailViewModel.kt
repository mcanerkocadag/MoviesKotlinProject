package com.example.movies.ui.moviedetail

import androidx.lifecycle.*
import com.example.movies.base.BaseViewModel
import com.example.movies.network.movies.Movie
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job

class MovieDetailViewModel : BaseViewModel() {

    private val _movie = MutableLiveData<Movie>()
    val movie: LiveData<Movie>
        get() = _movie

    init {
    }

    fun postMovie(movie: Movie?) {
        _movie.value = movie
    }
}
