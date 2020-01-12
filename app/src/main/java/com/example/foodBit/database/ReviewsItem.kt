package com.example.foodBit.database

import com.google.gson.annotations.SerializedName

data class ReviewsItem(

	@field:SerializedName("review")
	val review: Review? = null
)