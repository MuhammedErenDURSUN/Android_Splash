package com.med.splash

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.iid.FirebaseInstanceId
import org.json.JSONArray

class WelcomeFragment:android.support.v4.app.Fragment() {

    val AUTOLOGIN_URL = "http://192.168.31.58:3000/api/autologin"
    val SERVER_STATUS_URL = "http://192.168.31.58:3000/api/server"

    lateinit var DataBase: DataBase
    lateinit var deviceToken: String
    lateinit var message: TextView

    lateinit var welcomeBtn: Button
    lateinit var loadingIcon: ImageView



    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_welcome, container, false)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        FirebaseInstanceId.getInstance().instanceId
                .addOnCompleteListener(OnCompleteListener { task ->
                    if (!task.isSuccessful) {
                        return@OnCompleteListener
                    }

                    deviceToken = task.result!!.token

                })

        val backgroundImage=view.findViewById<ImageView>(R.id.backgroundImage)
        loadingIcon=view.findViewById<ImageView>(R.id.loadingIcon)
        val circleBig=view.findViewById<ImageView>(R.id.circleBig)
        welcomeBtn=view.findViewById<Button>(R.id.welcomeBtn)
        message=view.findViewById<Button>(R.id.message)
        val messageLine=view.findViewById<ImageView>(R.id.messageLine)

        DataBase = DataBase(context!!)
        DataBase.Database_Open()

       (activity as SplashActivity).buttonEffect(welcomeBtn,welcomeBtn,"color")

        val animBlink = AnimationUtils.loadAnimation((activity as SplashActivity), R.anim.blink)
        val animLine = AnimationUtils.loadAnimation((activity as SplashActivity), R.anim.line)

        messageLine.startAnimation(animLine)

        circleBig.startAnimation(animBlink)

        serviceCheck()

    }
    fun serviceCheck() {

        message.textSize=24f
        message.text = "Lütfen Bekleyin"

        (activity as SplashActivity).loadingButton(welcomeBtn,view!!,loadingIcon,true)

        val queue = Volley.newRequestQueue(context)

        val postRequest = object : StringRequest(Request.Method.POST, SERVER_STATUS_URL, Response.Listener<String>
        {

            response ->

            val jsonArray = JSONArray(response)

            val showdata = jsonArray.getJSONObject(0)

            splashControl(showdata.getString("serviceCheck"))

        },

                Response.ErrorListener {
                    error ->

                    splashControl("error")

                }
        ) {
            override fun getParams(): Map<String, String> {

                val params = HashMap<String, String>()

                params.put("serviceCheck", resources.getString(R.string.package_name))

                return params
            }

        }
        queue.add(postRequest)

    }

    @SuppressLint("SetTextI18n")
    fun splashControl(status: String) = when (status) {
        "true" -> /// internet kontrol

            when {
                DataBase.Data_Read("user", "userMail", "_id=1").equals(" ") -> { // uygulama ilk kuruluşu

                    message.textSize=48f
                    message.text = "Merhaba"

                    (activity as SplashActivity).loadingButton(welcomeBtn,view!!,loadingIcon,false)

                    welcomeBtn.text="Devam Et"

                    welcomeBtn.setOnClickListener {

                        (activity as SplashActivity).pageChange("login")

                    }

                }
                DataBase.Data_Read("user", "loginStatus", "_id=1").equals("false") -> { // uygulamadan çıkış vapılmış

                    message.textSize=24f
                    message.text = "Merhaba " + DataBase.Data_Read("user", "userName", "_id=1")

                    (activity as SplashActivity).loadingButton(welcomeBtn,view!!,loadingIcon,false)

                    welcomeBtn.text="Giriş Yap"

                    welcomeBtn.setOnClickListener {

                        (activity as SplashActivity).pageChange("login")
                    }

                }
                DataBase.Data_Read("user", "loginStatus", "_id=1").equals("true") -> {// auto login işlemi

                    message.textSize=24f
                    message.text = "Merhaba " + DataBase.Data_Read("user", "userName", "_id=1")

                    loginRequest(AUTOLOGIN_URL)

                }
                else -> {

                    message.textSize=24f
                    message.text = "Bir sorun oluştu"

                    (activity as SplashActivity).loadingButton(welcomeBtn,view!!,loadingIcon,false)

                    welcomeBtn.text="Tekrar Deneyin"

                    welcomeBtn.setOnClickListener {

                        loginRequest(AUTOLOGIN_URL)
                    }
                }
            }

        "care" -> {

            message.textSize=24f
            message.text = "Geçici olarak bakımdayız"

            (activity as SplashActivity).loadingButton(welcomeBtn,view!!,loadingIcon,false)

            welcomeBtn.text="Tekrar Deneyin"

            welcomeBtn.setOnClickListener {

                serviceCheck()
            }

        }
        else -> {
            message.textSize=24f
            message.text = "İnternet Bağlantınızı Kontrol Edin"

            (activity as SplashActivity).loadingButton(welcomeBtn,view!!,loadingIcon,false)

            welcomeBtn.text="Tekrar Deneyin"

            welcomeBtn.setOnClickListener {

                serviceCheck()
            }


        }
    }

    fun loginRequest(url: String) {

        (activity as SplashActivity).loadingButton(welcomeBtn,view!!,loadingIcon,true)

        val queue = Volley.newRequestQueue(context)

        val postRequest = object : StringRequest(Request.Method.POST, url, Response.Listener<String>
        {

            response ->

            val jsonArray = JSONArray(response)

            val showdata = jsonArray.getJSONObject(0)

            val status = showdata.getString("status")

            when {
                status.equals("true") -> (activity as SplashActivity).pageSwitch()

                status.equals("false") -> {

                    message.textSize=24f
                    message.text = showdata.getString("message")

                    (activity as SplashActivity).loadingButton(welcomeBtn,view!!,loadingIcon,false)

                    welcomeBtn.text="Tekrar Deneyin"

                    welcomeBtn.setOnClickListener {
                        loginRequest(AUTOLOGIN_URL)
                    }

                }
                status.equals("deviceError") -> {

                    message.textSize=24f
                    message.text = showdata.getString("message")

                    (activity as SplashActivity).loadingButton(welcomeBtn,view!!,loadingIcon,false)

                    welcomeBtn.text="Giriş Yap"

                    welcomeBtn.setOnClickListener {

                        (activity as SplashActivity).pageChange("login")
                    }

                }
            }


        },

                Response.ErrorListener {


                    error ->

                    message.textSize=24f
                    message.text = "İnternet Bağlantınızı Kontrol Edin"

                    (activity as SplashActivity).loadingButton(welcomeBtn,view!!,loadingIcon,false)

                    welcomeBtn.text="Tekrar Deneyin"

                    welcomeBtn.setOnClickListener {
                        loginRequest(AUTOLOGIN_URL)
                    }


                }
        ) {
            override fun getParams(): Map<String, String> {

                val params = HashMap<String, String>()

                params.put("userMail", DataBase.Data_Read("user", "userMail", "_id=1"))
                params.put("userPassword", DataBase.Data_Read("user", "userPassword", "_id=1"))
                params.put("deviceToken", deviceToken)
                params.put("appCheck", resources.getString(R.string.package_name))

                return params
            }
        }


        queue.add(postRequest)


    }
}