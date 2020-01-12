package com.example.foodBit.ui

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingComponent
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import com.example.foodBit.R
import com.example.foodBit.database.Restaurant
import com.example.foodBit.databinding.ListItemRestaurantBinding
import com.example.foodBit.util.AppExecutors
import com.example.foodBit.util.DataBoundPagedListAdapter
import com.example.foodBit.util.Status

class RestaurantListAdapter(
    private val dataBindingComponent: DataBindingComponent,
    appExecutors: AppExecutors,
    private val callback: ((Restaurant?, action: String) -> Unit)?
) : DataBoundPagedListAdapter<Restaurant, ListItemRestaurantBinding>(
    appExecutors = appExecutors,
    diffCallback = object : DiffUtil.ItemCallback<Restaurant>() {
        override fun areItemsTheSame(oldItem: Restaurant, newItem: Restaurant): Boolean {
            return oldItem.resId == newItem.resId
        }

        override fun areContentsTheSame(oldItem: Restaurant, newItem: Restaurant): Boolean {
            return oldItem.resId == newItem.resId
        }
    }
) {
    override fun retryAction() {
        callback?.invoke(null,"retry")
    }

    private var netState: Status? = null
    override fun updateNetworkState(newNetworkState: Status) {
        super.updateNetworkState(newNetworkState)
        this.netState = newNetworkState
    }

    override fun createBinding(parent: ViewGroup): ListItemRestaurantBinding {
        val binding = DataBindingUtil
            .inflate<ListItemRestaurantBinding>(
                LayoutInflater.from(parent.context),
                R.layout.list_item_restaurant,
                parent,
                false,
                dataBindingComponent
            )

        binding.root.setOnClickListener { _ ->
            binding.restaurant?.let { it ->
                callback?.invoke(it,"itemClick")
            }
        }

        return binding
    }

    override fun bind(binding: ListItemRestaurantBinding, item: Restaurant, position: Int) {
        Log.d(Thread.currentThread().name, "position $position")
        binding.restaurant = item
    }
}