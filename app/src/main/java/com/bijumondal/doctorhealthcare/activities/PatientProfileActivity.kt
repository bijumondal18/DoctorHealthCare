package com.bijumondal.doctorhealthcare.activities

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import com.bijumondal.doctorhealthcare.R
import com.bijumondal.doctorhealthcare.api.APIInterface
import com.bijumondal.doctorhealthcare.models.patientProfileDetails.RequestPatientProfileDetails
import com.bijumondal.doctorhealthcare.models.patientProfileDetails.ResponsePatientProfileDetails
import com.bijumondal.doctorhealthcare.utils.HealthCarePreference
import com.bijumondal.doctorhealthcare.utils.Helper
import com.bijumondal.doctorhealthcare.utils.ImageLoader
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_patient_profile.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PatientProfileActivity : AppCompatActivity() {

    companion object {
        private const val TAG: String = "ProfileActivity"
        private const val REQUEST_CODE = 121
    }

    private lateinit var mPreference: HealthCarePreference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_patient_profile)
        mPreference = HealthCarePreference(this@PatientProfileActivity)

        setupToolbar()

        if (mPreference.getUserId() != null) {
            val patientId = mPreference.getUserId()
            val request = RequestPatientProfileDetails(patientId.toString())
            fetchProfileDetails(request)
        }

        btn_edit_profile.setOnClickListener {
            startActivityForResult(
                Intent(this@PatientProfileActivity, UpdatePatientProfileActivity::class.java),
                REQUEST_CODE
            )
        }

        tv_my_appointments.setOnClickListener {
            startActivity(Intent(this@PatientProfileActivity, MyAppointmentsActivity::class.java))
        }

        tv_my_prescriptions.setOnClickListener {
            startActivity(Intent(this@PatientProfileActivity, MyPrescriptionsActivity::class.java))
        }

        tv_rate_us.setOnClickListener {
            Helper.openPlayStore(this@PatientProfileActivity)
        }

        tv_share_app.setOnClickListener {
            Helper.appShare(this@PatientProfileActivity)
        }

        btn_logout.setOnClickListener {
            val builder = AlertDialog.Builder(this)
            builder.setTitle("Logout")
            builder.setMessage("Are you sure want to logout?")
            builder.setPositiveButton("Yes") { dialog, which ->
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                tv_profile_name.text = data!!.getStringExtra("firstName") +" "+ data!!.getStringExtra("lastName")
                tv_profile_email.text = data!!.getStringExtra("email")
                tv_profile_address.text = data!!.getStringExtra("address")
                //todo blood group. gender and dob
            }
        }

    }

    private fun fetchProfileDetails(request: RequestPatientProfileDetails) {
        if (Helper.isConnectedToInternet(this@PatientProfileActivity)) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                Helper.showLoading(this)
            }
            val call: Call<ResponsePatientProfileDetails> = APIInterface.create().getPatientProfileDetails(request)
            Helper.showLog(TAG, " request :- ${Gson().toJson(request)}")
            call.enqueue(object : Callback<ResponsePatientProfileDetails> {
                override fun onResponse(
                    call: Call<ResponsePatientProfileDetails>,
                    response: Response<ResponsePatientProfileDetails>
                ) {
                    Helper.hideLoading()
                    if (response.isSuccessful) {
                        Helper.showLog(TAG, "Response : ${response.body()}")
                        if (response.body()!!.success) {
                            val mData = response.body()!!.data
                            if (mData != null) {

                                if (mData.photo != null) {
                                    ImageLoader.loadCircleImageFromUrl(iv_profile_image, mData.photo, R.color.colorTransparent)
                                }
                                if (mData.name != null) {
                                    tv_profile_name.text = mData.name
                                }
                                if (mData.phone != null) {
                                    tv_profile_mobile_number.text = mData.phone
                                    tv_profile_mobile_number.visibility = View.VISIBLE
                                }
                                if (mData.email != null) {
                                    tv_profile_email.text = mData.email
                                    tv_profile_email.visibility = View.VISIBLE
                                }
                                if (mData.address != null) {
                                    tv_profile_address.text = mData.address
                                    tv_profile_address.visibility = View.VISIBLE
                                }


                            }

                            if (response.body()!!.data.message != null) {
                                Helper.toastShort(this@PatientProfileActivity, response.body()!!.data.message)

                            } else if (response.body()!!.errors != null) {
                                Helper.toastShort(this@PatientProfileActivity, response.body()!!.errors)
                            }

                        } else {
                            if (response.body()!!.data.message != null) {
                                Helper.toastShort(this@PatientProfileActivity, response.body()!!.data.message)

                            } else if (response.body()!!.errors != null) {
                                Helper.toastShort(this@PatientProfileActivity, response.body()!!.errors)
                            }
                        }

                    } else {
                        Helper.toastNetworkError(this@PatientProfileActivity)
                    }
                }

                override fun onFailure(call: Call<ResponsePatientProfileDetails>, t: Throwable) {
                    Helper.toastShort(this@PatientProfileActivity, "${t.message}")
                    Helper.hideLoading()
                }
            })
        }

    }

    private fun setupToolbar() {
        setSupportActionBar(toolbar_profile_activity)
        val actionBar = supportActionBar
        actionBar!!.title = "My Profile"
        actionBar.setDisplayHomeAsUpEnabled(true)
    }


    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

}
