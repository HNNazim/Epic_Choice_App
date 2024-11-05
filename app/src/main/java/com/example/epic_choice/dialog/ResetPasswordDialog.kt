package com.example.epic_choice.dialog

import android.widget.Button
import android.widget.EditText
import androidx.constraintlayout.motion.widget.MotionScene.Transition.TransitionOnClick
import androidx.fragment.app.Fragment
import com.example.epic_choice.R
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog

fun Fragment.setupBottomSheetDialog(
    onSendClick: (String) -> Unit
){
    val dialog = BottomSheetDialog(requireContext(),R.style.DialogStyle)
    val view = layoutInflater.inflate(R.layout.reset_password_dialog,null)
    dialog.setContentView(view)
    dialog.behavior.state = BottomSheetBehavior.STATE_EXPANDED
    dialog.show()

    val ETEmail = view.findViewById<EditText>(R.id.ETResetPassword)
    val btnSend = view.findViewById<Button>(R.id.btnSendLink)
    val btnCancel = view.findViewById<Button>(R.id.btnCancelReset)

    btnSend.setOnClickListener{
        val email = ETEmail.text.toString().trim()
        onSendClick(email)
        dialog.dismiss()
    }

    btnCancel.setOnClickListener{
        dialog.dismiss()
    }
}