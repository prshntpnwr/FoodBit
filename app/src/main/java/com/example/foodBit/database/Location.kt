package com.example.foodBit.database

import com.google.gson.annotations.SerializedName

data class Location (

	@field:SerializedName("zipcode")
	val zipcode: String? = null,

	@field:SerializedName("address")
	val address: String? = null,

	@field:SerializedName("city")
	val city: String? = null,

	@field:SerializedName("locality_verbose")
	val localityVerbose: String? = null,

	@field:SerializedName("latitude")
	val latitude: String? = null,

	@field:SerializedName("locality")
	val locality: String? = null,

	@field:SerializedName("country_id")
	val countryId: Int? = null,

	@field:SerializedName("city_id")
	val cityId: Int? = null,

	@field:SerializedName("longitude")
	val longitude: String? = null
) {
	override fun toString(): String {
		return city ?: ""
	}
}