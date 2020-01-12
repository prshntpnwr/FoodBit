package com.example.foodBit.util

import androidx.lifecycle.LiveData
import com.example.foodBit.database.CategoryResponse
import com.example.foodBit.database.RestaurantResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * contains all api calls
 */
interface WebService {

    @GET("categories")
    fun fetchCategories(): LiveData<ApiResponse<CategoryResponse>>

    @GET("search")
    fun fetchRestaurantList(
        @Query("count") limit: String,
        @Query("start") after: String,
        @Query("entity_id") entityId: Int,
        @Query("entity_type") entityType: String = "city",
        @Query("category") categories: String
    ): Call<RestaurantResponse>

}