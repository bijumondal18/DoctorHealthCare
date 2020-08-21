package com.bijumondal.doctorhealthcare.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bijumondal.doctorhealthcare.R
import com.bijumondal.doctorhealthcare.utils.HealthCarePreference
import kotlinx.android.synthetic.main.activity_my_prescriptions.*
import kotlinx.android.synthetic.main.activity_patient_profile.*

class MyPrescriptionsActivity : AppCompatActivity() {

    companion object {
        private const val TAG: String = "MyPrescriptionsActivity"
    }

    private lateinit var mPreference: HealthCarePreference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_prescriptions)
        mPreference = HealthCarePreference(this@MyPrescriptionsActivity)

        setupToolbar()




    }

    private fun setupToolbar() {
        setSupportActionBar(toolbar_my_prescriptions_activity)
        val actionBar = supportActionBar
        actionBar!!.title = "My Prescriptions"
        actionBar.setDisplayHomeAsUpEnabled(true)
    }


    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}
