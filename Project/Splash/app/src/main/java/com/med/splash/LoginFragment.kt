package com.med.splash

import android.content.Context
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.text.InputType
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.*
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.iid.FirebaseInstanceId
import org.json.JSONArray


class LoginFragment:android.support.v4.app.Fragment() {

    val LOGIN_URL = "http://192.168.31.58:3000/api/login"
    val PASSWORD_URL = "http://192.168.31.58/mobile/passwordforget/passwordforget.php"

    lateinit var animZoomIn: Animation
    lateinit var animZoomOut: Animation

    lateinit var DataBase: DataBase
    lateinit var deviceToken: String

    lateinit var loadingIcon: ImageView
    lateinit var loginBtn: Button
    lateinit var remember: CheckBox
    lateinit var message: TextView

    lateinit var userName: EditText
    lateinit var userPassword: EditText

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_login, container, false)

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
        val circleBig=view.findViewById<ImageView>(R.id.circleBig)
        val signupLine=view.findViewById<ImageView>(R.id.signupLine)
        loginBtn=view.findViewById<Button>(R.id.loginBtn)
        val signupBtn=view.findViewById<Button>(R.id.signupBtn)
        val helpBtn=view.findViewById<Button>(R.id.helpBtn)
        userName=view.findViewById<EditText>(R.id.userName)
        userPassword=view.findViewById<EditText>(R.id.userPassword)
        val passwordShow=view.findViewById<ImageView>(R.id.passwordShow)
        remember=view.findViewById<CheckBox>(R.id.remember)
        loadingIcon=view.findViewById<ImageView>(R.id.loadingIcon)
        message=view.findViewById<TextView>(R.id.message)

        remember.requestFocus()

        DataBase = DataBase(context!!)
        DataBase.Database_Open()


        (activity as SplashActivity).buttonEffect(signupBtn,signupLine,"line")
        (activity as SplashActivity).buttonEffect(helpBtn,helpBtn, "normal")
        (activity as SplashActivity).buttonEffect(loginBtn,loginBtn, "color")

        val animBlink = AnimationUtils.loadAnimation((activity as SplashActivity), R.anim.blink)

        animZoomIn = AnimationUtils.loadAnimation((activity as SplashActivity), R.anim.zoom_in)

        animZoomOut = AnimationUtils.loadAnimation((activity as SplashActivity), R.anim.zoom_out)

        (activity as SplashActivity).textEffect(userName)
        (activity as SplashActivity).textEffect(userPassword)


        circleBig.startAnimation(animBlink)


        passwordShow.setOnClickListener {

            if (userPassword.inputType == 129) {
                userPassword.inputType = InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
            } else if (userPassword.inputType == InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD) {
                userPassword.inputType = 129
            }

        }

        signupBtn.setOnClickListener {

            (activity as SplashActivity).pageChange("signup")
        }

        loginBtn.setOnClickListener {

            if (userName.text.toString().trim().equals("") || userPassword.text.toString().trim().equals("")) {


                (activity as SplashActivity).messageShow(message,"Bilgileri Kontrol Edin")


            } else if (userPassword!!.text.toString().length < 8) {

                (activity as SplashActivity).messageShow(message,"Parola en az 8 karakter olabilir")

            } else {
                loginRequest(LOGIN_URL)
            }
        }


        backgroundImage.setOnClickListener {

            userName.startAnimation(animZoomOut)
            userPassword.startAnimation(animZoomOut)
            remember.requestFocus()

            val imm =  (activity as SplashActivity).getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(backgroundImage.windowToken, 0)

        }
        remember.setOnCheckedChangeListener { buttonView, isChecked ->

            userName.startAnimation(animZoomOut)
            userPassword.startAnimation(animZoomOut)
            remember.requestFocus()

            val imm =  (activity as SplashActivity).getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(buttonView.windowToken, 0)
        }


        userPassword.setOnEditorActionListener { v, actionId, event ->
            var handled = false
            if (actionId == EditorInfo.IME_ACTION_NEXT) {

                userPassword.clearFocus()

                val imm =  (activity as SplashActivity).getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(v.windowToken, 0)

                remember.requestFocus()

                handled = true
            }
            handled
        }

    }

    fun loginRequest(url: String) {



        (activity as SplashActivity).loadingButton(loginBtn, view!!, loadingIcon, true)

        (activity as SplashActivity).messageShow(message,"Giriş Yapılıyor")

        val queue = Volley.newRequestQueue(context)

        val postRequest = object : StringRequest(Request.Method.POST, url, Response.Listener<String>
        {

            response ->

            val jsonArray = JSONArray(response)

            val showdata = jsonArray.getJSONObject(0)

            val status = showdata.getBoolean("status")

            Log.i("login_status", response.toString())

            if (status) {

                (activity as SplashActivity).messageShow(message,showdata.getString("message"))
                val userName = showdata.getString("userName")
                val userMail = showdata.getString("userMail")
                val userPassword = showdata.getString("userPassword")
                val deviceToken = showdata.getString("deviceToken")
                val lastLogin = showdata.getString("lastLogin")


                DataBase.Update_User(userPassword, deviceToken, userName, userMail, lastLogin, remember.isChecked.toString())

                (activity as SplashActivity).pageSwitch()

            } else  {

                (activity as SplashActivity).messageShow(message,showdata.getString("message"))

                (activity as SplashActivity).loadingButton(loginBtn, view!!, loadingIcon, false)
                loginBtn.text="Tekrar Deneyin"

            }

        },

                Response.ErrorListener {


                    error ->

                    var errorMessage="Tekrar Deneyin"

                    if (error.toString().indexOf(':') != -1) {
                        if (error.toString().substring(0, error.toString().indexOf(':')).trim().equals("com.android.volley.NoConnectionError") || error.toString().substring(0, error.toString().indexOf(':')).trim().equals("com.android.volley.error.NoConnectionError")) {
                            errorMessage = "İnternet Bağlantınızı Kontrol Edin"

                        }
                    } else if (error.toString().trim().equals("com.android.volley.TimeoutError")) {

                        errorMessage = "İnternet Bağlantınız Yavaş"

                    } else if (error.toString().trim().equals("com.android.volley.error.TimeoutError")) {

                        errorMessage = "İnternet Bağlantınızda Sorun Var"

                    }

                    (activity as SplashActivity).messageShow(message,errorMessage)

                    (activity as SplashActivity).loadingButton(loginBtn, view!!, loadingIcon, false)
                    loginBtn.text="Tekrar Deneyin"

                }
        ) {
            override fun getParams(): Map<String, String> {

                val params = HashMap<String, String>()

                params.put("userMail", userName.text.toString().trim())
                params.put("userPassword", userPassword.text.toString().trim())
                params.put("deviceToken", deviceToken)
                params.put("appCheck", resources.getString(R.string.package_name))

                return params
            }

        }

        queue.add(postRequest)
    }
}