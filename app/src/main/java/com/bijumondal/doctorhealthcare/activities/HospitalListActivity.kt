package com.bijumondal.doctorhealthcare.activities

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bijumondal.doctorhealthcare.R
import com.bijumondal.doctorhealthcare.adapters.DoctorDeptAdapter
import com.bijumondal.doctorhealthcare.adapters.HospitalListAdapter
import com.bijumondal.doctorhealthcare.api.APIInterface
import com.bijumondal.doctorhealthcare.models.doctorDepartment.Data
import com.bijumondal.doctorhealthcare.models.doctorDepartment.ResponseDoctorDepartment
import com.bijumondal.doctorhealthcare.models.hospitalList.ResponseHospitalList
import com.bijumondal.doctorhealthcare.utils.HealthCarePreference
import com.bijumondal.doctorhealthcare.utils.Helper
import kotlinx.android.synthetic.main.activity_doctor_department.*
import kotlinx.android.synthetic.main.activity_hospital_list.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HospitalListActivity : AppCompatActivity() {

    companion object {
        private const val TAG: String = "HospitalListActivity"
        //private const val REQUEST_CODE = 232
    }


    private lateinit var mPreference: HealthCarePreference
    private lateinit var mRecyclerView: RecyclerView
    private lateinit var hospitalListAdapter: HospitalListAdapter
    private var hospitalList: ArrayList<com.bijumondal.doctorhealthcare.models.hospitalList.Data> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hospital_list)

        initViews()

        setupToolbar()

        val layoutManager = LinearLayoutManager(this@HospitalListActivity, LinearLayoutManager.VERTICAL, false)
        mRecyclerView.layoutManager = layoutManager
        fetchHospitalList()

    }

    private fun fetchHospitalList() {
        if (Helper.isConnectedToInternet(this@HospitalListActivity)) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                Helper.showLoading(this)
            }
            val call: Call<ResponseHospitalList> = APIInterface.create().getHospitalList()
            call.enqueue(object : Callback<ResponseHospitalList> {
                override fun onResponse(
                    call: Call<ResponseHospitalList>,
                    response: Response<ResponseHospitalList>
                ) {
                    Helper.hideLoading()
                    if (response.isSuccessful) {
                        Helper.showLog(TAG, "Response : ${response.body()}")
                        if (response.body()!!.success) {
                            val mData = response.body()!!.data
                            if (mData != null) {
                                hospitalList = mData as ArrayList<com.bijumondal.doctorhealthcare.models.hospitalList.Data>
                                hospitalListAdapter = HospitalListAdapter(hospitalList, this@HospitalListActivity)
                                mRecyclerView.adapter = hospitalListAdapter
                                // mRecyclerView.addItemDecoration(DividerItemDecoration(this@HospitalListActivity, LinearLayoutManager.HORIZONTAL))
                                hospitalListAdapter.notifyDataSetChanged()

                            }

                            if (response.body()!!.message != null) {
                                Helper.toastShort(this@HospitalListActivity, response.body()!!.message)

                            } else if (response.body()!!.errors != null) {
                                Helper.toastShort(this@HospitalListActivity, response.body()!!.errors)
                            }

                        } else {
                            if (response.body()!!.message != null) {
                                Helper.toastShort(this@HospitalListActivity, response.body()!!.message)

                            } else if (response.body()!!.errors != null) {
                                Helper.toastShort(this@HospitalListActivity, response.body()!!.errors)
                            }
                        }

                    } else {
                        Helper.toastNetworkError(this@HospitalListActivity)
                    }
                }

                override fun onFailure(call: Call<ResponseHospitalList>, t: Throwable) {
                    Helper.toastShort(this@HospitalListActivity, "${t.message}")
                    Helper.hideLoading()
                }
            })
        }


    }

    private fun initViews() {
        mPreference = HealthCarePreference(this@HospitalListActivity)
        mRecyclerView = findViewById(R.id.rv_hospital_list)
    }

    private fun setupToolbar() {
        setSupportActionBar(toolbar_hospital_list_activity)
        val actionBar = supportActionBar
        actionBar!!.title = "Choose your Hospital"
        actionBar.setDisplayHomeAsUpEnabled(true)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}
