package com.thurainx.wechat_app.utils

import android.content.Context
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import androidx.datastore.preferences.rxjava3.RxPreferenceDataStoreBuilder
import androidx.datastore.preferences.rxjava3.rxPreferencesDataStore
import androidx.datastore.rxjava3.RxDataStore
import androidx.datastore.rxjava3.rxDataStore
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first


const val DATASTORE_USER = "DATASTORE_USER"
object DataStoreUtils {
    val Context.userDataStore: RxDataStore<Preferences> by rxPreferencesDataStore(name = DATASTORE_USER)

    suspend fun DataStore<Preferences>.writeString(key: String, value: String) {
        val dataStoreKey = stringPreferencesKey(key)
        this.edit { preference ->
            preference[dataStoreKey] = value
        }
    }

    suspend fun DataStore<Preferences>.readString(key: String): String? {
        val dataStoreKey = stringPreferencesKey(key)
        val preferences = this.data.first()
        return preferences[dataStoreKey]
    }

    suspend fun DataStore<Preferences>.writeInt(key: String, value: Int) {
        val dataStoreKey = stringPreferencesKey(key)
        this.edit { preference ->
            preference[dataStoreKey] = value.toString()
        }
    }

    suspend fun DataStore<Preferences>.readInt(key: String): Int? {
        val dataStoreKey = stringPreferencesKey(key)
        val preferences = this.data.first()
        return preferences[dataStoreKey]?.toInt()
    }

    suspend fun DataStore<Preferences>.deleteDataStore() {
        this.edit {
            it.clear()
        }
    }
    /* rx kotlin */

    @OptIn(ExperimentalCoroutinesApi::class)
    fun RxDataStore<Preferences>.writeToRxDatastore(key: String, value: String): Observable<Preferences> {
         return this.updateDataAsync { prefsIn ->
            val mutablePreferences: MutablePreferences = prefsIn.toMutablePreferences()
            val stringKey  = stringPreferencesKey(key)
            mutablePreferences[stringKey] = value
            Single.just(mutablePreferences)
        }.toObservable()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    fun RxDataStore<Preferences>.readFromRxDatastore(key: String) : Observable<String>? {
        val stringKey  = stringPreferencesKey(key)
        return this.data().map { pref -> pref.get(stringKey).toString() }.toObservable()
    }


    @OptIn(ExperimentalCoroutinesApi::class)
    fun RxDataStore<Preferences>.clearRxDataStore(): Observable<Preferences>{
        return this.updateDataAsync{ prefsIn ->
            val mutablePreferences: MutablePreferences = prefsIn.toMutablePreferences()
            mutablePreferences.clear()
            Single.just(mutablePreferences)
        }.toObservable()
    }

    fun RxDataStore<Preferences>.readQuick(key: String,onSuccess : (String) -> Unit){
        Log.d("rx","user")
        this.readFromRxDatastore(key)
            ?.first("")
            ?.subscribeOn(Schedulers.io())
            ?.observeOn(AndroidSchedulers.mainThread())
            ?.subscribe(
                {
                    Log.d("rx",it ?: "empty")
                    onSuccess(it)
                },
                {
                    Log.d("rx",it.message ?: "failed to read from datastore")
                }
            )
    }


}