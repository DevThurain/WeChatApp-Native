package com.thurainx.wechat_app.fragments

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.thurainx.wechat_app.R
import com.thurainx.wechat_app.delegate.EditDialogDelegate
import com.thurainx.wechat_app.utils.afterTextChanged
import kotlinx.android.synthetic.main.fragment_edit_dialog.*


class EditDialogFragment : DialogFragment() {

    lateinit var editDialog: Dialog
    var mEditDialogDelegate: EditDialogDelegate? = null


    companion object{
        const val TAG = "PurchaseConfirmationDialog"

        var selectedName: String = ""
        var selectedPhone: String = ""
        var selectedDay: String = "1"
        var selectedMonth: String = "1"
        var selectedYear: String = "1995"
        var selectedGender: String = ""

    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    fun setDelegate(delegate: EditDialogDelegate){
        mEditDialogDelegate = delegate
    }


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        editDialog = Dialog(requireContext())
        editDialog.setCancelable(true)
        editDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        editDialog.setContentView(R.layout.fragment_edit_dialog)
        setUpDateOfBirth(editDialog)
        setUpGender(editDialog)
        setUpEditText(editDialog)
        setUpListeners(editDialog)



        return editDialog
    }

    private fun setUpEditText(dialog: Dialog) {
        dialog.edtEditName.setText(selectedName)
        dialog.edtEditPhone.setText(selectedPhone)
    }

    private fun setUpListeners(dialog: Dialog) {
        dialog.edtEditName.afterTextChanged {
            selectedName = it
        }

        dialog.edtEditPhone.afterTextChanged {
            selectedPhone = it
        }

        dialog.btnEditCancel.setOnClickListener {
            dialog.dismiss()
        }

        dialog.btnEditSave.setOnClickListener {
            mEditDialogDelegate?.onSaveProfile(
                name = selectedName,
                phone = selectedPhone,
                dob = selectedDay.plus("/").plus(selectedMonth).plus("/").plus(selectedYear),
                gender = selectedGender
            )
            dialog.dismiss()
        }
    }

    private fun setUpDateOfBirth(dialog: Dialog) {
        val dayArray = IntArray(30) { it + 1 }
        val dayAdapter = ArrayAdapter(
            requireContext(),
            androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,
            dayArray.toTypedArray()
        )

        dialog.spEditDay.adapter = dayAdapter
        dialog.spEditDay.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                p1: View?,
                position: Int,
                id: Long
            ) {
                Log.d("day", parent?.getItemAtPosition(position).toString())
                selectedDay = parent?.getItemAtPosition(position).toString()
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {}
        }
        dialog.spEditDay.setSelection(dayArray.indexOf(selectedDay.toIntOrNull() ?: 1))



        val monthArray = IntArray(11) { it + 1 }
        val monthAdapter = ArrayAdapter(
            requireContext(),
            androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,
            monthArray.toTypedArray()
        )
        dialog.spEditMonth.adapter = monthAdapter
        dialog.spEditMonth.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                p1: View?,
                position: Int,
                id: Long
            ) {
                Log.d("month", parent?.getItemAtPosition(position).toString())
                selectedMonth = parent?.getItemAtPosition(position).toString()

            }

            override fun onNothingSelected(p0: AdapterView<*>?) {}
        }
        dialog.spEditMonth.setSelection(monthArray.indexOf(selectedMonth.toIntOrNull() ?: 1))


        val yearArray = IntArray(20) { it + 1995 }
        val yearAdapter = ArrayAdapter(
            requireContext(),
            androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,
            yearArray.toTypedArray()
        )
        dialog.spEditYear.adapter = yearAdapter
        dialog.spEditYear.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                p1: View?,
                position: Int,
                id: Long
            ) {
                Log.d("year", parent?.getItemAtPosition(position).toString())
                selectedYear = parent?.getItemAtPosition(position).toString()

            }

            override fun onNothingSelected(p0: AdapterView<*>?) {}
        }
        dialog.spEditYear.setSelection(yearArray.indexOf(selectedYear.toIntOrNull() ?: 1996))


    }

    private fun setUpGender(dialog: Dialog) {
        Log.d("selectedGender", selectedGender)
        when (selectedGender) {
            "Male" -> {
                dialog.radioEditGroupGender.check(R.id.rbEditMale)
            }
            "Female" -> {
                dialog.radioEditGroupGender.check(R.id.rbEditFemale)
            }
            "Other" -> {
                dialog.radioEditGroupGender.check(R.id.rbEditOther)
            }
        }
        dialog.radioEditGroupGender.setOnCheckedChangeListener { radioGroup, id ->

            when (id) {
                R.id.rbEditMale -> {
                    selectedGender = dialog.rbEditMale.text.toString()
                    Log.d("gender", selectedGender)

                }
                R.id.rbEditFemale -> {
                    selectedGender = dialog.rbEditFemale.text.toString()
                    Log.d("gender", selectedGender)

                }
                R.id.rbEditOther -> {
                    selectedGender = dialog.rbEditOther.text.toString()
                    Log.d("gender", selectedGender)

                }
                else -> {
                    Log.d("gender", "cannot get gender")

                }

            }

        }
    }


}