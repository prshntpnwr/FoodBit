package com.example.foodBit.database

import com.google.gson.annotations.SerializedName

data class AllReviews(

	@field:SerializedName("reviews")
	val reviews: List<Any?>? = null
)