package com.example.flashlight

import android.content.Context
import android.graphics.Color
import android.hardware.camera2.CameraAccessException
import android.hardware.camera2.CameraManager
import android.os.Bundle
import android.widget.ImageButton
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Flashlight access
        val cameraManager = getSystemService(Context.CAMERA_SERVICE) as CameraManager
        val cameraId = cameraManager.cameraIdList[0]

        // views
        val bg : ConstraintLayout = findViewById(R.id.bg)
        val power : ImageButton = findViewById(R.id.btnFlash)

        // button state
        var on = false

        // flashlight initially off
        power.setImageResource(R.drawable.off)
        power.setBackgroundColor(Color.GRAY)
        bg.setBackgroundColor(Color.GRAY)

        // alert dialog
        val builder = AlertDialog.Builder(this)
        builder.setMessage("App cannot access flashlight!")
        builder.setCancelable(false)
        builder.setNeutralButton("Ok") { _,_ ->
            finish()
        }
        val alert = builder.create()

        // event listener
        power.setOnClickListener {
            try {
                toggleFlash(power, bg, !on, cameraManager, cameraId)
                on = !on
            }
            catch (e: CameraAccessException) {
                displayMessage(alert)
            }
        }
    }

    // method to toggle flashlight
    private fun toggleFlash(btn: ImageButton, background: ConstraintLayout,
                            state: Boolean, cM: CameraManager, cI: String) {
        if(state) {
            btn.setImageResource(R.drawable.on)
            btn.setBackgroundColor(Color.YELLOW)
            background.setBackgroundColor(Color.YELLOW)
            cM.setTorchMode(cI, true)
        }
        else {
            btn.setImageResource(R.drawable.off)
            btn.setBackgroundColor(Color.GRAY)
            background.setBackgroundColor(Color.GRAY)
            cM.setTorchMode(cI, false)
        }
    }

    // method to display message if flashlight access attempt yields error
    private fun displayMessage(alert: AlertDialog) {
        alert.show()
    }
}