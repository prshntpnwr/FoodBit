package com.example.foodBit.database

import com.google.gson.annotations.SerializedName

data class CategoryResponse(

	@field:SerializedName("categories")
	val categories: List<CategoriesItem>? = null

) {

	data class CategoriesItem(

		@field:SerializedName("categories")
		val categories: Categories? = null

	)
}