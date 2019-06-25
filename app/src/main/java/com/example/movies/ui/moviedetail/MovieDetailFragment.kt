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
import com.example.movies.ui.movies.MovieRecyclerviewAdapter
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

        var movie = arguments?.let { MovieDetailFragmentArgs.fromBundle(it).movie }
        viewModel.postMovie(movie)

        // Allows Data Binding to Observe LiveData with the lifecycle of this Fragment
        binding.lifecycleOwner = this
        binding.viewmodel = viewModel


        observers()
       // viewModel.getPopularMovies(1)

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


        val filmAdapter = MovieRecyclerviewAdapter(MovieRecyclerviewAdapter.MovieListener { movieID ->
            Toast.makeText(context, "Film ID ${movieID.toString()}", Toast.LENGTH_SHORT).show()

            //viewModel.onSubeClick(subeID)
        })
        //binding.recyclerView.adapter = filmAdapter

        viewModel.movie.observe(this, Observer {

            it?.let { subeler ->
               // filmAdapter.addList(it.results!!)
            }
        })

    }
}
