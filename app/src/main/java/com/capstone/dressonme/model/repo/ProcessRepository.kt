package com.capstone.dressonme.model.repo

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.capstone.dressonme.model.remote.api.ApiService
import com.capstone.dressonme.model.remote.response.*
import com.capstone.dressonme.ui.ApiCallbackString
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import org.json.JSONTokener
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import javax.inject.Inject

class ProcessRepository @Inject constructor(
    private val apiService: ApiService
){

    private val _userPhotos = MutableLiveData<ArrayList<ProcessItem>>()
    val userPhotos: LiveData<ArrayList<ProcessItem>> = _userPhotos

    private val _userProcess = MutableLiveData<ProcessDetail>()
    val userProcess: LiveData<ProcessDetail> = _userProcess

    fun getUserPhoto(token : String, userId: String, callback: ApiCallbackString) {
        val client = apiService.getAllData(token)
        client.enqueue(object : Callback<AllProcessResponse> {
            override fun onResponse(
                call: Call<AllProcessResponse>,
                response: Response<AllProcessResponse>
            ) {
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null && !responseBody.error) {
                        callback.onResponse(response.body() != null, SUCCESS)
                         val userPhoto = ArrayList<ProcessItem>()
                        for (i in responseBody.process) {
                            if(i.userId == userId && i.status == false) {
                                userPhoto.add(i)
                            }
                        }
                        _userPhotos.value = userPhoto
                    }
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                    val jsonObject = JSONTokener(response.errorBody()!!.string()).nextValue() as JSONObject
                    val message = jsonObject.getString("message")
                    callback.onResponse(false, message)
                }
            }

            override fun onFailure(call: Call<AllProcessResponse>, t: Throwable) {
                Log.e(TAG, "onFailure: ${t.message}")
                callback.onResponse(false, t.message.toString())
            }
        })
    }

    fun getUserProcess(token : String, id : String, callback: ApiCallbackString) {
        val client = apiService.getUserProcess(token, id)
        client.enqueue(object : Callback<ProcessResponse> {
            override fun onResponse(
                call: Call<ProcessResponse>,
                response: Response<ProcessResponse>
            ) {
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null && !responseBody.error) {
                        callback.onResponse(response.body() != null, SUCCESS)
                        _userProcess.value = responseBody.process
                    }
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                    val jsonObject = JSONTokener(response.errorBody()!!.string()).nextValue() as JSONObject
                    val message = jsonObject.getString("message")
                    callback.onResponse(false, message)
                }
            }

            override fun onFailure(call: Call<ProcessResponse>, t: Throwable) {
                Log.e(TAG, "onFailure: ${t.message}")
                callback.onResponse(false, t.message.toString())
            }

        })
    }

    fun startProcess(token : String, userId : String, imgFile : File, callback: ApiCallbackString ){
        val requestImage = imgFile.asRequestBody("image/jpeg".toMediaTypeOrNull())
        val uId = userId.toRequestBody("text/plain".toMediaType())
        val imageModel = MultipartBody.Part.createFormData(
            "linkModel",
            imgFile.name,
            requestImage
        )
        val client = apiService.startProcess(token, imageModel, uId)
        client.enqueue(object : Callback<ProcessResponse> {
            override fun onResponse(
                call: Call<ProcessResponse>,
                response: Response<ProcessResponse>
            ) {
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null && !responseBody.error) {
                        callback.onResponse(response.body() != null, SUCCESS)
                        _userProcess.value = responseBody.process
                    }
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                    val jsonObject = JSONTokener(response.errorBody()!!.string()).nextValue() as JSONObject
                    val message = jsonObject.getString("message")
                    callback.onResponse(false, message)
                }
            }

            override fun onFailure(call: Call<ProcessResponse>, t: Throwable) {
                Log.e(TAG, "onFailure: ${t.message}")
                callback.onResponse(false, t.message.toString())
            }

        })
    }

    fun updateResult(token : String, id : String, imgFile : File, callback: ApiCallbackString ){
        val requestImage = imgFile.asRequestBody("image/jpeg".toMediaTypeOrNull())
        val imageModel = MultipartBody.Part.createFormData(
            "linkModel",
            imgFile.name,
            requestImage
        )
        val client = apiService.updateLinkResult(token, id , imageModel)
        client.enqueue(object : Callback<ProcessResponse> {
            override fun onResponse(
                call: Call<ProcessResponse>,
                response: Response<ProcessResponse>
            ) {
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null && !responseBody.error) {
                        callback.onResponse(response.body() != null, SUCCESS)
                        _userProcess.value = responseBody.process
                    }
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                    val jsonObject = JSONTokener(response.errorBody()!!.string()).nextValue() as JSONObject
                    val message = jsonObject.getString("message")
                    callback.onResponse(false, message)
                }
            }

            override fun onFailure(call: Call<ProcessResponse>, t: Throwable) {
                Log.e(TAG, "onFailure: ${t.message}")
                callback.onResponse(false, t.message.toString())
            }

        })
    }

    fun deleteProcess(token : String, id : String, callback: ApiCallbackString ) {
        val client = apiService.deleteProcess(token, id)
        client.enqueue(object : Callback<ApiResponse> {
            override fun onResponse(call: Call<ApiResponse>, response: Response<ApiResponse>) {
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null && !responseBody.error) {
                        callback.onResponse(response.body() != null, SUCCESS)
                    }
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                    val jsonObject = JSONTokener(response.errorBody()!!.string()).nextValue() as JSONObject
                    val message = jsonObject.getString("message")
                    callback.onResponse(false, message)
                }

            }
            override fun onFailure(call: Call<ApiResponse>, t: Throwable) {
                Log.e(TAG, "onFailure: ${t.message}")
                callback.onResponse(false, t.message.toString())
            }
        })
    }

    companion object {
        private const val TAG = "ProcessRepository"
        private const val SUCCESS = "success"
    }
}