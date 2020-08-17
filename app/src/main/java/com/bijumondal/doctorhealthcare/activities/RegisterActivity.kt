package com.bijumondal.doctorhealthcare.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import com.bijumondal.doctorhealthcare.R
import com.bijumondal.doctorhealthcare.models.patientRegistration.RequestPatientRegistration
import com.bijumondal.doctorhealthcare.utils.HealthCarePreference
import com.bijumondal.doctorhealthcare.utils.Helper
import kotlinx.android.synthetic.main.activity_register.*

class RegisterActivity : AppCompatActivity() {

    companion object {
        private const val TAG = "RegisterActivity"
    }

    private lateinit var mPreference: HealthCarePreference

    private var firstName: String = ""
    private var lastName: String = ""
    private var email: String = ""
    private var mobile: String = ""
    private var password: String = ""
    private var cnfPassword: String = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        mPreference = HealthCarePreference(this@RegisterActivity)

        validateRegistration()

        tv_goto_sign_in.setOnClickListener {
            startActivity(Intent(this@RegisterActivity, LoginActivity::class.java))
        }

        btn_sign_up.setOnClickListener {

            if (firstName.isNotEmpty() && lastName.isNotEmpty() && mobile.isNotEmpty() && password.isNotEmpty() && cnfPassword.isNotEmpty()) {
                if (password == cnfPassword) {

                    val requestPatientRegistration = RequestPatientRegistration(email, firstName, lastName, mobile, password)
                    doPatientRegistration(requestPatientRegistration)

                } else {
                    Helper.toastShort(this@RegisterActivity, "Password doesn't match!")
                }

            } else {
                Helper.toastShort(this@RegisterActivity, "Field's should not be empty!")

            }
            //startActivity(Intent(this@RegisterActivity, MainActivity::class.java))

        }

    }

    private fun doPatientRegistration(request: RequestPatientRegistration) {


    }

    private fun validateRegistration() {

        edt_first_name.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                firstName = edt_first_name.text.trim().toString()
            }

        })

        edt_last_name.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                lastName = edt_last_name.text.trim().toString()
            }

        })

        edt_email.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                email = edt_email.text.trim().toString()
            }

        })

        edt_phone_number.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                mobile = edt_phone_number.text.trim().toString()
            }

        })

        edt_password.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                password = edt_password.text.trim().toString()
            }

        })

        edt_cnf_password.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                cnfPassword = edt_cnf_password.text.trim().toString()
            }

        })


    }
}
