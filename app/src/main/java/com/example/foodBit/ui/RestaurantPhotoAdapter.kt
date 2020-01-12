package com.example.foodBit.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingComponent
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import com.example.foodBit.R
import com.example.foodBit.database.Photo
import com.example.foodBit.databinding.ListItemRestaurantPhotoBinding
import com.example.foodBit.util.AppExecutors
import com.example.foodBit.util.DataBoundListAdapter

class RestaurantPhotoAdapter(
    private val dataBindingComponent: DataBindingComponent,
    appExecutors: AppExecutors,
    private val callback: ((Photo) -> Unit)?
) : DataBoundListAdapter<Photo, ListItemRestaurantPhotoBinding>(
    appExecutors = appExecutors,
    diffCallback = object : DiffUtil.ItemCallback<Photo>() {
        override fun areContentsTheSame(oldItem: Photo, newItem: Photo): Boolean {
            return oldItem.id == newItem.id && oldItem.url == newItem.thumbUrl
        }

        override fun areItemsTheSame(oldItem: Photo, newItem: Photo): Boolean {
            return oldItem.id == newItem.id
        }
    }
) {
    override fun createBinding(parent: ViewGroup): ListItemRestaurantPhotoBinding {
        val binding = DataBindingUtil.inflate<ListItemRestaurantPhotoBinding>(
            LayoutInflater.from(parent.context),
            R.layout.list_item_restaurant_photo,
            parent,
            false,
            dataBindingComponent
        )

        binding.root.setOnClickListener {
            binding.photo.let {
                callback?.invoke(it!!)
            }
        }

        return binding
    }

    override fun bind(binding: ListItemRestaurantPhotoBinding, item: Photo, position: Int) {
        binding.photo = item
    }
}