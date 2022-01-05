package com.teknasyontestapplication.data.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Satellite(
    @SerializedName("id")
    var id: Int? = null,
    @SerializedName("active")
    var active: Boolean? = null,
    @SerializedName("name")
    var name: String? = null,
    @SerializedName("cost_per_launch")
    var costPerLaunch: Long? = null,
    @SerializedName("first_flight")
    var firstFlight: String? = null,
    @SerializedName("height")
    var height: Int? = null,
    @SerializedName("mass")
    var mass: Long? = null,
) : Parcelable