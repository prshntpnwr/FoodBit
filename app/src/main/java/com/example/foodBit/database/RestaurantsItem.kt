package com.example.foodBit.database

import com.google.gson.annotations.SerializedName

data class RestaurantsItem(

	@field:SerializedName("restaurant")
	val restaurant: Restaurant? = null
)