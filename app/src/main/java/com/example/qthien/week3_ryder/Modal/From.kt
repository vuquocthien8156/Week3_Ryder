package com.example.qthien.week3_ryder.Modal

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class From (
    var name : String?,
    var picture : Picture?,
    var id : String?
    ) : Parcelable

