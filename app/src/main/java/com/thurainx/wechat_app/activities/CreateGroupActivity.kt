package com.thurainx.wechat_app.activities

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.core.widget.ImageViewCompat
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar
import com.thurainx.wechat_app.R
import com.thurainx.wechat_app.adapters.ContactGroupAdapter
import com.thurainx.wechat_app.data.vos.ContactVO
import com.thurainx.wechat_app.mvp.presenters.CreateGroupPresenter
import com.thurainx.wechat_app.mvp.presenters.CreateGroupPresenterImpl
import com.thurainx.wechat_app.mvp.views.CreateGroupView
import com.thurainx.wechat_app.utils.VIEW_TYPE_SELECT
import com.thurainx.wechat_app.utils.loadBitmapFromUri
import com.thurainx.wechat_app.utils.scaleToRatio
import com.thurainx.wechat_app.utils.toContactGroupList
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_create_group.*
import kotlinx.android.synthetic.main.activity_set_up_profile.*
import org.checkerframework.checker.units.qual.s

class CreateGroupActivity : BaseActivity(), CreateGroupView {
    lateinit var mPresenter: CreateGroupPresenter
    lateinit var mContactGroupAdapter: ContactGroupAdapter
    private var mChosenImageBitmap: Bitmap? = null

    companion object {
        fun getIntent(context: Context): Intent {
            val intent = Intent(context, CreateGroupActivity::class.java)
            return intent
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_group)

        setUpPresenter()
        setUpRecyclerView()
        setUpListeners()
        mPresenter.onUiReady(this,this)
    }

    private fun setUpPresenter() {
        mPresenter = ViewModelProvider(this)[CreateGroupPresenterImpl::class.java]
        mPresenter.initView(this)
    }

    private fun setUpRecyclerView() {
        mContactGroupAdapter = ContactGroupAdapter(VIEW_TYPE_SELECT, mPresenter, mPresenter)
        rvSelectContactGroup.adapter = mContactGroupAdapter
    }

    private fun setUpListeners(){
        ivGroupPhoto.setOnClickListener {
            mPresenter.pickGroupImage()
        }

        btnCreateGroup.setOnClickListener {
            if(edtGroupName.text.toString().isNotEmpty() && mChosenImageBitmap != null){
                mPresenter.createGroup(
                    name = edtGroupName.text.toString(),
                    bitmap = mChosenImageBitmap!!,
                )
            }
        }

        ivCreateGroupBack.setOnClickListener {
            mPresenter.onTapBack()
        }
    }

    override fun bindContacts(contactList: List<ContactVO>) {
        mContactGroupAdapter.setNewData(contactList.toContactGroupList())
    }

    override fun pickGroupImage() {
        selectImageFromGallery(type = INTENT_TYPE_IMAGE)
    }

    override fun createGroupSuccess() {
        setResult(RESULT_OK)
        finish()
    }

    override fun navigateBack() {
        super.onBackPressed()
    }

    override fun showErrorMessage(message: String) {
        Snackbar.make(window.decorView, message, Snackbar.LENGTH_SHORT).show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == BaseActivity.INTENT_REQUEST_CODE_SELECT_IMAGE_FROM_GALLERY) {
            val imageUri = data?.data
            imageUri?.let { image ->

                Observable.just(image)
                    .map { it.loadBitmapFromUri(applicationContext) }
                    .map { it.scaleToRatio(0.35) }
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe {
                        mChosenImageBitmap = it
                        ImageViewCompat.setImageTintList(ivGroupPhoto, null)
                        ivGroupPhoto.setImageBitmap(it)
                    }

            }
        }
    }

}