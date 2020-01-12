package com.example.foodBit.database

import com.google.gson.annotations.SerializedName

data class RestaurantResponse(

	@field:SerializedName("results_found")
	val resultsFound: Int? = null,

	@field:SerializedName("results_shown")
	val resultsShown: Int? = null,

	@field:SerializedName("restaurants")
	val restaurants: List<RestaurantsItem?>? = null,

	@field:SerializedName("results_start")
	val resultsStart: Int? = null
)