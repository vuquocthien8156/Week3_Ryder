package com.example.qthien.week3_ryder.Modal

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class DataAttachments (
    var description : String?,
    var media: Media?,
    var target: Target?,
    var title: String?,
    var type : String?,
    var url : String?
) : Parcelable