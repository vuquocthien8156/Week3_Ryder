package com.example.qthien.week3_ryder.Modal

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Image(
    var height : Int?,
    var src : String?,
    var width : Int?
) : Parcelable