package com.bijumondal.doctorhealthcare.activities

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import com.bijumondal.doctorhealthcare.R
import com.bijumondal.doctorhealthcare.api.APIInterface
import com.bijumondal.doctorhealthcare.models.doctorProfileDetails.RequestDoctorProfileDetails
import com.bijumondal.doctorhealthcare.models.doctorProfileDetails.ResponseDoctorProfileDetails
import com.bijumondal.doctorhealthcare.models.patientProfileDetails.RequestPatientProfileDetails
import com.bijumondal.doctorhealthcare.models.patientProfileDetails.ResponsePatientProfileDetails
import com.bijumondal.doctorhealthcare.utils.HealthCarePreference
import com.bijumondal.doctorhealthcare.utils.Helper
import com.bijumondal.doctorhealthcare.utils.ImageLoader
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_doctor_profile.*
import kotlinx.android.synthetic.main.activity_patient_profile.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DoctorProfileActivity : AppCompatActivity() {

    companion object {
        private const val TAG: String = "DoctorProfileActivity"
        private const val REQUEST_CODE = 122
    }

    private lateinit var mPreference: HealthCarePreference


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_doctor_profile)
        mPreference = HealthCarePreference(this@DoctorProfileActivity)

        setupToolbar()

        if (mPreference.getUserId() != null) {
            val doctorId = mPreference.getUserId()
            val request = RequestDoctorProfileDetails(doctorId.toString())
            fetchProfileDetails(request)
        }

        btn_edit_profile_doc.setOnClickListener {
            startActivityForResult(
                Intent(this@DoctorProfileActivity, UpdateDoctorProfileActivity::class.java),
                REQUEST_CODE
            )
        }

        tv_my_appointments_doc.setOnClickListener {
            startActivity(Intent(this@DoctorProfileActivity, MyAppointmentsActivity::class.java))
        }

        tv_my_prescriptions_doc.setOnClickListener {
            startActivity(Intent(this@DoctorProfileActivity, MyPrescriptionsActivity::class.java))
        }

        tv_rate_us_doc.setOnClickListener {
            Helper.openPlayStore(this@DoctorProfileActivity)
        }

        tv_share_app_doc.setOnClickListener {
            Helper.appShare(this@DoctorProfileActivity)
        }

        btn_logout_doc.setOnClickListener {
            val builder = AlertDialog.Builder(this)
            builder.setTitle("Logout")
            builder.setMessage("Are you sure want to logout?")
            builder.setPositiveButton("Yes") { dialog, which ->
                mPreference.setIsLoggedIn(false)
                mPreference.clearSharedPreference()
                startActivity(
                    Intent(this, WelcomeActivity::class.java)
                        .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                        .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                )
                finish()
            }
            builder.setNegativeButton("No") { dialog, which ->
                return@setNegativeButton
            }
            val dialog: AlertDialog = builder.create()
            dialog.show()
        }

    }

    private fun fetchProfileDetails(request: RequestDoctorProfileDetails) {
        if (Helper.isConnectedToInternet(this@DoctorProfileActivity)) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                Helper.showLoading(this)
            }
            val call: Call<ResponseDoctorProfileDetails> = APIInterface.create().getDoctorProfileDetails(request)
            Helper.showLog(TAG, " request :- ${Gson().toJson(request)}")
            call.enqueue(object : Callback<ResponseDoctorProfileDetails> {
                override fun onResponse(
                    call: Call<ResponseDoctorProfileDetails>,
                    response: Response<ResponseDoctorProfileDetails>
                ) {
                    Helper.hideLoading()
                    if (response.isSuccessful) {
                        Helper.showLog(TAG, "Response : ${response.body()}")
                        if (response.body()!!.success) {
                            val mData = response.body()!!.data
                            if (mData != null) {

                                if (mData.photo != null) {
                                    ImageLoader.loadCircleImageFromUrl(iv_doc_profile_image, mData.photo, R.color.colorTransparent)
                                }
                                if (mData.name != null) {
                                    tv_doc_profile_name.text = mData.name
                                }
                                if (mData.phone != null) {
                                    tv_doc_profile_mobile_number.text = mData.phone
                                    tv_doc_profile_mobile_number.visibility = View.VISIBLE
                                    mPreference.setNumber(mData.phone)
                                }
                                if (mData.email != null) {
                                    tv_doc_profile_email.text = mData.email
                                    tv_doc_profile_email.visibility = View.VISIBLE
                                    mPreference.setEmail(mData.email)
                                }
                                if (mData.address != null) {
                                    tv_doc_profile_address.text = mData.address
                                    tv_doc_profile_address.visibility = View.VISIBLE
                                    mPreference.setAddress(mData.address)
                                }

                                if (mData.department != null) {
                                    tv_doc_dept.text = mData.department
                                }


                            }

                            if (response.body()!!.data.message != null) {
                                Helper.toastShort(this@DoctorProfileActivity, response.body()!!.data.message)

                            } else if (response.body()!!.errors != null) {
                                Helper.toastShort(this@DoctorProfileActivity, response.body()!!.errors)
                            }

                        } else {
                            if (response.body()!!.data.message != null) {
                                Helper.toastShort(this@DoctorProfileActivity, response.body()!!.data.message)

                            } else if (response.body()!!.errors != null) {
                                Helper.toastShort(this@DoctorProfileActivity, response.body()!!.errors)
                            }
                        }

                    } else {
                        Helper.toastNetworkError(this@DoctorProfileActivity)
                    }
                }

                override fun onFailure(call: Call<ResponseDoctorProfileDetails>, t: Throwable) {
                    Helper.toastShort(this@DoctorProfileActivity, "${t.message}")
                    Helper.hideLoading()
                }
            })
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                tv_doc_profile_name.text = data!!.getStringExtra("firstName") + " " + data!!.getStringExtra("lastName")
                tv_doc_profile_email.text = data!!.getStringExtra("email")
                tv_doc_profile_address.text = data!!.getStringExtra("address")
                tv_doc_profile_address.visibility = View.VISIBLE
                tv_doc_dept.text = data.getStringExtra("docDept")
            }
        }

    }

    private fun setupToolbar() {
        setSupportActionBar(toolbar_doctor_profile_activity)
        val actionBar = supportActionBar
        actionBar!!.title = "My Profile"
        actionBar.setDisplayHomeAsUpEnabled(true)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }


}
