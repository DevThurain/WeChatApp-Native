package com.thurainx.wechat_app.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.thurainx.wechat_app.R
import com.thurainx.wechat_app.mvp.presenters.ProfilePresenter
import com.thurainx.wechat_app.mvp.presenters.ProfilePresenterImpl
import com.thurainx.wechat_app.mvp.views.ProfileView
import kotlinx.android.synthetic.main.fragment_profile.*

class ProfileFragment : Fragment(), ProfileView{

    lateinit var mPresenter: ProfilePresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpPresenter()

        mPresenter.onUiReady(requireContext(),requireActivity())
    }

    private fun setUpPresenter(){
        mPresenter = ViewModelProvider(this)[ProfilePresenterImpl::class.java]
        mPresenter.initView(this)
    }

    override fun bindProfileData(name: String, phone: String, dob: String, gender: String, profile: String) {
        tvProfileName.text = name
        tvProfilePhone.text = phone
        tvProfileDob.text = dob
        tvProfileGender.text = gender

        Glide.with(this)
            .load(profile)
            .into(ivProfileImage)
    }

    override fun showEditDialog(name: String, phone: String, dob: String, gender: String) {

    }

    override fun showQrDialog(qrCode: String) {

    }

    override fun showErrorMessage(message: String) {
    }

}