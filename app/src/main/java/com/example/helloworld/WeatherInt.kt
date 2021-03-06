package com.example.helloworld

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherInt {
    @GET("data/2.5/weather?")
    suspend fun getCurrentWeatherData(@Query("q") city: String, @Query("appid") app_id: String, @Query("units") unit: String, @Query("lang") language: String): Response<WeatherResp>
}