package com.thurainx.wechat_app.activities

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar
import com.thurainx.wechat_app.R
import com.thurainx.wechat_app.mvp.presenters.OtpPresenter
import com.thurainx.wechat_app.mvp.presenters.OtpPresenterImpl
import com.thurainx.wechat_app.mvp.views.OtpView
import com.thurainx.wechat_app.utils.SharedPreferenceUtils
import com.thurainx.wechat_app.utils.afterTextChanged
import kotlinx.android.synthetic.main.activity_otp.*
import kotlinx.android.synthetic.main.activity_register.*

const val EXTRA_NAME = "EXTRA_NAME"
const val EXTRA_PASSWORD = "EXTRA_PASSWORD"
const val EXTRA_DOB = "EXTRA_DOB"
const val EXTRA_GENDER = "EXTRA_GENDER"
class OtpActivity : AppCompatActivity(), OtpView {
    lateinit var mPresenter: OtpPresenter

    companion object{
        fun getIntent(context: Context,name: String,password: String, dob: String, gender: String): Intent {
            val intent = Intent(context, OtpActivity::class.java)
            intent.putExtra(EXTRA_NAME,name)
            intent.putExtra(EXTRA_PASSWORD, password)
            intent.putExtra(EXTRA_DOB, dob)
            intent.putExtra(EXTRA_GENDER, gender)
            return intent
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_otp)
        setUpPresenter()

        setUpListeners()
        setUpPhoneAndOtp()

        mPresenter.onUiReady(this, this)
    }

    private fun setUpPresenter(){
        mPresenter = ViewModelProvider(this)[OtpPresenterImpl::class.java]
        mPresenter.initView(this)
    }

    private fun setUpListeners() {
        btnVerify.setOnClickListener {
            if(checkButtonAvailable()){
                mPresenter.onTapVerify(
                    name = intent.getStringExtra(EXTRA_NAME) ?: "",
                    password = intent.getStringExtra(EXTRA_PASSWORD) ?: "",
                    phone = edtOtpPhone.text.toString(),
                    dob = intent.getStringExtra(EXTRA_DOB) ?: "",
                    gender = intent.getStringExtra(EXTRA_GENDER) ?: ""
                )
            }
        }
    }

    private fun setUpPhoneAndOtp(){
        edtOtpPhone.afterTextChanged { text ->
            if (text.isEmpty()) {
                edtOtpPhone.error = "Phone cannot be empty."
            }
            if (text.length < 6) {
                edtOtpPhone.error = "Phone need at least 6 characters."
            }
            checkButtonAvailable()

        }
        pinOtp.afterTextChanged { text ->
            checkButtonAvailable()
        }
    }

    private fun validate(): Boolean {
        return pinOtp.text?.length == 4 &&
                edtOtpPhone.text?.isNotEmpty() == true &&
                edtOtpPhone.text!!.length > 5
    }

    private fun checkButtonAvailable(): Boolean {
        if (validate()) {
            btnVerify.alpha = 1F
            return true
        } else {
            btnVerify.alpha = 0.5f
            return false
        }
    }

    override fun navigateToSetUpProfileScreen() {
        val intent = SetUpProfileActivity.getIntent(this, edtOtpPhone.text.toString())
        startActivity(intent)
    }

    override fun showErrorMessage(message: String) {
        Snackbar.make(window.decorView,message,Snackbar.LENGTH_SHORT).show()
    }
}