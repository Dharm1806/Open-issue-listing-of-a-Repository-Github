package com.dharam.githubissues

import android.app.Application
import com.dharam.githubissues.constants.Constants.BASE_URL
import com.dharam.githubissues.constants.Constants.CACHE_TIME_IN_HOURS
import com.dharam.githubissues.repository.api.IssueApis
import com.google.gson.GsonBuilder
import okhttp3.*
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.util.concurrent.TimeUnit


class App : Application() {

    //For the sake of simplicity, for now we use this instead of Dagger
    companion object {
        private lateinit var retrofit: Retrofit
        lateinit var issuesApi: IssueApis

    }

    override fun onCreate() {
        super.onCreate()
        //create the gsonBuilder instance
        val gGson = GsonBuilder()
        //create the retrofit instance
        retrofit = Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create(gGson.create()))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(provideOkHttpClient()!!)
                .baseUrl(BASE_URL)
                .build()

        issuesApi = retrofit.create(IssueApis::class.java)

    }

    // function to return intercepter
    fun provideCacheInterceptor(): Interceptor? = Interceptor { chain ->

        val response: Response = chain.proceed(chain.request())
        // re-write response header to force use of cache
        val cacheControl = CacheControl.Builder()
                .maxAge(CACHE_TIME_IN_HOURS, TimeUnit.HOURS)
                .build()
        response.newBuilder()
                .header("Cache-Control", cacheControl.toString())
                .build()
    }

    //return okhttp client
    private fun provideOkHttpClient(): OkHttpClient? = OkHttpClient.Builder()
            .addNetworkInterceptor(provideCacheInterceptor())
            .cache(provideCache())
            .build()

    //return the cache object with properties
    private fun provideCache(): Cache? {

        var cache: Cache? = null
        try {
            cache = Cache(File(applicationContext.getCacheDir(), "http-cache"),
                    10 * 1024 * 1024) // 10 MB
        } catch (e: Exception) {
        }
        return cache
    }

}
