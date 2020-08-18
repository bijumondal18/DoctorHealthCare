package com.bijumondal.doctorhealthcare.activities

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import com.bijumondal.doctorhealthcare.R
import com.bijumondal.doctorhealthcare.api.APIInterface
import com.bijumondal.doctorhealthcare.models.doctorLogin.RequestDoctorLogin
import com.bijumondal.doctorhealthcare.models.doctorLogin.ResponseDoctorLogin
import com.bijumondal.doctorhealthcare.models.patientLogin.RequestPatientLogin
import com.bijumondal.doctorhealthcare.models.patientLogin.ResponsePatientLogin
import com.bijumondal.doctorhealthcare.models.patientRegistration.ResponsePatientRegistration
import com.bijumondal.doctorhealthcare.utils.HealthCarePreference
import com.bijumondal.doctorhealthcare.utils.Helper
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_login.edt_email
import kotlinx.android.synthetic.main.activity_register.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {

    companion object {
        private const val TAG = "LoginActivity"
    }

    private var email: String = ""
    private var password: String = ""
    private lateinit var mPreference: HealthCarePreference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        mPreference = HealthCarePreference(this@LoginActivity)

        validateLogin()

        btn_sign_in.setOnClickListener {

            if (mPreference.getUserType() == 1) {

                if (email.isNotEmpty() && password.isNotEmpty()) {
                    val request = RequestPatientLogin(password, email)
                    doPatientLogin(request)
                } else {
                    Helper.toastShort(this@LoginActivity, "Field's should not be empty!")
                }

            } else if (mPreference.getUserType() == 2) {

                if (email.isNotEmpty() && password.isNotEmpty()) {
                    val requestDoctorLogin = RequestDoctorLogin(password, email)
                    doDoctorLogin(requestDoctorLogin)
                } else {
                    Helper.toastShort(this@LoginActivity, "Field's should not be empty!")
                }

            }


        }

        tv_goto_sign_up.setOnClickListener {
            startActivity(Intent(this@LoginActivity, RegisterActivity::class.java))
        }


    }

    private fun doDoctorLogin(request: RequestDoctorLogin) {

        if (Helper.isConnectedToInternet(this@LoginActivity)) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                Helper.showLoading(this)
            }
            val call: Call<ResponseDoctorLogin> = APIInterface.create().doctorLogin(request)
            Helper.showLog(TAG, " request :- ${Gson().toJson(request)}")
            call.enqueue(object : Callback<ResponseDoctorLogin> {
                override fun onResponse(
                    call: Call<ResponseDoctorLogin>,
                    response: Response<ResponseDoctorLogin>
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

                                startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                                finish()
                                mPreference.setIsLoggedIn(true)
                            }

                            if (response.body()!!.data.message != null) {
                                Helper.toastShort(this@LoginActivity, response.body()!!.data.message)

                            } else if (response.body()!!.errors != null) {
                                Helper.toastShort(this@LoginActivity, response.body()!!.errors)
                            }

                        } else {
                            if (response.body()!!.data.message != null) {
                                Helper.toastShort(this@LoginActivity, response.body()!!.data.message)

                            } else if (response.body()!!.errors != null) {
                                Helper.toastShort(this@LoginActivity, response.body()!!.errors)
                            }
                        }

                    } else {
                        Helper.toastNetworkError(this@LoginActivity)
                    }
                }

                override fun onFailure(call: Call<ResponseDoctorLogin>, t: Throwable) {
                    Helper.toastShort(this@LoginActivity, "${t.message}")
                    Helper.hideLoading()
                }
            })
        }
    }

    private fun doPatientLogin(request: RequestPatientLogin) {
        if (Helper.isConnectedToInternet(this@LoginActivity)) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                Helper.showLoading(this)
            }
            val call: Call<ResponsePatientLogin> = APIInterface.create().patientLogin(request)
            Helper.showLog(TAG, " request :- ${Gson().toJson(request)}")
            call.enqueue(object : Callback<ResponsePatientLogin> {
                override fun onResponse(
                    call: Call<ResponsePatientLogin>,
                    response: Response<ResponsePatientLogin>
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
                                startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                                finish()
                                mPreference.setIsLoggedIn(true)
                            }

                            if (response.body()!!.data.message != null) {
                                Helper.toastShort(this@LoginActivity, response.body()!!.data.message)

                            } else if (response.body()!!.errors != null) {
                                Helper.toastShort(this@LoginActivity, response.body()!!.errors)
                            }

                        } else {
                            if (response.body()!!.data.message != null) {
                                Helper.toastShort(this@LoginActivity, response.body()!!.data.message)

                            } else if (response.body()!!.errors != null) {
                                Helper.toastShort(this@LoginActivity, response.body()!!.errors)
                            }
                        }

                    } else {
                        Helper.toastNetworkError(this@LoginActivity)
                    }
                }

                override fun onFailure(call: Call<ResponsePatientLogin>, t: Throwable) {
                    Helper.toastShort(this@LoginActivity, "${t.message}")
                    Helper.hideLoading()
                }
            })
        }

    }

    private fun validateLogin() {
        edt_email.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                email = edt_email.text.trim().toString()
            }

        })

        edt_password_login.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                password = edt_password_login.text.trim().toString()
            }

        })
    }
}
