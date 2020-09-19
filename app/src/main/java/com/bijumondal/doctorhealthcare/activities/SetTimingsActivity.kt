package com.bijumondal.doctorhealthcare.activities

import android.app.TimePickerDialog
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.TimePicker
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bijumondal.doctorhealthcare.R
import com.bijumondal.doctorhealthcare.adapters.HolidaysListAdapter
import com.bijumondal.doctorhealthcare.adapters.TimeSlotsListAdapter
import com.bijumondal.doctorhealthcare.api.APIInterface
import com.bijumondal.doctorhealthcare.models.addDoctorHolidays.ResponseAddDoctorHolidays
import com.bijumondal.doctorhealthcare.models.addDoctorTimeslots.RequestAddDoctorTimeslots
import com.bijumondal.doctorhealthcare.models.addDoctorTimeslots.ResponseAddDoctorTimeslots
import com.bijumondal.doctorhealthcare.models.deleteDoctorTimeSlots.RequestDeleteDoctorTimeSlots
import com.bijumondal.doctorhealthcare.models.deleteDoctorTimeSlots.ResponseDeleteDoctorTimeSlots
import com.bijumondal.doctorhealthcare.models.doctorHolidaysList.RequestDoctorHolidaysList
import com.bijumondal.doctorhealthcare.models.doctorTimeSlotsList.Data
import com.bijumondal.doctorhealthcare.models.doctorTimeSlotsList.RequestDoctorTimeSlotsList
import com.bijumondal.doctorhealthcare.models.doctorTimeSlotsList.ResponseDoctorTimeSlotsList
import com.bijumondal.doctorhealthcare.utils.ClickListener
import com.bijumondal.doctorhealthcare.utils.HealthCarePreference
import com.bijumondal.doctorhealthcare.utils.Helper
import com.bijumondal.doctorhealthcare.utils.RecyclerTouchListener
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_set_holidays.*
import kotlinx.android.synthetic.main.activity_set_holidays.toolbar_set_holidays_activity
import kotlinx.android.synthetic.main.activity_set_timings.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*

class SetTimingsActivity : AppCompatActivity() {

    companion object {
        private const val TAG: String = "SetTimingsActivity"
    }

    private lateinit var mPreference: HealthCarePreference

    private lateinit var spinnerWeekday: Spinner
    var selectedWeekday: Int? = null
    val weekDaysList = arrayOf("Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday")
    var weekDay = ""
    var startTime = ""
    var endTime = ""

