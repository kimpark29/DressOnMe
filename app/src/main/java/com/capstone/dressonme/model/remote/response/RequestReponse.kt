package com.capstone.dressonme.model.remote.response

import com.google.gson.annotations.SerializedName

data class AllProcessResponse(

    @field:SerializedName("process")
    val process: ArrayList<ProcessItem>,

    @field:SerializedName("count")
    val count: Int,

    @field:SerializedName("error")
    val error: Boolean,

    @field:SerializedName("message")
    val message: String
)

data class ProcessItem(

    @field:SerializedName("linkModel")
    val linkModel: String,

    @field:SerializedName("createdAt")
    val createdAt: String,

    @field:SerializedName("linkFiltering")
    val linkFiltering: String,

    @field:SerializedName("_id")
    val id: String,

    @field:SerializedName("linkResult")
    val linkResult: String,

    @field:SerializedName("userId")
    val userId: String,

    @field:SerializedName("status")
    val status: Boolean,

    @field:SerializedName("updatedAt")
    val updatedAt: String
)

data class ProcessResponse(

    @field:SerializedName("process")
    val process: ProcessDetail,

    @field:SerializedName("error")
    val error: Boolean,

    @field:SerializedName("message")
    val message: String
)

data class ProcessDetail(

    @field:SerializedName("linkModel")
    val linkModel: String,

    @field:SerializedName("createdAt")
    val createdAt: String,

    @field:SerializedName("linkFiltering")
    val linkFiltering: String,

    @field:SerializedName("__v")
    val V: Int,

    @field:SerializedName("_id")
    val id: String,

    @field:SerializedName("linkResult")
    val linkResult: String,

    @field:SerializedName("userId")
    val userId: String,

    @field:SerializedName("status")
    val status: Boolean,

    @field:SerializedName("updatedAt")
    val updatedAt: String
)
