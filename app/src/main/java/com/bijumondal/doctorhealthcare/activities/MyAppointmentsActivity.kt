package com.bijumondal.doctorhealthcare.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bijumondal.doctorhealthcare.R
import com.bijumondal.doctorhealthcare.utils.HealthCarePreference
import kotlinx.android.synthetic.main.activity_my_appointments.*

class MyAppointmentsActivity : AppCompatActivity() {

    companion object {
        private const val TAG: String = "MyAppointmentsActivity"
    }

    private lateinit var mPreference: HealthCarePreference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_appointments)
        mPreference = HealthCarePreference(this@MyAppointmentsActivity)

        setupToolbar()


    }

    private fun setupToolbar() {
        setSupportActionBar(toolbar_my_appointments_activity)
        val actionBar = supportActionBar
        actionBar!!.title = "My Appointments"
        actionBar.setDisplayHomeAsUpEnabled(true)
    }


    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}
