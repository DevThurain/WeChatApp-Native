package com.thurainx.wechat_app.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import com.thurainx.wechat_app.R
import com.thurainx.wechat_app.utils.afterTextChanged
import com.thurainx.wechat_app.utils.validateEmail
import kotlinx.android.synthetic.main.activity_register.*
import kotlinx.android.synthetic.main.activity_register.view.*

class RegisterActivity : AppCompatActivity() {
    private var selectedDay: String = "1"
    private var selectedMonth: String = "1"
    private var selectedYear: String = "1995"
    private var selectedGender: String = ""
    private var checkedTermAndCondition : Boolean = false


    fun getIntent(context: Context): Intent {
        val intent = Intent(context, RegisterActivity::class.java)
        return intent
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        setUpEditText()
        setUpDateOfBirth()
        setUpGender()
        setUpTermAndCondition()
        setUpListeners()
    }

    private fun setUpListeners(){
        btnRegister.setOnClickListener {
            if(checkButtonAvailable()){

            }
        }
    }

    private fun setUpTermAndCondition() {
        cbTermAndCondition.setOnClickListener {
            checkedTermAndCondition = cbTermAndCondition.isChecked
            checkButtonAvailable()
            Log.d("term",checkedTermAndCondition.toString())
        }
    }

    private fun setUpDateOfBirth(){
        val dayArray = IntArray(30) { it + 1 }
        val dayAdapter = ArrayAdapter(this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,dayArray.toTypedArray())
        spDay.adapter = dayAdapter
        spDay.onItemSelectedListener =object :AdapterView.OnItemSelectedListener{
            override fun onItemSelected(parent: AdapterView<*>?, p1: View?, position: Int, id: Long) {
                    Log.d("day",parent?.getItemAtPosition(position).toString())
                    selectedDay = parent?.getItemAtPosition(position).toString()
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {}
        }



        val monthArray = IntArray(11) { it + 1 }
        val monthAdapter = ArrayAdapter(this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,monthArray.toTypedArray())
        spMonth.adapter = monthAdapter
        spMonth.onItemSelectedListener =object :AdapterView.OnItemSelectedListener{
            override fun onItemSelected(parent: AdapterView<*>?, p1: View?, position: Int, id: Long) {
                Log.d("month",parent?.getItemAtPosition(position).toString())
                selectedMonth = parent?.getItemAtPosition(position).toString()

            }

            override fun onNothingSelected(p0: AdapterView<*>?) {}
        }

        val yearArray = IntArray(20) { it + 1995 }
        val yearAdapter = ArrayAdapter(this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,yearArray.toTypedArray())
        spYear.adapter = yearAdapter
        spYear.onItemSelectedListener =object :AdapterView.OnItemSelectedListener{
            override fun onItemSelected(parent: AdapterView<*>?, p1: View?, position: Int, id: Long) {
                Log.d("year",parent?.getItemAtPosition(position).toString())
                selectedYear = parent?.getItemAtPosition(position).toString()

            }

            override fun onNothingSelected(p0: AdapterView<*>?) {}
        }

    }
    
    private fun setUpGender(){
        radioGroupGender.setOnCheckedChangeListener { radioGroup, id ->

            when(id){
                R.id.rbMale -> {
                    selectedGender = rbMale.text.toString()
                    Log.d("gender",selectedGender)

                }
                R.id.rbFemale -> {
                    selectedGender = rbFemale.text.toString()
                    Log.d("gender",selectedGender)

                }
                R.id.rbOther -> {
                    selectedGender = rbOther.text.toString()
                    Log.d("gender",selectedGender)

                }

            }
            checkButtonAvailable()
        }
    }

    private fun setUpEditText(){
        edtRegisterName.afterTextChanged { text ->
            if(text.isEmpty()){
                edtRegisterName.error = "Name cannot be empty."
            }
            checkButtonAvailable()
        }

        edtRegisterPassword.afterTextChanged { text ->
            if(text.isEmpty()){
                edtRegisterPassword.error = "Password cannot be empty."
            }
            if(text.length < 6){
                edtRegisterPassword.error = "Password need at least 6 characters."
            }
            checkButtonAvailable()
        }

        edtRegisterEmail.afterTextChanged { text ->
            edtRegisterEmail.validateEmail()
            checkButtonAvailable()
        }
    }

    private fun validate() : Boolean{
        return edtRegisterName.text?.isNotEmpty() == true &&
                edtRegisterPassword.text?.isNotEmpty() == true &&
                edtRegisterPassword.text!!.length > 5 &&
                edtRegisterEmail.validateEmail() &&
                selectedGender.isNotEmpty() &&
                checkedTermAndCondition
    }

    private fun checkButtonAvailable() : Boolean{
        if(validate()){
            btnRegister.alpha = 1F
            return true
        }else{
            btnRegister.alpha = 0.5f
            return false
        }
    }
}