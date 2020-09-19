package com.bijumondal.doctorhealthcare.activities

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.View
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bijumondal.doctorhealthcare.R
import com.bijumondal.doctorhealthcare.adapters.DoctorTimeSlotsAdapter
import com.bijumondal.doctorhealthcare.api.APIInterface
import com.bijumondal.doctorhealthcare.models.bookAppointment.RequestBookAppointment
import com.bijumondal.doctorhealthcare.models.bookAppointment.ResponseBookAppointment
import com.bijumondal.doctorhealthcare.models.doctorTimeSlotsList.Data
import com.bijumondal.doctorhealthcare.models.doctorTimeSlotsList.RequestDoctorTimeSlotsList
import com.bijumondal.doctorhealthcare.models.doctorTimeSlotsList.ResponseDoctorTimeSlotsList
import com.bijumondal.doctorhealthcare.utils.*
import kotlinx.android.synthetic.main.activity_booking.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.collections.ArrayList

class BookingActivity : AppCompatActivity() {

    companion object {
        private const val TAG: String = "BookingActivity"
    }

    private lateinit var mPreference: HealthCarePreference

    private lateinit var mRecyclerView: RecyclerView
    private lateinit var timeSlotsListAdapter: DoctorTimeSlotsAdapter
    private var timeSlotsList: ArrayList<Data> = ArrayList()

    var dateOfBooking: String = ""
    var dateInString: String = ""
    var doctorId = ""
    var hospitalId = ""
    var patientId = ""
    var name: String = ""
    var phone: String = ""
    var gender: String = ""
    var bloodGroup: String = ""
    var timeslot: String = ""
    var appointmentForName: String = ""

    var dayOfWeek = ""

    var isSelected: Boolean = false

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
        val c = Calendar.getInstance()
        val date = c.time
        edt_booking_date.hint = "$formatted (${SimpleDateFormat("EEEE").format(date.time)})"

        edt_booking_date.setOnClickListener {
            showDatePicker()
        }

        val layoutManager = LinearLayoutManager(this@BookingActivity, LinearLayoutManager.HORIZONTAL, false)
        mRecyclerView.layoutManager = layoutManager
        val request = RequestDoctorTimeSlotsList(doctorId, dayOfWeek)
        fetchTimeSlots(request)

        if (mPreference.getBloodGroup() != null) {
            bloodGroup = mPreference.getBloodGroup().toString()
        }

        if (mPreference.getGender() != null) {
            gender = mPreference.getGender().toString()
        }

        if (mPreference.getUserId() != null) {
            patientId = mPreference.getUserId().toString()
        }

