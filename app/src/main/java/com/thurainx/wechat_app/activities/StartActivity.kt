package com.thurainx.wechat_app.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.thurainx.wechat_app.R
import com.thurainx.wechat_app.utils.DataStoreUtils.clearRxDataStore
import com.thurainx.wechat_app.utils.DataStoreUtils.readQuick
import com.thurainx.wechat_app.utils.DataStoreUtils.userDataStore
import com.thurainx.wechat_app.utils.FIRE_STORE_REF_NAME
import com.thurainx.wechat_app.utils.FIRE_STORE_REF_PHONE

class StartActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start)

        checkUserExist { isUserExist ->
            if(isUserExist){
                val intent = NavigationActivity.getIntent(this)
                finish()
                startActivity(intent)
            }else{
                val intent = GreetingActivity.getIntent(this)
                finish()
                startActivity(intent)
            }
        }
    }

    private fun checkUserExist(isUserExist: (Boolean) -> Unit){
        Log.d("blahblah","check user exist")

        val dataStore = this.userDataStore
//        dataStore.clearRxDataStore()
        dataStore.readQuick(FIRE_STORE_REF_PHONE){
            Log.d("blahblah",it)
            isUserExist(it != "null")
        }
    }
}