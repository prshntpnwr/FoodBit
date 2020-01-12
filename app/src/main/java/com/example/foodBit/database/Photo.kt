package com.example.foodBit.database

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "restaurant_photo")
data class Photo(

	@PrimaryKey
	@field:SerializedName("id")
	var id: String = "0",

	@field:SerializedName("res_id")
	var rId: Int = 0,

	@field:SerializedName("restaurant_id")
	var resId: String = "0",

	@field:SerializedName("thumb_url")
	var thumbUrl: String? = null,

	@field:SerializedName("friendly_time")
	var friendlyTime: String? = null,

	@field:SerializedName("width")
	var width: Int? = null,

	@field:SerializedName("caption")
	var caption: String? = null,

	@Ignore
	@field:SerializedName("user")
	var user: User? = null,

	@field:SerializedName("url")
	var url: String? = null,

	@field:SerializedName("timestamp")
	var timestamp: Int? = null,

	@field:SerializedName("height")
	var height: Int? = null
)