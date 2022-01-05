package com.teknasyontestapplication.data.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class PositionList(
    @SerializedName("id")
    var id: Int? = null,
    @SerializedName("positions")
    var position: MutableList<Position>? = null,
) : Parcelable