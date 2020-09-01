package com.bijumondal.doctorhealthcare.activities

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bijumondal.doctorhealthcare.R
import com.bijumondal.doctorhealthcare.adapters.DoctorDeptAdapter
import com.bijumondal.doctorhealthcare.adapters.DoctorTimeSlotsAdapter
import com.bijumondal.doctorhealthcare.api.APIInterface
import com.bijumondal.doctorhealthcare.models.doctorDepartment.ResponseDoctorDepartment
import com.bijumondal.doctorhealthcare.models.doctorTimeSlotsList.Data
import com.bijumondal.doctorhealthcare.models.doctorTimeSlotsList.RequestDoctorTimeSlotsList
import com.bijumondal.doctorhealthcare.models.doctorTimeSlotsList.ResponseDoctorTimeSlotsList
import com.bijumondal.doctorhealthcare.utils.HealthCarePreference
import com.bijumondal.doctorhealthcare.utils.Helper
import com.bijumondal.doctorhealthcare.utils.ImageLoader
import kotlinx.android.synthetic.main.activity_booking.*
import kotlinx.android.synthetic.main.activity_doctor_list.*
import kotlinx.android.synthetic.main.activity_set_holidays.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class BookingActivity : AppCompatActivity() {

    companion object {
        private const val TAG: String = "BookingActivity"
    }

    private lateinit var mPreference: HealthCarePreference

    private lateinit var mRecyclerView: RecyclerView
    private lateinit var timeSlotsListAdapter: DoctorTimeSlotsAdapter
    private var timeSlotsList: ArrayList<Data> = ArrayList()

    var doctorId = ""

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_booking)
        mPreference = HealthCarePreference(this@BookingActivity)
        mRecyclerView = findViewById(R.id.rv_timeslots)
        setupToolbar()

        if (intent.hasExtra("doctorId") != null) {
            doctorId = intent.getStringExtra("doctorId").toString()
        }

        val currentDate = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy")
        val formatted = currentDate.format(formatter)
        edt_booking_date.hint = formatted.toString()

        val layoutManager = LinearLayoutManager(this@BookingActivity, LinearLayoutManager.HORIZONTAL, false)
        mRecyclerView.layoutManager = layoutManager
        val request = RequestDoctorTimeSlotsList(doctorId)
        fetchTimeSlots(request)



        btn_confirm_and_pay.setOnClickListener {
            startActivity(Intent(this@BookingActivity, BookingSuccessfulActivity::class.java))
        }

    }

    private fun fetchTimeSlots(request: RequestDoctorTimeSlotsList) {
        if (Helper.isConnectedToInternet(this@BookingActivity)) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                Helper.showLoading(this)
            }
            val call: Call<ResponseDoctorTimeSlotsList> = APIInterface.create().getDoctorTimeSlotsList(request)
            call.enqueue(object : Callback<ResponseDoctorTimeSlotsList> {
                override fun onResponse(
                    call: Call<ResponseDoctorTimeSlotsList>,
                    response: Response<ResponseDoctorTimeSlotsList>
                ) {
                    Helper.hideLoading()
                    if (response.isSuccessful) {
                        Helper.showLog(TAG, "Response : ${response.body()}")
                        if (response.body()!!.success) {
                            val mData = response.body()!!.data
                            if (mData != null && !mData.isEmpty()) {
                                timeSlotsList = mData as ArrayList<Data>
                                timeSlotsListAdapter = DoctorTimeSlotsAdapter(timeSlotsList, this@BookingActivity)
                                mRecyclerView.adapter = timeSlotsListAdapter
                                timeSlotsListAdapter.notifyDataSetChanged()

                            } else {
                                tv_no_time_slots.visibility = View.VISIBLE
                                mRecyclerView.visibility = View.GONE
                            }

                            if (response.body()!!.message != null) {
                                Helper.toastShort(this@BookingActivity, response.body()!!.message)

                            } else if (response.body()!!.errors != null) {
                                Helper.toastShort(this@BookingActivity, response.body()!!.errors)
                            }

                        } else {
                            if (response.body()!!.message != null) {
                                Helper.toastShort(this@BookingActivity, response.body()!!.message)

                            } else if (response.body()!!.errors != null) {
                                Helper.toastShort(this@BookingActivity, response.body()!!.errors)
                            }
                        }

                    } else {
                        Helper.toastNetworkError(this@BookingActivity)
                    }
                }

                override fun onFailure(call: Call<ResponseDoctorTimeSlotsList>, t: Throwable) {
                    Helper.toastShort(this@BookingActivity, "${t.message}")
                    Helper.hideLoading()
                }
            })
        }

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

        if (mPreference.getName() != null) {
            edt_appointment_for_name.setText("${mPreference.getName()}")
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
