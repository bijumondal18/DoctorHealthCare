package com.bijumondal.doctorhealthcare.activities

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import com.bijumondal.doctorhealthcare.R
import com.bijumondal.doctorhealthcare.api.APIInterface
import com.bijumondal.doctorhealthcare.models.createDoctorProfile.RequestCreateDoctorProfile
import com.bijumondal.doctorhealthcare.models.createDoctorProfile.ResponseCreateDoctorProfile
import com.bijumondal.doctorhealthcare.utils.HealthCarePreference
import com.bijumondal.doctorhealthcare.utils.Helper
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_create_doctor_profile.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CreateDoctorProfileActivity : AppCompatActivity() {

    companion object {
        private const val TAG: String = "CreateDoctorProfileActivity"
        private const val REQUEST_CODE = 232
        private const val REQUEST_CODE1 = 233

    }

    private lateinit var mPreference: HealthCarePreference

    var hospitalName = ""
    var visitAmount = ""
    var address = ""
    var docDept = ""
    var userId = ""
    var firstname = ""
    var lastname = ""
    var email = ""
    var phone = ""
    var hospitalId = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_doctor_profile)

        initViews()


    }

    private fun initViews() {
        mPreference = HealthCarePreference(this@CreateDoctorProfileActivity)

        edt_choose_department.setOnClickListener {
            startActivityForResult(Intent(this@CreateDoctorProfileActivity, DoctorDepartmentActivity::class.java), REQUEST_CODE)
        }

        edt_choose_hospital.setOnClickListener {
            startActivityForResult(Intent(this@CreateDoctorProfileActivity, HospitalListActivity::class.java), REQUEST_CODE1)

        }

        btn_create_doc_profile.setOnClickListener {
            validateProfile()
        }


    }

    private fun validateProfile() {
        docDept = edt_choose_department.text.trim().toString()
        visitAmount = edt_set_visit_amount.text.trim().toString()
        hospitalName = edt_choose_hospital.text.trim().toString()
        address = edt_set_address_doc.text.trim().toString()

        userId = mPreference.getUserId().toString()
        firstname = mPreference.getFirstName().toString()
        lastname = mPreference.getLastName().toString()
        if (mPreference.getEmail() != null) {
            email = mPreference.getEmail().toString()
        } else {
            email = ""
        }
        phone = mPreference.getNumber().toString()
        hospitalId = mPreference.getHospitalId().toString()

        if (!TextUtils.isEmpty(docDept)
            && !TextUtils.isEmpty(visitAmount)
            && !TextUtils.isEmpty(hospitalName)
        ) {
            val request = RequestCreateDoctorProfile(address, docDept, userId, email, "", phone, firstname, lastname, "", hospitalId, visitAmount)
            createDoctorProfile(request)

        } else {
            Helper.toastLong(this@CreateDoctorProfileActivity, "Fields shouldn't be empty!")
        }
    }

    private fun createDoctorProfile(request: RequestCreateDoctorProfile) {
        if (Helper.isConnectedToInternet(this@CreateDoctorProfileActivity)) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                Helper.showLoading(this)
            }
            val call: Call<ResponseCreateDoctorProfile> = APIInterface.create().createDoctorProfile(request)
            Helper.showLog(TAG, " request :- ${Gson().toJson(request)}")
            call.enqueue(object : Callback<ResponseCreateDoctorProfile> {
                override fun onResponse(
                    call: Call<ResponseCreateDoctorProfile>,
                    response: Response<ResponseCreateDoctorProfile>
                ) {
                    Helper.hideLoading()
                    if (response.isSuccessful) {
                        Helper.showLog(TAG, "Response : ${response.body()}")
                        if (response.body()!!.success) {
                            val mData = response.body()!!.data
                            if (mData != null) {

                                mPreference.setVisitAmount(visitAmount)
                                mPreference.setDoctorDept(docDept)
                                mPreference.setHospitalName(hospitalName)
                                mPreference.setHospitalId(hospitalId!!)

                                startActivity(Intent(this@CreateDoctorProfileActivity, MainActivity::class.java))
                                finish()
                                mPreference.setIsLoggedIn(true)

                            }

                            if (response.body()!!.data.message != null) {
                                Helper.toastShort(this@CreateDoctorProfileActivity, response.body()!!.data.message)

                            } else if (response.body()!!.errors != null) {
                                Helper.toastShort(this@CreateDoctorProfileActivity, response.body()!!.errors)
                            }

                        } else {
                            if (response.body()!!.data.message != null) {
                                Helper.toastShort(this@CreateDoctorProfileActivity, response.body()!!.data.message)

                            } else if (response.body()!!.errors != null) {
                                Helper.toastShort(this@CreateDoctorProfileActivity, response.body()!!.errors)
                            }
                        }

                    } else {
                        Helper.toastNetworkError(this@CreateDoctorProfileActivity)
                    }
                }

                override fun onFailure(call: Call<ResponseCreateDoctorProfile>, t: Throwable) {
                    Helper.toastShort(this@CreateDoctorProfileActivity, "${t.message}")
                    Helper.hideLoading()
                }
            })
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                docDept = data!!.getStringExtra("docDept")!!
                edt_choose_department.text = docDept

            }

        } else if (requestCode == REQUEST_CODE1)
            if (resultCode == RESULT_OK) {
                hospitalName = data!!.getStringExtra("hospitalName")!!
                edt_choose_hospital.text = hospitalName
                hospitalId = data.getStringExtra("hospitalId")!!
                hospitalId = mPreference.setHospitalId(hospitalId).toString()
            }
    }

}
