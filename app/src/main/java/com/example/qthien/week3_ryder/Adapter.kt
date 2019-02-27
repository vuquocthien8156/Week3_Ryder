package com.example.qthien.week3_ryder

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.support.v7.widget.RecyclerView
import android.text.format.DateFormat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.request.RequestOptions.bitmapTransform
import com.facebook.share.widget.LikeView
import jp.wasabeef.glide.transformations.CropCircleTransformation
import kotlinx.android.synthetic.main.item_recyler.view.*
import java.util.*
import android.text.format.DateUtils
import android.widget.LinearLayout
import com.choota.dev.ctimeago.TimeAgo


class Adapter(var con: Context, var arr: ArrayList<Datum>) : RecyclerView.Adapter<Adapter.ViewHolder>() {
    enum class Type{
        profile_media,
        share,
        cover_photo
    }


    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(con).inflate(R.layout.item_recyler , p0 , false))
    }

    override fun getItemCount(): Int = arr.size

    fun getTimeFromTimeSnap(timestamp : Long) : String{
        val cal = Calendar.getInstance(Locale.ENGLISH)
        cal.timeInMillis = timestamp * 1000L
        val date = DateFormat.format("dd-MM-yyyy hh:mm:ss", cal).toString()
        return date
    }

    @SuppressLint("SimpleDateFormat")
    override fun onBindViewHolder(viewHolder : ViewHolder, position: Int) {
        val datum = arr[position]

//        var date = SimpleDateFormat("dd-MM-yyyy hh:mm:ss").parse()
        viewHolder.txtDate.text = DateUtils.getRelativeTimeSpanString(datum.created_time * 1000L , System.currentTimeMillis(),DateUtils.MINUTE_IN_MILLIS).toString()

        if(datum.attachments?.data?.get(0)?.url != null){
            viewHolder.txtLink.text = datum.attachments?.data!![0].url
            viewHolder.txtLink.visibility = View.VISIBLE
        }
        else {
            viewHolder.txtLink.visibility = View.GONE
        }

        val typeText = when(datum.attachments?.data?.get(0)?.type){
            Type.share.name -> " đã share"
            Type.cover_photo.name -> " đã thay đổi ảnh bìa"
            Type.profile_media.name -> " đã thay đổi ảnh đại diện"
            else -> ""
        }
        viewHolder.txtType.text = typeText

        viewHolder.txt.setText(datum.from?.name)

        if(datum.message == null)
            viewHolder.txtStatus.visibility = View.GONE
        else {
            viewHolder.txtStatus.visibility = View.VISIBLE
            if(datum.message?.any { it == '#' } == false)
                viewHolder.txtStatus.setText(datum.message)
            else{
                viewHolder.txtStatus.setText(datum.message)
                viewHolder.txtStatus.setTextColor(Color.BLACK)
            }

        }
        GlideApp.with(con).load(datum.attachments?.data?.get(0)?.media?.image?.src)
            .placeholder(R.drawable.image_placeholder)
            .into(viewHolder.img)
        GlideApp.with(con).load(datum.from?.picture?.data?.url)
            .placeholder(R.drawable.user)
            .apply(bitmapTransform(CropCircleTransformation()))
            .into(viewHolder.imgAvata)

        viewHolder.txtLike.setOnClickListener({
            viewHolder.txtLike.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_likee, 0, 0, 0)
            viewHolder.txtLike.setTextColor(Color.parseColor("#3578e5"))
        })

//        viewHolder.likeView.setLikeViewStyle(LikeView.Style.STANDARD);
//        viewHolder.likeView.setAuxiliaryViewPosition(LikeView.AuxiliaryViewPosition.INLINE)
//        viewHolder.likeView.setObjectIdAndType(
//            datum.object_id,
//            LikeView.ObjectType.OPEN_GRAPH)

        viewHolder.linear.setOnClickListener({
            var i = Intent(con , DetailActivity::class.java)
            i.putExtra("datum" , datum)
            con.startActivity(i)
        })

    }

    class ViewHolder(item : View) : RecyclerView.ViewHolder(item){

        var txt : TextView = item.txt
        var txtDate : TextView = item.txtDate
        var txtLike : TextView = item.txtLike
        var txtLink : TextView = item.txtLink
//        var likeView : LikeView = item.likeView
        var txtType : TextView = item.txtType
        var txtComment : TextView = item.txtComment
        var txtShare : TextView = item.txtShare
        var txtStatus : TextView = item.txtStatus
        var img : ImageView = item.img
        var imgAvata : ImageView = item.imgAvata
        var linear : LinearLayout = item.linear
    }
}