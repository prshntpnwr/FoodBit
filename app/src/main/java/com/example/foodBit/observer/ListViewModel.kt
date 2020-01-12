package com.example.foodBit.observer

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.Transformations.map
import androidx.lifecycle.Transformations.switchMap
import androidx.lifecycle.ViewModel
import com.example.foodBit.database.Categories
import com.example.foodBit.repo.AppRepository
import com.example.foodBit.util.Resource
import javax.inject.Inject

class ListViewModel @Inject constructor(
    repo: AppRepository,
    val pageRepo: AppRepository.RestaurantPaginationRepository
): ViewModel() {


    var selectedLocationPosition = 0
    var shouldFetch = true
    private val _filterId: MutableLiveData<Boolean> = MutableLiveData()
    val filterId: LiveData<Boolean>
        get() = _filterId

    private val categoryFetch: MutableLiveData<Boolean?> = MutableLiveData()
    val fetchFlag: Boolean
        get() = categoryFetch.value ?: false

    fun setFetchCategories(flag: Boolean?) {
        if (shouldFetch) categoryFetch.value = flag
    }

    val resResult = map(filterId) { pageRepo.restaurantList() }
    val posts = switchMap(resResult) { it.pagedList }
    val networkState = switchMap(resResult) { it.networkState }
    val refreshState = switchMap(resResult) { it.refreshState }

    val result: LiveData<Resource<List<Categories>>> = Transformations
        .switchMap(categoryFetch) {
            repo.fetchCategoryList()
        }

    fun refresh() {
        resResult.value?.refresh?.invoke()
    }

    fun retry() {
        val listing = resResult.value
        listing?.retry?.invoke()
    }

    fun setIdParam(cityId: Int?, categories: String?) {
        Log.d(Thread.currentThread().name, "setIDParam $shouldFetch")
        if (cityId == null || categories == null || !shouldFetch) return
        pageRepo.setParams(categories, cityId)
        _filterId.value = true

        Log.d(Thread.currentThread().name, "setIDParam_1 $shouldFetch")
    }

    fun setCityId(cityId: Int?) {
        cityId?.let {
            pageRepo.setCityId(it)
            _filterId.value = true
        }
    }

    fun setCategory(categories: String?) {
        categories?.let {
            pageRepo.setCategory(it)
            _filterId.value = true
        }
    }

    fun removeOlderEntry() {
        pageRepo.deleteOlderRecords()
    }
}