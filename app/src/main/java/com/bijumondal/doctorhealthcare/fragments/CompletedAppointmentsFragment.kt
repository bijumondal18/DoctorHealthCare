package com.bijumondal.doctorhealthcare.fragments

import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import com.bijumondal.doctorhealthcare.R
import com.bijumondal.doctorhealthcare.activities.MyAppointmentsActivity
import com.bijumondal.doctorhealthcare.adapters.DoctorAppointmentsListAdapter
import com.bijumondal.doctorhealthcare.adapters.PatientAppointmentsListAdapter
import com.bijumondal.doctorhealthcare.api.APIInterface
import com.bijumondal.doctorhealthcare.models.appointmentListForPatient.RequestAppointmentListForPatient
import com.bijumondal.doctorhealthcare.models.appointmentListForPatient.ResponseAppointmentListForPatient
import com.bijumondal.doctorhealthcare.models.appointmentsListForDoctor.Data
import com.bijumondal.doctorhealthcare.models.appointmentsListForDoctor.RequestAppointmentsListForDoctor
import com.bijumondal.doctorhealthcare.models.appointmentsListForDoctor.ResponseAppointmentsListForDoctor
import com.bijumondal.doctorhealthcare.utils.HealthCarePreference
import com.bijumondal.doctorhealthcare.utils.Helper
import kotlinx.android.synthetic.main.fragment_completed_appointments.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class CompletedAppointmentsFragment : Fragment() {

    companion object {
        fun newInstance(): CompletedAppointmentsFragment = CompletedAppointmentsFragment()
        private const val TAG: String = "CompletedAppointmentsFragment"
    }

    private lateinit var mPreference: HealthCarePreference

    private lateinit var appointmentsListRecyclerView: RecyclerView
    private lateinit var drAdapter: DoctorAppointmentsListAdapter

    private lateinit var patientAdapter: PatientAppointmentsListAdapter
    private lateinit var layoutManager: LinearLayoutManager
    private var appointmentsListForDoctor: ArrayList<Data> = ArrayList()
    private var appointmentsListForPatient: ArrayList<com.bijumondal.doctorhealthcare.models.appointmentListForPatient.Data> = ArrayList()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_completed_appointments, container, false)
        mPreference = HealthCarePreference(context!!)
        appointmentsListRecyclerView = view.findViewById(R.id.rv_completed_appointments_list)

        if (mPreference.getUserType() == 1) {

            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            appointmentsListRecyclerView.layoutManager = layoutManager
            val request1 = RequestAppointmentListForPatient(mPreference.getUserId().toString(), 2)
            fetchAppointmentsListForPatient(request1)

        } else if (mPreference.getUserType() == 2) {

            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            appointmentsListRecyclerView.layoutManager = layoutManager
            val request = RequestAppointmentsListForDoctor(mPreference.getUserId().toString(), 2)
            fetchAppointmentsListForDoctor(request)
        }

        return view

    }

    private fun fetchAppointmentsListForPatient(request1: RequestAppointmentListForPatient) {
        if (Helper.isConnectedToInternet(context!!)) {
            /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                Helper.showLoading(context!!)
            }*/
            val call: Call<ResponseAppointmentListForPatient> = APIInterface.create().getPatientAppointmentsList(request1)
            call.enqueue(object : Callback<ResponseAppointmentListForPatient> {
                override fun onResponse(
                    call: Call<ResponseAppointmentListForPatient>,
                    response: Response<ResponseAppointmentListForPatient>
                ) {
                    //Helper.hideLoading()
                    if (response.isSuccessful) {
                        Helper.showLog(TAG, "Response : ${response.body()}")
                        if (response.body()!!.success) {
                            val mData = response.body()!!.data
                            if (mData != null && !mData.isEmpty()) {
                                appointmentsListForPatient = mData as ArrayList<com.bijumondal.doctorhealthcare.models.appointmentListForPatient.Data>
                                patientAdapter = PatientAppointmentsListAdapter(appointmentsListForPatient, context!!)
                                appointmentsListRecyclerView.adapter = patientAdapter
                                patientAdapter.notifyDataSetChanged()
                                appointmentsListRecyclerView.visibility = View.VISIBLE
                                tv_no_completed_appointments_found.visibility = View.GONE

                            } else {
                                appointmentsListRecyclerView.visibility = View.GONE
                                tv_no_completed_appointments_found.visibility = View.VISIBLE
                            }

                            if (response.body()!!.message != null) {
                                Helper.toastShort(context!!, response.body()!!.message)

                            } else if (response.body()!!.errors != null) {
                                Helper.toastShort(context!!, response.body()!!.errors)
                            }

                        } else {
                            if (response.body()!!.message != null) {
                                Helper.toastShort(context!!, response.body()!!.message)

                            } else if (response.body()!!.errors != null) {
                                Helper.toastShort(context!!, response.body()!!.errors)
                            }
                        }

                    } else {
                        Helper.toastNetworkError(context!!)
                        //Helper.hideLoading()
                    }
                }

                override fun onFailure(call: Call<ResponseAppointmentListForPatient>, t: Throwable) {
                    Helper.toastShort(context!!, "${t.message}")
                    //Helper.hideLoading()
                }

            })
        }
    }


    private fun fetchAppointmentsListForDoctor(request: RequestAppointmentsListForDoctor) {
        if (Helper.isConnectedToInternet(context!!)) {
            val call: Call<ResponseAppointmentsListForDoctor> = APIInterface.create().getDoctorAppointmentsList(request)
            call.enqueue(object : Callback<ResponseAppointmentsListForDoctor> {
                override fun onResponse(
                    call: Call<ResponseAppointmentsListForDoctor>,
                    response: Response<ResponseAppointmentsListForDoctor>
                ) {
                    //Helper.hideLoading()
                    if (response.isSuccessful) {
                        Helper.showLog(TAG, "Response : ${response.body()}")
                        if (response.body()!!.success) {
                            val mData = response.body()!!.data
                            if (mData != null && !mData.isEmpty()) {
                                appointmentsListForDoctor = mData as ArrayList<Data>
                                drAdapter = DoctorAppointmentsListAdapter(appointmentsListForDoctor, context!!)
                                appointmentsListRecyclerView.adapter = drAdapter
                                drAdapter.notifyDataSetChanged()
                                appointmentsListRecyclerView.visibility = View.VISIBLE
                                tv_no_completed_appointments_found.visibility = View.GONE

                            } else {
                                appointmentsListRecyclerView.visibility = View.GONE
                                tv_no_completed_appointments_found.visibility = View.VISIBLE
                            }

                            if (response.body()!!.message != null) {
                                Helper.toastShort(context!!, response.body()!!.message)

                            } else if (response.body()!!.errors != null) {
                                Helper.toastShort(context!!, response.body()!!.errors)
                            }

                        } else {
                            if (response.body()!!.message != null) {
                                Helper.toastShort(context!!, response.body()!!.message)

                            } else if (response.body()!!.errors != null) {
                                Helper.toastShort(context!!, response.body()!!.errors)
                            }
                        }

                    } else {
                        Helper.toastNetworkError(context!!)
                        //Helper.hideLoading()
                    }
                }

                override fun onFailure(call: Call<ResponseAppointmentsListForDoctor>, t: Throwable) {
                    Helper.toastShort(context!!, "${t.message}")
                    //Helper.hideLoading()
                }

            })
        }

    }


}
