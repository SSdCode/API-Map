package com.sdcode.markerlist.retrofit

import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

object APIService {
    private const val BASE_URL = "https://9code.info/"
    private var markerService: MarkerService? = null

    @JvmStatic
    val service: MarkerService?
        get() {
            if (markerService == null) {
                val retrofit = Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
                markerService = retrofit.create(MarkerService::class.java)
            }
            return markerService
        }

    interface MarkerService {
        @get:GET("api/get.php/")
        val markerList: Call<JsonObject?>?
    }
}