package com.example.movies.ui.movies

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.paging.PagedList
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.movies.R
import com.example.movies.databinding.MovieRecyclerviewItemBinding
import com.example.movies.databinding.ProgressRecyclerviewItemBinding
import com.example.movies.enums.ApiStatus
import com.example.movies.network.movies.Movie
import kotlinx.android.synthetic.main.fragment_movie.view.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

private const val ITEM_VIEW_TYPE_ITEM = 1
private const val ITEM_VIEW_TYPE_PROGRESS = 2

class MovieRecyclerviewAdapter(private val retry: () -> Unit, private val clickListener: MovieListener) :
    PagedListAdapter<Movie, RecyclerView.ViewHolder>(SleepNightDiffCallback()) {

    private var state = ApiStatus.LOADING
    private val adapterScope = CoroutineScope(Dispatchers.Default)

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        when (getItemViewType(position)) {
            ITEM_VIEW_TYPE_ITEM -> {
                val item = getItem(position) as Movie
                (holder as ViewHolder).bind(item, clickListener)
            }
            ITEM_VIEW_TYPE_PROGRESS -> {
                (holder as ProgressViewHolder).bind(state)
            }
            else -> {
                val item = getItem(position) as Movie
                (holder as ViewHolder).bind(item, clickListener)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (position < super.getItemCount()) ITEM_VIEW_TYPE_ITEM else ITEM_VIEW_TYPE_PROGRESS
    }

    override fun getItemCount(): Int {
        return super.getItemCount() + if (hasLoading()) 1 else 0
    }

    private fun hasLoading(): Boolean {
        return super.getItemCount() != 0 && (state == ApiStatus.LOADING || state == ApiStatus.ERROR)
    }

    fun setState(state: ApiStatus) {
        this.state = state
        notifyItemChanged(super.getItemCount())
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        return if (viewType == ITEM_VIEW_TYPE_ITEM) ViewHolder.from(parent)
        else ProgressViewHolder.create(retry, parent)
    }

    fun addList(list: PagedList<Movie>?) {
        adapterScope.launch {

            withContext(Dispatchers.Main) {
                submitList(list)
            }
        }
    }

    class ViewHolder private constructor(val binding: MovieRecyclerviewItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(
            item: Movie,
            clickListener: MovieListener
        ) {
            binding.movie = item
            binding.listener = clickListener
            // Binding utils
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {

                val layoutInflater = LayoutInflater.from(parent.context)
                var dataBinding = MovieRecyclerviewItemBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(dataBinding)
            }
        }
    }

    class ProgressViewHolder(val binding: ProgressRecyclerviewItemBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(status: ApiStatus?) {
            itemView.progress_bar.visibility = if (status == ApiStatus.LOADING) VISIBLE else View.INVISIBLE
            itemView.txt_error.visibility = if (status == ApiStatus.ERROR) VISIBLE else View.INVISIBLE
        }

        companion object {
            fun create(retry: () -> Unit, parent: ViewGroup): ProgressViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                var dataBinding = ProgressRecyclerviewItemBinding.inflate(layoutInflater, parent, false)
                dataBinding.txtError.setOnClickListener {
                    //retry()
                }
                return ProgressViewHolder(dataBinding)
            }
        }
    }

    class SleepNightDiffCallback : DiffUtil.ItemCallback<Movie>() {
        override fun areItemsTheSame(oldItem: Movie, newItem: Movie): Boolean {
            return oldItem.id == newItem.id
        }

        @SuppressLint("DiffUtilEquals")
        override fun areContentsTheSame(oldItem: Movie, newItem: Movie): Boolean {
            return oldItem == newItem
        }

    }

    class MovieListener(val clickListener: (movie: Movie) -> Unit) {
        fun onClick(movie: Movie) = clickListener(movie)
        fun invalidate(movie: Movie) = clickListener(movie)
    }

}