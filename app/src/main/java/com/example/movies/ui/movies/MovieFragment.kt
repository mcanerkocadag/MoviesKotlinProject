package com.example.movies.ui.movies

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.movies.databinding.FragmentMovieBinding
import com.example.movies.enums.ApiStatus
import com.example.movies.listener.PaginationScrollListener
import com.example.movies.utility.Utility

class MovieFragment : Fragment() {

    private lateinit var binding: FragmentMovieBinding
    private lateinit var movieRecyclerviewAdapter: MovieRecyclerviewAdapter
    var isLastPage: Boolean = false
    var isLoading: Boolean = false
    var isMoreItems: Boolean = false

    private val viewModel: MovieViewModel by lazy {
        ViewModelProviders.of(this).get(MovieViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentMovieBinding.inflate(inflater)

        // Allows Data Binding to Observe LiveData with the lifecycle of this Fragment
        binding.lifecycleOwner = this
        binding.viewmodel = viewModel


        observers()
        viewModel.getPopularMovies(1)

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
                    //Utility.showDialogPopup(fragmentManager, it)
                    Utility.hideDialogPopup(fragmentManager)
                    Toast.makeText(context, "İşlem Bitti", Toast.LENGTH_SHORT).show()
                }
                ApiStatus.ERROR -> {
                    // Utility.hideDialogPopup(fragmentManager)
                    Utility.showDialogPopup(fragmentManager, it)
                    Toast.makeText(context, "İstek Başarısız", Toast.LENGTH_SHORT).show()
                }
                else -> {

                }
            }
            viewModel.onStatusTransactionComplated()
        })


        movieRecyclerviewAdapter = MovieRecyclerviewAdapter(MovieRecyclerviewAdapter.MovieListener { movie ->
            Toast.makeText(context, "Film ID ${movie.toString()}", Toast.LENGTH_SHORT).show()

            this.findNavController()
                .navigate(
                    MovieFragmentDirections.actionMovieFragmentToMovieDetailFragment(
                        movie
                    )
                )

            //viewModel.onSubeClick(subeID)
        })
        binding.recyclerView.adapter = movieRecyclerviewAdapter
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            binding.recyclerView.addOnScrollListener(object :
                PaginationScrollListener(binding.recyclerView.layoutManager as LinearLayoutManager) {
                override fun isLastPage(): Boolean {
                    return isLastPage
                }

                override fun isLoading(): Boolean {
                    return isLoading
                }

                override fun loadMoreItems() {
                    isLoading = true
                    //you have to call loadmore items to get more data
                    getMoreItems()
                }


            })
        }

        viewModel.movies.observe(this, Observer {

            it?.let { movies ->

                if (isMoreItems) {

                    movieRecyclerviewAdapter.addData(movies.results!!)
                    isMoreItems = false
                    isLoading = false
                } else {
                    movieRecyclerviewAdapter.addList(it.results!!)
                }

                viewModel.setNullMovie()
            }
        })

    }

    fun getMoreItems() {

        isMoreItems = true

        viewModel.getPopularMovies(2)
    }
}
