package com.thurainx.wechat_app.mvp.presenters

import android.content.Context
import android.util.Log
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.rxjava3.RxDataStore
import androidx.lifecycle.LifecycleOwner
import com.thurainx.wechat_app.data.models.WeChatModelImpl
import com.thurainx.wechat_app.mvp.views.ProfileView
import com.thurainx.wechat_app.network.auth.AuthManagerImpl
import com.thurainx.wechat_app.utils.*
import com.thurainx.wechat_app.utils.DataStoreUtils.readFromRxDatastore
import com.thurainx.wechat_app.utils.DataStoreUtils.readQuick
import com.thurainx.wechat_app.utils.DataStoreUtils.userDataStore
import com.thurainx.wechat_app.utils.DataStoreUtils.writeToRxDatastore
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers

class ProfilePresenterImpl : AbstractBasedPresenter<ProfileView>(), ProfilePresenter {

    val mWeChatModel = WeChatModelImpl
    val mAuthManager = AuthManagerImpl
    var dataStore: RxDataStore<Preferences>? = null
    var mProfileImage = ""
    var mId = ""
    var mName = ""
    var mPhone = ""
    var mDob = ""
    var mGender = ""
    var mPassword = ""


    override fun onTapQr() {
        mView.showQrDialog(mId)
    }

    override fun onTapEditProfile() {
        mView.showEditDialog(name = mName, phone = mPhone, dob = mDob, gender = mGender)
    }

    override fun onUiReady(context: Context, owner: LifecycleOwner) {
        dataStore = context.userDataStore

        getUserData()
        dataStore?.readQuick(FIRE_STORE_REF_ID) {
            mId = it
            mWeChatModel.getBookMarkMoments(
                id = mId,
                onSuccess = {
                    mView.bindMoments(it)
                },
                onFailure = { error ->
                    mView.showErrorMessage(error)
                }
            )
        }

    }

    private fun refreshMoment() {
        dataStore?.readQuick(FIRE_STORE_REF_ID) {
            mId = it
            mWeChatModel.getBookMarkMoments(
                id = mId,
                onSuccess = {
                    mView.bindMoments(it)
                },
                onFailure = { error ->
                    mView.showErrorMessage(error)
                }
            )
        }
    }

    override fun onTapLike(
        momentMillis: String,
        totalLike: Int,
        isLike: Boolean,
        onSuccess: () -> Unit
    ) {
        mWeChatModel.likeMoment(
            like = isLike,
            id = mId,
            totalLike = totalLike,
            momentMillis = momentMillis,
            onSuccess = {
                Log.d("reaction", "reaction success")
                onSuccess()

            },
            onFailure = {
                mView.showErrorMessage(it)
            },
        )

    }

    override fun onTapBookmark(momentMillis: String, isBookmark: Boolean, onSuccess: () -> Unit) {
        mWeChatModel.bookMarkMoment(
            isBookMark = isBookmark,
            id = mId,
            momentMillis = momentMillis,
            onSuccess = {
                Log.d("reaction", "reaction success")
                onSuccess()
                refreshMoment()
            },
            onFailure = {
                mView.showErrorMessage(it)
            },
        )
    }

    override fun onSaveProfile(name: String, phone: String, dob: String, gender: String) {
        if (phone != mPhone) {
            mAuthManager.updateUser(
                phone,
                mPassword,
                onSuccess = {
                    mWeChatModel.updateUser(
                        id = mId,
                        name = name,
                        phone = phone,
                        password = mPassword,
                        dob = dob,
                        gender = gender,
                        profileImage = mProfileImage,
                        onSuccess = {
                            updateUserData(
                                name = name,
                                phone = phone,
                                dob = dob,
                                gender = gender,
                                profileImage = mProfileImage
                            )

                        },
                        onFailure = {
                            mView.showErrorMessage(it)
                        }
                    )
                },
                onFailure = {
                    mView.showErrorMessage(it)

                },
            )
        } else {
            mWeChatModel.updateUser(
                id = mId,
                name = name,
                phone = phone,
                password = mPassword,
                dob = dob,
                gender = gender,
                profileImage = mProfileImage,
                onSuccess = {
                    updateUserData(
                        name = name,
                        phone = phone,
                        dob = dob,
                        gender = gender,
                        profileImage = mProfileImage
                    )
                },
                onFailure = {
                    mView.showErrorMessage(it)
                }
            )
        }
    }

