package com.thurainx.wechat_app.activities

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.app.ActivityCompat.finishAffinity
import androidx.core.content.ContextCompat.startActivity
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar
import com.thurainx.wechat_app.R
import com.thurainx.wechat_app.mvp.presenters.LoginPresenter
import com.thurainx.wechat_app.mvp.presenters.LoginPresenterImpl
import com.thurainx.wechat_app.mvp.views.LoginView
import com.thurainx.wechat_app.utils.afterTextChanged
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_register.*

class LoginActivity : AppCompatActivity(), LoginView {
    lateinit var mPresenter: LoginPresenter

    companion object {
        fun getIntent(context: Context): Intent {
            val intent = Intent(context, LoginActivity::class.java)
            return intent
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        setUpPresenter()

        setUpListeners()
        setUpPhoneAndPassword()

        mPresenter.onUiReady(this, this)
    }

    private fun setUpPresenter() {
        mPresenter = ViewModelProvider(this)[LoginPresenterImpl::class.java]
        mPresenter.initView(this)
    }

    private fun setUpListeners() {
        btnLogin.setOnClickListener {
            if (checkButtonAvailable()) {
                mPresenter.onTapLogin(
                    edtLoginPhone.text.toString(),
                    edtLoginPassword.text.toString()
                )
            }
        }
    }

    private fun setUpPhoneAndPassword() {
        edtLoginPhone.afterTextChanged { text ->
            if (text.isEmpty()) {
                edtLoginPhone.error = "Phone cannot be empty."
            }
            if (text.length < 6) {
                edtLoginPhone.error = "Phone need at least 6 characters."
            }
            checkButtonAvailable()
        }

        edtLoginPassword.afterTextChanged { text ->
            if (text.isEmpty()) {
                edtLoginPassword.error = "Password cannot be empty."
            }
            if (text.length < 6) {
                edtLoginPassword.error = "Password need at least 6 characters."
            }
            checkButtonAvailable()
        }
    }

    private fun validate(): Boolean {
        return edtLoginPhone.text?.isNotEmpty() == true &&
                edtLoginPhone.text!!.length > 5 &&
                edtLoginPassword.text?.isNotEmpty() == true &&
                edtLoginPassword.text!!.length > 5

    }

    private fun checkButtonAvailable(): Boolean {
        if (validate()) {
            btnLogin.alpha = 1F
            return true
        } else {
            btnLogin.alpha = 0.5f
            return false
        }
    }

    override fun navigateToNavigationScreen() {
        val intent = NavigationActivity.getIntent(this)
        startActivity(intent)
        finishAffinity()
    }

    override fun showErrorMessage(message: String) {
        Snackbar.make(window.decorView, message, Snackbar.LENGTH_SHORT).show()
    }


}