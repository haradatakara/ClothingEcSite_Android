package com.example.clothingecsite_30.view

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import com.example.clothingecsite_30.R
import com.example.clothingecsite_30.databinding.FragmentUserConfirmDialogBinding
import com.example.clothingecsite_30.viewModel.authentication.UpdateViewModel
import com.example.clothingecsite_30.viewModel.authentication.UpdateViewModelFactory

class UserConfirmDialogFragment : DialogFragment() {

    interface DialogListener {
        fun onDialogMapReceive(dialog: DialogFragment, myMutableMap: Map<String, String>)
    }

    var listener: DialogListener? = null

    private lateinit var updateViewModel: UpdateViewModel

    private var _binding: FragmentUserConfirmDialogBinding? = null
    private val binding get() = _binding!!

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            _binding = DataBindingUtil.inflate(
                layoutInflater,
                R.layout.fragment_user_confirm_dialog,
                null,
                false
            )
            updateViewModel =
                ViewModelProvider(this, UpdateViewModelFactory())[UpdateViewModel::class.java]
            binding.viewModel = updateViewModel
            val view = binding.root
            val builder = AlertDialog.Builder(it)
            builder.setView(view)
                .setPositiveButton(
                    R.string.update
                ) { _, _ ->
                    if (
                        !binding.confirmEmail.text.equals("")
                        && !binding.confirmPassword.equals("")) {
                        dialog?.cancel()
                        listener?.onDialogMapReceive(
                            this, mapOf(
                                "confirmEmail" to binding.confirmEmail.text.toString(),
                                "confirmPass" to binding.confirmPassword.text.toString()
                            )
                        )
                    } else {
                        Toast.makeText(
                            context,
                            "ちゃんと入力してね！",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
                .setNegativeButton(R.string.cancel) { _, _ ->
                    dialog?.cancel()
                }
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            listener = context as DialogListener
        } catch (e: Exception) {
            Log.e("ERROR", "CANNOT FIND LISTENER")
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

}