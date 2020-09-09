package com.bijumondal.doctorhealthcare.activities

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import com.bijumondal.doctorhealthcare.R
import com.bijumondal.doctorhealthcare.api.APIInterface
import com.bijumondal.doctorhealthcare.models.addDoctorPrescriptions.RequestAddPrescriptions
import com.bijumondal.doctorhealthcare.models.addDoctorPrescriptions.ResponseAddPrescriptions
import com.bijumondal.doctorhealthcare.utils.HealthCarePreference
import com.bijumondal.doctorhealthcare.utils.Helper
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_add_prescriptions.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AddPrescriptionsActivity : AppCompatActivity() {

    companion object {
        private const val TAG: String = "AddPrescriptionsActivity"
    }

    private lateinit var mPreference: HealthCarePreference

    private var medicineName: String = ""
    private var symptom: String = ""
    private var advice: String = ""
    private var note: String = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_prescriptions)
        mPreference = HealthCarePreference(this@AddPrescriptionsActivity)

        setupToolbar()

        validateFields()

        btn_add_medicine.setOnClickListener {
            doAddMedicineValidation()
        }


    }


    private fun doAddMedicineValidation() {
        if (!TextUtils.isEmpty(medicineName) &&
            !TextUtils.isEmpty(advice) &&
            !TextUtils.isEmpty(symptom) &&
            !TextUtils.isEmpty(note)
        ) {

            val request = RequestAddPrescriptions(advice, mPreference.getUserId().toString(), medicineName, note, "", symptom)
            addPrescriptions(request)

        } else {
            Helper.toastLong(this@AddPrescriptionsActivity, "empty Field's !")
        }

    }

    private fun addPrescriptions(request: RequestAddPrescriptions) {
        if (Helper.isConnectedToInternet(this@AddPrescriptionsActivity)) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                Helper.showLoading(this@AddPrescriptionsActivity)
            }
            val call: Call<ResponseAddPrescriptions> = APIInterface.create().addPrescription(request)
            Helper.showLog(TAG, " request :- ${Gson().toJson(request)}")
            call.enqueue(object : Callback<ResponseAddPrescriptions> {
                override fun onResponse(
                    call: Call<ResponseAddPrescriptions>,
                    response: Response<ResponseAddPrescriptions>
                ) {
                    Helper.hideLoading()
                    if (response.isSuccessful) {
                        Helper.showLog(TAG, "Response : ${response.body()}")
                        if (response.body()!!.success) {
                            val mData = response.body()!!.data
                            if (mData != null) {

                                Helper.toastShort(this@AddPrescriptionsActivity, mData.message)

                            }

                            if (response.body()!!.data.message != null) {
                                Helper.toastShort(this@AddPrescriptionsActivity, response.body()!!.data.message)

                            } else if (response.body()!!.errors != null) {
                                Helper.toastShort(this@AddPrescriptionsActivity, response.body()!!.errors)
                            }

                        } else {
                            if (response.body()!!.data.message != null) {
                                Helper.toastShort(this@AddPrescriptionsActivity, response.body()!!.data.message)

                            } else if (response.body()!!.errors != null) {
                                Helper.toastShort(this@AddPrescriptionsActivity, response.body()!!.errors)
                            }
                        }

                    } else {
                        Helper.toastNetworkError(this@AddPrescriptionsActivity)
                    }
                }

                override fun onFailure(call: Call<ResponseAddPrescriptions>, t: Throwable) {
                    Helper.toastShort(this@AddPrescriptionsActivity, "${t.message}")
                    Helper.hideLoading()
                }
            })
        }

    }

    private fun validateFields() {

        edt_medicine_name.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                medicineName = edt_medicine_name.text.trim().toString()
            }

        })

        edt_symptom.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                symptom = edt_symptom.text.trim().toString()
            }

        })

        edt_advice.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                advice = edt_advice.text.trim().toString()
            }

        })

        edt_note.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                note = edt_note.text.trim().toString()
            }

        })


    }

    private fun setupToolbar() {
        setSupportActionBar(toolbar_add_prescriptions_activity)
        val actionBar = supportActionBar
        actionBar!!.title = "Add Prescriptions"
        actionBar!!.setDisplayHomeAsUpEnabled(true)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}
