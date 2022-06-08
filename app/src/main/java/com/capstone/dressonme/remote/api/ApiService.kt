package com.capstone.dressonme.remote.api


import com.capstone.dressonme.remote.response.ApiResponse
import com.capstone.dressonme.remote.response.LoginResponse
import retrofit2.Call
import retrofit2.http.*


interface ApiService {

  @FormUrlEncoded
  @POST("api/user/register")
  fun register(
    @Field("name") name: String,
    @Field("email") email: String,
    @Field("password") pass: String
  ): Call<ApiResponse>

  @FormUrlEncoded
  @POST("api/user/login")
  fun login(
    @Field("email") email: String,
    @Field("password") pass: String
  ): Call<LoginResponse>

}
