package com.example.foodBit.repo

import android.util.Log
import com.example.foodBit.database.Restaurant
import com.example.foodBit.database.RestaurantResponse
import com.example.foodBit.util.AppExecutors
import com.example.foodBit.util.DataBoundBoundaryCallback
import com.example.foodBit.util.WebService
import retrofit2.Call

class RestaurantListBoundaryCallback(
    private val handleResponse: (RestaurantResponse?) -> Unit,
    appExecutors: AppExecutors,
    private val webService: WebService,
    private val networkPageSize: Int,
    private val cityId: Int,
    private val categoryIds: String
) : DataBoundBoundaryCallback<Restaurant, RestaurantResponse>(
    appExecutors = appExecutors
) {

    override fun handleAPIResponse(response: RestaurantResponse?) {
        handleResponse(response)
    }

    override fun itemAtEndLoaded(item: Restaurant): Call<RestaurantResponse> {
        Log.d(Thread.currentThread().name, "itemAtEndLoaded ${item.indexInResponse}")
        return webService.fetchRestaurantList(
            limit = networkPageSize.toString(),
            after = item.indexInResponse.toString(),
            entityId = cityId,
            categories = categoryIds
        )
    }

    override fun zeroItemLoaded(): Call<RestaurantResponse> {
        Log.d(Thread.currentThread().name, "zeroItemLoaded")
        return webService.fetchRestaurantList(
            limit = networkPageSize.toString(),
            after = "0",
            entityId = cityId,
            categories = categoryIds
        )
    }
}