    private fun getUserData() {
        Single.zip(
            dataStore?.readFromRxDatastore(FIRE_STORE_REF_ID)?.first("-") ?: Single.just("-"),
            dataStore?.readFromRxDatastore(FIRE_STORE_REF_NAME)?.first("-") ?: Single.just("-"),
            dataStore?.readFromRxDatastore(FIRE_STORE_REF_PHONE)?.first("-") ?: Single.just("-"),
            dataStore?.readFromRxDatastore(FIRE_STORE_REF_DOB)?.first("-") ?: Single.just("-"),
            dataStore?.readFromRxDatastore(FIRE_STORE_REF_GENDER)?.first("-") ?: Single.just("-"),
            dataStore?.readFromRxDatastore(FIRE_STORE_REF_PROFILE_IMAGE)?.first("-") ?: Single.just(
                "-"
            ),
            dataStore?.readFromRxDatastore(FIRE_STORE_REF_PASSWORD)?.first("-") ?: Single.just("-"),

            ) { id, name, phone, dob, gender, profile, password ->
            Log.d("rx_read", "$name - $profile")
            return@zip mapOf(
                FIRE_STORE_REF_ID to id,
                FIRE_STORE_REF_NAME to name,
                FIRE_STORE_REF_PHONE to phone,
                FIRE_STORE_REF_DOB to dob,
                FIRE_STORE_REF_GENDER to gender,
                FIRE_STORE_REF_PROFILE_IMAGE to profile,
                FIRE_STORE_REF_PASSWORD to password
            )
        }.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                mId = it[FIRE_STORE_REF_ID].toString()
                mName = it[FIRE_STORE_REF_NAME].toString()
                mPhone = it[FIRE_STORE_REF_PHONE].toString()
                mDob = it[FIRE_STORE_REF_DOB].toString()
                mGender = it[FIRE_STORE_REF_GENDER].toString()
                mPassword = it[FIRE_STORE_REF_PASSWORD].toString()
                mProfileImage = it[FIRE_STORE_REF_PROFILE_IMAGE].toString()

                mView.bindProfileData(
                    it[FIRE_STORE_REF_NAME].toString(),
                    it[FIRE_STORE_REF_PHONE].toString(),
                    it[FIRE_STORE_REF_DOB].toString(),
                    it[FIRE_STORE_REF_GENDER].toString(),
                    it[FIRE_STORE_REF_PROFILE_IMAGE].toString()
                )
            }, {
                Log.d("rx", it.message.toString())
            })
    }

    private fun updateUserData(
        name: String,
        phone: String,
        dob: String,
        gender: String,
        profileImage: String
    ) {
        Single.zip(
            dataStore?.writeToRxDatastore(FIRE_STORE_REF_NAME, name)?.firstOrError() ?: Single.just("-"),
            dataStore?.writeToRxDatastore(FIRE_STORE_REF_PHONE, phone)?.firstOrError() ?: Single.just("-"),
            dataStore?.writeToRxDatastore(FIRE_STORE_REF_DOB, dob)?.firstOrError() ?: Single.just("-"),
            dataStore?.writeToRxDatastore(FIRE_STORE_REF_GENDER, gender)?.firstOrError() ?: Single.just("-"),
            dataStore?.writeToRxDatastore(FIRE_STORE_REF_PROFILE_IMAGE, profileImage)?.firstOrError() ?: Single.just("-")
            ){ xName,xPhone,xDob,xGender,xProfileImage ->

        }.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                getUserData()
                },
                {
                    Log.d("rx", it.message.toString())
                }
            )


    }
}