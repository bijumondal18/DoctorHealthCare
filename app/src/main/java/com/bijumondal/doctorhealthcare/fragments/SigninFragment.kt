package com.bijumondal.doctorhealthcare.fragments

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.TextView

import com.bijumondal.doctorhealthcare.R
import com.bijumondal.doctorhealthcare.activities.MainActivity
import com.bijumondal.doctorhealthcare.api.APIInterface
import com.bijumondal.doctorhealthcare.models.doctorLogin.RequestDoctorLogin
import com.bijumondal.doctorhealthcare.models.doctorLogin.ResponseDoctorLogin
import com.bijumondal.doctorhealthcare.models.patientLogin.RequestPatientLogin
import com.bijumondal.doctorhealthcare.models.patientLogin.ResponsePatientLogin
import com.bijumondal.doctorhealthcare.utils.HealthCarePreference
import com.bijumondal.doctorhealthcare.utils.Helper
import com.google.gson.Gson
import kotlinx.android.synthetic.main.fragment_signin.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class SigninFragment : Fragment() {

    companion object {
        fun newInstance(): SigninFragment = SigninFragment()
        private const val TAG: String = "SigninFragment"
    }

    private lateinit var btnSignUp: TextView
    private lateinit var parentFrameLayout: FrameLayout
    private var email: String = ""
    private var password: String = ""
    private lateinit var mPreference: HealthCarePreference

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_signin, container, false)
        mPreference = HealthCarePreference(context!!)
        btnSignUp = view.findViewById(R.id.tv_goto_sign_up)
        parentFrameLayout = activity!!.findViewById(R.id.main_frame_layout)


        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        btnSignUp.setOnClickListener {
            setFragment(SignupFragment.newInstance())
        }

        validateLoginField()

        btn_sign_in.setOnClickListener {
            doLoginValidation()
        }
    }


    private fun validateLoginField() {
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

    private fun doLoginValidation() {
        if (mPreference.getUserType() == 1) {

            if (email.isNotEmpty() && password.isNotEmpty()) {
                val request = RequestPatientLogin(password, email)
                doPatientLogin(request)
            } else {
                Helper.toastShort(context!!, "Field's should not be empty!")
            }

        } else if (mPreference.getUserType() == 2) {

            if (email.isNotEmpty() && password.isNotEmpty()) {
                val requestDoctorLogin = RequestDoctorLogin(password, email)
                doDoctorLogin(requestDoctorLogin)
            } else {
                Helper.toastShort(context!!, "Field's should not be empty!")
            }

        }
    }

    private fun doDoctorLogin(request: RequestDoctorLogin) {
        if (Helper.isConnectedToInternet(context!!)) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                Helper.showLoading(context!!)
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
                                startActivity(Intent(context!!, MainActivity::class.java))
                                activity!!.finish()
                                mPreference.setIsLoggedIn(true)

                            }

                            if (response.body()!!.data.message != null) {
                                Helper.toastShort(context!!, response.body()!!.data.message)

                            } else if (response.body()!!.errors != null) {
                                Helper.toastShort(context!!, response.body()!!.errors)
                            }

                        } else {
                            if (response.body()!!.data.message != null) {
                                Helper.toastShort(context!!, response.body()!!.data.message)

                            } else if (response.body()!!.errors != null) {
                                Helper.toastShort(context!!, response.body()!!.errors)
                            }
                        }

                    } else {
                        Helper.toastNetworkError(context!!)
                    }
                }

                override fun onFailure(call: Call<ResponseDoctorLogin>, t: Throwable) {
                    Helper.toastShort(context!!, "${t.message}")
                    Helper.hideLoading()
                }
            })
        }
    }

    private fun doPatientLogin(request: RequestPatientLogin) {
        if (Helper.isConnectedToInternet(context!!)) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                Helper.showLoading(context!!)
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
                                startActivity(Intent(context!!, MainActivity::class.java))
                                activity!!.finish()
                                mPreference.setIsLoggedIn(true)

                            }

                            if (response.body()!!.data.message != null) {
                                Helper.toastShort(context!!, response.body()!!.data.message)

                            } else if (response.body()!!.errors != null) {
                                Helper.toastShort(context!!, response.body()!!.errors)
                            }

                        } else {
                            if (response.body()!!.data.message != null) {
                                Helper.toastShort(context!!, response.body()!!.data.message)

                            } else if (response.body()!!.errors != null) {
                                Helper.toastShort(context!!, response.body()!!.errors)
                            }
                        }

                    } else {
                        Helper.toastNetworkError(context!!)
                    }
                }

                override fun onFailure(call: Call<ResponsePatientLogin>, t: Throwable) {
                    Helper.toastShort(context!!, "${t.message}")
                    Helper.hideLoading()
                }
            })
        }

    }

    private fun setFragment(fragment: Fragment) {
        val fragmentTransaction = activity!!.supportFragmentManager.beginTransaction()
        fragmentTransaction.setCustomAnimations(R.anim.right_in, R.anim.left_out)
        fragmentTransaction.replace(parentFrameLayout.id, fragment)
        fragmentTransaction.commit()
    }

}
