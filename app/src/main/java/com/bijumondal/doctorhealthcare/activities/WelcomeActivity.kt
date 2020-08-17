package com.bijumondal.doctorhealthcare.activities

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bijumondal.doctorhealthcare.R
import com.bijumondal.doctorhealthcare.adapters.UserTypeAdapter
import com.bijumondal.doctorhealthcare.models.userType.UserType
import com.bijumondal.doctorhealthcare.utils.HealthCarePreference
import com.bijumondal.doctorhealthcare.utils.Helper
import kotlinx.android.synthetic.main.activity_welcome.*

class WelcomeActivity : AppCompatActivity() {

    var userTypeAdapter: UserTypeAdapter? = null
    var userTypeList: ArrayList<UserType> = ArrayList()
    private lateinit var mRecyclerView: RecyclerView
    private lateinit var mPreferences: HealthCarePreference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome)
        mPreferences = HealthCarePreference(this@WelcomeActivity)
        mRecyclerView = findViewById(R.id.rv_user_type)

        btn_agree_and_continue.setOnClickListener {
            if (mPreferences.getUserType() == 0) {
                startActivity(Intent(this@WelcomeActivity, LoginActivity::class.java))
                finish()
            } else if (mPreferences.getUserType() == 1) {
                startActivity(Intent(this@WelcomeActivity, LoginActivity::class.java))
                finish()
            } else {
                Helper.toastShort(this@WelcomeActivity, "Please choose a user type!")
            }

        }

        userTypeList.add(UserType(1, R.drawable.patient_thumbnail, "Patient"))
        userTypeList.add(UserType(2, R.drawable.doctor_thumbnail, "Doctor"))

        val layoutManager = GridLayoutManager(this@WelcomeActivity, 2)
        mRecyclerView.layoutManager = layoutManager

        userTypeAdapter = UserTypeAdapter(userTypeList, this@WelcomeActivity)
        mRecyclerView.adapter = userTypeAdapter
        userTypeAdapter!!.notifyDataSetChanged()

    }


}
