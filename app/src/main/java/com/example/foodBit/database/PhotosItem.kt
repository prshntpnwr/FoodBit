package com.example.foodBit.database

import com.google.gson.annotations.SerializedName

data class PhotosItem(

	@field:SerializedName("photo")
	val photo: Photo? = null
)