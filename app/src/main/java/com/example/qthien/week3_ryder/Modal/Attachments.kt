package com.example.qthien.week3_ryder.Modal

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Attachments(var data : ArrayList<DataAttachments>?) : Parcelable
