package com.example.foodBit.observer

import androidx.databinding.ObservableInt
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.example.foodBit.database.RestaurantWithPhoto
import com.example.foodBit.repo.AppRepository
import com.example.foodBit.util.AbsentedLiveData
import com.example.foodBit.util.Resource
import javax.inject.Inject

class DetailViewModel @Inject constructor(
    repo: AppRepository
): ViewModel() {

    private val _resId: MutableLiveData<String> = MutableLiveData()
    val resId: LiveData<String>
        get() = _resId
    fun setResId(resId: String) {
        _resId.value = resId
    }

    val checkedBtnObs = ObservableInt(3)

    val result: LiveData<Resource<RestaurantWithPhoto>> = Transformations
        .switchMap(_resId) {
            when(it) {
                null -> AbsentedLiveData.create()
                else -> repo.fetchRestaurant(it)
            }
        }
}
