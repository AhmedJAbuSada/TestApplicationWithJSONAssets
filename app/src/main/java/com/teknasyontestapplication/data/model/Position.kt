package com.teknasyontestapplication.data.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Position(
    @SerializedName("posX")
    var posX: Double? = null,
    @SerializedName("posY")
    var posY: Double? = null,
) : Parcelable