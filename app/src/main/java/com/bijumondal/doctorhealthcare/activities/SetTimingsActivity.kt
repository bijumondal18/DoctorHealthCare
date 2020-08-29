package com.bijumondal.doctorhealthcare.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bijumondal.doctorhealthcare.R
import com.bijumondal.doctorhealthcare.utils.HealthCarePreference
import kotlinx.android.synthetic.main.activity_set_holidays.*
import kotlinx.android.synthetic.main.activity_set_holidays.toolbar_set_holidays_activity
import kotlinx.android.synthetic.main.activity_set_timings.*

class SetTimingsActivity : AppCompatActivity() {

    companion object {
        private const val TAG: String = "SetTimingsActivity"
    }

    private lateinit var mPreference: HealthCarePreference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_set_timings)
        mPreference = HealthCarePreference(this@SetTimingsActivity)

        setupToolbar()


    }

    private fun setupToolbar() {
        setSupportActionBar(toolbar_set_timings_activity)
        val actionBar = supportActionBar
        actionBar!!.title = "Set Timings"
        actionBar.setDisplayHomeAsUpEnabled(true)
    }


    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}
