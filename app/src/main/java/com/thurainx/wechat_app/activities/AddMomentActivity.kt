package com.thurainx.wechat_app.activities

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import com.thurainx.wechat_app.R
import com.thurainx.wechat_app.mvp.presenters.AddMomentPresenter
import com.thurainx.wechat_app.mvp.presenters.AddMomentPresenterImpl
import com.thurainx.wechat_app.mvp.views.AddMomentView
import kotlinx.android.synthetic.main.activity_add_moment.*

class AddMomentActivity : BaseActivity(), AddMomentView {
    lateinit var mPresenter: AddMomentPresenter
    companion object {
        fun getIntent(context: Context): Intent {
            val intent = Intent(context, AddMomentActivity::class.java)
            return intent
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_moment)
        setUpPresenter()

        mPresenter.onUiReady(this, this)
    }

    fun setUpPresenter(){
        mPresenter = ViewModelProvider(this)[AddMomentPresenterImpl::class.java]
        mPresenter.initView(this)
    }

    override fun pickFiles() {

    }

    override fun onBindUserData(name: String, profileImage: String) {
        Glide.with(this)
            .load(profileImage)
            .into(ivAddMomentProfile)

        tvAddMomentName.text = name
    }

    override fun showErrorMessage(message: String) {
        Snackbar.make(window.decorView, message, Snackbar.LENGTH_SHORT).show()
    }
}