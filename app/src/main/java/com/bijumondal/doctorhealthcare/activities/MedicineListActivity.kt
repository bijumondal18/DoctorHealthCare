package com.bijumondal.doctorhealthcare.activities

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.View
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bijumondal.doctorhealthcare.R
import com.bijumondal.doctorhealthcare.adapters.MedicineListAdapter
import com.bijumondal.doctorhealthcare.api.APIInterface
import com.bijumondal.doctorhealthcare.models.addMedicine.RequestAddMedicine
import com.bijumondal.doctorhealthcare.models.addMedicine.ResponseAddMedicine
import com.bijumondal.doctorhealthcare.models.medicineList.Data
import com.bijumondal.doctorhealthcare.models.medicineList.ResponseMedicineList
import com.bijumondal.doctorhealthcare.utils.ClickListener
import com.bijumondal.doctorhealthcare.utils.HealthCarePreference
import com.bijumondal.doctorhealthcare.utils.Helper
import com.bijumondal.doctorhealthcare.utils.RecyclerTouchListener
import kotlinx.android.synthetic.main.activity_medicine_list.*
import kotlinx.android.synthetic.main.activity_update_doctor_profile.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class MedicineListActivity : AppCompatActivity() {

    companion object {
        private const val TAG: String = "DoctorDepartmentActivity"
    }

    private lateinit var mPreference: HealthCarePreference
    private lateinit var mRecyclerView: RecyclerView
    private lateinit var medicineListAdapter: MedicineListAdapter
    private var medicineList: ArrayList<Data> = ArrayList()

    private var medicineName = ""
    private var genericName = ""

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_medicine_list)

        initViews()
        setupToolbar()

        val layoutManager = LinearLayoutManager(this@MedicineListActivity, LinearLayoutManager.VERTICAL, false)
        mRecyclerView.layoutManager = layoutManager
        fetchMedicineList()

        btn_add_new_medicine.setOnClickListener {
            if (!TextUtils.isEmpty(medicineName)) {
                if (!TextUtils.isEmpty(genericName)) {

                    val request = RequestAddMedicine(mPreference.getUserId().toString(), genericName, mPreference.getHospitalId().toString(), medicineName)
                    addMedicineToDatabase(request)

                } else {
                    Helper.toastShort(this@MedicineListActivity, "Please add generic name")
                }

            } else {
                Helper.toastShort(this@MedicineListActivity, "Please add a medicine first")
            }

        }

    }

    private fun addMedicineToDatabase(request: RequestAddMedicine) {

        if (Helper.isConnectedToInternet(this@MedicineListActivity)) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                Helper.showLoading(this)
            }
            val call: Call<ResponseAddMedicine> = APIInterface.create().addMedicine(request)
            call.enqueue(object : Callback<ResponseAddMedicine> {
                override fun onResponse(
                    call: Call<ResponseAddMedicine>,
                    response: Response<ResponseAddMedicine>
                ) {
                    Helper.hideLoading()
                    if (response.isSuccessful) {
                        Helper.showLog(TAG, "Response : ${response.body()}")
                        if (response.body()!!.success) {
                            val mData = response.body()!!.data
                            if (mData != null) {

                                fetchMedicineList()
                                edt_add_medicine_name.setText("")
                                edt_add_generic_name.setText("")
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

                override fun onFailure(call: Call<ResponseAddMedicine>, t: Throwable) {
                    Helper.toastShort(this@MedicineListActivity, "${t.message}")
                    Helper.hideLoading()
                }
            })
        }

    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun initViews() {
        mRecyclerView = findViewById(R.id.rv_medicine_list)
        mPreference = HealthCarePreference(this@MedicineListActivity)


        edt_add_medicine_name.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                medicineName = edt_add_medicine_name.text.trim().toString()
            }

            override fun afterTextChanged(p0: Editable?) {

            }

        })

        edt_add_generic_name.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                genericName = edt_add_generic_name.text.trim().toString()
            }

            override fun afterTextChanged(p0: Editable?) {

            }

        })

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
