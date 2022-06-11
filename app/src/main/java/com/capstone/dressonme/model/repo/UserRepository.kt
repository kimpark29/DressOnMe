package com.capstone.dressonme.model.repo

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.capstone.dressonme.model.User
import com.capstone.dressonme.model.remote.api.ApiService
import com.capstone.dressonme.model.remote.response.ApiResponse
import com.capstone.dressonme.model.remote.response.LoginResponse
import com.capstone.dressonme.helper.ApiCallbackString
import org.json.JSONObject
import org.json.JSONTokener
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

class UserRepository @Inject constructor(
    private val apiService: ApiService
) {

    private val _loginData = MutableLiveData<User>()
    val loginData: LiveData<User> = _loginData

    fun login(email: String, pass: String, callback: ApiCallbackString){
        val service = apiService.login(email, pass)
        service.enqueue(object : Callback<LoginResponse> {
            override fun onResponse(
                call: Call<LoginResponse>,
                response: Response<LoginResponse>
            ) {
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null && !responseBody.error) {
                        callback.onResponse(response.body() != null, SUCCESS)
                        _loginData.value = User(responseBody.userId, responseBody.authToken, responseBody.email, responseBody.name)
                    }
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                    val jsonObject = JSONTokener(response.errorBody()!!.string()).nextValue() as JSONObject
                    val message = jsonObject.getString("message")
                    callback.onResponse(false, message)
                }
            }
            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                Log.e(TAG, "onFailure: ${t.message}")
                callback.onResponse(false, t.message.toString())
            }
        })
    }

    fun register(name: String, email: String, pass: String, callback: ApiCallbackString){
        val service = apiService.register(name, email, pass)
        service.enqueue(object : Callback<ApiResponse> {
            override fun onResponse(
                call: Call<ApiResponse>,
                response: Response<ApiResponse>
            ) {
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null && !responseBody.error)
                        callback.onResponse(response.body() != null, SUCCESS)
                } else {
                    Log.e(TAG, "onFailure1: ${response.message()}")
                    val jsonObject = JSONTokener(response.errorBody()!!.string()).nextValue() as JSONObject
                    val message = jsonObject.getString("message")
                    callback.onResponse(false, message)
                }
            }
            override fun onFailure(call: Call<ApiResponse>, t: Throwable) {
                Log.e(TAG, "onFailure2: ${t.message}")
                callback.onResponse(false, t.message.toString())
            }
        })
    }

    companion object {
        private const val TAG = "UserRepository"
        private const val SUCCESS = "success"
    }

}