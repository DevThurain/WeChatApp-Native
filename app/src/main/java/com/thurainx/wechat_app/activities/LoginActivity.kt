package com.thurainx.wechat_app.activities

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.thurainx.wechat_app.R

class LoginActivity : AppCompatActivity() {
    fun getIntent(context: Context): Intent {
        val intent = Intent(context, LoginActivity::class.java)
        return intent
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
    }
}