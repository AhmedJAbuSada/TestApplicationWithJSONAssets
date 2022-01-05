package com.teknasyontestapplication.data.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class PositionResponse(
    @SerializedName("list")
    var list: MutableList<PositionList>? = null,
) : Parcelable