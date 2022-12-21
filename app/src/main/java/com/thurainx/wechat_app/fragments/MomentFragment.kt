package com.thurainx.wechat_app.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar
import com.thurainx.wechat_app.R
import com.thurainx.wechat_app.activities.AddMomentActivity
import com.thurainx.wechat_app.adapters.MomentAdapter
import com.thurainx.wechat_app.data.vos.MomentVO
import com.thurainx.wechat_app.mvp.presenters.MomentPresenter
import com.thurainx.wechat_app.mvp.presenters.MomentPresenterImpl
import com.thurainx.wechat_app.mvp.views.MomentView
import kotlinx.android.synthetic.main.fragment_moment.*

class MomentFragment : Fragment(), MomentView {

    lateinit var mPresenter: MomentPresenter
    lateinit var mMomentAdapter: MomentAdapter
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
        setUpListeners()
        setUpRecyclerView()

        mPresenter.onUiReady(view.context, this)
    }

    private fun setUpPresenter(){
        mPresenter = ViewModelProvider(this)[MomentPresenterImpl::class.java]
        mPresenter.initView(this)
    }

    private fun setUpListeners(){
        btnAddMoment.setOnClickListener {
            mPresenter.onTapAddMoment()
        }
    }

    private fun setUpRecyclerView(){
        mMomentAdapter = MomentAdapter()
        rvMoment.adapter = mMomentAdapter
    }

    override fun navigateToAddMomentScreen() {
        val intent = Intent(requireContext(), AddMomentActivity::class.java)
        startActivity(intent)
    }

    override fun bindMoments(momentList: List<MomentVO>) {
        mMomentAdapter.setNewData(momentList)
    }

    override fun showErrorMessage(message: String) {
        Snackbar.make(requireView(),message,Snackbar.LENGTH_SHORT).show()
    }


}