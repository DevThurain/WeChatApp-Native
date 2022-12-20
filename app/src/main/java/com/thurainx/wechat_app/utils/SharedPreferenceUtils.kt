package com.thurainx.wechat_app.utils

import android.content.Context
import android.content.SharedPreferences
import androidx.preference.PreferenceManager
import com.thurainx.wechat_app.R

const val PREF_NAME = "PREF_NAME"
const val PREF_PHONE = "PREF_PHONE"
const val PREF_DOB = "PREF_DOB"
const val PREF_GENDER = "PREF_GENDER"
const val PREF_PROFILE_IMAGE = "PREF_PROFILE_IMAGE"
const val PREF_USER = "PREF_USER"


object SharedPreferenceUtils {
    fun writeValue(context: Context, key: String,value: String){
        val sharedPref = context.getSharedPreferences(PREF_USER,0) ?: return
        sharedPref.edit().putString(key, value).apply()

    }

    fun readValue(context: Context, key: String) : String{
        val sharedPref = context.getSharedPreferences(PREF_USER,0) ?: return ""
        return sharedPref.getString(key,"").toString()
    }
}
