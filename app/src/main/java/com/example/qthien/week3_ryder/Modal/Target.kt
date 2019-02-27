package com.example.qthien.week3_ryder.Modal

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Target (
    var id : String?,
    var url : String?
) : Parcelable