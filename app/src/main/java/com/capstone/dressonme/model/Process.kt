package com.capstone.dressonme.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
class Process(
    val _id: String,
    val userId: String,
    val linkModel: String,
    val linkFiltering: String,
    val linkResult: String,
) : Parcelable