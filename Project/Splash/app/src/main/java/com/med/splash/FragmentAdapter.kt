package com.med.splash

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import android.view.ViewGroup

/**
 * Created by med on 22.10.2018.
 */
class FragmentAdapter (fragmentManager: FragmentManager): FragmentStatePagerAdapter(fragmentManager) {

   var pageName="welcome"

    override fun getCount(): Int {

        when (pageName) {
            "login" -> {
                return 1
            }
            "signup" -> {
                return 1
            }
            "welcome" -> {
                return 1
            }
            else ->
                return 1
        }
    }

    override fun getItem(position: Int): Fragment {

        when (pageName) {
            "login" -> {
                return LoginFragment()
            }
            "signup" -> {
                return SignupFragment()
            }
            "welcome" -> {
                return WelcomeFragment()
            }
            else ->
                return WelcomeFragment()
        }

    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        super.destroyItem(container, position, `object`)
    }

}