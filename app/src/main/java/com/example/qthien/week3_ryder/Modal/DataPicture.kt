package com.example.qthien.week3_ryder.Modal

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class DataPicture (
    var height : Int?,
    var is_silhouette : Boolean?,
    var url : String?,
    var width : String?
) : Parcelable