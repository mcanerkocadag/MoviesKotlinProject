package com.example.movies.ui.movies

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.example.movies.databinding.FragmentMovieBinding
import com.example.movies.enums.ApiStatus
import com.example.movies.utility.Utility
import com.example.movies.utility.GridSpacingItemDecoration


class MovieFragment : Fragment() {

    private lateinit var binding: FragmentMovieBinding
    private lateinit var movieRecyclerviewAdapter: MovieRecyclerviewAdapter

    private val viewModel: MovieViewModel by lazy {
        ViewModelProviders.of(this).get(MovieViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentMovieBinding.inflate(inflater)
        binding.lifecycleOwner = this
        binding.viewmodel = viewModel

        initAdapter()
        observers()

        setHasOptionsMenu(true)
        return binding.root
    }

    private fun initAdapter() {

        movieRecyclerviewAdapter = MovieRecyclerviewAdapter({ viewModel.retry() },
            MovieRecyclerviewAdapter.MovieListener { movie ->

                movie.let {
                    this.findNavController()
                        .navigate(
                            MovieFragmentDirections.actionMovieFragmentToMovieDetailFragment(
                                movie!!
                            )
                        )
                }
            })
        binding.recyclerView.adapter = movieRecyclerviewAdapter

        val spanCount = 2 // 2 columns
        val spacing = 20 // 20px
        val includeEdge = false
        binding.recyclerView.addItemDecoration(GridSpacingItemDecoration(spanCount, spacing, includeEdge))
    }

    /***
     * Gözlemciler
     */
    private fun observers() {

        statusObserver()
        moviesPageListObserver()
        pageListObserver()
    }

    /**
     * moviesPageListObserver
     */
    private fun moviesPageListObserver() {

        viewModel.newsList.observe(this, Observer {
            movieRecyclerviewAdapter.addList(it)
        })
    }

    /**
     * pagelist observer
     */
    private fun pageListObserver() {
        binding.txtError.setOnClickListener { viewModel.retry() }
        viewModel.getState().observe(this, Observer { state ->
            binding.progressBar.visibility =
                if (viewModel.listIsEmpty() && state == ApiStatus.LOADING) View.VISIBLE else View.GONE
            binding.txtError.visibility =
                if (viewModel.listIsEmpty() && state == ApiStatus.ERROR) View.VISIBLE else View.GONE
            if (!viewModel.listIsEmpty()) {
                movieRecyclerviewAdapter.setState(state ?: ApiStatus.DONE)
            }
        })
    }

    /**
     * servis isteklerin durumunu gözler
     */
    private fun statusObserver() {
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