    private lateinit var mRecyclerView: RecyclerView
    private var timeSlotsList: ArrayList<Data> = ArrayList()
    private lateinit var timeslotsListAdapter: TimeSlotsListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_set_timings)
        mPreference = HealthCarePreference(this@SetTimingsActivity)
        mRecyclerView = findViewById(R.id.rv_timeslots_list)

        setupToolbar()

        setupWeekdaySpinner()

        edt_start_time.setOnClickListener {
            pickStartTimeFromTimePicker()
        }

        edt_end_time.setOnClickListener {
            pickEndTimeFromTimePicker()
        }

        btn_add_timeslots.setOnClickListener {
            val request = RequestAddDoctorTimeslots(mPreference.getUserId().toString(), endTime, mPreference.getHospitalId().toString(), startTime, weekDay)
            addTimeSlots(request)
        }

        val layoutManager = LinearLayoutManager(this@SetTimingsActivity, LinearLayoutManager.VERTICAL, false)
        mRecyclerView.layoutManager = layoutManager

        val requestTimeslotsList = RequestDoctorTimeSlotsList(mPreference.getUserId().toString(), "")
        fetchTimeSlotsList(requestTimeslotsList)


    }

    private fun fetchTimeSlotsList(request: RequestDoctorTimeSlotsList) {
        if (Helper.isConnectedToInternet(this@SetTimingsActivity)) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                Helper.showLoading(this)
            }
            val call: Call<ResponseDoctorTimeSlotsList> = APIInterface.create().getDoctorTimeSlotsList(request)
            Helper.showLog(TAG, " request :- ${Gson().toJson(request)}")
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
                            if (mData != null) {

                                timeSlotsList = mData as ArrayList<Data>

                                if (timeSlotsList.size > 0) {
                                    btn_add_timeslots.text = "Add More TimeSlots"
                                    tv_no_timeslots_found.visibility = View.GONE
                                    mRecyclerView.visibility = View.VISIBLE
                                } else {
                                    btn_add_timeslots.text = "Add TimeSlots"
                                    mRecyclerView.visibility = View.GONE
                                    tv_no_timeslots_found.visibility = View.VISIBLE
                                }

                                if (!mData.isEmpty()) {
                                    timeslotsListAdapter = TimeSlotsListAdapter(timeSlotsList, this@SetTimingsActivity)
                                    mRecyclerView.adapter = timeslotsListAdapter
                                    timeslotsListAdapter.notifyDataSetChanged()
                                    tv_no_timeslots_found.visibility = View.GONE
                                    mRecyclerView.visibility = View.VISIBLE


                                    mRecyclerView.addOnItemTouchListener(RecyclerTouchListener(this@SetTimingsActivity, mRecyclerView, object : ClickListener {
                                        override fun onClick(view: View?, position: Int) {
                                            val timeSlotsId = timeSlotsList[position].id
                                            val request = RequestDeleteDoctorTimeSlots(timeSlotsId)
                                            deleteTimeSlots(request)

                                        }

                                        override fun onLongClick(view: View?, position: Int) {
                                            //Helper.toastShort(this@BookingActivity, timeSlotsList[position].weekday)
                                        }

                                    }))

                                } else {
                                    mRecyclerView.visibility = View.GONE
                                    tv_no_timeslots_found.visibility = View.VISIBLE
                                }

                            }

                            if (response.body()!!.message != null) {
                                Helper.toastShort(this@SetTimingsActivity, response.body()!!.message)

                            } else if (response.body()!!.errors != null) {
                                Helper.toastShort(this@SetTimingsActivity, response.body()!!.errors)
                            }

                        } else {
                            if (response.body()!!.message != null) {
                                Helper.toastShort(this@SetTimingsActivity, response.body()!!.message)

                            } else if (response.body()!!.errors != null) {
                                Helper.toastShort(this@SetTimingsActivity, response.body()!!.errors)
                            }
                        }

                    } else {
                        Helper.toastNetworkError(this@SetTimingsActivity)
                    }
                }

                override fun onFailure(call: Call<ResponseDoctorTimeSlotsList>, t: Throwable) {
                    Helper.toastShort(this@SetTimingsActivity, "${t.message}")
                    Helper.hideLoading()
                }
            })
        }

    }

    private fun deleteTimeSlots(request: RequestDeleteDoctorTimeSlots) {
        if (Helper.isConnectedToInternet(this@SetTimingsActivity)) {
            /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                Helper.showLoading(this@SetTimingsActivity)
            }*/
            val call: Call<ResponseDeleteDoctorTimeSlots> = APIInterface.create().deleteDoctorTimeSlots(request)
            Helper.showLog("TAG", " request :- ${Gson().toJson(request)}")
            call.enqueue(object : Callback<ResponseDeleteDoctorTimeSlots> {
                override fun onResponse(
                    call: Call<ResponseDeleteDoctorTimeSlots>,
                    response: Response<ResponseDeleteDoctorTimeSlots>
                ) {
                    Helper.hideLoading()
                    if (response.isSuccessful) {
                        Helper.showLog("TAG", "Response : ${response.body()}")
                        if (response.body()!!.success) {
                            val mData = response.body()!!.data
                            if (mData != null) {

                                val requestTimeslotsList = RequestDoctorTimeSlotsList(mPreference.getUserId().toString(), "")
                                fetchTimeSlotsList(requestTimeslotsList)

                            }

                            if (response.body()!!.message != null) {
                                Helper.toastShort(this@SetTimingsActivity, response.body()!!.message)

                            } else if (response.body()!!.errors != null) {
                                Helper.toastShort(this@SetTimingsActivity, response.body()!!.errors)
                            }

                        } else {
                            if (response.body()!!.message != null) {
                                Helper.toastShort(this@SetTimingsActivity, response.body()!!.message)

                            } else if (response.body()!!.errors != null) {
                                Helper.toastShort(this@SetTimingsActivity, response.body()!!.errors)
                            }
                        }

                    } else {
                        Helper.toastNetworkError(this@SetTimingsActivity)
                    }
                }

                override fun onFailure(call: Call<ResponseDeleteDoctorTimeSlots>, t: Throwable) {
                    Helper.toastShort(this@SetTimingsActivity, "${t.message}")
                    Helper.hideLoading()
                }
            })
        }

    }


    private fun pickStartTimeFromTimePicker() {
        val cal = Calendar.getInstance()
        val timeSetListener = TimePickerDialog.OnTimeSetListener { timePicker, hour, minute ->
            cal.set(Calendar.HOUR_OF_DAY, hour)
            cal.set(Calendar.MINUTE, minute)
            startTime = SimpleDateFormat("hh:mm a").format(cal.time)
            edt_start_time.text = startTime
        }
        TimePickerDialog(this, timeSetListener, cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), false).show()

    }

    private fun pickEndTimeFromTimePicker() {
        val cal = Calendar.getInstance()
        val timeSetListener = TimePickerDialog.OnTimeSetListener { timePicker, hour, minute ->
            cal.set(Calendar.HOUR_OF_DAY, hour)
            cal.set(Calendar.MINUTE, minute)
            endTime = SimpleDateFormat("hh:mm a").format(cal.time)
            edt_end_time.text = endTime
        }
        TimePickerDialog(this, timeSetListener, cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), false).show()

    }

    private fun setupWeekdaySpinner() {
        spinnerWeekday = findViewById<Spinner>(R.id.spinner_weekday)
        val weekdayAdapter = ArrayAdapter(this@SetTimingsActivity, R.layout.support_simple_spinner_dropdown_item, weekDaysList)
        weekdayAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item)
        spinnerWeekday.adapter = weekdayAdapter

        spinnerWeekday.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("Not yet implemented")
            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                selectedWeekday = spinnerWeekday.selectedItemPosition
                Helper.weekDays(selectedWeekday!!)
                weekDay = Helper.weekDays(selectedWeekday!!) // weekday will pass on request parameters
            }

        }
    }

    private fun addTimeSlots(request: RequestAddDoctorTimeslots) {
        if (Helper.isConnectedToInternet(this@SetTimingsActivity)) {
            /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                Helper.showLoading(this)
            }*/
            val call: Call<ResponseAddDoctorTimeslots> = APIInterface.create().addDoctorTimeSlots(request)
            Helper.showLog(TAG, " request :- ${Gson().toJson(request)}")
            call.enqueue(object : Callback<ResponseAddDoctorTimeslots> {
                override fun onResponse(
                    call: Call<ResponseAddDoctorTimeslots>,
                    response: Response<ResponseAddDoctorTimeslots>
                ) {
                    Helper.hideLoading()
                    if (response.isSuccessful) {
                        Helper.showLog(TAG, "Response : ${response.body()}")
                        if (response.body()!!.success) {
                            val mData = response.body()!!.data
                            if (mData != null) {

                                val requestTimeslotsList = RequestDoctorTimeSlotsList(mPreference.getUserId().toString(), "")
                                fetchTimeSlotsList(requestTimeslotsList)

                                //Helper.toastShort(this@SetTimingsActivity, mData.message)
                            }

                            if (response.body()!!.data.message != null) {
                                Helper.toastShort(this@SetTimingsActivity, response.body()!!.data.message)

                            } else if (response.body()!!.errors != null) {
                                Helper.toastShort(this@SetTimingsActivity, response.body()!!.errors)
                            }

                        } else {
                            if (response.body()!!.data.message != null) {
                                Helper.toastShort(this@SetTimingsActivity, response.body()!!.data.message)

                            } else if (response.body()!!.errors != null) {
                                Helper.toastShort(this@SetTimingsActivity, response.body()!!.errors)
                            }
                        }

                    } else {
                        Helper.toastNetworkError(this@SetTimingsActivity)
                    }
                }

                override fun onFailure(call: Call<ResponseAddDoctorTimeslots>, t: Throwable) {
                    Helper.toastShort(this@SetTimingsActivity, "${t.message}")
                    Helper.hideLoading()
                }
            })
        }

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
