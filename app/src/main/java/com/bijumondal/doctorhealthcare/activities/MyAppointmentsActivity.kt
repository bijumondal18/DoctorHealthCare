package com.bijumondal.doctorhealthcare.activities

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bijumondal.doctorhealthcare.R
import com.bijumondal.doctorhealthcare.adapters.AllDoctorsListAdapter
import com.bijumondal.doctorhealthcare.adapters.DoctorAppointmentsListAdapter
import com.bijumondal.doctorhealthcare.adapters.PatientAppointmentsListAdapter
import com.bijumondal.doctorhealthcare.api.APIInterface
import com.bijumondal.doctorhealthcare.models.allDoctorsList.ResponseAllDoctorsList
import com.bijumondal.doctorhealthcare.models.appointmentListForPatient.RequestAppointmentListForPatient
import com.bijumondal.doctorhealthcare.models.appointmentListForPatient.ResponseAppointmentListForPatient
import com.bijumondal.doctorhealthcare.models.appointmentsListForDoctor.Data
import com.bijumondal.doctorhealthcare.models.appointmentsListForDoctor.RequestAppointmentsListForDoctor
import com.bijumondal.doctorhealthcare.models.appointmentsListForDoctor.ResponseAppointmentsListForDoctor
import com.bijumondal.doctorhealthcare.utils.HealthCarePreference
import com.bijumondal.doctorhealthcare.utils.Helper
import kotlinx.android.synthetic.main.activity_my_appointments.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MyAppointmentsActivity : AppCompatActivity() {

    companion object {
        private const val TAG: String = "MyAppointmentsActivity"
    }

    private lateinit var mPreference: HealthCarePreference

    private lateinit var appointmentsListRecyclerView: RecyclerView
    private lateinit var drAdapter: DoctorAppointmentsListAdapter

    private lateinit var patientAdapter: PatientAppointmentsListAdapter
    private lateinit var layoutManager: LinearLayoutManager
    private var appointmentsListForDoctor: ArrayList<Data> = ArrayList()
    private var appointmentsListForPatient: ArrayList<com.bijumondal.doctorhealthcare.models.appointmentListForPatient.Data> = ArrayList()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_appointments)
        mPreference = HealthCarePreference(this@MyAppointmentsActivity)
        appointmentsListRecyclerView = findViewById(R.id.rv_appointments_list)

        setupToolbar()

        if (mPreference.getUserType() == 1) {

            layoutManager = LinearLayoutManager(this@MyAppointmentsActivity, LinearLayoutManager.VERTICAL, false)
            appointmentsListRecyclerView.layoutManager = layoutManager
            val request1 = RequestAppointmentListForPatient(mPreference.getUserId().toString())
            fetchAppointmentsListForPatient(request1)

        } else if (mPreference.getUserType() == 2) {

            layoutManager = LinearLayoutManager(this@MyAppointmentsActivity, LinearLayoutManager.VERTICAL, false)
            appointmentsListRecyclerView.layoutManager = layoutManager
            val request = RequestAppointmentsListForDoctor(mPreference.getUserId().toString())
            fetchAppointmentsListForDoctor(request)
        }


    }

    private fun fetchAppointmentsListForPatient(request1: RequestAppointmentListForPatient) {
        if (Helper.isConnectedToInternet(this@MyAppointmentsActivity)) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                Helper.showLoading(this@MyAppointmentsActivity)
            }
            val call: Call<ResponseAppointmentListForPatient> = APIInterface.create().getPatientAppointmentsList(request1)
            call.enqueue(object : Callback<ResponseAppointmentListForPatient> {
                override fun onResponse(
                    call: Call<ResponseAppointmentListForPatient>,
                    response: Response<ResponseAppointmentListForPatient>
                ) {
                    Helper.hideLoading()
                    if (response.isSuccessful) {
                        Helper.showLog(TAG, "Response : ${response.body()}")
                        if (response.body()!!.success) {
                            val mData = response.body()!!.data
                            if (mData != null && !mData.isEmpty()) {
                                appointmentsListForPatient = mData as ArrayList<com.bijumondal.doctorhealthcare.models.appointmentListForPatient.Data>
                                patientAdapter = PatientAppointmentsListAdapter(appointmentsListForPatient, this@MyAppointmentsActivity)
                                appointmentsListRecyclerView.adapter = patientAdapter
                                patientAdapter.notifyDataSetChanged()
                                appointmentsListRecyclerView.visibility = View.VISIBLE
                                tv_no_appointments_found.visibility = View.GONE

                            } else {
                                appointmentsListRecyclerView.visibility = View.GONE
                                tv_no_appointments_found.visibility = View.VISIBLE
                            }

                            if (response.body()!!.message != null) {
                                Helper.toastShort(this@MyAppointmentsActivity, response.body()!!.message)

                            } else if (response.body()!!.errors != null) {
                                Helper.toastShort(this@MyAppointmentsActivity, response.body()!!.errors)
                            }

                        } else {
                            if (response.body()!!.message != null) {
                                Helper.toastShort(this@MyAppointmentsActivity, response.body()!!.message)

                            } else if (response.body()!!.errors != null) {
                                Helper.toastShort(this@MyAppointmentsActivity, response.body()!!.errors)
                            }
                        }

                    } else {
                        Helper.toastNetworkError(this@MyAppointmentsActivity)
                        Helper.hideLoading()
                    }
                }

                override fun onFailure(call: Call<ResponseAppointmentListForPatient>, t: Throwable) {
                    Helper.toastShort(this@MyAppointmentsActivity, "${t.message}")
                    Helper.hideLoading()
                }

            })
        }
    }

    private fun fetchAppointmentsListForDoctor(request: RequestAppointmentsListForDoctor) {
        if (Helper.isConnectedToInternet(this@MyAppointmentsActivity)) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                Helper.showLoading(this@MyAppointmentsActivity)
            }
            val call: Call<ResponseAppointmentsListForDoctor> = APIInterface.create().getDoctorAppointmentsList(request)
            call.enqueue(object : Callback<ResponseAppointmentsListForDoctor> {
                override fun onResponse(
                    call: Call<ResponseAppointmentsListForDoctor>,
                    response: Response<ResponseAppointmentsListForDoctor>
                ) {
                    Helper.hideLoading()
                    if (response.isSuccessful) {
                        Helper.showLog(TAG, "Response : ${response.body()}")
                        if (response.body()!!.success) {
                            val mData = response.body()!!.data
                            if (mData != null && !mData.isEmpty()) {
                                appointmentsListForDoctor = mData as ArrayList<Data>
                                drAdapter = DoctorAppointmentsListAdapter(appointmentsListForDoctor, this@MyAppointmentsActivity)
                                appointmentsListRecyclerView.adapter = drAdapter
                                drAdapter.notifyDataSetChanged()
                                appointmentsListRecyclerView.visibility = View.VISIBLE
                                tv_no_appointments_found.visibility = View.GONE

                            } else {
                                appointmentsListRecyclerView.visibility = View.GONE
                                tv_no_appointments_found.visibility = View.VISIBLE
                            }

                            if (response.body()!!.message != null) {
                                Helper.toastShort(this@MyAppointmentsActivity, response.body()!!.message)

                            } else if (response.body()!!.errors != null) {
                                Helper.toastShort(this@MyAppointmentsActivity, response.body()!!.errors)
                            }

                        } else {
                            if (response.body()!!.message != null) {
                                Helper.toastShort(this@MyAppointmentsActivity, response.body()!!.message)

                            } else if (response.body()!!.errors != null) {
                                Helper.toastShort(this@MyAppointmentsActivity, response.body()!!.errors)
                            }
                        }

                    } else {
                        Helper.toastNetworkError(this@MyAppointmentsActivity)
                        Helper.hideLoading()
                    }
                }

                override fun onFailure(call: Call<ResponseAppointmentsListForDoctor>, t: Throwable) {
                    Helper.toastShort(this@MyAppointmentsActivity, "${t.message}")
                    Helper.hideLoading()
                }

            })
        }

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
