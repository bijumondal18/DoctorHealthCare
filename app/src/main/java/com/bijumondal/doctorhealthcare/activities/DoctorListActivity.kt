package com.bijumondal.doctorhealthcare.activities

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.bijumondal.doctorhealthcare.R
import com.bijumondal.doctorhealthcare.adapters.AllDoctorsListAdapter
import com.bijumondal.doctorhealthcare.api.APIInterface
import com.bijumondal.doctorhealthcare.models.allDoctorsList.Data
import com.bijumondal.doctorhealthcare.models.allDoctorsList.RequestAllDoctorsList
import com.bijumondal.doctorhealthcare.models.allDoctorsList.ResponseAllDoctorsList
import com.bijumondal.doctorhealthcare.utils.HealthCarePreference
import com.bijumondal.doctorhealthcare.utils.Helper
import kotlinx.android.synthetic.main.activity_doctor_list.*
import kotlinx.android.synthetic.main.activity_doctor_profile.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DoctorListActivity : AppCompatActivity() {

    companion object {
        private const val TAG: String = "DoctorListActivity"
    }

    private lateinit var mPreference: HealthCarePreference

    var deptName = ""

    private lateinit var allDoctorsRecyclerView: RecyclerView
    private var allDoctorsList: ArrayList<Data> = ArrayList()
    private lateinit var allDoctorsAdapter: AllDoctorsListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_doctor_list)
        mPreference = HealthCarePreference(this@DoctorListActivity)
        allDoctorsRecyclerView = findViewById(R.id.rv_doctors)

        if (intent.hasExtra("deptName") != null) {
            deptName = intent.getStringExtra("deptName").toString()
        }

        setupToolbar()

        val layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        allDoctorsRecyclerView.layoutManager = layoutManager
        val request = RequestAllDoctorsList(deptName)
        fetchDoctors(request)

    }

    private fun fetchDoctors(request: RequestAllDoctorsList) {
        if (Helper.isConnectedToInternet(this@DoctorListActivity)) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                Helper.showLoading(this)
            }
            val call: Call<ResponseAllDoctorsList> = APIInterface.create().getAllDoctorsList(request)
            call.enqueue(object : Callback<ResponseAllDoctorsList> {
                override fun onResponse(
                    call: Call<ResponseAllDoctorsList>,
                    response: Response<ResponseAllDoctorsList>
                ) {
                    Helper.hideLoading()
                    if (response.isSuccessful) {
                        Helper.showLog(TAG, "Response : ${response.body()}")
                        if (response.body()!!.success) {
                            val mData = response.body()!!.data
                            if (mData != null) {
                                allDoctorsList = mData as ArrayList<Data>
                                allDoctorsAdapter = AllDoctorsListAdapter(allDoctorsList, this@DoctorListActivity)
                                allDoctorsRecyclerView.adapter = allDoctorsAdapter
                                allDoctorsAdapter.notifyDataSetChanged()
                                rv_doctors.visibility = View.VISIBLE
                                iv_no_data.visibility = View.GONE

                            } else {
                                rv_doctors.visibility = View.GONE
                                iv_no_data.visibility = View.VISIBLE
                            }

                            if (response.body()!!.message != null) {
                                Helper.toastShort(this@DoctorListActivity, response.body()!!.message)

                            } else if (response.body()!!.errors != null) {
                                Helper.toastShort(this@DoctorListActivity, response.body()!!.errors)
                            }

                        } else {
                            if (response.body()!!.message != null) {
                                Helper.toastShort(this@DoctorListActivity, response.body()!!.message)

                            } else if (response.body()!!.errors != null) {
                                Helper.toastShort(this@DoctorListActivity, response.body()!!.errors)
                            }
                        }

                    } else {
                        Helper.toastNetworkError(this@DoctorListActivity)
                    }
                }

                override fun onFailure(call: Call<ResponseAllDoctorsList>, t: Throwable) {
                    Helper.toastShort(this@DoctorListActivity, "${t.message}")
                    Helper.hideLoading()

                }

            })
        }
    }

    private fun setupToolbar() {
        setSupportActionBar(toolbar_doctor_list_activity)
        val actionBar = supportActionBar
        actionBar!!.title = deptName
        actionBar.setDisplayHomeAsUpEnabled(true)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

}
