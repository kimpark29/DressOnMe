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
    val linkModel: String? = null,

    @field:SerializedName("createdAt")
    val createdAt: String? = null,

    @field:SerializedName("linkFiltering")
    val linkFiltering: String? = null,

    @field:SerializedName("_id")
    val id: String? = null,

    @field:SerializedName("linkResult")
    val linkResult: String? = null,

    @field:SerializedName("userId")
    val userId: String? = null,

    @field:SerializedName("status")
    val status: Boolean? = null,

    @field:SerializedName("updatedAt")
    val updatedAt: String? = null
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
    val linkModel: String? = null,

    @field:SerializedName("createdAt")
    val createdAt: String? = null,

    @field:SerializedName("linkFiltering")
    val linkFiltering: String? = null,

    @field:SerializedName("__v")
    val V: Int? = null,

    @field:SerializedName("_id")
    val id: String? = null,

    @field:SerializedName("linkResult")
    val linkResult: Any? = null,

    @field:SerializedName("userId")
    val userId: String? = null,

    @field:SerializedName("status")
    val status: Boolean? = null,

    @field:SerializedName("updatedAt")
    val updatedAt: String? = null
)
