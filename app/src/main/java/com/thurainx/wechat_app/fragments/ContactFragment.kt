package com.thurainx.wechat_app.fragments

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.rxjava3.RxDataStore
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar
import com.thurainx.wechat_app.R
import com.thurainx.wechat_app.activities.ChatRoomActivity
import com.thurainx.wechat_app.activities.CreateGroupActivity
import com.thurainx.wechat_app.activities.QrScannerActivity
import com.thurainx.wechat_app.adapters.ChatGroupAdapter
import com.thurainx.wechat_app.adapters.ContactGroupAdapter
import com.thurainx.wechat_app.data.vos.ContactVO
import com.thurainx.wechat_app.data.vos.GroupVO
import com.thurainx.wechat_app.mvp.presenters.ContactPresenter
import com.thurainx.wechat_app.mvp.presenters.ContactPresenterImpl
import com.thurainx.wechat_app.mvp.views.ContactView
import com.thurainx.wechat_app.utils.*
import kotlinx.android.synthetic.main.activity_create_group.*
import kotlinx.android.synthetic.main.fragment_contact.*

class ContactFragment : Fragment(), ContactView {
    lateinit var mContactGroupAdapter: ContactGroupAdapter
    lateinit var mChatGroupAdapter: ChatGroupAdapter
    lateinit var mPresenter: ContactPresenter
    private var allContacts: ArrayList<ContactVO> = arrayListOf()
    private var allGroups: ArrayList<GroupVO> = arrayListOf()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_contact, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpPresenter()
        setUpRecyclerView()
        setUpListeners()
        mPresenter.onUiReady(requireContext(), requireActivity())
    }

    private fun setUpPresenter() {
        mPresenter = ViewModelProvider(this)[ContactPresenterImpl::class.java]
        mPresenter.initView(this)
    }

    private fun setUpRecyclerView() {
        mContactGroupAdapter = ContactGroupAdapter(VIEW_TYPE_NORMAL, mPresenter, mPresenter)
        rvNormalContact.adapter = mContactGroupAdapter

        mChatGroupAdapter = ChatGroupAdapter(mPresenter)
        rvGroup.adapter = mChatGroupAdapter
    }

    private fun setUpListeners() {
        btnAddContact.setOnClickListener {
            mPresenter.onTapAddContact()
        }

        btnAddGroup.setOnClickListener {
            mPresenter.onTapCreateGroup()
        }

        edtContactSearch.afterTextChanged {
            if (it.isEmpty()) {
                mContactGroupAdapter.setNewData(allContacts.toContactGroupList())
                mChatGroupAdapter.setNewData(allGroups)
                btnAddGroup.visibility = View.VISIBLE
                layoutEmptyContact.visibility = View.GONE
            } else {

                val contactSearchList = allContacts.filter { contactVO ->
                    contactVO.name.lowercase().startsWith(it.lowercase())
                }.toContactGroupList()

                mContactGroupAdapter.setNewData(contactSearchList)

                val groupSearchList = allGroups.filter { groupVO ->
                    groupVO.members.any { contactVO ->
                        contactVO.name.lowercase().startsWith(it.lowercase())
                    }
                }
                mChatGroupAdapter.setNewData(groupSearchList)

                if(contactSearchList.isEmpty() && groupSearchList.isEmpty()){
                    btnAddGroup.visibility = View.INVISIBLE
                    layoutEmptyContact.visibility = View.VISIBLE
                }else{
                    btnAddGroup.visibility = View.VISIBLE
                    layoutEmptyContact.visibility = View.GONE

                }
            }
        }
    }

    override fun navigateToQrScannerScreen() {
        val intent = QrScannerActivity.getIntent(requireContext())
        intentLauncher.launch(intent)
    }

    override fun navigateToCreateGroupScreen() {
        val intent = CreateGroupActivity.getIntent(requireContext())
        createGroupLauncher.launch(intent)
    }

    override fun bindContacts(contactList: List<ContactVO>) {
        allContacts.clear()
        allContacts.addAll(contactList)
        mContactGroupAdapter.setNewData(contactList.toContactGroupList())
    }

    override fun bindGroups(groupList: List<GroupVO>) {
        allGroups.clear()
        allGroups.addAll(groupList)
        mChatGroupAdapter.setNewData(groupList)
    }

    override fun navigateToGroupChatScreen(groupVO: GroupVO) {
        val intent = ChatRoomActivity.getIntent(requireContext())
        ChatRoomActivity.mContact = ContactVO(
            id = groupVO.id.toString(),
            name = groupVO.name.toString(),
            photoUrl = groupVO.photo.toString(),
        )
        ChatRoomActivity.isGroup = true
        startActivity(intent)
    }

    override fun navigateToChatRoomScreen(contactVO: ContactVO) {
        val intent = ChatRoomActivity.getIntent(requireContext())
        ChatRoomActivity.mContact = contactVO
        ChatRoomActivity.isGroup = false
        startActivity(intent)
    }

    override fun showErrorMessage(message: String) {
        Snackbar.make(requireView(), message, Snackbar.LENGTH_SHORT).show()
    }

    private val intentLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                Log.d("qr_data_reach_ok", result.data?.getStringExtra(EXTRA_QR) ?: "-")
                val id = result.data?.getStringExtra(EXTRA_QR) ?: "-"
                if (id != "-") {
                    mPresenter.addContact(id)
                }
            }
        }

    private val createGroupLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                mPresenter.refreshGroupList()
            }
        }

}