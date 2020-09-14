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
import com.bijumondal.doctorhealthcare.activities.CreateDoctorProfileActivity
import com.bijumondal.doctorhealthcare.activities.MainActivity
import com.bijumondal.doctorhealthcare.api.APIInterface
import com.bijumondal.doctorhealthcare.models.doctorRegistration.RequestDoctorRegistration
import com.bijumondal.doctorhealthcare.models.doctorRegistration.ResponseDoctorRegistration
import com.bijumondal.doctorhealthcare.models.patientRegistration.RequestPatientRegistration
import com.bijumondal.doctorhealthcare.models.patientRegistration.ResponsePatientRegistration
import com.bijumondal.doctorhealthcare.utils.HealthCarePreference
import com.bijumondal.doctorhealthcare.utils.Helper
import com.google.gson.Gson
import kotlinx.android.synthetic.main.fragment_signup.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class SignupFragment : Fragment() {

    companion object {
        fun newInstance(): SignupFragment = SignupFragment()
        private const val TAG: String = "SignupFragment"
    }

    private lateinit var btnSignIn: TextView
    private lateinit var parentFrameLayout: FrameLayout
    private lateinit var mPreference: HealthCarePreference

    private var firstName: String = ""
    private var lastName: String = ""
    private var email: String = ""
    private var mobile: String = ""
    private var password: String = ""
    private var cnfPassword: String = ""


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_signup, container, false)
        mPreference = HealthCarePreference(context!!)
        btnSignIn = view.findViewById(R.id.tv_goto_sign_in)
        parentFrameLayout = activity!!.findViewById(R.id.main_frame_layout)


        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        btnSignIn.setOnClickListener {
            setFragment(SigninFragment.newInstance())
        }

        validateRegistrationField()

        btn_sign_up.setOnClickListener {

            doSignUpValidation()

        }

    }

    private fun doSignUpValidation() {
        if (mPreference.getUserType() == 1) {

            if (firstName.isNotEmpty() && lastName.isNotEmpty() && mobile.isNotEmpty() && password.isNotEmpty() && cnfPassword.isNotEmpty()) {
                if (password == cnfPassword) {

                    val request = RequestPatientRegistration(email, firstName, lastName, mobile, password)
                    doPatientRegistration(request)

                } else {
                    Helper.toastShort(context!!, "Password doesn't match!")
                }

            } else {
                Helper.toastShort(context!!, "Field's should not be empty!")

            }

        } else if (mPreference.getUserType() == 2) {

            if (firstName.isNotEmpty() && lastName.isNotEmpty() && mobile.isNotEmpty() && password.isNotEmpty() && cnfPassword.isNotEmpty()) {
                if (password == cnfPassword) {

                    val request = RequestDoctorRegistration(email, firstName, lastName, mobile, password)
                    doDoctorRegistration(request)

                } else {
                    Helper.toastShort(context!!, "Password doesn't match!")
                }

            } else {
                Helper.toastShort(context!!, "Field's should not be empty!")

            }
        }

    }

    private fun validateRegistrationField() {

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
                mPreference.setEmail(email)
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

    private fun doDoctorRegistration(request: RequestDoctorRegistration) {
        if (Helper.isConnectedToInternet(context!!)) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                Helper.showLoading(context!!)
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
                                mPreference.setFirstName(firstName)
                                mPreference.setLastName(lastName)
                                mPreference.setEmail(email)
                                mPreference.setNumber(mobile)

                                startActivity(Intent(context!!, CreateDoctorProfileActivity::class.java))
                                activity!!.finish()

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

                override fun onFailure(call: Call<ResponseDoctorRegistration>, t: Throwable) {
                    Helper.toastShort(context!!, "${t.message}")
                    Helper.hideLoading()
                }
            })
        }

    }

    private fun doPatientRegistration(request: RequestPatientRegistration) {
        if (Helper.isConnectedToInternet(context!!)) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                Helper.showLoading(context!!)
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

                                mPreference.setFirstName(firstName)
                                mPreference.setLastName(lastName)
                                mPreference.setEmail(email)
                                mPreference.setNumber(mobile)

                                mPreference.setIsLoggedIn(true)
                                startActivity(Intent(context!!, MainActivity::class.java))
                                activity!!.finish()

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

                override fun onFailure(call: Call<ResponsePatientRegistration>, t: Throwable) {
                    Helper.toastShort(context!!, "${t.message}")
                    Helper.hideLoading()
                }
            })
        }

    }

    private fun setFragment(fragment: Fragment) {
        val fragmentTransaction = activity!!.supportFragmentManager.beginTransaction()
        fragmentTransaction.setCustomAnimations(R.anim.left_in, R.anim.right_out)
        fragmentTransaction.replace(parentFrameLayout.id, fragment)
        fragmentTransaction.commit()
    }

}
