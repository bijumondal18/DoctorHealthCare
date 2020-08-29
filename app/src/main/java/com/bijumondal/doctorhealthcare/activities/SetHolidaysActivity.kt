package com.bijumondal.doctorhealthcare.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bijumondal.doctorhealthcare.R
import com.bijumondal.doctorhealthcare.utils.HealthCarePreference
import kotlinx.android.synthetic.main.activity_my_appointments.*
import kotlinx.android.synthetic.main.activity_my_appointments.toolbar_my_appointments_activity
import kotlinx.android.synthetic.main.activity_set_holidays.*

class SetHolidaysActivity : AppCompatActivity() {

    companion object {
        private const val TAG: String = "SetHolidaysActivity"
    }

    private lateinit var mPreference: HealthCarePreference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_set_holidays)
        mPreference = HealthCarePreference(this@SetHolidaysActivity)

        setupToolbar()


    }

    private fun setupToolbar() {
        setSupportActionBar(toolbar_set_holidays_activity)
        val actionBar = supportActionBar
        actionBar!!.title = "Set Holidays"
        actionBar.setDisplayHomeAsUpEnabled(true)
    }


    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}
