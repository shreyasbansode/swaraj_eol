package com.carnot.swaraj.eol

import android.app.Activity
import android.app.Dialog
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.TextView

class CustomDialog(private val activity: Activity) {

    private val dialog: Dialog = Dialog(activity)

    fun show(message: String, isCancellable: Boolean = false) {
        val view = LayoutInflater.from(activity).inflate(R.layout.dialog_success, null)

        // Find the views in the custom dialog layout
        val closeButton: ImageView = view.findViewById(R.id.closeButton)
        val dialogMessage: TextView = view.findViewById(R.id.dialogMessage)

        // Set the custom message
        dialogMessage.text = message

        // Set a click listener for the close button
        closeButton.setOnClickListener {
            dialog.dismiss()
            activity.finish()
        }

        // Set the custom view for the dialog
        dialog.setContentView(view)
        dialog.setCancelable(isCancellable)
        dialog.show()
    }

    fun dismiss() {
        dialog.dismiss()
    }
}
