package com.example.foodBit.database

import com.google.gson.annotations.SerializedName

data class UserRating(

	@field:SerializedName("aggregate_rating")
	val aggregateRating: String? = null,

	@field:SerializedName("rating_color")
	val ratingColor: String? = null,

	@field:SerializedName("rating_tool_tip")
	val ratingToolTip: String? = null,

	@field:SerializedName("rating_text")
	val ratingText: String? = null,

	@field:SerializedName("custom_rating_text_background")
	val customRatingTextBackground: String? = null,

	@field:SerializedName("votes")
	val votes: String? = null,

	@field:SerializedName("custom_rating_text")
	val customRatingText: String? = null
)