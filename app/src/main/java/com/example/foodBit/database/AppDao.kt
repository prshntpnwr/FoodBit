package com.example.foodBit.database

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.paging.DataSource
import androidx.room.*

@Dao
interface AppDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertCategories(list: Categories?)

    @Query("SELECT * from category")
    fun fetchCategoryList(): LiveData<List<Categories>>

    @Query("DELETE FROM category")
    fun deleteCategories()

    @Transaction
    fun insertCategoryList(list: List<CategoryResponse.CategoriesItem>) {
        list.forEach { ct -> insertCategories(ct.categories) }
    }

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertRestaurant(list: List<Restaurant>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertRestaurant(list: Restaurant)

    @Query("DELETE FROM restaurant")
    fun deleteRestaurant()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertRestaurantPhoto(list: Photo)

    @Query("SELECT MAX(indexInResponse) + 1 FROM restaurant WHERE cityId = :cityId AND categories = :categories")
    fun getNextIndex(cityId: Int, categories: String) : Int

    @Transaction
    fun insertRestaurants(list: List<RestaurantsItem?>, cityId: Int, categories: String) {
        val start = getNextIndex(cityId, categories)

        list.mapIndexed { index, restaurantsItem ->
            restaurantsItem?.restaurant?.let {
                it.categories = categories
                it.cityId = cityId
                it.userAggregateRating = it.userRating?.aggregateRating ?: "0"
                it.indexInResponse = start + index // increment the index of each record
                Log.d(Thread.currentThread().name, "start: $start index $index indexInResponse ${it.indexInResponse}")

                insertRestaurant(it)

                it.photos?.forEach { photoItem ->
                    photoItem.photo?.let { photo ->
                        photo.resId = photo.rId.toString() // use to create relation with restaurant entity
                        insertRestaurantPhoto(photo)
                    }
                }
            }
        }
    }

    @Transaction
    fun deleteAndInsertData(list: List<RestaurantsItem?>, cityId: Int, categories: String) {
        deleteRestaurant()
        insertRestaurants(list, cityId, categories)
    }

    @Query("SELECT * FROM restaurant WHERE cityId = :cityId AND categories = :categories")
    fun loadRestaurants(cityId: Int, categories: String): DataSource.Factory<Int, Restaurant>

    @Transaction
    @Query("SELECT * FROM restaurant WHERE resId = :resId")
    fun loadRestaurantById(resId: String): LiveData<RestaurantWithPhoto>
}