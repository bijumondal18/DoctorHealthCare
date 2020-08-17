package com.bijumondal.doctorhealthcare.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bijumondal.doctorhealthcare.R
import kotlinx.android.synthetic.main.activity_welcome.*

class WelcomeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome)

        btn_agree_and_continue.setOnClickListener {
            startActivity(Intent(this@WelcomeActivity, LoginActivity::class.java))
            finish()
        }


    }


}
