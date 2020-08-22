package com.bijumondal.doctorhealthcare.activities

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.*
import com.bijumondal.doctorhealthcare.R
import com.bijumondal.doctorhealthcare.api.APIInterface
import com.bijumondal.doctorhealthcare.models.createPatientProfile.RequestCreatePatientProfile
import com.bijumondal.doctorhealthcare.models.createPatientProfile.ResponseCreatePatientProfile
import com.bijumondal.doctorhealthcare.models.patientProfileDetails.RequestPatientProfileDetails
import com.bijumondal.doctorhealthcare.models.patientProfileDetails.ResponsePatientProfileDetails
import com.bijumondal.doctorhealthcare.utils.HealthCarePreference
import com.bijumondal.doctorhealthcare.utils.Helper
import com.bijumondal.doctorhealthcare.utils.ImageLoader
import com.google.gson.Gson
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.activity_create_profile.*
import kotlinx.android.synthetic.main.activity_patient_profile.*
import kotlinx.android.synthetic.main.activity_update_patient_profile.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class UpdatePatientProfileActivity : AppCompatActivity() {

    companion object {
        private const val TAG: String = "UpdatePatientProfileActivity"
    }

    private lateinit var mPreference: HealthCarePreference

    var bloodGroupList = arrayOf("A+", "A-", "B+", "B-", "AB+", "AB-", "O+", "O-")
    var selectedBloodGroup: Int? = null
    var bloodGroup = ""
    private lateinit var spinnerBloodGroup: Spinner
    var dob = ""
    var gender = ""
    var firstname = ""
    var lastname = ""
    var email = ""
    var phone = ""
    var address = ""

    var userId = ""
    private lateinit var imgProfilePic: ImageView
    private lateinit var imgEditProfilePic: ImageView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update_patient_profile)

        initViews()
        setupToolbar()

        setupDateOfBirth()
        setupBloodGroupSpinner()
        setupGender()



        btn_update_profile.setOnClickListener {
            validateProfile()
        }

    }

    private fun initViews() {
        mPreference = HealthCarePreference(this@UpdatePatientProfileActivity)
        imgProfilePic = findViewById(R.id.iv_user_image)
        imgEditProfilePic = findViewById(R.id.iv_edit_profile_picture)

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

        if (mPreference.getNumber() != null) {
            edt_phone_number.setText(mPreference.getNumber())
        }

        if (mPreference.getDOB() != null) {
            tv_date.setText(mPreference.getDOB())
        }

    }

    private fun validateProfile() {

        firstname = edt_first_name.text.trim().toString()
        lastname = edt_last_name.text.trim().toString()
        email = edt_email.text.trim().toString()
        phone = edt_phone_number.text.trim().toString()
        address = edt_address.text.trim().toString()

        if (!TextUtils.isEmpty(firstname) &&
            !TextUtils.isEmpty(lastname) &&
            !TextUtils.isEmpty(phone)
        ) {

            val request = RequestCreatePatientProfile(address, "", bloodGroup, dob, email, phone, "${firstname} ${lastname}", userId, gender)
            updatePatientProfile(request)

        } else {
            Helper.toastLong(this@UpdatePatientProfileActivity, "Fields shouldn't be empty!")
        }


    }

    private fun updatePatientProfile(request: RequestCreatePatientProfile) {
        if (Helper.isConnectedToInternet(this@UpdatePatientProfileActivity)) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                Helper.showLoading(this)
            }
            val call: Call<ResponseCreatePatientProfile> = APIInterface.create().createPatientProfile(request)
            Helper.showLog(TAG, " request :- ${Gson().toJson(request)}")
            call.enqueue(object : Callback<ResponseCreatePatientProfile> {
                override fun onResponse(
                    call: Call<ResponseCreatePatientProfile>,
                    response: Response<ResponseCreatePatientProfile>
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
                                mPreference.setGender(gender)
                                mPreference.setDOB(dob)
                                mPreference.setBloodGroup(bloodGroup)

                                val intent = Intent()
                                intent.putExtra("firstName", firstname)
                                intent.putExtra("lastName", lastname)
                                intent.putExtra("email", email)
                                intent.putExtra("phone", phone)
                                intent.putExtra("address", address)
                                intent.putExtra("gender", gender)
                                intent.putExtra("bloodGroup", bloodGroup)
                                intent.putExtra("dob", dob)
                                setResult(RESULT_OK, intent)
                                finish()


                            }

                            if (response.body()!!.data.message != null) {
                                Helper.toastShort(this@UpdatePatientProfileActivity, response.body()!!.data.message)

                            } else if (response.body()!!.errors != null) {
                                Helper.toastShort(this@UpdatePatientProfileActivity, response.body()!!.errors)
                            }

                        } else {
                            if (response.body()!!.data.message != null) {
                                Helper.toastShort(this@UpdatePatientProfileActivity, response.body()!!.data.message)

                            } else if (response.body()!!.errors != null) {
                                Helper.toastShort(this@UpdatePatientProfileActivity, response.body()!!.errors)
                            }
                        }

                    } else {
                        Helper.toastNetworkError(this@UpdatePatientProfileActivity)
                    }
                }

                override fun onFailure(call: Call<ResponseCreatePatientProfile>, t: Throwable) {
                    Helper.toastShort(this@UpdatePatientProfileActivity, "${t.message}")
                    Helper.hideLoading()
                }
            })
        }

    }

    private fun setupGender() {
        if (rb_male.isChecked) {
            gender = Helper.getGender(1)
            // Helper.toastShort(this, gender)

        } else if (rb_female.isChecked) {
            gender = Helper.getGender(2)
            //Helper.toastShort(this, gender)

        } else if (rb_others.isChecked) {
            gender = Helper.getGender(3)
            // Helper.toastShort(this, gender)
        }
    }


    private fun setupDateOfBirth() {
        ll_date_picker.setOnClickListener {
            showDatePicker()
        }
    }

    private fun setupBloodGroupSpinner() {
        spinnerBloodGroup = findViewById<Spinner>(R.id.spinner_blood_group)
        val bloodGroupAdapter = ArrayAdapter(this@UpdatePatientProfileActivity, R.layout.support_simple_spinner_dropdown_item, bloodGroupList)
        bloodGroupAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item)
        spinnerBloodGroup.adapter = bloodGroupAdapter

        spinnerBloodGroup.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("Not yet implemented")
            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                selectedBloodGroup = spinnerBloodGroup.selectedItemPosition + 1.toString().toInt()
                Helper.bloodGroup(selectedBloodGroup!!)
                bloodGroup = Helper.bloodGroup(selectedBloodGroup!!) // bloodGroup will pass on request parameters
            }

        }
    }

    private fun showDatePicker() {
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)


        val dpd = DatePickerDialog(this, DatePickerDialog.OnDateSetListener { view, dayOfMonth, monthOfYear, year ->

            // Display Selected date in textbox
            Helper.showLog(TAG, "$dayOfMonth-$monthOfYear-$year")
            dob = "$dayOfMonth-${monthOfYear + 1}-$year"
            tv_date.text = dob

        }, day, month, year)

        dpd.show()
    }

    private fun setupToolbar() {
        setSupportActionBar(toolbar_update_profile_activity)
        val actionBar = supportActionBar
        actionBar!!.title = "Update Profile"
        actionBar.setDisplayHomeAsUpEnabled(true)
    }


    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}
