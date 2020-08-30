package com.bijumondal.doctorhealthcare.activities

import android.content.Intent
import android.os.Bundle
import android.view.KeyEvent
import androidx.appcompat.app.AppCompatActivity
import com.bijumondal.doctorhealthcare.R
import kotlinx.android.synthetic.main.activity_booking_successful.*

class BookingSuccessfulActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_booking_successful)

        btn_go_to_home_screen.setOnClickListener {
            startActivity(
                Intent(this@BookingSuccessfulActivity, MainActivity::class.java)
                    .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                    .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                    .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            )
            finish()
        }

    }

    override fun onBackPressed() {
        super.onBackPressed()
        startActivity(
            Intent(this@BookingSuccessfulActivity, MainActivity::class.java)
                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        )
        finish()

    }

}
