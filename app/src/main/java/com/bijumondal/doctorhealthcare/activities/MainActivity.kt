package com.bijumondal.doctorhealthcare.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.Gravity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.bijumondal.doctorhealthcare.R
import com.bijumondal.doctorhealthcare.utils.HealthCarePreference
import com.bijumondal.doctorhealthcare.utils.Helper
import com.google.android.material.navigation.NavigationView
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    companion object {
        private const val TAG: String = "MainActivity"
    }

    private lateinit var mPreference: HealthCarePreference
    private lateinit var navigationView: NavigationView
    private lateinit var navigationMenu: Menu
    private lateinit var drawerLayout: DrawerLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        val actionBar = supportActionBar
        actionBar!!.title = ""
        actionBar.setDisplayShowTitleEnabled(false)
        actionBar.displayOptions = ActionBar.DISPLAY_SHOW_CUSTOM
        val inflater = layoutInflater.inflate(R.layout.abs_layout, null)
        actionBar.setCustomView(
            inflater, ActionBar.LayoutParams(
                ActionBar.LayoutParams.WRAP_CONTENT,
                ActionBar.LayoutParams.WRAP_CONTENT, Gravity.CENTER
            )
        )

        mPreference = HealthCarePreference(this@MainActivity)

        initViews()

        setDrawerLayout()

    }

    private fun initViews() {
        navigationView = findViewById(R.id.nav_view)
        navigationView.setNavigationItemSelectedListener(this)
        val headerView: View = nav_view.inflateHeaderView(R.layout.nav_header_main)
    }

    private fun setDrawerLayout() {
        val toggle = ActionBarDrawerToggle(
            this, drawer_layout, toolbar,
            R.string.drawer_open,
            R.string.drawer_close
        )
        toggle.isDrawerIndicatorEnabled = false
        toggle.setHomeAsUpIndicator(R.drawable.ic_hamburger_menu) //custom drawer hamburger icon
        toggle.setToolbarNavigationClickListener {
            drawer_layout.openDrawer(GravityCompat.START)
        }
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {

            R.id.navigation_home -> {
                drawer_layout.closeDrawer(GravityCompat.START)
            }

            R.id.nav_profile -> {
                if (mPreference.getUserType() == 1) {  // patient profile details
                    Handler().postDelayed(Runnable {
                        startActivity(Intent(this@MainActivity, PatientProfileActivity::class.java))
                    }, 500)
                } else if (mPreference.getUserType() == 2) { // doctor profile details
                    Handler().postDelayed(Runnable {
                        startActivity(Intent(this@MainActivity, DoctorProfileActivity::class.java))
                    }, 500)
                }
            }
            R.id.nav_appointments -> {
                Handler().postDelayed(Runnable {
                    startActivity(Intent(this@MainActivity, MyAppointmentsActivity::class.java))
                }, 500)
            }

            R.id.nav_prescriptions -> {
                Handler().postDelayed(Runnable {
                    startActivity(Intent(this@MainActivity, MyPrescriptionsActivity::class.java))
                }, 500)
            }

            R.id.nav_rate_app -> {
                Helper.openPlayStore(this@MainActivity)
            }

            R.id.nav_share -> {
                Helper.appShare(this@MainActivity)
            }

            R.id.nav_logout -> {
                val builder = AlertDialog.Builder(this)
                builder.setTitle("Logout")
                builder.setMessage("Are you sure want to logout?")
                builder.setPositiveButton("Yes") { dialog, which ->
                    mPreference.clearSharedPreference()
                    startActivity(Intent(this, WelcomeActivity::class.java))
                    finish()
                }
                builder.setNegativeButton("No") { dialog, which ->
                    return@setNegativeButton
                }
                val dialog: AlertDialog = builder.create()
                dialog.show()
            }
        }

        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }


    override fun onSupportNavigateUp(): Boolean {
        super.onBackPressed()
        return true
    }

}
