package com.bijumondal.doctorhealthcare.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.FrameLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.bijumondal.doctorhealthcare.R
import com.bijumondal.doctorhealthcare.fragments.SigninFragment
import com.bijumondal.doctorhealthcare.utils.HealthCarePreference

class RegistrationActivity : AppCompatActivity() {

    companion object {
        private const val TAG = "RegistrationActivity"
    }

    private lateinit var mPreference: HealthCarePreference
    private lateinit var frameLayout: FrameLayout


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)
        mPreference = HealthCarePreference(this@RegistrationActivity)
        frameLayout = findViewById(R.id.main_frame_layout)

        setFragment(SigninFragment.newInstance())

    }

    private fun setFragment(fragment: Fragment) {
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(frameLayout.id, fragment)
        fragmentTransaction.commit()
    }

}
