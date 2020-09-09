package com.bijumondal.doctorhealthcare.activities

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bijumondal.doctorhealthcare.R
import com.bijumondal.doctorhealthcare.adapters.DoctorAppointmentsListAdapter
import com.bijumondal.doctorhealthcare.adapters.DoctorPrescriptionsListAdapter
import com.bijumondal.doctorhealthcare.adapters.PatientPrescriptionsListAdapter
import com.bijumondal.doctorhealthcare.api.APIInterface
import com.bijumondal.doctorhealthcare.models.doctorPrescriptions.Data
import com.bijumondal.doctorhealthcare.models.doctorPrescriptions.RequestDoctorPrescriptionsList
import com.bijumondal.doctorhealthcare.models.doctorPrescriptions.ResponseDoctorPrescriptionsList
import com.bijumondal.doctorhealthcare.models.patientPrescriptions.RequestPatientPrescriptionsList
import com.bijumondal.doctorhealthcare.models.patientPrescriptions.ResponsePatientPrescriptionsList
import com.bijumondal.doctorhealthcare.utils.HealthCarePreference
import com.bijumondal.doctorhealthcare.utils.Helper
import kotlinx.android.synthetic.main.activity_my_prescriptions.*
import kotlinx.android.synthetic.main.activity_patient_profile.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MyPrescriptionsActivity : AppCompatActivity() {

    companion object {
        private const val TAG: String = "MyPrescriptionsActivity"
    }

    private lateinit var mPreference: HealthCarePreference
    private lateinit var layoutManager: LinearLayoutManager
    private lateinit var prescriptionsListRecyclerView: RecyclerView
    private lateinit var doctorPrescriptionAdapter: DoctorPrescriptionsListAdapter
    private lateinit var patientPrescriptionAdapter: PatientPrescriptionsListAdapter

    private var prescriptionsListForDoctor: ArrayList<Data> = ArrayList()
    private var prescriptionsListForPatient: ArrayList<com.bijumondal.doctorhealthcare.models.patientPrescriptions.Data> = ArrayList()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_prescriptions)
        mPreference = HealthCarePreference(this@MyPrescriptionsActivity)
        prescriptionsListRecyclerView = findViewById(R.id.rv_prescription_list)
        setupToolbar()

        if (mPreference.getUserType() == 1) {

            layoutManager = LinearLayoutManager(this@MyPrescriptionsActivity, LinearLayoutManager.VERTICAL, false)
            prescriptionsListRecyclerView.layoutManager = layoutManager
            val request1 = RequestPatientPrescriptionsList(mPreference.getUserId().toString())
            fetchPrescriptionsListForPatient(request1)

        } else if (mPreference.getUserType() == 2) {

            layoutManager = LinearLayoutManager(this@MyPrescriptionsActivity, LinearLayoutManager.VERTICAL, false)
            prescriptionsListRecyclerView.layoutManager = layoutManager
            val request = RequestDoctorPrescriptionsList(mPreference.getUserId().toString())
            fetchPrescriptionsListForDoctor(request)
        }


    }

    private fun fetchPrescriptionsListForDoctor(request: RequestDoctorPrescriptionsList) {
        if (Helper.isConnectedToInternet(this@MyPrescriptionsActivity)) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                Helper.showLoading(this@MyPrescriptionsActivity)
            }
            val call: Call<ResponseDoctorPrescriptionsList> = APIInterface.create().getDoctorPrescriptionsLis(request)
            call.enqueue(object : Callback<ResponseDoctorPrescriptionsList> {
                override fun onResponse(
                    call: Call<ResponseDoctorPrescriptionsList>,
                    response: Response<ResponseDoctorPrescriptionsList>
                ) {
                    Helper.hideLoading()
                    if (response.isSuccessful) {
                        Helper.showLog(TAG, "Response : ${response.body()}")
                        if (response.body()!!.success) {
                            val mData = response.body()!!.data
                            if (mData != null && !mData.isEmpty()) {
                                prescriptionsListForDoctor = mData as ArrayList<Data>
                                doctorPrescriptionAdapter = DoctorPrescriptionsListAdapter(prescriptionsListForDoctor, this@MyPrescriptionsActivity)
                                prescriptionsListRecyclerView.adapter = doctorPrescriptionAdapter
                                doctorPrescriptionAdapter.notifyDataSetChanged()
                                prescriptionsListRecyclerView.visibility = View.VISIBLE
                                tv_no_prescriptions.visibility = View.GONE

                            } else {
                                prescriptionsListRecyclerView.visibility = View.GONE
                                tv_no_prescriptions.visibility = View.VISIBLE
                            }

                            if (response.body()!!.message != null) {
                                Helper.toastShort(this@MyPrescriptionsActivity, response.body()!!.message)

                            } else if (response.body()!!.errors != null) {
                                Helper.toastShort(this@MyPrescriptionsActivity, response.body()!!.errors)
                            }

                        } else {
                            if (response.body()!!.message != null) {
                                Helper.toastShort(this@MyPrescriptionsActivity, response.body()!!.message)

                            } else if (response.body()!!.errors != null) {
                                Helper.toastShort(this@MyPrescriptionsActivity, response.body()!!.errors)
                            }
                        }

                    } else {
                        Helper.toastNetworkError(this@MyPrescriptionsActivity)
                        Helper.hideLoading()
                    }
                }

                override fun onFailure(call: Call<ResponseDoctorPrescriptionsList>, t: Throwable) {
                    Helper.toastShort(this@MyPrescriptionsActivity, "${t.message}")
                    Helper.hideLoading()
                }

            })
        }

    }

    private fun fetchPrescriptionsListForPatient(request1: RequestPatientPrescriptionsList) {
        if (Helper.isConnectedToInternet(this@MyPrescriptionsActivity)) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                Helper.showLoading(this@MyPrescriptionsActivity)
            }
            val call: Call<ResponsePatientPrescriptionsList> = APIInterface.create().getPatientPrescriptionsList(request1)
            call.enqueue(object : Callback<ResponsePatientPrescriptionsList> {
                override fun onResponse(
                    call: Call<ResponsePatientPrescriptionsList>,
                    response: Response<ResponsePatientPrescriptionsList>
                ) {
                    Helper.hideLoading()
                    if (response.isSuccessful) {
                        Helper.showLog(TAG, "Response : ${response.body()}")
                        if (response.body()!!.success) {
                            val mData = response.body()!!.data
                            if (mData != null && !mData.isEmpty()) {
                                prescriptionsListForPatient = mData as ArrayList<com.bijumondal.doctorhealthcare.models.patientPrescriptions.Data>
                                patientPrescriptionAdapter = PatientPrescriptionsListAdapter(prescriptionsListForPatient, this@MyPrescriptionsActivity)
                                prescriptionsListRecyclerView.adapter = patientPrescriptionAdapter
                                patientPrescriptionAdapter.notifyDataSetChanged()
                                prescriptionsListRecyclerView.visibility = View.VISIBLE
                                tv_no_prescriptions.visibility = View.GONE

                            } else {
                                prescriptionsListRecyclerView.visibility = View.GONE
                                tv_no_prescriptions.visibility = View.VISIBLE
                            }

                            if (response.body()!!.message != null) {
                                Helper.toastShort(this@MyPrescriptionsActivity, response.body()!!.message)

                            } else if (response.body()!!.errors != null) {
                                Helper.toastShort(this@MyPrescriptionsActivity, response.body()!!.errors)
                            }

                        } else {
                            if (response.body()!!.message != null) {
                                Helper.toastShort(this@MyPrescriptionsActivity, response.body()!!.message)

                            } else if (response.body()!!.errors != null) {
                                Helper.toastShort(this@MyPrescriptionsActivity, response.body()!!.errors)
                            }
                        }

                    } else {
                        Helper.toastNetworkError(this@MyPrescriptionsActivity)
                        Helper.hideLoading()
                    }
                }

                override fun onFailure(call: Call<ResponsePatientPrescriptionsList>, t: Throwable) {
                    Helper.toastShort(this@MyPrescriptionsActivity, "${t.message}")
                    Helper.hideLoading()
                }

            })
        }

    }


    private fun setupToolbar() {
        setSupportActionBar(toolbar_my_prescriptions_activity)
        val actionBar = supportActionBar
        actionBar!!.title = "My Prescriptions"
        actionBar.setDisplayHomeAsUpEnabled(true)
    }


    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}
