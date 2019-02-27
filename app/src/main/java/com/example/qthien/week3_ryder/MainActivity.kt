package com.example.qthien.week3_ryder

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.net.ConnectivityManager
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView

import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import com.facebook.appevents.AppEventsLogger
import com.facebook.login.LoginResult
import kotlinx.android.synthetic.main.activity_main.*
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.example.qthien.week3_ryder.Modal.*
import com.facebook.*
import com.facebook.login.LoginManager
import org.json.JSONObject


class MainActivity : AppCompatActivity() {

    lateinit var dpHelper : DPHelper

    lateinit var result : GraphObject

    lateinit var callbackManager : CallbackManager

    lateinit var arrData : ArrayList<Datum>
    lateinit var adapter : Adapter
//    var tokenTracker :AccessTokenTracker? = null

    lateinit var scrollListener : EndlessRecyclerViewScrollListener

    var lastGraphResponse : GraphResponse? = null
    var firstGraphResponse : GraphResponse? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setTitle("Dòng thời gian của bạn")

//        tokenTracker = object : AccessTokenTracker() {
//            override fun onCurrentAccessTokenChanged(oldAccessToken: AccessToken?, currentAccessToken: AccessToken?) {
//                if (currentAccessToken == null) {
//                    floatingActionButton.hide()
//                    supportActionBar?.hide()
//                }
//                else
//                {
//                    floatingActionButton.show()
//                    supportActionBar?.show()
//                }
//            }
//        }
//        tokenTracker?.startTracking()
        val token = AccessToken.getCurrentAccessToken()

        dpHelper = DPHelper(this)
        dpHelper.initDB()
        arrData = ArrayList()


        if (token == null) { // Đã đăng xuất
            login_button.visibility = View.VISIBLE
            floatingActionButton.hide()
            supportActionBar?.hide()
            img_fb.visibility = View.VISIBLE
            login_button.isEnabled = true
            swipeRefresh.isEnabled = false
        }
        else{ // Đã đăng nhập
            if(!isNetworkAvailable()){
                arrData.addAll(dpHelper.getAllData())
                swipeRefresh.isEnabled = false
            }
            else {
                dpHelper.clearData()
                getGraph().executeAsync()
            }

            login_button.visibility = View.GONE
            supportActionBar?.show()
            floatingActionButton.show()
            img_fb.visibility = View.GONE
            login_button.isEnabled = false
        }

        adapter = Adapter(this, arrData)
        val layout = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        recyclerView.layoutManager = layout
        recyclerView.adapter = adapter

