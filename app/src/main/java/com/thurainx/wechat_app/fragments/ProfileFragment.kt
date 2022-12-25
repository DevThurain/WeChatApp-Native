package com.thurainx.wechat_app.fragments

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.google.zxing.BarcodeFormat
import com.google.zxing.MultiFormatWriter
import com.google.zxing.WriterException
import com.journeyapps.barcodescanner.BarcodeEncoder
import com.thurainx.wechat_app.R
import com.thurainx.wechat_app.mvp.presenters.ProfilePresenter
import com.thurainx.wechat_app.mvp.presenters.ProfilePresenterImpl
import com.thurainx.wechat_app.mvp.views.ProfileView
import kotlinx.android.synthetic.main.dialog_qr.*
import kotlinx.android.synthetic.main.fragment_profile.*

class ProfileFragment : Fragment(), ProfileView{

    lateinit var mPresenter: ProfilePresenter
    lateinit var qrDialog: Dialog
    lateinit var editDialog: DialogFragment


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
        setUpListeners()

        mPresenter.onUiReady(requireContext(),requireActivity())
    }

    private fun setUpPresenter(){
        mPresenter = ViewModelProvider(this)[ProfilePresenterImpl::class.java]
        mPresenter.initView(this)
    }

    private fun setUpListeners(){
        ivProfileQr.setOnClickListener {
            mPresenter.onTapQr()
        }

        btnProfileEdit.setOnClickListener {
            mPresenter.onTapEditProfile()
        }

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


        EditDialogFragment().show(
            childFragmentManager, EditDialogFragment.TAG)


    }

    override fun showQrDialog(qrCode: String) {
        qrDialog = Dialog(requireContext())
        qrDialog.setContentView(R.layout.dialog_qr)
        qrDialog.setCancelable(true)
        val multiFormatWriter = MultiFormatWriter()
        try {
            val bitMatrix = multiFormatWriter.encode(qrCode, BarcodeFormat.QR_CODE, 250, 250)
            val barcodeEncoder = BarcodeEncoder()
            val bitmap = barcodeEncoder.createBitmap(bitMatrix)
            qrDialog.ivDialogQr.setImageBitmap(bitmap)
            qrDialog.btnQrClose.setOnClickListener {
                qrDialog.dismiss()
            }
        } catch (e: WriterException) {
            e.printStackTrace()
        }

        qrDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        qrDialog.show()
    }

    override fun showErrorMessage(message: String) {
    }

}