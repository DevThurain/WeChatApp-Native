package com.thurainx.wechat_app.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar
import com.thurainx.wechat_app.R
import com.thurainx.wechat_app.activities.GreetingActivity
import com.thurainx.wechat_app.mvp.presenters.SettingPresenter
import com.thurainx.wechat_app.mvp.presenters.SettingPresenterImpl
import com.thurainx.wechat_app.mvp.views.SettingView
import kotlinx.android.synthetic.main.fragment_setting.*

class SettingFragment : Fragment(),SettingView {
    lateinit var mPresenter: SettingPresenter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_setting, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpPresenter()
        setUpListeners()

        mPresenter.onUiReady(requireContext(),requireActivity())

    }

    private fun setUpPresenter(){
        mPresenter = ViewModelProvider(this)[SettingPresenterImpl::class.java]
        mPresenter.initView(this)
    }

    private fun setUpListeners(){
        btnLogout.setOnClickListener {
            mPresenter.onTapLogout()
        }
    }

    override fun navigateToGreetingScreen() {
        val intent = GreetingActivity.getIntent(requireContext())
        startActivity(intent)
        requireActivity().finishAffinity()
    }

    override fun showErrorMessage(message: String) {
        Snackbar.make(requireView(), message,Snackbar.LENGTH_SHORT).show()
    }

}