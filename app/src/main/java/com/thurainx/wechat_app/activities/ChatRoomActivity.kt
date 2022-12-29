package com.thurainx.wechat_app.activities

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.View.OnFocusChangeListener
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import com.thurainx.wechat_app.R
import com.thurainx.wechat_app.adapters.ContentAdapter
import com.thurainx.wechat_app.adapters.FileAdapter
import com.thurainx.wechat_app.adapters.MessageAdapter
import com.thurainx.wechat_app.data.vos.*
import com.thurainx.wechat_app.mvp.presenters.ChatRoomPresenter
import com.thurainx.wechat_app.mvp.presenters.ChatRoomPresenterImpl
import com.thurainx.wechat_app.mvp.views.ChatRoomView
import com.thurainx.wechat_app.utils.*
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_add_moment.*
import kotlinx.android.synthetic.main.activity_chat_room.*


class ChatRoomActivity : BaseActivity(), ChatRoomView {
    lateinit var mPresenter: ChatRoomPresenter
    lateinit var mContentAdapter: ContentAdapter
    lateinit var mMessageAdapter: MessageAdapter
    lateinit var mFileAdapter: FileAdapter
    var selectedFileList: ArrayList<FileVO> = arrayListOf()


    companion object {
        var mContact : ContactVO? = null
        var isGroup: Boolean = false
        fun getIntent(context: Context): Intent {
            val intent = Intent(context, ChatRoomActivity::class.java)
            return intent
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_room)
        setUpPresenter()
        setUpRecyclerView()
        setUpListeners()
        bindData()

        mContact?.let {
            mPresenter.onUiReadyWithId(this, this, it.id, isGroup)
        }
    }

    private fun setUpPresenter() {
        mPresenter = ViewModelProvider(this)[ChatRoomPresenterImpl::class.java]
        mPresenter.initView(this)
    }

    private fun setUpRecyclerView() {
        mContentAdapter = ContentAdapter(mPresenter)
        rvContent.adapter = mContentAdapter
        mContentAdapter.setNewData(DEFAULT_CONTENT)

        mMessageAdapter = MessageAdapter()
        rvMessage.adapter = mMessageAdapter

        mFileAdapter = FileAdapter(mPresenter)
        rvChatFiles.adapter = mFileAdapter
    }

    private fun setUpListeners(){
        btnChatRoomBack.setOnClickListener {
            mPresenter.onTapBack()
        }

        btnChatSent.setOnClickListener {
            mContact?.let { contact ->
                mPresenter.sentMessage(
                   contactVO = contact,
                    fileList = selectedFileList,
                    message = MessageVO(
                        text = edtChatText.text.toString(),
                        millis = System.currentTimeMillis(),
                        photoList = arrayListOf(),
                        videoLink = "",
                        name = "",
                        id = "",
                        profileImage = "",
                    )
                )

                edtChatText.text.clear()
                mFileAdapter.setNewData(listOf())
                rvChatFiles.visibility = View.GONE

            }
        }
    }

    private fun bindData(){
        mContact?.let {
            Glide.with(this)
                .load(it.photoUrl)
                .into(ivChatRoomProfile)

            tvChatroomName.text = it.name
            tvChatroomActiveStatus.text = "online"
        }
    }

    override fun onTapContent(content: CONTENT) {
        selectedFileList.clear()
        rvChatFiles.visibility = View.GONE

        val newList = DEFAULT_CONTENT.map {
            ContentVO(
                content = it.content,
                image = it.image,
                isSelected = (it.content == content)
            )
        }.toList()
        mContentAdapter.setNewData(newList)

        if(content == CONTENT.IMAGE){
            selectImageFromGallery(INTENT_TYPE_FILE)
        }else if(content == CONTENT.CAMERA){
            mPresenter.onTapCamera()
        }
    }

    override fun bindMessages(ownId: String,messageList: List<MessageVO>) {
        mMessageAdapter.setId(ownId)
        mMessageAdapter.setNewData(messageList)
        if(messageList.isNotEmpty()){
            rvMessage.smoothScrollToPosition(messageList.size - 1)
        }
        selectedFileList.clear()
        mFileAdapter.setNewData(selectedFileList)
        rvChatFiles.visibility = View.GONE

    }

    override fun navigateBack() {
        super.onBackPressed()
    }

    override fun navigateToCameraScreen() {
        val intent = CameraActivity.getIntent(this)
        intentLauncher.launch(intent)
    }

    override fun onFileRemove(fileVO: FileVO) {
        selectedFileList.remove(fileVO)
        mFileAdapter.setNewData(selectedFileList)

        if(selectedFileList.isEmpty()){
            rvChatFiles.visibility = View.GONE
            mContentAdapter.setNewData(DEFAULT_CONTENT)
        }

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
                        if(selectedFileList.isNotEmpty()){
                            rvChatFiles.visibility = View.VISIBLE
                        }else{
                            rvChatFiles.visibility = View.GONE
                        }
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
                            if(selectedFileList.isNotEmpty()){
                                rvChatFiles.visibility = View.VISIBLE
                            }else{
                                rvChatFiles.visibility = View.GONE
                            }

                        }
                }
            }
        }
    }

    private val intentLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                Log.d("uri_data_reach_ok", result.data?.getStringExtra(EXTRA_URI) ?: "")
                val uri = Uri.parse(result.data?.getStringExtra(EXTRA_URI))
//                val fileVO = FileVO(
//                    bitmap = bitmap as Bitmap,
//
//                )
                Observable.just(uri)
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
                        if(selectedFileList.isNotEmpty()){
                            rvChatFiles.visibility = View.VISIBLE
                        }else{
                            rvChatFiles.visibility = View.GONE
                        }

                    }
            }
        }

}