package com.example.qthien.week3_ryder

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.os.Message
import android.support.v4.app.DialogFragment
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.bumptech.glide.request.RequestOptions
import com.example.qthien.week3_ryder.Modal.From
import jp.wasabeef.glide.transformations.RoundedCornersTransformation
import kotlinx.android.synthetic.main.dialog_add.view.*
import android.text.InputFilter
import android.util.Log
import android.view.Window
import com.facebook.*
import com.facebook.login.LoginResult
import com.facebook.share.Sharer
import com.facebook.share.model.SharePhoto
import com.facebook.share.model.SharePhotoContent
import com.facebook.share.widget.ShareDialog
import kotlinx.android.synthetic.main.dialog_add.*
import org.json.JSONObject
import java.io.InputStream


class DialogAdd : DialogFragment(){
    lateinit var imgAvata : ImageView
    lateinit var txtName : TextView
    lateinit var btnX : ImageButton
    lateinit var btnPost : Button

    private var callbackManager : CallbackManager? = null

    lateinit var user : From
    private var bitmap: Bitmap? = null


    var shareDialog : ShareDialog? = null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        dialog.window.requestFeature(Window.FEATURE_NO_TITLE)
        shareDialog = ShareDialog(this)
        return LayoutInflater.from(context).inflate(R.layout.dialog_add , container , false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        imgAvata = view.imgAvata
        txtName = view.txtName
        btnX = view.btnX
        btnPost = view.btnPost


        callbackManager = CallbackManager.Factory.create();

//        FacebookSdk.sdkInitialize(getActivity());

        user = arguments?.get("from") as From
        GlideApp.with(context!!).load(user.picture?.data?.url).apply(RequestOptions.bitmapTransform(
            RoundedCornersTransformation(20 , 0)
        )).into(imgAvata)

        txtName.text = user.name

        btnX.setOnClickListener({
            dismiss()
        })

//        edtContent.addTextChangedListener(object : TextWatcher{
//            override fun afterTextChanged(s: Editable?) {
//
//            }
//
//            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
//            }
//
//            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
//                var lengthNow = maxLength - edtContent.text.length
//                if(lengthNow != 0)
//                    txtLength.text = lengthNow.toString()
//            }
//
//        })

        imgSelected.setOnClickListener({
            val intent = Intent(Intent.ACTION_PICK)
            intent.setType("image/*")
            startActivityForResult(intent , 100)
        })

        btnPost.setOnClickListener({
            val photo = SharePhoto.Builder()
                .setBitmap(bitmap)
                .build()
            val content = SharePhotoContent.Builder()
                .addPhoto(photo)
                .build()
            shareDialog?.show(content)
            shareDialog?.registerCallback(callbackManager , object : FacebookCallback<Sharer.Result>{
                override fun onCancel() {
                }

                override fun onError(error: FacebookException?) {
                }

                override fun onSuccess(result: Sharer.Result?) {
                    Log.d("onSuccessss" , "aaaaaaaaa")
                    (context as MainActivity).loadDataRefresh()
                    dismiss()
                }

            })
        })
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        this.callbackManager?.onActivityResult(requestCode, resultCode, data)
        Log.d("qwe" , "Not")
        if(requestCode == 100 && resultCode == Activity.RESULT_OK){
            Log.d("qwe" , data?.data.toString())
            txtSelectImg.visibility = View.GONE
            val inputStream = context?.contentResolver?.openInputStream(data?.data)
            bitmap = BitmapFactory.decodeStream(inputStream)
            imgSelected.setImageBitmap(bitmap)
        }
    }

}