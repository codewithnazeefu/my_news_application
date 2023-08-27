package com.codewithnazeefo.mynewsapp.api

import com.codewithnazeefo.mynewsapp.util.Constant.Companion.BASE_URL
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import kotlin.math.log

object RetrofitInstance {

    val retrofit by lazy {

        var loggingInterceptor  = HttpLoggingInterceptor()
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
        val okHttpClient = OkHttpClient.Builder().addInterceptor(loggingInterceptor).build()
    Retrofit.Builder().baseUrl(BASE_URL).client(okHttpClient).addConverterFactory(GsonConverterFactory.create()).build()
    }

    val api by lazy {

        retrofit.create(NewsApi::class.java)

    }

}