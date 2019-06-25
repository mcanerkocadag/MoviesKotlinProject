package com.example.movies.ui.movies

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.movies.databinding.MovieRecyclerviewItemBinding
import com.example.movies.network.movies.Result
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

private const val ITEM_VIEW_TYPE_HEADER = 0
private const val ITEM_VIEW_TYPE_ITEM = 1

class MovieRecyclerviewAdapter(private val clickListener: MovieListener) :
    ListAdapter<MovieRecyclerviewAdapter.DataItem, RecyclerView.ViewHolder>(SleepNightDiffCallback()) {

    private val adapterScope = CoroutineScope(Dispatchers.Default)

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is ViewHolder -> {

                val item = getItem(position) as DataItem.MovieItem
                holder.bind(item.movie, clickListener)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is DataItem.Header -> ITEM_VIEW_TYPE_HEADER
            is DataItem.MovieItem -> ITEM_VIEW_TYPE_ITEM
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        return ViewHolder.from(parent)
    }

    fun addList(list: List<Result?>) {
        adapterScope.launch {
            val items = list.map {
                DataItem.MovieItem(it!!)
            }
            withContext(Dispatchers.Main) {
                submitList(items)
            }
        }
    }

    fun addData(listItems: List<Result?>) {
        var size = itemCount
        this.addList(listItems)
        var sizeNew = this.currentList.size
        notifyItemRangeChanged(size, sizeNew)
    }


    class ViewHolder private constructor(val binding: MovieRecyclerviewItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(
            item: Result,
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

    class SleepNightDiffCallback : DiffUtil.ItemCallback<DataItem>() {
        override fun areItemsTheSame(oldItem: DataItem, newItem: DataItem): Boolean {
            return oldItem.id == newItem.id
        }

        @SuppressLint("DiffUtilEquals")
        override fun areContentsTheSame(oldItem: DataItem, newItem: DataItem): Boolean {
            return oldItem == newItem
        }

    }

    class MovieListener(val clickListener: (movie: Result) -> Unit) {
        fun onClick(movie: Result) = clickListener(movie)
    }

    sealed class DataItem {
        data class MovieItem(val movie: Result) : DataItem() {
            override val id = movie.id.toLong()
        }

        object Header : DataItem() {
            override val id = Long.MIN_VALUE
        }

        abstract val id: Long

    }

}