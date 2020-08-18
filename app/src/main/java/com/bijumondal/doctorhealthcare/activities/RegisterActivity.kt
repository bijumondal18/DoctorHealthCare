package com.bijumondal.doctorhealthcare.activities

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import com.bijumondal.doctorhealthcare.R
import com.bijumondal.doctorhealthcare.api.APIInterface
import com.bijumondal.doctorhealthcare.models.doctorRegistration.RequestDoctorRegistration
import com.bijumondal.doctorhealthcare.models.doctorRegistration.ResponseDoctorRegistration
import com.bijumondal.doctorhealthcare.models.patientRegistration.RequestPatientRegistration
import com.bijumondal.doctorhealthcare.models.patientRegistration.ResponsePatientRegistration
import com.bijumondal.doctorhealthcare.utils.HealthCarePreference
import com.bijumondal.doctorhealthcare.utils.Helper
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_register.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterActivity : AppCompatActivity() {

    companion object {
        private const val TAG = "RegisterActivity"
    }

    private lateinit var mPreference: HealthCarePreference

    private var firstName: String = ""
    private var lastName: String = ""
    private var email: String = ""
    private var mobile: String = ""
    private var password: String = ""
    private var cnfPassword: String = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        mPreference = HealthCarePreference(this@RegisterActivity)

        validateRegistration()

        tv_goto_sign_in.setOnClickListener {
            startActivity(Intent(this@RegisterActivity, LoginActivity::class.java))
        }

        btn_sign_up.setOnClickListener {

            if (mPreference.getUserType() == 1) {

                if (firstName.isNotEmpty() && lastName.isNotEmpty() && mobile.isNotEmpty() && password.isNotEmpty() && cnfPassword.isNotEmpty()) {
                    if (password == cnfPassword) {

                        val request = RequestPatientRegistration(email, firstName, lastName, mobile, password)
                        doPatientRegistration(request)

                    } else {
                        Helper.toastShort(this@RegisterActivity, "Password doesn't match!")
                    }

                } else {
                    Helper.toastShort(this@RegisterActivity, "Field's should not be empty!")

                }

            } else if (mPreference.getUserType() == 2) {

                if (firstName.isNotEmpty() && lastName.isNotEmpty() && mobile.isNotEmpty() && password.isNotEmpty() && cnfPassword.isNotEmpty()) {
                    if (password == cnfPassword) {

                        val request = RequestDoctorRegistration(email, firstName, lastName, mobile, password)
                        doDoctorRegistration(request)

                    } else {
                        Helper.toastShort(this@RegisterActivity, "Password doesn't match!")
                    }

                } else {
                    Helper.toastShort(this@RegisterActivity, "Field's should not be empty!")

                }
            }

        }

    }

    private fun doDoctorRegistration(request: RequestDoctorRegistration) {
        if (Helper.isConnectedToInternet(this@RegisterActivity)) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                Helper.showLoading(this)
            }
            val call: Call<ResponseDoctorRegistration> = APIInterface.create().doctorRegistration(request)
            Helper.showLog(TAG, " request :- ${Gson().toJson(request)}")
            call.enqueue(object : Callback<ResponseDoctorRegistration> {
                override fun onResponse(
                    call: Call<ResponseDoctorRegistration>,
                    response: Response<ResponseDoctorRegistration>
                ) {
                    Helper.hideLoading()
                    if (response.isSuccessful) {
                        Helper.showLog(TAG, "Response : ${response.body()}")
                        if (response.body()!!.success) {
                            val mData = response.body()!!.data
                            if (mData != null) {

                                if (mData.doctor_id != null) {
                                    val doctorId = mData.doctor_id
                                    mPreference.setUserId(doctorId)
                                }
                                startActivity(Intent(this@RegisterActivity, MainActivity::class.java))
                                finish()
                                mPreference.setIsLoggedIn(true)
                            }

                            if (response.body()!!.data.message != null) {
                                Helper.toastShort(this@RegisterActivity, response.body()!!.data.message)

                            } else if (response.body()!!.errors != null) {
                                Helper.toastShort(this@RegisterActivity, response.body()!!.errors)
                            }

                        } else {
                            if (response.body()!!.data.message != null) {
                                Helper.toastShort(this@RegisterActivity, response.body()!!.data.message)

                            } else if (response.body()!!.errors != null) {
                                Helper.toastShort(this@RegisterActivity, response.body()!!.errors)
                            }
                        }

                    } else {
                        Helper.toastNetworkError(this@RegisterActivity)
                    }
                }

                override fun onFailure(call: Call<ResponseDoctorRegistration>, t: Throwable) {
                    Helper.toastShort(this@RegisterActivity, "${t.message}")
                    Helper.hideLoading()
                }
            })
        }

    }

    private fun doPatientRegistration(request: RequestPatientRegistration) {
        if (Helper.isConnectedToInternet(this@RegisterActivity)) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                Helper.showLoading(this)
            }
            val call: Call<ResponsePatientRegistration> = APIInterface.create().patientRegistration(request)
            Helper.showLog(TAG, " request :- ${Gson().toJson(request)}")
            call.enqueue(object : Callback<ResponsePatientRegistration> {
                override fun onResponse(
                    call: Call<ResponsePatientRegistration>,
                    response: Response<ResponsePatientRegistration>
                ) {
                    Helper.hideLoading()
                    if (response.isSuccessful) {
                        Helper.showLog(TAG, "Response : ${response.body()}")
                        if (response.body()!!.success) {
                            val mData = response.body()!!.data
                            if (mData != null) {

                                if (mData.patient_id != null) {
                                    val patientId = mData.patient_id
                                    mPreference.setUserId(patientId)

                                }
                                startActivity(Intent(this@RegisterActivity, MainActivity::class.java))
                                finish()
                                mPreference.setIsLoggedIn(true)
                            }

                            if (response.body()!!.data.message != null) {
                                Helper.toastShort(this@RegisterActivity, response.body()!!.data.message)

                            } else if (response.body()!!.errors != null) {
                                Helper.toastShort(this@RegisterActivity, response.body()!!.errors)
                            }

                        } else {
                            if (response.body()!!.data.message != null) {
                                Helper.toastShort(this@RegisterActivity, response.body()!!.data.message)

                            } else if (response.body()!!.errors != null) {
                                Helper.toastShort(this@RegisterActivity, response.body()!!.errors)
                            }
                        }

                    } else {
                        Helper.toastNetworkError(this@RegisterActivity)
                    }
                }

                override fun onFailure(call: Call<ResponsePatientRegistration>, t: Throwable) {
                    Helper.toastShort(this@RegisterActivity, "${t.message}")
                    Helper.hideLoading()
                }
            })
        }

    }

    private fun validateRegistration() {

        edt_first_name.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                firstName = edt_first_name.text.trim().toString()
            }

        })

        edt_last_name.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                lastName = edt_last_name.text.trim().toString()
            }

        })

        edt_email.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                email = edt_email.text.trim().toString()
            }

        })

        edt_phone_number.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                mobile = edt_phone_number.text.trim().toString()
            }

        })

        edt_password.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                password = edt_password.text.trim().toString()
            }

        })

        edt_cnf_password.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                cnfPassword = edt_cnf_password.text.trim().toString()
            }

        })


    }
}
