package com.bijumondal.doctorhealthcare.activities

import android.annotation.SuppressLint
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
        private const val TAG: String = "PatientProfileActivity"
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
                mPreference.setIsLoggedIn(false)
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

    @SuppressLint("SetTextI18n")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                tv_profile_name.text = data!!.getStringExtra("firstName") + " " + data!!.getStringExtra("lastName")
                tv_profile_email.text = data!!.getStringExtra("email")
                tv_profile_address.text = data!!.getStringExtra("address")
                tv_profile_address.visibility = View.VISIBLE
                tv_blood_group.text = data!!.getStringExtra("bloodGroup")
                tv_gender.text = data!!.getStringExtra("gender")
                tv_dob.text = data!!.getStringExtra("dob")

                val request = RequestPatientProfileDetails(mPreference.getUserId().toString())
                fetchProfileDetails(request)

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
                                    ImageLoader.loadCircleImageFromUrl(iv_profile_image, mData.photo, R.drawable.ic_avatar)
                                }
                                if (mData.name != null) {
                                    tv_profile_name.text = mData.name
                                    mPreference.setName(mData.name)
                                }
                                if (mData.phone != null) {
                                    tv_profile_mobile_number.text = mData.phone
                                    tv_profile_mobile_number.visibility = View.VISIBLE
                                    mPreference.setNumber(mData.phone)
                                }
                                if (mData.email != null) {
                                    tv_profile_email.text = mData.email
                                    tv_profile_email.visibility = View.VISIBLE
                                    mPreference.setEmail(mData.email)
                                }
                                if (mData.address != null) {
                                    tv_profile_address.text = mData.address
                                    tv_profile_address.visibility = View.VISIBLE
                                    mPreference.setAddress(mData.address)
                                }

                                if (mData.birthdate != null) {
                                    tv_dob.text = mData.birthdate
                                    mPreference.setDOB(mData.birthdate)
                                }

                                if (mData.sex != null) {
                                    tv_gender.text = mData.sex
                                    mPreference.setGender(mData.sex)
                                }

                                if (mData.bloodgroup != null) {
                                    tv_blood_group.text = mData.bloodgroup
                                    mPreference.setBloodGroup(mData.bloodgroup)
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
