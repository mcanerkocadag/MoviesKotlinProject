package com.example.movies.ui.movies

import androidx.lifecycle.*
import com.example.movies.base.BaseViewModel
import com.example.movies.enums.ApiStatus
import com.example.movies.network.movies.Data
import com.example.movies.network.BtcApi
import com.example.onlinebakiyekotlin.network.model.User
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class MovieViewModel : BaseViewModel() {

    private val _user = MutableLiveData<User>()
    val user: LiveData<User>
        get() = _user

    private var viewModelJob = Job()
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    init {
    }


    private val _movies = MutableLiveData<Data>()
    val movies: LiveData<Data>
        get() = _movies
    fun getPopularMovies(page:Int) {

        coroutineScope.launch {

            var response = BtcApi.retrofitService.getPopularMovies(page)

            try {
                _status.value = createMessageModel("Şifre hatırlatma maili gönderiliyor", ApiStatus.LOADING)
                val popularMovies = response.await()
                if (popularMovies.results?.size!! > 0) {

                    _movies.value = popularMovies
                    _status.value = createMessageModel("En popular filmler listelendi", ApiStatus.DONE)
                } else {
                    _movies.value = null
                    _status.value = createMessageModel("Popular filmler indirilirken bir sorun ile karşılaşıldı", ApiStatus.ERROR)
                }

            } catch (t: Throwable) {
                _movies.value = null
                _status.value = createMessageModel(t.message!!, ApiStatus.ERROR)
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

    fun onBankaFragmentNavigated() {
        _user.value = null
    }

    fun setNullMovie() {
        _movies.value = null
    }
}
