package com.bijumondal.doctorhealthcare.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bijumondal.doctorhealthcare.R
import kotlinx.android.synthetic.main.activity_register.*

class RegisterActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)


        tv_goto_sign_in.setOnClickListener {
            startActivity(Intent(this@RegisterActivity, LoginActivity::class.java))
        }

        btn_sign_up.setOnClickListener {
            startActivity(Intent(this@RegisterActivity, MainActivity::class.java))

        }

    }
}
