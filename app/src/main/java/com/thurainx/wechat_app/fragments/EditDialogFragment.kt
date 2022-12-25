package com.thurainx.wechat_app.fragments

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.thurainx.wechat_app.R


class EditDialogFragment : DialogFragment() {

    lateinit var editDialog : Dialog

//    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog =
//       Dialog(requireContext())
//


    companion object {
        const val TAG = "PurchaseConfirmationDialog"
    }



    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        editDialog = Dialog(requireContext())
        editDialog.setCancelable(true)
        editDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        editDialog.setContentView(R.layout.fragment_edit_dialog)
        return editDialog
    }

//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//    }
//
//    override fun onCreateView(
//        inflater: LayoutInflater,
//        container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View {
//        val v: View = inflater.inflate(R.layout.fragment_edit_dialog, container, false)
//
//        editDialog = Dialog(requireContext())
//        editDialog.setCancelable(true)
//        editDialog.setContentView(layoutInflater.inflate(R.layout.fragment_edit_dialog, null))
//        editDialog.show()
//
//    return v
//    }

}