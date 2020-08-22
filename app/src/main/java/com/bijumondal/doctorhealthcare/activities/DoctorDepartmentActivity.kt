package com.bijumondal.doctorhealthcare.activities

import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bijumondal.doctorhealthcare.R
import com.bijumondal.doctorhealthcare.adapters.DoctorDeptAdapter
import com.bijumondal.doctorhealthcare.api.APIInterface
import com.bijumondal.doctorhealthcare.models.doctorDepartment.Data
import com.bijumondal.doctorhealthcare.models.doctorDepartment.ResponseDoctorDepartment
import com.bijumondal.doctorhealthcare.utils.HealthCarePreference
import com.bijumondal.doctorhealthcare.utils.Helper
import kotlinx.android.synthetic.main.activity_doctor_department.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class DoctorDepartmentActivity : AppCompatActivity() {

    companion object {
        private const val TAG: String = "DoctorDepartmentActivity"
    }

    private lateinit var mPreference: HealthCarePreference
    private lateinit var mRecyclerView: RecyclerView
    private lateinit var deptAdapter: DoctorDeptAdapter
    private var docDeptList: ArrayList<Data> = ArrayList()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_doctor_department)

        initViews()

        setupToolbar()

        val layoutManager = LinearLayoutManager(this@DoctorDepartmentActivity, LinearLayoutManager.VERTICAL, false)
        mRecyclerView.layoutManager = layoutManager
        fetchDoctorDeptList()

    }

    private fun fetchDoctorDeptList() {
        if (Helper.isConnectedToInternet(this@DoctorDepartmentActivity)) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                Helper.showLoading(this)
            }
            val call: Call<ResponseDoctorDepartment> = APIInterface.create().getDoctorDepartments()
            call.enqueue(object : Callback<ResponseDoctorDepartment> {
                override fun onResponse(
                    call: Call<ResponseDoctorDepartment>,
                    response: Response<ResponseDoctorDepartment>
                ) {
                    Helper.hideLoading()
                    if (response.isSuccessful) {
                        Helper.showLog(TAG, "Response : ${response.body()}")
                        if (response.body()!!.success) {
                            val mData = response.body()!!.data
                            if (mData != null) {

                                docDeptList = mData as ArrayList<Data>
                                deptAdapter = DoctorDeptAdapter(docDeptList, this@DoctorDepartmentActivity)
                                mRecyclerView.adapter = deptAdapter
                                mRecyclerView.addItemDecoration(DividerItemDecoration(this@DoctorDepartmentActivity, LinearLayoutManager.HORIZONTAL))
                                deptAdapter.notifyDataSetChanged()

                            }

                            if (response.body()!!.message != null) {
                                Helper.toastShort(this@DoctorDepartmentActivity, response.body()!!.message)

                            } else if (response.body()!!.errors != null) {
                                Helper.toastShort(this@DoctorDepartmentActivity, response.body()!!.errors)
                            }

                        } else {
                            if (response.body()!!.message != null) {
                                Helper.toastShort(this@DoctorDepartmentActivity, response.body()!!.message)

                            } else if (response.body()!!.errors != null) {
                                Helper.toastShort(this@DoctorDepartmentActivity, response.body()!!.errors)
                            }
                        }

                    } else {
                        Helper.toastNetworkError(this@DoctorDepartmentActivity)
                    }
                }

                override fun onFailure(call: Call<ResponseDoctorDepartment>, t: Throwable) {
                    Helper.toastShort(this@DoctorDepartmentActivity, "${t.message}")
                    Helper.hideLoading()
                }
            })
        }


    }

    private fun initViews() {
        mPreference = HealthCarePreference(this@DoctorDepartmentActivity)
        mRecyclerView = findViewById(R.id.rv_doctor_dept)

    }

    private fun setupToolbar() {
        setSupportActionBar(toolbar_doctor_dept_activity)
        val actionBar = supportActionBar
        actionBar!!.title = "Choose your department"
        actionBar.setDisplayHomeAsUpEnabled(true)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}