        edt_appointment_for_name.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun afterTextChanged(p0: Editable?) {
                appointmentForName = edt_appointment_for_name.text.trim().toString()
            }

        })


        btn_confirm_and_pay.setOnClickListener {
            if (dateOfBooking != null && !TextUtils.isEmpty(dateOfBooking)) {
                if (gender != null && bloodGroup != null) {
                    if (timeslot != null && !TextUtils.isEmpty(timeslot)) {
                        if (!TextUtils.isEmpty(appointmentForName)) {

                            val request = RequestBookAppointment("", dateInString, bloodGroup, doctorId, appointmentForName, patientId, gender, timeslot, hospitalId)
                            bookAppointment(request)

                        } else {
                            Helper.toastLong(this@BookingActivity, "Please enter patient name !")
                        }

                    } else {
                        Helper.toastLong(this@BookingActivity, "Please choose a time slot !")
                    }

                } else {
                    Helper.toastLong(this@BookingActivity, "Please update your profile first !")
                }

            } else {
                Helper.toastLong(this@BookingActivity, "Please choose a date !")
            }
        }

    }

    private fun bookAppointment(request: RequestBookAppointment) {
        if (Helper.isConnectedToInternet(this@BookingActivity)) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                Helper.showLoading(this)
            }
            val call: Call<ResponseBookAppointment> = APIInterface.create().bookAppointment(request)
            call.enqueue(object : Callback<ResponseBookAppointment> {
                override fun onResponse(
                    call: Call<ResponseBookAppointment>,
                    response: Response<ResponseBookAppointment>
                ) {
                    Helper.hideLoading()
                    if (response.isSuccessful) {
                        Helper.showLog(TAG, "Response : ${response.body()}")
                        if (response.body()!!.success) {
                            val mData = response.body()!!.data
                            if (mData != null) {

                                if (mData.name != null) {
                                    val appointmentForName = mData.name
                                }

                                startActivity(Intent(this@BookingActivity, BookingSuccessfulActivity::class.java))
                                finish()

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

                override fun onFailure(call: Call<ResponseBookAppointment>, t: Throwable) {
                    Helper.toastShort(this@BookingActivity, "${t.message}")
                    Helper.hideLoading()
                }
            })
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

                                tv_no_time_slots.visibility = View.GONE
                                mRecyclerView.visibility = View.VISIBLE

                                mRecyclerView.addOnItemTouchListener(RecyclerTouchListener(this@BookingActivity, mRecyclerView, object : ClickListener {
                                    override fun onClick(view: View?, position: Int) {
                                        timeslot = "${timeSlotsList[position].starttime} - ${timeSlotsList[position].endtime}"
                                        //Helper.toastShort(this@BookingActivity, timeslot)

                                    }

                                    override fun onLongClick(view: View?, position: Int) {
                                        //Helper.toastShort(this@BookingActivity, timeSlotsList[position].weekday)
                                    }

                                }))

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

    @RequiresApi(Build.VERSION_CODES.M)
    private fun setupToolbar() {
        setSupportActionBar(toolbar_booking_activity)
        val actionBar = supportActionBar
        actionBar!!.title = "Book appointment"
        actionBar!!.setDisplayHomeAsUpEnabled(true)

        if (intent.hasExtra("doctorName") != null) {
            tv_doc_name_booking.text = intent.getStringExtra("doctorName")
        }
        if (intent.hasExtra("doctorPhoto") != null) {
            ImageLoader.loadImageFromUrl(iv_doc_image_booking, intent.getStringExtra("doctorPhoto")!!, R.drawable.ic_avatar)
        } else {
            iv_doc_image_booking.setImageResource(R.drawable.ic_avatar)
        }

        if (intent.hasExtra("hospitalId") != null) {
            hospitalId = "${intent.getStringExtra("hospitalId")}"
        }
        if (intent.hasExtra("doctorAddress") != null) {
            tv_doc_address_booking.text = intent.getStringExtra("doctorAddress")
        }
        if (intent.hasExtra("doctorDept") != null) {
            tv_doc_department_booking.text = intent.getStringExtra("doctorDept")
        }
        if (intent.hasExtra("hospitalPhone") != null) {
            tv_hospital_phone_booking.text = "${intent.getStringExtra("hospitalPhone")}"
        }
        if (intent.hasExtra("doctorVisitAmount") != null) {
            tv_visit_amount_booking.text = "Consultation fees - ${intent.getStringExtra("doctorVisitAmount")}"
            btn_confirm_and_pay.text = "Confirm Appointment ${intent.getStringExtra("doctorVisitAmount")}" //todo "confirm & pay" ->
        }
        if (intent.hasExtra("hospitalNameAndAddress") != null) {
            tv_hospital_name_and_address_booking.text = "${intent.getStringExtra("hospitalNameAndAddress")}"
        }

    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun showDatePicker() {
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)
        val dateFormat = SimpleDateFormat("dd-MM-yyyy")

        val dpd = DatePickerDialog(this, { view, mYear, mMonth, mDay ->
            Helper.showLog(TAG, "$mDay-$mMonth-$mYear")
            dateInString = "$mDay-${mMonth + 1}-$mYear"
            val date: Date = dateFormat.parse(dateInString)
            dayOfWeek = SimpleDateFormat("EEEE", Locale.ENGLISH).format(date)
            dateOfBooking = "${dateInString} ($dayOfWeek)"
            edt_booking_date.text = dateOfBooking

            val request = RequestDoctorTimeSlotsList(doctorId, dayOfWeek)
            fetchTimeSlots(request)


        }, day, month, year)
        dpd.datePicker.minDate = System.currentTimeMillis() - 1000 // chose only after date from current data
        dpd.show()
    }


    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

}
