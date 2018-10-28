package com.med.splash

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.PorterDuff
import android.os.*
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import android.view.MotionEvent
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.*
import android.view.View.OnFocusChangeListener


class SplashActivity: AppCompatActivity() {

    lateinit var animZoomInFull:Animation
    lateinit var animZoomIn:Animation
    lateinit var animZoomOut:Animation
    lateinit var animDown:Animation
    lateinit var viewPager: ViewPager
    val FragmentAdapter = FragmentAdapter(supportFragmentManager)
    var currentPage:String="welcome"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_splash)
        setTheme(R.style.AppTheme)

        viewPager = findViewById(R.id.viewPager)

        animZoomInFull = AnimationUtils.loadAnimation(applicationContext, R.anim.zoom_in_full);
        animZoomIn = AnimationUtils.loadAnimation(applicationContext, R.anim.zoom_in);

        animZoomOut = AnimationUtils.loadAnimation(applicationContext , R.anim.zoom_out);
        animDown = AnimationUtils.loadAnimation(applicationContext, R.anim.slide_down)
        FragmentAdapter.pageName = currentPage
        viewPager.adapter = FragmentAdapter

    }
    @SuppressLint("ClickableViewAccessibility")
    fun buttonEffect(button: Button, line: View, isLine:String="normal") {
        button.setOnTouchListener { v, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {

                   when (isLine){

                       "line" ->{
                           line.startAnimation(animZoomInFull)
                           v.startAnimation(animZoomIn)
                       }
                       "normal" ->{
                           v.startAnimation(animZoomIn)
                       }
                       "color" ->{
                           v.background.setColorFilter(resources.getColor(R.color.colorPrimaryDark), PorterDuff.Mode.SRC_ATOP)
                           button.setTextColor(resources.getColor(R.color.colorAccent))
                           v.invalidate()
                       }
                    }

                }
                MotionEvent.ACTION_UP -> {

                    when (isLine){

                        "line" ->{
                            line.startAnimation(animZoomOut)
                            v.startAnimation(animZoomOut)
                        }
                        "normal" ->{
                            v.startAnimation(animZoomOut)
                        }
                        "color" ->{
                            v.background.clearColorFilter()
                            button.setTextColor(resources.getColor(R.color.colorPrimaryDark))
                            v.invalidate()
                        }
                    }
                }
            }
            false
        }
    }
    fun textEffect(editText: EditText) {
        editText.onFocusChangeListener = OnFocusChangeListener { view, hasFocus ->
            if (hasFocus) {
                editText.startAnimation(animZoomIn);
            } else {
                editText.startAnimation(animZoomOut);
            }
        }
    }
    fun  pageSwitch()
    {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    fun pageChange(pageName: String) {

        currentPage = pageName
        FragmentAdapter.pageName = pageName
        viewPager.adapter = FragmentAdapter
    }

    fun loadingButton(button:Button,view:View,loadingIcon:ImageView,isLoading:Boolean){

        if (isLoading){

            button.text=""
            val resizeAnimation = ResizeAnimation(button, 107)
            resizeAnimation.duration = 300
            view.startAnimation(resizeAnimation)

            loadingIcon.visibility=View.VISIBLE

            val animLoading = AnimationUtils.loadAnimation(applicationContext, R.anim.loading)
            loadingIcon.startAnimation(animLoading)
        }
        else{

            loadingIcon.clearAnimation()
            loadingIcon.visibility=View.GONE

            val resizeAnimation = ResizeAnimation(button, 847)
            resizeAnimation.duration = 300
            view.startAnimation(resizeAnimation)
        }


    }

    fun messageShow(textView: TextView,message: String){

        textView.visibility=View.VISIBLE
        textView.text= message
        textView.startAnimation(animDown)

    }
    override fun onBackPressed() {

    }
}