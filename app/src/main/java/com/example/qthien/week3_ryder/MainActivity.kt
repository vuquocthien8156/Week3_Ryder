package com.example.qthien.week3_ryder

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.FacebookSdk
import com.facebook.appevents.AppEventsLogger
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import kotlinx.android.synthetic.main.activity_main.*
import com.facebook.GraphResponse
import com.facebook.GraphRequest
import com.facebook.HttpMethod
import com.facebook.AccessToken
import java.util.*


class MainActivity : AppCompatActivity() {

    var callbackManager : CallbackManager? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setTitle("Đăng nhập với Facebook")

        callbackManager = CallbackManager.Factory.create();

        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this)

        login_button.setReadPermissions("email")
        login_button.setReadPermissions("user_posts")

        // Callback registration
        login_button.registerCallback(callbackManager, object : FacebookCallback<LoginResult> {
            override fun onSuccess(loginResult: LoginResult) {
                GraphRequest(
                    AccessToken.getCurrentAccessToken(),
                        "/me/feed?fields=likes.summary(true),attachments{},object_id,created_time,id,message,from{name,picture.height(300).width(300),id}&date_format=U",
                    null,
                    HttpMethod.GET, object :
                        GraphRequest.Callback {
                        override fun onCompleted(response: GraphResponse?) {
                            Log.d("responsee" , response.toString())
                        }

                    }
                ).executeAsync()
            }

            override fun onCancel() {
                Toast.makeText(applicationContext , "onCancel" , Toast.LENGTH_SHORT).show()
            }

            override fun onError(exception: FacebookException) {
                Toast.makeText(applicationContext , "onError" , Toast.LENGTH_SHORT).show()
            }
        })

    }



    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        callbackManager?.onActivityResult(requestCode, resultCode, data)
    }
}
