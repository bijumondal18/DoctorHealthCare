package com.bijumondal.doctorhealthcare.activities

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.ImageView
import com.bijumondal.doctorhealthcare.R
import com.bijumondal.doctorhealthcare.api.APIInterface
import com.bijumondal.doctorhealthcare.models.createDoctorProfile.RequestCreateDoctorProfile
import com.bijumondal.doctorhealthcare.models.createDoctorProfile.ResponseCreateDoctorProfile
import com.bijumondal.doctorhealthcare.models.createPatientProfile.RequestCreatePatientProfile
import com.bijumondal.doctorhealthcare.models.createPatientProfile.ResponseCreatePatientProfile
import com.bijumondal.doctorhealthcare.utils.CaptureImage
import com.bijumondal.doctorhealthcare.utils.HealthCarePreference
import com.bijumondal.doctorhealthcare.utils.Helper
import com.google.gson.Gson
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.activity_update_doctor_profile.*
import kotlinx.android.synthetic.main.activity_update_patient_profile.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*

class UpdateDoctorProfileActivity : AppCompatActivity() {

    companion object {
        private const val TAG: String = "UpdateDoctorProfileActivity"
    }

    private lateinit var mPreference: HealthCarePreference

    var userId = ""
    private lateinit var imgProfilePic: CircleImageView
    private lateinit var imgEditProfilePic: ImageView
    var firstname = ""
    var lastname = ""
    var email = ""
    var phone = ""
    var address = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update_doctor_profile)

        initViews()

        setupToolbar()

    }

    private fun initViews() {
        mPreference = HealthCarePreference(this@UpdateDoctorProfileActivity)
        imgProfilePic = findViewById(R.id.iv_user_image_doc)
        imgEditProfilePic = findViewById(R.id.iv_edit_profile_picture_doc)

        if (mPreference.getUserId() != null) {
            userId = mPreference.getUserId().toString()
        }

        if (mPreference.getFirstName() != null) {
            edt_first_name.setText(mPreference.getFirstName())
        }

        if (mPreference.getLastName() != null) {
            edt_last_name.setText(mPreference.getLastName())
        }

        if (mPreference.getEmail() != null) {
            edt_email.setText(mPreference.getEmail())
        }

        if (mPreference.getAddress() != null) {
            edt_address.setText(mPreference.getAddress())
        }

        if (mPreference.getNumber() != null) {
            edt_phone_number.setText(mPreference.getNumber())
        }

        imgEditProfilePic.setOnClickListener {
            CaptureImage.showPictureDialog(this@UpdateDoctorProfileActivity)
        }

        btn_update_doc_profile.setOnClickListener {
            validateProfile()
        }

    }

    private fun validateProfile() {
        firstname = edt_first_name_doc.text.trim().toString()
        lastname = edt_last_name_doc.text.trim().toString()
        email = edt_email_doc.text.trim().toString()
        phone = edt_phone_number_doc.text.trim().toString()
        address = edt_address_doc.text.trim().toString()

        if (!TextUtils.isEmpty(firstname) &&
            !TextUtils.isEmpty(lastname) &&
            !TextUtils.isEmpty(phone)
        ) {

            val request = RequestCreateDoctorProfile(address, "", email, phone, "${firstname} ${lastname}", userId, "")
            updateDoctorProfile(request)

        } else {
            Helper.toastLong(this@UpdateDoctorProfileActivity, "Fields shouldn't be empty!")
        }
    }

    private fun updateDoctorProfile(request: RequestCreateDoctorProfile) {

        if (Helper.isConnectedToInternet(this@UpdateDoctorProfileActivity)) {
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

                                mPreference.setFirstName(firstname)
                                mPreference.setLastName(lastname)
                                mPreference.setEmail(email)
                                mPreference.setNumber(phone)

                                val intent = Intent()
                                intent.putExtra("firstName", firstname)
                                intent.putExtra("lastName", lastname)
                                intent.putExtra("email", email)
                                intent.putExtra("phone", phone)
                                intent.putExtra("address", address)
                                setResult(RESULT_OK, intent)
                                finish()


                            }

                            if (response.body()!!.data.message != null) {
                                Helper.toastShort(this@UpdateDoctorProfileActivity, response.body()!!.data.message)

                            } else if (response.body()!!.errors != null) {
                                Helper.toastShort(this@UpdateDoctorProfileActivity, response.body()!!.errors)
                            }

                        } else {
                            if (response.body()!!.data.message != null) {
                                Helper.toastShort(this@UpdateDoctorProfileActivity, response.body()!!.data.message)

                            } else if (response.body()!!.errors != null) {
                                Helper.toastShort(this@UpdateDoctorProfileActivity, response.body()!!.errors)
                            }
                        }

                    } else {
                        Helper.toastNetworkError(this@UpdateDoctorProfileActivity)
                    }
                }

                override fun onFailure(call: Call<ResponseCreateDoctorProfile>, t: Throwable) {
                    Helper.toastShort(this@UpdateDoctorProfileActivity, "${t.message}")
                    Helper.hideLoading()
                }
            })
        }

    }

    private fun setupToolbar() {
        setSupportActionBar(toolbar_update_doctor_profile_activity)
        val actionBar = supportActionBar
        actionBar!!.title = "Update Profile"
        actionBar.setDisplayHomeAsUpEnabled(true)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

}
