package com.example.foodBit.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "category")
data class Categories(

	@PrimaryKey(autoGenerate = false)
	@field:SerializedName("id")
	val id: Int = 0,

	@field:SerializedName("name")
	val name: String? = null
) {
	override fun toString(): String {
		return name ?: ""
	}
}