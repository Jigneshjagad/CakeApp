package com.theappidea.cakeapp.retrofit

import com.theappidea.cakeapp.model.Cake
import retrofit2.Call
import retrofit2.http.GET

interface RetroService {

    @GET("t-reed/739df99e9d96700f17604a3971e701fa/raw/1d4dd9c5a0ec758ff5ae92b7b13fe4d57d34e1dc/waracle_cake-android-client")
    fun getCakeDataFromApi(): Call<Cake>
}