package com.bijumondal.doctorhealthcare.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.bijumondal.doctorhealthcare.R
import com.bijumondal.doctorhealthcare.fragments.HomeFragment
import com.bijumondal.doctorhealthcare.fragments.MyAppointmentFragment
import com.bijumondal.doctorhealthcare.fragments.ProfileFragment
import com.bijumondal.doctorhealthcare.utils.HealthCarePreference
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    companion object {
        private const val TAG: String = "MainActivity"
    }

    private val homeFragment = "HomeFragment"
    private val myAppointmentFragment = "MyAppointmentFragment"
    private val profileFragment = "ProfileFragment"

    private lateinit var mPreference: HealthCarePreference

    private val mOnNavigationItemSelectedListener =
        BottomNavigationView.OnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_home -> {
                    /*if (supportFragmentManager.findFragmentByTag(homeFragment) != null) {
                        supportFragmentManager.beginTransaction().show(supportFragmentManager.findFragmentByTag(homeFragment)!!).commit()
                    } else {
                        supportFragmentManager.beginTransaction().add(R.id.main_frame, HomeFragment.newInstance(), homeFragment).commit()
                    }
                    if (supportFragmentManager.findFragmentByTag(myAppointmentFragment) != null) {
                        supportFragmentManager.beginTransaction().hide(supportFragmentManager.findFragmentByTag(myAppointmentFragment)!!).commit()

                    }
                    if (supportFragmentManager.findFragmentByTag(profileFragment) != null) {
                        supportFragmentManager.beginTransaction().hide(supportFragmentManager.findFragmentByTag(profileFragment)!!).commit()
                    }*/
                    val homeFragment = HomeFragment.newInstance()
                    openFragment(homeFragment)
                    return@OnNavigationItemSelectedListener true
                }
                R.id.navigation_my_appointments -> {
                   /* if (supportFragmentManager.findFragmentByTag(myAppointmentFragment) != null) {
                        supportFragmentManager.beginTransaction().show(supportFragmentManager.findFragmentByTag(myAppointmentFragment)!!).commit()
                    } else {
                        supportFragmentManager.beginTransaction().add(R.id.main_frame, MyAppointmentFragment.newInstance(), myAppointmentFragment).commit()
                    }
                    if (supportFragmentManager.findFragmentByTag(homeFragment) != null) {
                        supportFragmentManager.beginTransaction().hide(supportFragmentManager.findFragmentByTag(homeFragment)!!).commit()

                    }
                    if (supportFragmentManager.findFragmentByTag(profileFragment) != null) {
                        supportFragmentManager.beginTransaction().hide(supportFragmentManager.findFragmentByTag(profileFragment)!!).commit()
                    }*/

                    val myApptsFragment = MyAppointmentFragment.newInstance()
                    openFragment(myApptsFragment)
                    return@OnNavigationItemSelectedListener true
                }
                R.id.navigation_profile -> {
                   /* if (supportFragmentManager.findFragmentByTag(profileFragment) != null) {
                        supportFragmentManager.beginTransaction().show(supportFragmentManager.findFragmentByTag(profileFragment)!!).commit()
                    } else {
                        supportFragmentManager.beginTransaction().add(R.id.main_frame, ProfileFragment.newInstance(), profileFragment).commit()
                    }
                    if (supportFragmentManager.findFragmentByTag(homeFragment) != null) {
                        supportFragmentManager.beginTransaction().hide(supportFragmentManager.findFragmentByTag(homeFragment)!!).commit()

                    }
                    if (supportFragmentManager.findFragmentByTag(myAppointmentFragment) != null) {
                        supportFragmentManager.beginTransaction().hide(supportFragmentManager.findFragmentByTag(myAppointmentFragment)!!).commit()
                    }*/
                    val profileFragment = ProfileFragment.newInstance()
                    openFragment(profileFragment)
                    return@OnNavigationItemSelectedListener true
                }
            }

            false
        }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mPreference = HealthCarePreference(this@MainActivity)

        initViews()

    }

    private fun initViews() {
        val bottomNavigation: BottomNavigationView = findViewById(R.id.bottom_navigation)
        bottomNavigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
        defaultFragment(HomeFragment)
    }

    private fun openFragment(fragment: Fragment): Fragment? {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.main_frame, fragment)
        //transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
        transaction.commit()
        return fragment
    }

    private fun defaultFragment(fragment: HomeFragment.Companion) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.main_frame, HomeFragment.newInstance())
        // transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
        transaction.commit()
    }

    override fun onSupportNavigateUp(): Boolean {
        super.onBackPressed()
        return true
    }

}
