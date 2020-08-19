package com.bijumondal.doctorhealthcare.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.bijumondal.doctorhealthcare.R
import com.bijumondal.doctorhealthcare.utils.HealthCarePreference
import kotlinx.android.synthetic.main.activity_create_profile.*

class CreateProfileActivity : AppCompatActivity() {

    companion object {
        private const val TAG = "CreateProfileActivity"
    }

    private lateinit var mPreference: HealthCarePreference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_profile)
        mPreference = HealthCarePreference(this@CreateProfileActivity)

        if (mPreference.getUserType() == 2) {
            ll_dr_dept.visibility = View.VISIBLE
        }
        if (mPreference.getUserType() == 1) {
            ll_age_and_blood_grp.visibility = View.VISIBLE

        }

        if (mPreference.getFirstName() != null && mPreference.getLastName() != null) {
            edt_fullname_create_profile.setText("${mPreference.getFirstName()} ${mPreference.getLastName()}")
        }

        if (mPreference.getEmail() != null) {
            edt_email_create_profile.setText(mPreference.getEmail())
        }

        if (mPreference.getNumber() != null) {
            edt_mobile_create_profile.setText(mPreference.getNumber())
        }

    }
}
