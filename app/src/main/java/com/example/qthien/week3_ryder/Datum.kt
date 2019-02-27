package com.example.qthien.week3_ryder

import android.os.Parcelable
import com.example.qthien.week3_ryder.Modal.Attachments
import com.example.qthien.week3_ryder.Modal.From
import com.example.qthien.week3_ryder.Modal.Likes
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Datum (
    var created_time : Long,
    var id : String?,
    var message : String?,
    var object_id : String?,
    var likes : Likes?,
    var from : From?,
    var attachments : Attachments?
) : Parcelable