        callbackManager = CallbackManager.Factory.create();

        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this)

        login_button.setReadPermissions("email")
        login_button.setReadPermissions("user_posts")

        // Callback registration
        login_button.registerCallback(callbackManager, object : FacebookCallback<LoginResult> {
            override fun onSuccess(loginResult: LoginResult) {
                getGraph().executeAsync()
                swipeRefresh.isEnabled = true
            }

            override fun onCancel() {
                Toast.makeText(applicationContext , "onCancel" , Toast.LENGTH_SHORT).show()
            }

            override fun onError(exception: FacebookException) {
                Toast.makeText(applicationContext , "onError : $exception" , Toast.LENGTH_SHORT).show()
                Log.d("Error" , exception.toString())
            }
        })


        scrollListener = object : EndlessRecyclerViewScrollListener(layout){
            override fun onLoadMore(page: Int, totalItemsCount: Int, view: RecyclerView) {
                Log.d("Pagee" , page.toString())
                progress_waiting.visibility = View.VISIBLE
                loadNextData()
            }

        }

        recyclerView.addOnScrollListener(scrollListener as EndlessRecyclerViewScrollListener)

        swipeRefresh.setOnRefreshListener {
            loadDataRefresh()
        }

        floatingActionButton.setOnClickListener({
            showDialog()
        })
    }

    private fun getGraph(): GraphRequest {
        return GraphRequest(
            AccessToken.getCurrentAccessToken(),
            "/me/feed?fields=created_time,likes.summary(true),attachments{},object_id,id,message,from{name,picture.height(300).width(300),id}&date_format=U",
            null,
            HttpMethod.GET, object :
                GraphRequest.Callback {
                override fun onCompleted(response: GraphResponse) {
                    swipeRefresh.isRefreshing = true
                    if(response.jsonObject != null){
                        login_button.visibility = View.GONE
                        login_button.isEnabled = false
                        img_fb.visibility = View.GONE
                        supportActionBar?.show()
                        floatingActionButton.show()
                        pareData(response.jsonObject)
                        lastGraphResponse = response
                        firstGraphResponse = response
                        if(arrData.size > 0)
                            dpHelper.insertData(arrData)
                    }
                    else
                        Toast.makeText(applicationContext , "Null" , Toast.LENGTH_SHORT).show()

                }

            }
        )
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main , menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if(item?.itemId == R.id.menuLogout){
           loadDialogLogout()
        }

        return super.onOptionsItemSelected(item)
    }

    fun loadDialogLogout(){
        val alertDialog = AlertDialog.Builder(this)
        alertDialog.setMessage("Bạn có chắc chắn?")
        alertDialog.setNegativeButton("Hủy" , object : DialogInterface.OnClickListener{
            override fun onClick(dialog: DialogInterface?, which: Int) {

            }
        })
        alertDialog.setPositiveButton("Đăng xuất" , object : DialogInterface.OnClickListener{
            override fun onClick(dialog: DialogInterface?, which: Int) {
                LoginManager.getInstance().logOut()
                login_button.visibility = View.VISIBLE
                floatingActionButton.hide()
                supportActionBar?.hide()
                arrData.clear()
                adapter.notifyDataSetChanged()
                img_fb.visibility = View.VISIBLE
                login_button.isEnabled = true
                swipeRefresh.isEnabled = false
                dpHelper.clearData()
            }
        })
        alertDialog.show()
    }

    fun showDialog(){
        val tran = supportFragmentManager
        val dialog = DialogAdd()

        val bundle = Bundle()
        bundle.putParcelable("from" , result.data.get(0).from)
        dialog.arguments = bundle

        dialog.show(tran , "dialog")
    }

    fun loadDataRefresh() {
        swipeRefresh.isRefreshing = true
        if(isNetworkAvailable()) {
            arrData.clear()
        }
        val graphRequest = firstGraphResponse?.request
        if(graphRequest != null){
            graphRequest.callback = GraphRequest.Callback { response ->
                if(response.jsonObject != null){
                    pareData(response.jsonObject)
                    scrollListener.resetState()
                    Log.d("abc" , "Refresh")
                    swipeRefresh.isRefreshing = false
                }
            }
        }
        else
            swipeRefresh.isRefreshing = false

        graphRequest?.executeAsync()
    }

    fun pareData(jsonObject: JSONObject) {

        Log.d("responsee" ,jsonObject.toString())
        val gsonBuilder = GsonBuilder()

        gsonBuilder.setDateFormat("dd/MM/yyyy hh:mm a")
        val gson = gsonBuilder.create()
        if(gson?.fromJson(jsonObject.toString(), GraphObject::class.java) != null){
            result = gson?.fromJson(jsonObject.toString(), GraphObject::class.java)
            if(result.data.size > 0){
                arrData.addAll(result!!.data)
                adapter.notifyItemRangeInserted(arrData.size , result!!.data.size - 1)
                adapter.notifyDataSetChanged()
                swipeRefresh.isRefreshing = false
                progress_waiting.visibility = View.GONE
            }
        }
    }

    fun loadNextData(){
        val nextResultsRequests = lastGraphResponse?.getRequestForPagedResults(GraphResponse.PagingDirection.NEXT)
        if (nextResultsRequests != null) {
            nextResultsRequests.setCallback(GraphRequest.Callback { response ->

                pareData(response.jsonObject)

                //save the last GraphResponse you received
                lastGraphResponse = response
            })
            nextResultsRequests.executeAsync()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        callbackManager.onActivityResult(requestCode, resultCode, data)
    }

    private fun isNetworkAvailable(): Boolean {
        val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetworkInfo = connectivityManager.activeNetworkInfo
        return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting
    }
}
