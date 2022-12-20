package com.thurainx.wechat_app.activities

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.thurainx.wechat_app.R
import com.thurainx.wechat_app.fragments.*
import kotlinx.android.synthetic.main.activity_navigation.*

class NavigationActivity : AppCompatActivity() {
    companion object{
        fun getIntent(context: Context): Intent {
            val intent = Intent(context, NavigationActivity::class.java)
            return intent
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_navigation)

        setupBottomNavigationView()
        setUpListeners()
    }

    private fun setUpListeners() {

    }

    private fun setupBottomNavigationView() {
        supportFragmentManager.beginTransaction()
            .add(R.id.flContainer, MomentFragment())
            .commit()

        bottomNavigationView.setOnItemSelectedListener { menuItem ->

            when (menuItem.itemId) {
                R.id.navMoment -> {
                    switchFragment(MomentFragment())
                }
                R.id.navChat -> {
                    switchFragment(ChatFragment())
                }
                R.id.navContact -> {
                    switchFragment(ContactFragment())
                }
                R.id.navProfile -> {
                    switchFragment(ProfileFragment())
                }
                R.id.navSetting -> {
                    switchFragment(SettingFragment())
                }
            }

            return@setOnItemSelectedListener true

        }
    }

    private fun switchFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.flContainer, fragment)
            .commit()
    }
}