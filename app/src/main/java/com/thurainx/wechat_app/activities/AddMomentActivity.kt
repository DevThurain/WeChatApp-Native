package com.thurainx.wechat_app.activities

import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.snackbar.Snackbar
import com.thurainx.wechat_app.R
import com.thurainx.wechat_app.adapters.FileAdapter
import com.thurainx.wechat_app.data.vos.FileVO
import com.thurainx.wechat_app.mvp.presenters.AddMomentPresenter
import com.thurainx.wechat_app.mvp.presenters.AddMomentPresenterImpl
import com.thurainx.wechat_app.mvp.views.AddMomentView
import com.thurainx.wechat_app.utils.getRealPathFromURI
import com.thurainx.wechat_app.utils.loadBitmapFromUri
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_add_moment.*
import java.io.File

class AddMomentActivity : BaseActivity(), AddMomentView {
    lateinit var mPresenter: AddMomentPresenter
    lateinit var mFileAdapter: FileAdapter
    var selectedFileList: ArrayList<FileVO> = arrayListOf()
    lateinit var loadingDialog: Dialog

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
        setUpListeners()
        setUpLoadingDialog()
        setUpFileRecyclerView()

        mPresenter.onUiReady(this, this)
    }

    private fun setUpPresenter() {
        mPresenter = ViewModelProvider(this)[AddMomentPresenterImpl::class.java]
        mPresenter.initView(this)
    }

    private fun setUpLoadingDialog(){
        loadingDialog = Dialog(this)
        loadingDialog.setContentView(R.layout.dialog_upload_moment)
        loadingDialog.setCancelable(false)
    }

    private fun setUpListeners() {
        ivPickFile.setOnClickListener {
            mPresenter.onTapPickFile()
        }
        btnCreateMoment.setOnClickListener {
            mPresenter.uploadMoment(edtMoment.text.toString(),selectedFileList)
        }
        ivAddMomentBack.setOnClickListener {
            mPresenter.onTapBack()
        }
    }

    private fun setUpFileRecyclerView(){
        mFileAdapter = FileAdapter()
        rvFile.adapter = mFileAdapter
    }

    override fun navigateBack() {
        super.onBackPressed()
    }

    override fun pickFiles() {
        selectImageFromGallery(INTENT_TYPE_FILE)
    }

    override fun onBindUserData(name: String, profileImage: String) {
        Glide.with(this)
            .load(profileImage)
            .into(ivAddMomentProfile)

        tvAddMomentName.text = name
    }

    override fun showLoadingDialog() {
        loadingDialog.show()
    }

    override fun dismissLoadingDialog() {
        setResult(Activity.RESULT_OK)
        loadingDialog.dismiss()
        mPresenter.onTapBack()
    }

    override fun showErrorMessage(message: String) {
        Snackbar.make(window.decorView, message, Snackbar.LENGTH_SHORT).show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == BaseActivity.INTENT_REQUEST_CODE_SELECT_IMAGE_FROM_GALLERY) {
            val clipPath = data?.clipData


            if (clipPath != null) {

                val uriList: ArrayList<Uri> = arrayListOf()
                Log.d("image_count", clipPath.itemCount.toString())
                for (i in 0 until clipPath.itemCount) {
                    if (clipPath.getItemAt(i).uri.toString().contains("video")) {
                        Log.d("image_invalid", "invalid file")
                        continue
                    } else if (clipPath.getItemAt(i).uri.toString().contains("image")) {
                        uriList.add(clipPath.getItemAt(i).uri)
                    }
                }
                Observable.just(uriList)
                    .map {
                        uriList.map { uri ->
                            Pair<Uri, Bitmap>(uri, uri.loadBitmapFromUri(this))
                        }
                    }
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe {
                        Log.d("image_count_bitmap", it.size.toString())

                        selectedFileList.clear()
                        it.forEach { pair ->
                            selectedFileList.add(
                                FileVO(
                                    uri = pair.first,
                                    bitmap = pair.second,
                                    isMovie = pair.first.toString().contains("video")
                                )
                            )
                        }

//                        mBitMapList = it
//                        mPhotoAdapter.setNewData(it)

                        mFileAdapter.setNewData(selectedFileList)
                    }
            } else {
                val singleImage = data?.data

                if (singleImage != null) {
                    Observable.just(singleImage)
                        .map { Pair<Uri, Bitmap>(it,it.loadBitmapFromUri(this)) }
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe {
                            selectedFileList.clear()
                            if(it.first.toString().contains("video")){
                                val realPath = getRealPathFromURI(this,it.first).toString()
                                selectedFileList.add(
                                    FileVO(uri = it.first, bitmap = it.second, isMovie = it.first.toString().contains("video"), realPath = realPath)
                                )
                            }else{
                                selectedFileList.add(
                                    FileVO(uri = it.first, bitmap = it.second, isMovie = it.first.toString().contains("video"))
                                )
                            }

//                            mPhotoAdapter.setNewData(listOf(it))
                            mFileAdapter.setNewData(selectedFileList)

                        }
                }
            }
        }
    }

}