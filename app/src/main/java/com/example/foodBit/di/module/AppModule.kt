package com.example.foodBit.di.module

import android.annotation.SuppressLint
import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.paging.DataSource
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import androidx.room.Room
import com.example.foodBit.database.AppDao
import com.example.foodBit.database.AppDatabase
import com.example.foodBit.repo.AppRepository
import com.example.foodBit.util.AppExecutors
import com.example.foodBit.util.LiveDataCallAdapterFactory
import com.example.foodBit.util.WebService
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module(includes = [(ViewModelModule::class)])
class AppModule {

    // database injection
    @Provides
    @Singleton
    fun provideDatabase(application: Application): AppDatabase {
        return Room.databaseBuilder(application, AppDatabase::class.java, "restaurantApp_db")
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    fun provideGson(): Gson = GsonBuilder().create()

    @Provides
    fun provideExecutor(): AppExecutors = AppExecutors()

    @Provides
    @Singleton
    fun provideAppDao(database: AppDatabase) = database.appDao()

    @Provides
    @Singleton
    fun provideAppRepository(webservice: WebService, executor: AppExecutors, dao: AppDao, pageConfig: PagedList.Config): AppRepository {
        return AppRepository(webservice, executor, dao, pageConfig)
    }


    @Provides
    @Singleton
    fun provideAppPaginationRepository(repo: AppRepository): AppRepository.RestaurantPaginationRepository {
        return repo.RestaurantPaginationRepository()
    }

    /**
     * Pagination injection
     */
    @Provides
    @Singleton
    fun providePagingConfig(): PagedList.Config {
        return PagedList.Config.Builder()
            .setPageSize(10)
            .setInitialLoadSizeHint(30)
            .setPrefetchDistance(10)
            .setEnablePlaceholders(false)
            .build()
    }

    @Provides
    @Singleton
    fun providePageBuilder(source: DataSource.Factory<Int, Any>, config: PagedList.Config, executor: AppExecutors): LiveData<PagedList<Any>> {
        return LivePagedListBuilder(source, config)
            .setFetchExecutor(executor.diskIO())
            .build()
    }

    private val apiKey = "97fa5b00c30b31fe61fab18d5f35d3ea"
    private val baseUrl = "https://developers.zomato.com/api/v2.1/"

    @SuppressLint("NewApi")
    @Provides
    fun provideOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS) .addInterceptor { chain ->
                val original = chain.request()
                Log.e(Thread.currentThread().toString(), "api called - ${chain.request().url()} ${chain.request().body().toString()}")

                // add request headers
                val request = original.newBuilder()
                    .header("user-key", apiKey)
                    .method(original.method(), original.body())
                    .build()
                chain.proceed(request)
            }
            .build()
    }

    @Provides
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        okHttpClient.sslSocketFactory()
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(LiveDataCallAdapterFactory())
            .build()
    }

    @Provides
    @Singleton
    fun provideApiWebservice(restAdapter: Retrofit): WebService {
        val webService = restAdapter.create(WebService::class.java)
//        WebServiceHolder.instance.setAPIService(webService)
        return webService
    }
}