package com.example.movies.ui.moviedetail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.movies.databinding.FragmentMovieDetailBinding
import com.example.movies.enums.ApiStatus
import com.example.movies.utility.Utility

class MovieDetailFragment : Fragment() {

    private lateinit var binding: FragmentMovieDetailBinding

    private val viewModel: MovieDetailViewModel by lazy {
        ViewModelProviders.of(this).get(MovieDetailViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentMovieDetailBinding.inflate(inflater)
        binding.lifecycleOwner = this
        binding.viewmodel = viewModel

        var movie = arguments?.let { MovieDetailFragmentArgs.fromBundle(it).movie }
        viewModel.postMovie(movie)

        observers()

        setHasOptionsMenu(true)
        return binding.root
    }

    /***
     * Gözlemciler
     */
    private fun observers() {

        // isteklerin durumunu gözler
        viewModel.status.observe(this, Observer {

            if (it == null)
                return@Observer

            when (it.apiStatus) {

                ApiStatus.LOADING -> {
                    Utility.showDialogPopup(fragmentManager, it)
                }
                ApiStatus.DONE -> {
                    Utility.hideDialogPopup(fragmentManager)
                }
                ApiStatus.ERROR -> {
                    Utility.showDialogPopup(fragmentManager, it)
                }
                else -> {

                }
            }
            viewModel.onStatusTransactionComplated()
        })
    }
}
