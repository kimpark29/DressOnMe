package com.capstone.dressonme.ui.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.capstone.dressonme.local.User
import com.capstone.dressonme.local.UserPreference
import com.capstone.dressonme.remote.api.ApiConfig
import com.capstone.dressonme.remote.response.LoginResponse
import com.capstone.dressonme.ui.ApiCallbackString
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import org.json.JSONObject
import org.json.JSONTokener
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@HiltViewModel
class LoginViewModel(private val pref: UserPreference) : ViewModel() {

    fun login(email: String, pass: String, callback: ApiCallbackString){
        val service = ApiConfig.getApiService().login(email, pass)
        service.enqueue(object : Callback<LoginResponse> {
            override fun onResponse(
                call: Call<LoginResponse>,
                response: Response<LoginResponse>
            ) {
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null && !responseBody.error) {
                        callback.onResponse(response.body() != null, SUCCESS)
                        val model = User(
                            responseBody.userId,
                            responseBody.authToken
                        )
                        saveUser(model)
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

    fun getUser(): LiveData<User> {
        return pref.getUser().asLiveData()
    }

    fun saveUser(user: User) {
        viewModelScope.launch {
            pref.saveUser(user)
        }
    }
    companion object {
        private const val TAG = "SignInViewModel"
        private const val SUCCESS = "success"
    }
}