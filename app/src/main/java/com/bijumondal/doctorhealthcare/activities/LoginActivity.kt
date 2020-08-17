package com.bijumondal.doctorhealthcare.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bijumondal.doctorhealthcare.R
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        btn_sign_in.setOnClickListener {
            startActivity(Intent(this@LoginActivity, MainActivity::class.java))
            finish()
        }

        tv_goto_sign_up.setOnClickListener {
            startActivity(Intent(this@LoginActivity, RegisterActivity::class.java))
        }


    }
}
