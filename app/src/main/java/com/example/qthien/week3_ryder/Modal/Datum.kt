package com.example.qthien.week3_ryder.Modal

data class Datum (
    var created_time : String ,
    var id : String ,
    var message : String?,
    var object_id : String,
    var likes : Likes,
    var from : From,
    var attachments : Attachments
)