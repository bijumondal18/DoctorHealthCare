package com.bijumondal.doctorhealthcare.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.bijumondal.doctorhealthcare.R
import com.bijumondal.doctorhealthcare.utils.HealthCarePreference
import com.bijumondal.doctorhealthcare.utils.ImageLoader
import kotlinx.android.synthetic.main.activity_add_prescriptions.*
import kotlinx.android.synthetic.main.activity_appointment_details.*

class AppointmentDetailsActivity : AppCompatActivity() {

    companion object {
        private const val TAG: String = "AddPrescriptionsActivity"
    }

    private lateinit var mPreference: HealthCarePreference

    private var patientId = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_appointment_details)
        mPreference = HealthCarePreference(this@AppointmentDetailsActivity)

        initViews()
        setupToolbar()

        if (mPreference.getUserType() == 2) {
            btn_add_prescription.visibility = View.VISIBLE

        } else if (mPreference.getUserType() == 1) {
            btn_add_prescription.visibility = View.GONE
        }

        btn_add_prescription.setOnClickListener {
            startActivity(
                Intent(this@AppointmentDetailsActivity, AddPrescriptionsActivity::class.java)
                    .putExtra("patientId", patientId)
            )
        }


    }

    private fun initViews() {

        if (intent.hasExtra("doctorName") != null) {
            tv_doctor_name.text = intent.getStringExtra("doctorName")
        }

        if (intent.hasExtra("doctorImage") != null) {
            ImageLoader.loadCircleImageFromUrl(iv_doctor_image, intent.getStringExtra("doctorImage").toString(), R.drawable.ic_avatar)
        }

        if (intent.hasExtra("appointmentDate") != null && intent.hasExtra("appointmentTime") != null) {
            tv_appointment_date_and_time.text = "${intent.getStringExtra("appointmentDate")}, ${intent.getStringExtra("appointmentTime")}"
        }

        if (intent.hasExtra("hospitalName") != null) {
            tv_appointment_hospital_name.text = intent.getStringExtra("hospitalName")
        }

        if (intent.hasExtra("hospitalAddress") != null) {
            tv_appointment_hospital_address.text = intent.getStringExtra("hospitalAddress")
        }

        if (intent.hasExtra("hospitalNumber") != null) {
            tv_appointment_hospital_number.text = intent.getStringExtra("hospitalNumber")
        }

        if (intent.hasExtra("appointmentForName") != null) {
            tv_appointment_for_name.text = intent.getStringExtra("appointmentForName")
        }

        if (intent.hasExtra("patientId") != null) {
            patientId = intent.getStringExtra("patientId").toString()
        }


    }

    private fun setupToolbar() {
        setSupportActionBar(toolbar_appointment_details_activity)
        val actionBar = supportActionBar
        actionBar!!.title = "Appointment Details"
        actionBar!!.setDisplayHomeAsUpEnabled(true)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

}
