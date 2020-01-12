package com.example.foodBit.repo

import android.util.Log
import androidx.annotation.MainThread
import androidx.lifecycle.LiveData
import androidx.paging.DataSource
import androidx.paging.PagedList
import com.example.foodBit.database.*
import com.example.foodBit.util.*
import com.google.gson.Gson
import retrofit2.Call
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AppRepository @Inject constructor(
    private val webservice: WebService,
    private val executor: AppExecutors,
    private val dao: AppDao,
    val pagedListConfig: PagedList.Config
) {

    fun fetchCategoryList(): LiveData<Resource<List<Categories>>> {
        Log.d(javaClass.name, "fetch_category_list $webservice $dao")
        return object : NetworkBoundResource<List<Categories>, CategoryResponse>(executor) {
            override fun saveCallResult(item: CategoryResponse) {
                Log.d(javaClass.name, "fetch_category: ${Gson().toJson(item)}")
                item.categories?.let {
                    dao.insertCategoryList(list = it)
                }
            }

            override fun shouldFetch(data: List<Categories>?) = data.isNullOrEmpty()

            override fun loadFromDb() = dao.fetchCategoryList()

            override fun createCall() = webservice.fetchCategories()

        }.asLiveData()
    }

    fun fetchRestaurant(resId: String): LiveData<Resource<RestaurantWithPhoto>> {
        return object : NetworkBoundResource<RestaurantWithPhoto, CategoryResponse>(executor) {
            override fun saveCallResult(item: CategoryResponse) {
               TODO()
            }

            override fun shouldFetch(data: RestaurantWithPhoto?) = false

            override fun loadFromDb() = dao.loadRestaurantById(resId = resId)

            override fun createCall() = TODO()

        }.asLiveData()
    }

    inner class RestaurantPaginationRepository  : PaginationRepository<Restaurant, RestaurantResponse>(
        executors = executor,
        pagedListConfig = pagedListConfig
    ) {
        private var categories: String = "1"
        private var cityId: Int = 1
        private val networkPageSize: Int = 10

        fun setParams(categories: String, cityId: Int) {
            this.categories = categories
            this.cityId = cityId
        }

        fun setCityId(cityId: Int) {
            this.cityId = cityId
            deleteOlderRecords()
        }

        fun setCategory(categories: String) {
            this.categories = categories
            deleteOlderRecords()
        }

        @MainThread
        fun restaurantList(): Listing<Restaurant> {
            return response()
        }

        override fun refreshAPI(): Call<RestaurantResponse> {
            return webservice.fetchRestaurantList(
                limit = networkPageSize.toString(),
                after = "0",
                entityId = cityId,
                categories = categories
            )
        }

        override fun boundaryCallback(): DataBoundBoundaryCallback<Restaurant, RestaurantResponse> {
            return RestaurantListBoundaryCallback(
                handleResponse = this::insertResultIntoDb,
                appExecutors = executor,
                webService = webservice,
                cityId = cityId,
                categoryIds = categories,
                networkPageSize = networkPageSize
            )
        }

        override fun dataSourceFactory(): DataSource.Factory<Int, Restaurant> {
            return dao.loadRestaurants(cityId, categories)
        }

        override fun refreshOperation(response: RestaurantResponse?) {
            deleteAndInsertResultIntoDb(response)
        }


        /**
         * Insert the response into the database
         */
        private fun insertResultIntoDb(body: RestaurantResponse?) {
            body?.restaurants?.let {
                dao.insertRestaurants(it, cityId, categories)
            }
        }


        /**
         * Delete older records from data base
         * Insert the response into the database
         */
        private fun deleteAndInsertResultIntoDb(body: RestaurantResponse?) {
            body?.restaurants?.let {
                dao.deleteAndInsertData(it, cityId, categories)
            }
        }

        fun deleteOlderRecords() {
            executor.diskIO().execute {
                dao.deleteRestaurant()
            }
        }
    }
}