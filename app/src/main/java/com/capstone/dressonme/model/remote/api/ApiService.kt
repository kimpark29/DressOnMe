package com.capstone.dressonme.model.remote.api


import com.capstone.dressonme.model.remote.response.ApiResponse
import com.capstone.dressonme.model.remote.response.LoginResponse
import com.capstone.dressonme.model.remote.response.AllProcessResponse
import com.capstone.dressonme.model.remote.response.ProcessResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
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

  @GET("api/process")
  fun getAllData(
    @Header("auth-token") auth: String,
  ): Call<AllProcessResponse>

  @GET("api/process/{_id}")
  fun getUserProcess(
    @Header("auth-token") auth: String,
    @Path("_id") _id: String,
  ): Call<ProcessResponse>

  @Multipart
  @POST("api/process")
  fun startProcess(
    @Header("auth-token") auth: String,
    @Part img: MultipartBody.Part,
    @Part("userId") userId: RequestBody
  ): Call<ProcessResponse>

  @Multipart
  @PATCH("api/process/{_id}/result")
  fun updateLinkResult(
    @Header("auth-token") auth: String,
    @Path("_id") _id: String,
    @Part("linkResult") img: MultipartBody.Part,
  ): Call<ProcessResponse>

  @DELETE("api/process/{_id}")
  fun deleteProcess(
    @Header("auth-token") auth: String,
    @Path("_id") _id: String,
  ): Call<ApiResponse>

}
