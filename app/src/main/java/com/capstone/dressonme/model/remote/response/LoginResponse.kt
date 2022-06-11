package com.capstone.dressonme.model.remote.response

import com.google.gson.annotations.SerializedName

data class LoginResponse(

	@field:SerializedName("authToken")
	val authToken: String,

	@field:SerializedName("name")
	val name: String,

	@field:SerializedName("error")
	val error: Boolean,

	@field:SerializedName("message")
	val message: String,

	@field:SerializedName("userId")
	val userId: String,

	@field:SerializedName("email")
	val email: String
)


