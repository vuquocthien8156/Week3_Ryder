package com.example.qthien.week3_ryder

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import bolts.Task.call
import com.facebook.AccessToken
import com.facebook.GraphRequest
import com.facebook.GraphResponse
import com.facebook.HttpMethod

class PersonalActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_personal)
        call()
    }

    fun call(){
        GraphRequest(
            AccessToken.getCurrentAccessToken(),
            "/me/feed",
            null,
            HttpMethod.GET, object :
            GraphRequest.Callback {
                override fun onCompleted(response: GraphResponse?) {
                    Toast.makeText(applicationContext , response.toString() , Toast.LENGTH_SHORT).show()
                }

            }
        ).executeAsync()
    }
}
