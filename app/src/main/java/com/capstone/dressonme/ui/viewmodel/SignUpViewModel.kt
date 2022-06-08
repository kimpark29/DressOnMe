package com.capstone.dressonme.ui.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import com.capstone.dressonme.remote.api.ApiConfig
import com.capstone.dressonme.remote.response.ApiResponse
import com.capstone.dressonme.ui.ApiCallbackString
import dagger.hilt.android.lifecycle.HiltViewModel
import org.json.JSONObject
import org.json.JSONTokener
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@HiltViewModel
class SignUpViewModel : ViewModel() {

  fun register(name: String, email: String, pass: String, callback: ApiCallbackString){
    val service = ApiConfig.getApiService().register(name, email, pass)
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
    private const val TAG = "SignUpViewModel"
    private const val SUCCESS = "success"
  }

}