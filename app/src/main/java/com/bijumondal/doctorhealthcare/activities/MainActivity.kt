package com.bijumondal.doctorhealthcare.activities

import android.content.Intent
import android.os.Build
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
import androidx.viewpager.widget.ViewPager
import com.bijumondal.doctorhealthcare.R
import com.bijumondal.doctorhealthcare.adapters.BannerSliderAdapter
import com.bijumondal.doctorhealthcare.api.APIInterface
import com.bijumondal.doctorhealthcare.models.banners.ResponseBannersList
import com.bijumondal.doctorhealthcare.utils.HealthCarePreference
import com.bijumondal.doctorhealthcare.utils.Helper
import com.google.android.material.navigation.NavigationView
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    companion object {
        private const val TAG: String = "MainActivity"
    }

    private lateinit var mPreference: HealthCarePreference
    private lateinit var navigationView: NavigationView
    private lateinit var navigationMenu: Menu
    private lateinit var drawerLayout: DrawerLayout

    private lateinit var bannerSliderViewPager: ViewPager
    private var bannerList: ArrayList<String> = ArrayList()
    private lateinit var bannerSliderAdapeter: BannerSliderAdapter
    private var currentPage = 0
    private var NUM_PAGES = 4
    private var timer: Timer = Timer()
    private var handler: Handler = Handler()

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

        initViews()
        setDrawerLayout()
        getBannerList()

    }

    private fun getBannerList() {
        if (Helper.isConnectedToInternet(this@MainActivity)) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                Helper.showLoading(this)
            }
            val call: Call<ResponseBannersList> = APIInterface.create().getBanners()
            call.enqueue(object : Callback<ResponseBannersList> {
                override fun onResponse(
                    call: Call<ResponseBannersList>,
                    response: Response<ResponseBannersList>
                ) {
                    Helper.hideLoading()
                    if (response.isSuccessful) {
                        Helper.showLog(TAG, "Response : ${response.body()}")
                        if (response.body()!!.success) {
                            val mData = response.body()!!.data.bannername
                            if (mData != null) {

                                for (i in 0..mData.size) {
                                    bannerList = mData as ArrayList<String>
                                    bannerSliderAdapeter = BannerSliderAdapter(this@MainActivity, bannerList)
                                    bannerSliderViewPager.adapter = bannerSliderAdapeter
                                    bannerSliderAdapeter.notifyDataSetChanged()
                                    bannerSliderViewPager.clipToPadding = false
                                    bannerSliderViewPager.pageMargin = 30

                                    setupAutoSlider()
                                }

                            }

                            if (response.body()!!.message != null) {
                                Helper.toastShort(this@MainActivity, response.body()!!.message)

                            } else if (response.body()!!.errors != null) {
                                Helper.toastShort(this@MainActivity, response.body()!!.errors)

                            }

                        } else {
                            if (response.body()!!.message != null) {
                                Helper.toastShort(this@MainActivity, response.body()!!.message)

                            } else if (response.body()!!.errors != null) {
                                Helper.toastShort(this@MainActivity, response.body()!!.errors)
                            }
                        }

                    } else {
                        Helper.toastNetworkError(this@MainActivity)
                    }
                }

                override fun onFailure(call: Call<ResponseBannersList>, t: Throwable) {
                    Helper.toastShort(this@MainActivity, "${t.message}")
                    Helper.hideLoading()
                }

            })
        }

    }

    private fun initViews() {
        mPreference = HealthCarePreference(this@MainActivity)
        navigationView = findViewById(R.id.nav_view)
        navigationView.setNavigationItemSelectedListener(this)
        bannerSliderViewPager = findViewById(R.id.banner_slider_view_pager)
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

    private fun setupAutoSlider() {
        val update: Runnable = Runnable {
            if (currentPage == NUM_PAGES) {
                currentPage = 0
            }
            bannerSliderViewPager.setCurrentItem(currentPage++, true)
        }

        timer.schedule(object : TimerTask() {
            override fun run() {
                handler.post(update)
            }
        }, 4000, 3000)
    }


    override fun onSupportNavigateUp(): Boolean {
        super.onBackPressed()
        return true
    }

}
