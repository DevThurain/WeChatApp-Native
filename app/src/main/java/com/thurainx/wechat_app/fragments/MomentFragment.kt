package com.thurainx.wechat_app.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.thurainx.wechat_app.R
import com.thurainx.wechat_app.mvp.presenters.MomentPresenter
import com.thurainx.wechat_app.mvp.presenters.MomentPresenterImpl
import com.thurainx.wechat_app.mvp.views.MomentView

class MomentFragment : Fragment(), MomentView {

    lateinit var mPresenter: MomentPresenter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_moment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpPresenter()

        mPresenter.onUiReady(view.context, this)
    }

    private fun setUpPresenter(){
        mPresenter = ViewModelProvider(this)[MomentPresenterImpl::class.java]
        mPresenter.initView(this)
    }

    override fun navigateToAddMomentScreen() {

    }

    override fun showErrorMessage(message: String) {

    }


}