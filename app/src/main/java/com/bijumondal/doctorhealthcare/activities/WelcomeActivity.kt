package com.bijumondal.doctorhealthcare.activities

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.RadioButton
import android.widget.RadioGroup
import com.bijumondal.doctorhealthcare.R
import com.bijumondal.doctorhealthcare.utils.HealthCarePreference
import com.bijumondal.doctorhealthcare.utils.Helper
import kotlinx.android.synthetic.main.activity_welcome.*

class WelcomeActivity : AppCompatActivity() {

    companion object {
        private const val TAG = "WelcomeActivity"
    }

    private lateinit var mPreferences: HealthCarePreference

    private var userTypeDoctor: Boolean = false
    private var userTypePatient: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome)
        mPreferences = HealthCarePreference(this@WelcomeActivity)

        mPreferences.clearSharedPreference()

        rg_user_type.setOnCheckedChangeListener(
            RadioGroup.OnCheckedChangeListener { group, checkedId ->
                when (findViewById<RadioButton>(checkedId)) {
                    findViewById<RadioButton>(R.id.rb_patient) -> {
                        rb_patient.isChecked = true
                        rb_doctor.isChecked = false
                        userTypePatient = true
                        userTypeDoctor = false
                        mPreferences.setUserType(1)  //1 for patient
                        rb_patient.setBackgroundColor(Color.parseColor("#3c4df7"))
                        rb_patient.setTextColor(Color.parseColor("#FFFFFF"))
                        rb_doctor.setTextColor(Color.parseColor("#000000"))
                        rb_doctor.setBackgroundColor(Color.parseColor("#FFFFFF"))
                    }

                    findViewById<RadioButton>(R.id.rb_doctor) -> {
                        rb_doctor.isChecked = true
                        rb_patient.isChecked = false
                        userTypePatient = false
                        userTypeDoctor = true
                        mPreferences.setUserType(2)   //2 for doctor
                        rb_doctor.setBackgroundColor(Color.parseColor("#3c4df7"))
                        rb_doctor.setTextColor(Color.parseColor("#FFFFFF"))
                        rb_patient.setTextColor(Color.parseColor("#000000"))
                        rb_patient.setBackgroundColor(Color.parseColor("#FFFFFF"))
                    }
                }
            }
        )

        btn_next.setOnClickListener {
            if (mPreferences.getUserType() != null) {

                if (mPreferences.getUserType() == 1) {
                    startActivity(Intent(this@WelcomeActivity, LoginActivity::class.java))
                    finish()
                } else if (mPreferences.getUserType() == 2) {
                    startActivity(Intent(this@WelcomeActivity, LoginActivity::class.java))
                    finish()
                } else {
                    Helper.toastShort(this@WelcomeActivity, "Please choose a user type!")
                }

            } else {
                Helper.toastShort(this@WelcomeActivity, "Please choose a user type!")
            }

        }


    }


}
