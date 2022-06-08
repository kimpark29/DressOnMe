package com.capstone.dressonme.model.repo

import com.capstone.dressonme.remote.api.ApiService
import com.capstone.dressonme.remote.response.LoginResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

class UserRepository @Inject constructor(
    private val apiService: ApiService
) {

}