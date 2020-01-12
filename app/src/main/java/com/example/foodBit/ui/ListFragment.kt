package com.example.foodBit.ui

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingComponent
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.example.foodBit.R
import com.example.foodBit.binding.FragmentDataBindingComponent
import com.example.foodBit.database.Location
import com.example.foodBit.databinding.FragmentListBinding
import com.example.foodBit.observer.ListViewModel
import com.example.foodBit.util.AppExecutors
import com.google.android.material.chip.Chip
import com.google.gson.Gson
import android.net.ConnectivityManager
import android.widget.ArrayAdapter
import androidx.lifecycle.ViewModelProvider
import com.example.foodBit.di.Injectable
import javax.inject.Inject

/**
 * A simple [Fragment] subclass.
 */
class ListFragment : Fragment(), Injectable {

    @Inject
    lateinit var executors: AppExecutors

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var viewModel: ListViewModel

    private var dataBindingComponent: DataBindingComponent = FragmentDataBindingComponent(this)

    private lateinit var binding: FragmentListBinding
    private lateinit var adapter: RestaurantListAdapter
    private lateinit var dialog: AlertDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loadLocation()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        DataBindingUtil.inflate<FragmentListBinding>(
            inflater,
            R.layout.fragment_list,
            container,
            false,
            dataBindingComponent
        )
            .also {
                it.lifecycleOwner = this
                it.currentLocation = "Delhi"
                binding = it
            }.run {
                return this.root
            }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        adapter = RestaurantListAdapter(
            dataBindingComponent = dataBindingComponent,
            appExecutors = executors
        ) { restaurant, action ->
            when (action) {
                "retry" -> viewModel.retry()
                "itemClick" -> {
                    restaurant?.let {
                        viewModel.shouldFetch = false
                        val act = ListFragmentDirections.actionListFragmentToDetailFragment(restaurant.resId)
                        findNavController().navigate(act)
                    }
                }
            }
        }.also {
            binding.recyclerView.adapter = it
        }

        viewModel = ViewModelProviders.of(this, viewModelFactory)
            .get(ListViewModel::class.java)
            .also {
                binding.viewModel = it
                if (it.shouldFetch) it.setFetchCategories(true)
                it.result.observe(this, Observer { res ->
                    Log.d(Thread.currentThread().name, "categories_are: ${Gson().toJson(res)}")

                    res?.data?.let { categories ->
                        for ((index, c) in categories.withIndex()) {
                            val chip = Chip(requireContext())
                            chip.id = c.id                      // dynamically setting id
                            chip.text = c.toString()
                            chip.isCheckable = true
                            if (index == 0) chip.isChecked = true
                            binding.categoryGroup.addView(chip)
                        }

                        if (categories.isNotEmpty()) {

                            if (isNetworkAvailable()) {
                                it.removeOlderEntry()
                                it.setIdParam(
                                    cityId = locationList[0].cityId,
                                    categories = categories[0].id.toString()
                                )
                            }
                        }
                    }

//                   viewModel.shouldFetch = false
                })

//                it.posts.observe(this, PagedList(adapter::submitList))

                it.posts.observe(this, Observer {
                    Log.d(Thread.currentThread().name, "restaurant_are: ${Gson().toJson(it)}")
                    adapter.submitList(it)
                })

                it.networkState.observe(this, Observer {
                    adapter.updateNetworkState(it)
                })
            }

        initLocationSelection()
        binding.categoryGroup.setOnCheckedChangeListener { _, i -> viewModel.setCategory(i.toString()) }

        binding.categoryGroup.setOnClickListener{ showDialog() }
        binding.titleTextView.setOnClickListener { dialog.show() }
    }

    private fun showDialog() {
        SingleChoiceDialog(
            context = requireContext(),
            title = getString(R.string.your_location),
            args = locationList,
            selectedPosition = viewModel.selectedLocationPosition,
            positiveActionTitle = getString(R.string.select),
            actionPositive = this::onLocationSelected
        ).show()
    }

    private fun initLocationSelection() {
        val arrayAdapter = ArrayAdapter(context, R.layout.select_dialog_singlechoice_material, locationList)
        dialog = AlertDialog.Builder(context)
            .setTitle(getString(R.string.location))
            .setSingleChoiceItems(arrayAdapter, 0) { _, i ->
                arrayAdapter.getItem(i)?.let {
                    onLocationSelected(it, i)
                }
                dialog.dismiss()
            }
            .create()
    }

    private fun onLocationSelected(location: Location, position: Int) {
        viewModel.selectedLocationPosition = position
        location.city?.let {
            binding.currentLocation = it
        }
        viewModel.setCityId(location.cityId)
    }

    // return true if network is available otherwise false in boolean.
    private fun isNetworkAvailable(): Boolean {
        val connectivityManager = requireContext().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetworkInfo = connectivityManager.activeNetworkInfo
        return activeNetworkInfo != null && activeNetworkInfo.isConnected
    }


    var locationList: MutableList<Location> = ArrayList()
    private fun loadLocation() {
        val delhi = Location(cityId = 1, city = "Delhi NCR")
        val bengaluru = Location(cityId = 4, city = "Bengaluru")
        val chennai = Location(cityId = 7, city = "Chennai")
        val hyderabad = Location(cityId = 6, city = "Hyderabad")
        val kolKata = Location(cityId = 2, city = "Kolkata")
        val mumbai = Location(cityId = 3, city = "Mumbai")
        val pune = Location(cityId = 3, city = "Pune")

        locationList.add(delhi)
        locationList.add(bengaluru)
        locationList.add(chennai)
        locationList.add(hyderabad)
        locationList.add(kolKata)
        locationList.add(mumbai)
        locationList.add(pune)
    }
}
