package com.thurainx.wechat_app.activities

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.thurainx.wechat_app.R
import kotlinx.android.synthetic.main.activity_greeting.*

class GreetingActivity : AppCompatActivity() {
    companion object{
        fun getIntent(context: Context): Intent {
            val intent = Intent(context, GreetingActivity::class.java)
            return intent
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_greeting)

        setUpListeners()
    }

    private fun setUpListeners() {
        btnGreetingLogin.setOnClickListener {
            val intent = LoginActivity.getIntent(this)
            startActivity(intent)
        }

        btnGreetingRegister.setOnClickListener {
            val intent = RegisterActivity.getIntent(this)
            startActivity(intent)
        }
    }
}