package com.bijumondal.doctorhealthcare.activities

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bijumondal.doctorhealthcare.R
import com.bijumondal.doctorhealthcare.adapters.MedicineListAdapter
import com.bijumondal.doctorhealthcare.api.APIInterface
import com.bijumondal.doctorhealthcare.models.medicineList.Data
import com.bijumondal.doctorhealthcare.models.medicineList.ResponseMedicineList
import com.bijumondal.doctorhealthcare.utils.HealthCarePreference
import com.bijumondal.doctorhealthcare.utils.Helper
import kotlinx.android.synthetic.main.activity_medicine_list.*
import kotlinx.android.synthetic.main.activity_update_doctor_profile.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MedicineListActivity : AppCompatActivity() {

    companion object {
        private const val TAG: String = "DoctorDepartmentActivity"
    }

    private lateinit var mPreference: HealthCarePreference
    private lateinit var mRecyclerView: RecyclerView
    private lateinit var medicineListAdapter: MedicineListAdapter
    private var medicineList: ArrayList<Data> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_medicine_list)

        initViews()

        setupToolbar()

        val layoutManager = LinearLayoutManager(this@MedicineListActivity, LinearLayoutManager.VERTICAL, false)
        mRecyclerView.layoutManager = layoutManager
        fetchMedicineList()

    }

    private fun initViews() {
        mRecyclerView = findViewById(R.id.rv_medicine_list)
        mPreference = HealthCarePreference(this@MedicineListActivity)
    }

    private fun setupToolbar() {
        setSupportActionBar(toolbar_medicine_list_activity)
        val actionBar = supportActionBar
        actionBar!!.title = "Select Medicine"
        actionBar.setDisplayHomeAsUpEnabled(true)
    }

    private fun fetchMedicineList() {
        if (Helper.isConnectedToInternet(this@MedicineListActivity)) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                Helper.showLoading(this)
            }
            val call: Call<ResponseMedicineList> = APIInterface.create().getMedicineList()
            call.enqueue(object : Callback<ResponseMedicineList> {
                override fun onResponse(
                    call: Call<ResponseMedicineList>,
                    response: Response<ResponseMedicineList>
                ) {
                    Helper.hideLoading()
                    if (response.isSuccessful) {
                        Helper.showLog(TAG, "Response : ${response.body()}")
                        if (response.body()!!.success) {
                            val mData = response.body()!!.data
                            if (mData != null) {

                                medicineList = mData as ArrayList<Data>
                                medicineListAdapter = MedicineListAdapter(medicineList, this@MedicineListActivity)
                                mRecyclerView.adapter = medicineListAdapter
                                mRecyclerView.addItemDecoration(DividerItemDecoration(this@MedicineListActivity, LinearLayoutManager.HORIZONTAL))
                                medicineListAdapter.notifyDataSetChanged()

                            }

                            if (response.body()!!.message != null) {
                                Helper.toastShort(this@MedicineListActivity, response.body()!!.message)

                            } else if (response.body()!!.errors != null) {
                                Helper.toastShort(this@MedicineListActivity, response.body()!!.errors)
                            }

                        } else {
                            if (response.body()!!.message != null) {
                                Helper.toastShort(this@MedicineListActivity, response.body()!!.message)

                            } else if (response.body()!!.errors != null) {
                                Helper.toastShort(this@MedicineListActivity, response.body()!!.errors)
                            }
                        }

                    } else {
                        Helper.toastNetworkError(this@MedicineListActivity)
                    }
                }

                override fun onFailure(call: Call<ResponseMedicineList>, t: Throwable) {
                    Helper.toastShort(this@MedicineListActivity, "${t.message}")
                    Helper.hideLoading()
                }
            })
        }

    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}
