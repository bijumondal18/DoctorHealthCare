package com.bijumondal.doctorhealthcare.activities

import android.app.DatePickerDialog
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.annotation.RequiresApi
import com.bijumondal.doctorhealthcare.R
import com.bijumondal.doctorhealthcare.api.APIInterface
import com.bijumondal.doctorhealthcare.models.addDoctorHolidays.RequestAddDoctorHolidays
import com.bijumondal.doctorhealthcare.models.addDoctorHolidays.ResponseAddDoctorHolidays
import com.bijumondal.doctorhealthcare.utils.HealthCarePreference
import com.bijumondal.doctorhealthcare.utils.Helper
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_my_appointments.*
import kotlinx.android.synthetic.main.activity_my_appointments.toolbar_my_appointments_activity
import kotlinx.android.synthetic.main.activity_set_holidays.*
import kotlinx.android.synthetic.main.activity_update_patient_profile.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

class SetHolidaysActivity : AppCompatActivity() {

    companion object {
        private const val TAG: String = "SetHolidaysActivity"
    }

    private lateinit var mPreference: HealthCarePreference

    var dateOfHolidays: String = ""

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_set_holidays)
        mPreference = HealthCarePreference(this@SetHolidaysActivity)

        setupToolbar()

        val currentDate = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy")
        val formatted = currentDate.format(formatter)
        edt_holidays.hint = formatted.toString()

        edt_holidays.setOnClickListener {
            showDatePicker()
        }

        btn_add_holidays.setOnClickListener {
            val request = RequestAddDoctorHolidays(dateOfHolidays, mPreference.getUserId().toString(), mPreference.getHospitalId().toString())
            addHolidays(request)
        }


    }

    private fun addHolidays(request: RequestAddDoctorHolidays) {
        if (Helper.isConnectedToInternet(this@SetHolidaysActivity)) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                Helper.showLoading(this)
            }
            val call: Call<ResponseAddDoctorHolidays> = APIInterface.create().addDoctorHolidays(request)
            Helper.showLog(TAG, " request :- ${Gson().toJson(request)}")
            call.enqueue(object : Callback<ResponseAddDoctorHolidays> {
                override fun onResponse(
                    call: Call<ResponseAddDoctorHolidays>,
                    response: Response<ResponseAddDoctorHolidays>
                ) {
                    Helper.hideLoading()
                    if (response.isSuccessful) {
                        Helper.showLog(TAG, "Response : ${response.body()}")
                        if (response.body()!!.success) {
                            val mData = response.body()!!.data
                            if (mData != null) {

                                Helper.toastShort(this@SetHolidaysActivity, mData.message)
                            }

                            if (response.body()!!.data.message != null) {
                                Helper.toastShort(this@SetHolidaysActivity, response.body()!!.data.message)

                            } else if (response.body()!!.errors != null) {
                                Helper.toastShort(this@SetHolidaysActivity, response.body()!!.errors)
                            }

                        } else {
                            if (response.body()!!.data.message != null) {
                                Helper.toastShort(this@SetHolidaysActivity, response.body()!!.data.message)

                            } else if (response.body()!!.errors != null) {
                                Helper.toastShort(this@SetHolidaysActivity, response.body()!!.errors)
                            }
                        }

                    } else {
                        Helper.toastNetworkError(this@SetHolidaysActivity)
                    }
                }

                override fun onFailure(call: Call<ResponseAddDoctorHolidays>, t: Throwable) {
                    Helper.toastShort(this@SetHolidaysActivity, "${t.message}")
                    Helper.hideLoading()
                }
            })
        }

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

    private fun showDatePicker() {
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)

        val dpd = DatePickerDialog(this, DatePickerDialog.OnDateSetListener { view, dayOfMonth, monthOfYear, year ->

            // Display Selected date in textbox
            Helper.showLog(TAG, "$dayOfMonth-$monthOfYear-$year")
            dateOfHolidays = "$dayOfMonth-${monthOfYear + 1}-$year"
            edt_holidays.text = dateOfHolidays

        }, day, month, year)
        dpd.datePicker.minDate = System.currentTimeMillis() - 1000 // chose only after date from current data
        dpd.show()
    }
}
