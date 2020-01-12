package com.example.foodBit.database

import androidx.room.Embedded
import androidx.room.Relation

class RestaurantWithPhoto {
    @Embedded
    var restaurant: Restaurant? = null

    @Relation(parentColumn = "resId", entityColumn = "resId")
    var photos: MutableList<Photo> = ArrayList()
}