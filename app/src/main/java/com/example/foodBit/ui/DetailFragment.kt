package com.example.foodBit.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingComponent
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.example.foodBit.R
import com.example.foodBit.binding.FragmentDataBindingComponent
import com.example.foodBit.databinding.DetailFragmentBinding
import com.example.foodBit.di.Injectable
import com.example.foodBit.observer.DetailViewModel
import com.example.foodBit.util.AppExecutors
import javax.inject.Inject

class DetailFragment : Fragment(), Injectable {
    @Inject
    lateinit var executors: AppExecutors

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var viewModel: DetailViewModel

    private lateinit var binding: DetailFragmentBinding
    private var restaurantId = "0"

    private var dataBindingComponent: DataBindingComponent = FragmentDataBindingComponent(this)

    private lateinit var photoAdapter: RestaurantPhotoAdapter

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(RESTAURANT_ID, restaurantId)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        DataBindingUtil.inflate<DetailFragmentBinding>(inflater,
            R.layout.detail_fragment,
            container,
            false,
            dataBindingComponent)
            .also {
                it.lifecycleOwner = this
                binding = it
                setupToolbar()
            }.run {
                return this.root
            }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        restaurantId = savedInstanceState?.getString(RESTAURANT_ID) ?: DetailFragmentArgs.fromBundle(arguments).restaurantId

        photoAdapter = RestaurantPhotoAdapter(
            dataBindingComponent = dataBindingComponent,
            appExecutors = executors,
            callback = null
        ).also {
            binding.photoRecyclerView.adapter = it
        }

        viewModel = ViewModelProviders.of(this, viewModelFactory)
            .get(DetailViewModel::class.java)
            .also {
                it.setResId(resId = restaurantId)
                it.result.observe(this, Observer { res ->
                    res?.data.let {
                        binding.restaurant = it?.restaurant
                        photoAdapter.submitList(it?.photos)
                        binding.notifyChange()
                    }
                })
            }
    }

    private fun setupToolbar() {
        binding.detailToolbar.let { toolbar ->
            toolbar.navigationIcon = ContextCompat.getDrawable(requireContext(), R.drawable.avd_arrow_back)
            toolbar.setNavigationOnClickListener {
                requireActivity().onBackPressed()
            }

            toolbar.title = ""
        }
    }

    companion object {
        const val RESTAURANT_ID = "restaurant_id"
    }
}
