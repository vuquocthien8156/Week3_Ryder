package com.example.qthien.week3_ryder

import android.databinding.DataBindingUtil
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast

import com.example.qthien.week3_ryder.databinding.ActivityDetailBinding
import android.databinding.BindingAdapter
import android.support.constraint.ConstraintLayout
import android.support.v4.app.DialogFragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentTransaction
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.Editable
import android.text.InputFilter
import android.text.TextWatcher
import android.text.format.DateUtils
import android.view.View
import android.widget.ArrayAdapter
import android.widget.ImageView
import com.bumptech.glide.request.RequestOptions
import jp.wasabeef.glide.transformations.CropCircleTransformation
import kotlinx.android.synthetic.main.activity_detail.*
import kotlinx.android.synthetic.main.activity_main.*
import android.view.View.FOCUS_DOWN
import android.view.ViewGroup.LayoutParams.FILL_PARENT
import android.widget.LinearLayout


class DetailActivity : AppCompatActivity() {

    object DataBindingAdapter{
        @BindingAdapter("imageUrl")
        @JvmStatic
        fun loadImage(view: ImageView, url: String?) {
            GlideApp.with(view.context).load(url)
                .placeholder(R.drawable.image_placeholder)
                .into(view)
        }

        @BindingAdapter("imageUrlAvata")
        @JvmStatic
        fun loadImageAvata(view: ImageView, url: String?) {
            GlideApp.with(view.context).load(url)
                .placeholder(R.drawable.user)
                .apply(RequestOptions.bitmapTransform(CropCircleTransformation()))
                .into(view)
        }
    }


    var maxLength = 140

    lateinit var adapter : AdapterRecylerCmt
    lateinit var arr : ArrayList<String>

    var datum : Datum? = null
    var bind : ActivityDetailBinding? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        setTitle("Detail")
        datum  = intent.extras.getParcelable("datum")
        if(datum != null){
            bind = DataBindingUtil.setContentView(this , R.layout.activity_detail)
            bind?.datumm = datum

            txtTimeAgo.text =
                DateUtils.getRelativeTimeSpanString(datum!!.created_time * 1000L , System.currentTimeMillis(), DateUtils.MINUTE_IN_MILLIS).toString()
        }

        val FilterArray = arrayOfNulls<InputFilter>(1)
        FilterArray[0] = InputFilter.LengthFilter(maxLength)
        edtCmt.setFilters(FilterArray)

        edtCmt.addTextChangedListener(object : TextWatcher{
            override fun afterTextChanged(s: Editable?) {

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val lengthNow = maxLength - edtCmt.text.length
                txtLength.text = lengthNow.toString()
                if(edtCmt.text.length > 0)
                    btnSend.isEnabled = true
                else
                    btnSend.isEnabled = false
            }

        })

//        edtCmt.setOnFocusChangeListener({ v , b ->
//            if(b)
//                nest.post(Runnable { nest.fullScroll(View.FOCUS_DOWN) })
//        })

        addArr()

        adapter = AdapterRecylerCmt(this ,  arr)
        recyclerCmt.layoutManager = LinearLayoutManager(this , LinearLayoutManager.VERTICAL , false)
        val divi = DividerItemDecoration(this , LinearLayoutManager.VERTICAL)
        recyclerCmt.addItemDecoration(divi)
        recyclerCmt.adapter = adapter

        img_detail.setOnClickListener {
            val tran = supportFragmentManager
            val dialog = DialogImageFull()

            val bundle = Bundle()
            bundle.putString("url" , datum?.attachments?.data!![0].media?.image?.src)
            dialog.arguments = bundle

            dialog.setStyle(DialogFragment.STYLE_NORMAL, R.style.dialog);

            dialog.show(tran , "dialog")
        }
    }

    private fun addArr() {
        arr = ArrayList()
        arr.add("AAAAAAAAAAAAAAAAAAA")
        arr.add("BBBBBBBBBBBBBBBBBBB")
        arr.add("CCCCCCCCCCCCCCCCCCC")
        arr.add("DDDDDDDDDDDDDDDDDDD")
        arr.add("EEEEEEEEEEEEEEEEEEE")
        arr.add("FFFFFFFFFFFFFFFFFFF")
        arr.add("GGGGGGGGGGGGGGGGGGG")
        arr.add("HHHHHHHHHHHHHHHHHHH")
        arr.add("AAAAAAAAAAAAAAAAAAA")
        arr.add("BBBBBBBBBBBBBBBBBBB")
        arr.add("CCCCCCCCCCCCCCCCCCC")
        arr.add("DDDDDDDDDDDDDDDDDDD")
        arr.add("EEEEEEEEEEEEEEEEEEE")
        arr.add("FFFFFFFFFFFFFFFFFFF")
        arr.add("GGGGGGGGGGGGGGGGGGG")
        arr.add("HHHHHHHHHHHHHHHHHHH")
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }
}
