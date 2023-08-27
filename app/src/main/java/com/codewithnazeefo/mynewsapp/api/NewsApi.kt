package com.codewithnazeefo.mynewsapp.api

import com.codewithnazeefo.mynewsapp.Model.ApiResponse
import com.codewithnazeefo.mynewsapp.util.Constant.Companion.API_KEY
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsApi {
    @GET("v2/top-headlines")
    suspend fun getBreakingNews(
        @Query("country") countryCode: String ="us",
        @Query("page") pageNumber :Int = 1,
        @Query("apiKey") apiKey: String = API_KEY): Response<ApiResponse>

    @GET("/v2/everything")
        suspend fun searchForNews(
        @Query("q") searchQuery: String,
        @Query("page") pageNumber :Int = 1,
        @Query("apiKey") apiKey: String = API_KEY): Response<ApiResponse>



}