package com.bijumondal.doctorhealthcare.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bijumondal.doctorhealthcare.R
import com.bijumondal.doctorhealthcare.utils.HealthCarePreference
import com.bijumondal.doctorhealthcare.utils.ImageLoader
import kotlinx.android.synthetic.main.activity_booking.*
import kotlinx.android.synthetic.main.activity_doctor_list.*

class BookingActivity : AppCompatActivity() {

    companion object {
        private const val TAG: String = "BookingActivity"
    }

    private lateinit var mPreference: HealthCarePreference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_booking)
        mPreference = HealthCarePreference(this@BookingActivity)

        setupToolbar()


    }

    private fun setupToolbar() {
        setSupportActionBar(toolbar_booking_activity)
        val actionBar = supportActionBar
        actionBar!!.title = "Book appointment"
        actionBar!!.setDisplayHomeAsUpEnabled(true)

        if (intent.hasExtra("doctorName") != null) {
            tv_doc_name_booking.text = intent.getStringExtra("doctorName")
        }
        if (intent.hasExtra("doctorPhoto") != null) {
            // ImageLoader.loadImageFromUrl(iv_doc_image_booking,intent.getStringExtra("doctorPhoto")!!,R.color.colorTransparent)
        }
        if (intent.hasExtra("doctorPhone") != null) {

        }
        if (intent.hasExtra("doctorAddress") != null) {
            tv_doc_address_booking.text = intent.getStringExtra("doctorAddress")
        }
        if (intent.hasExtra("doctorDept") != null) {
            tv_doc_department_booking.text = intent.getStringExtra("doctorDept")
        }
        if (intent.hasExtra("hospitalPhone") != null) {
            tv_hospital_phone_booking.text = "Hospital number - ${intent.getStringExtra("hospitalPhone")}"
        }
        if (intent.hasExtra("doctorVisitAmount") != null) {
            tv_visit_amount_booking.text = "Consultation fees - ${intent.getStringExtra("doctorVisitAmount")}"
            btn_confirm_and_pay.text = "Confirm & Pay ${intent.getStringExtra("doctorVisitAmount")}"
        }
        if (intent.hasExtra("hospitalNameAndAddress") != null) {
            tv_hospital_name_and_address_booking.text = "Hospital - ${intent.getStringExtra("hospitalNameAndAddress")}"
        }

        if (mPreference.getFirstName() != null && mPreference.getLastName() != null) {
            edt_appointment_for_name.setText("${mPreference.getFirstName()} ${mPreference.getLastName()}")
        }

        if (mPreference.getNumber() != null) {
            edt_appointment_for_number.setText("${mPreference.getNumber()}")
        }

    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }


}
