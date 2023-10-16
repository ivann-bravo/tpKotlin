package com.example.myapplication

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface GeniusApiService {
    @GET("search")
    suspend fun searchArtistInfo(
        @Header("Authorization") token: String,
        @Query("q") artistName: String
    ): Response<SearchResponse>
}
