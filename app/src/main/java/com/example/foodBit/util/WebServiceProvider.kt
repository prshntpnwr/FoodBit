package com.example.foodBit.util

import android.annotation.SuppressLint
import android.util.Log
import androidx.paging.PagedList
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

/**
 * a singleton class that provide webservice and paging config
  */
@Singleton
class WebServiceProvider private constructor() {
    private val apiKey = "c750173e8cf7e5fdc2c331cf897ee8c3"
    private val baseUrl = "https://developers.zomato.com/api/v2.1/"

    @SuppressLint("NewApi")
    private fun getOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .addInterceptor { chain ->
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

    private fun getRetrofit(): Retrofit {
        val okHttpClient: OkHttpClient = getOkHttpClient()
        okHttpClient.sslSocketFactory()
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(LiveDataCallAdapterFactory())
            .build()
    }

    fun getWebService(): WebService {
        return if (webservice != null) {
            webservice!!
        } else {
            val restAdapter: Retrofit = getRetrofit()
            val ws = restAdapter.create(WebService::class.java)
            setAPIService(ws)
            ws
        }
    }

    fun getPagedConfig(): PagedList.Config {
        return if (pagedConfig != null) {
            pagedConfig!!
        } else {
            val config = PagedList.Config.Builder()
                .setPageSize(10)
                .setInitialLoadSizeHint(5)
                .setPrefetchDistance(5)
                .setEnablePlaceholders(true)
                .build()
            this.pagedConfig = config
            config
        }
    }

    private var webservice: WebService? = null
    private var pagedConfig: PagedList.Config? = null

    private fun setAPIService(webservice: WebService) {
        this.webservice = webservice
    }

    companion object {
        private var webServiceProvider: WebServiceProvider? = null

        val instance: WebServiceProvider
            get() {
                if (webServiceProvider == null) {
                    webServiceProvider = WebServiceProvider()
                }
                return webServiceProvider!!
            }
    }
}