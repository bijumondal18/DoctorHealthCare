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
        private const val REQUEST_CODE = 212
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
    var docDept = ""

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
            edt_first_name_doc.setText(mPreference.getFirstName())
        }

        if (mPreference.getLastName() != null) {
            edt_last_name_doc.setText(mPreference.getLastName())
        }

        if (mPreference.getEmail() != null) {
            edt_email_doc.setText(mPreference.getEmail())
        }

        if (mPreference.getAddress() != null) {
            edt_address_doc.setText(mPreference.getAddress())
        }

        if (mPreference.getNumber() != null) {
            edt_phone_number_doc.setText(mPreference.getNumber())
        }
        if (mPreference.getDoctorDept() != null) {
            edt_department_doc.text = mPreference.getDoctorDept()
        }

        imgEditProfilePic.setOnClickListener {
            CaptureImage.showPictureDialog(this@UpdateDoctorProfileActivity)
        }

        edt_department_doc.setOnClickListener {
            startActivityForResult(Intent(this@UpdateDoctorProfileActivity, DoctorDepartmentActivity::class.java), REQUEST_CODE)
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
        docDept = edt_department_doc.text.trim().toString()
        userId = mPreference.getUserId().toString()

        if (!TextUtils.isEmpty(firstname) &&
            !TextUtils.isEmpty(lastname) &&
            !TextUtils.isEmpty(phone) &&
            !TextUtils.isEmpty(docDept)
        ) {

            val request = RequestCreateDoctorProfile(address, docDept, userId, email, phone, "${firstname} ${lastname}", "")
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
                                mPreference.setDoctorDept(docDept)

                                val intent = Intent()
                                intent.putExtra("firstName", firstname)
                                intent.putExtra("lastName", lastname)
                                intent.putExtra("email", email)
                                intent.putExtra("phone", phone)
                                intent.putExtra("address", address)
                                intent.putExtra("docDept", docDept)
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                docDept = data!!.getStringExtra("docDept")!!
                edt_department_doc.text = docDept
            }
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
