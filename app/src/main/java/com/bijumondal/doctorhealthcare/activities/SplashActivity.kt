package com.bijumondal.doctorhealthcare.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.bijumondal.doctorhealthcare.R
import com.bijumondal.doctorhealthcare.utils.HealthCarePreference

class SplashActivity : AppCompatActivity() {

    companion object {
        private const val TAG = "SplashActivity"
    }

    private lateinit var mPreference: HealthCarePreference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        mPreference = HealthCarePreference(this@SplashActivity)

        if (mPreference.isLoggedIn()) {
            Handler().postDelayed(Runnable {
                startActivity(Intent(this@SplashActivity, MainActivity::class.java))
                finish()
            }, 1500)

        } else {
            Handler().postDelayed(Runnable {
                startActivity(Intent(this@SplashActivity, WelcomeActivity::class.java))
                finish()
            }, 1500)
        }
    }
}
