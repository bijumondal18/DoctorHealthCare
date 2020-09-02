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
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import androidx.viewpager.widget.ViewPager
import com.bijumondal.doctorhealthcare.R
import com.bijumondal.doctorhealthcare.adapters.AllDoctorsListAdapter
import com.bijumondal.doctorhealthcare.adapters.BannerSliderAdapter
import com.bijumondal.doctorhealthcare.adapters.DoctorDeptAdapter
import com.bijumondal.doctorhealthcare.adapters.TopSpecialistAdapter
import com.bijumondal.doctorhealthcare.api.APIInterface
import com.bijumondal.doctorhealthcare.models.allDoctorsList.Data
import com.bijumondal.doctorhealthcare.models.allDoctorsList.RequestAllDoctorsList
import com.bijumondal.doctorhealthcare.models.allDoctorsList.ResponseAllDoctorsList
import com.bijumondal.doctorhealthcare.models.banners.ResponseBannersList
import com.bijumondal.doctorhealthcare.models.doctorDepartment.ResponseDoctorDepartment
import com.bijumondal.doctorhealthcare.models.doctorProfileDetails.RequestDoctorProfileDetails
import com.bijumondal.doctorhealthcare.models.doctorProfileDetails.ResponseDoctorProfileDetails
import com.bijumondal.doctorhealthcare.models.patientProfileDetails.RequestPatientProfileDetails
import com.bijumondal.doctorhealthcare.models.patientProfileDetails.ResponsePatientProfileDetails
import com.bijumondal.doctorhealthcare.utils.HealthCarePreference
import com.bijumondal.doctorhealthcare.utils.Helper
import com.bijumondal.doctorhealthcare.utils.ImageLoader
import com.google.android.material.navigation.NavigationView
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_doctor_profile.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_patient_profile.*
import kotlinx.android.synthetic.main.app_bar_main.*
import kotlinx.android.synthetic.main.content_main.*
import kotlinx.android.synthetic.main.nav_header_main.view.*
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
    private var NUM_PAGES = 3
    private var timer: Timer = Timer()
    private var handler: Handler = Handler()

    private lateinit var allDoctorsRecyclerView: RecyclerView
    private var allDoctorsList: ArrayList<Data> = ArrayList()
    private lateinit var allDoctorsAdapter: AllDoctorsListAdapter

    private lateinit var topSpecialitiesRecyclerView: RecyclerView
    private var topSpecialitiesList: ArrayList<com.bijumondal.doctorhealthcare.models.doctorDepartment.Data> = ArrayList()
    private lateinit var topSpecialitiesAdapter: TopSpecialistAdapter

    private lateinit var headerView: View

    private var profileName: String = ""
    private var profileImage: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mPreference = HealthCarePreference(this@MainActivity)
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

        if (mPreference.getUserType() == 1) {
            layout_doctor_section.visibility = View.GONE

            getBannerList()

            val layoutManager = LinearLayoutManager(this@MainActivity, LinearLayoutManager.VERTICAL, false)
            allDoctorsRecyclerView.layoutManager = layoutManager
            val request = RequestAllDoctorsList("ALL")
            fetchAllDoctorsList(request)

            val topSpecialitiesLayoutManager = LinearLayoutManager(this@MainActivity, LinearLayoutManager.HORIZONTAL, false)
            topSpecialitiesRecyclerView.layoutManager = topSpecialitiesLayoutManager
            fetchTopSpecialities()

        } else if (mPreference.getUserType() == 2) {
            banner_slider_view_pager.visibility = View.GONE
            layout_all_dr_dept.visibility = View.GONE
            layout_all_doctor.visibility = View.GONE

        }

        card_appointments.setOnClickListener {
            startActivity(Intent(this@MainActivity, MyAppointmentsActivity::class.java))
        }
        card_prescriptions.setOnClickListener {
            startActivity(Intent(this@MainActivity, MyPrescriptionsActivity::class.java))
        }
        card_set_holidays.setOnClickListener {
            startActivity(Intent(this@MainActivity, SetHolidaysActivity::class.java))
        }
        card_set_timings.setOnClickListener {
            startActivity(Intent(this@MainActivity, SetTimingsActivity::class.java))
        }

        fetchProfileDetails()

    }

    private fun fetchProfileDetails() {
        if (mPreference.getUserType() == 1) {
            val request = RequestPatientProfileDetails(mPreference.getUserId().toString())
            fetchPatientProfileDetails(request)

        } else if (mPreference.getUserType() == 2) {
            val request = RequestDoctorProfileDetails(mPreference.getUserId().toString())
            fetchDoctorProfileDetails(request)
        }

    }

    private fun fetchDoctorProfileDetails(request: RequestDoctorProfileDetails) {
        if (Helper.isConnectedToInternet(this@MainActivity)) {
            val call: Call<ResponseDoctorProfileDetails> = APIInterface.create().getDoctorProfileDetails(request)
            Helper.showLog(TAG, " request :- ${Gson().toJson(request)}")
            call.enqueue(object : Callback<ResponseDoctorProfileDetails> {
                override fun onResponse(
                    call: Call<ResponseDoctorProfileDetails>,
                    response: Response<ResponseDoctorProfileDetails>
                ) {
                    if (response.isSuccessful) {
                        Helper.showLog(TAG, "Response : ${response.body()}")
                        if (response.body()!!.success) {
                            val mData = response.body()!!.data
                            if (mData != null) {

                                if (mData.photo != null) {
                                    ImageLoader.loadCircleImageFromUrl(headerView.iv_profile_image_nav_header, mData.photo, R.color.colorTransparent)
                                }
                                if (mData.name != null) {
                                    profileName = mData.name
                                    headerView.tv_profile_name_nav_header.text = profileName
                                }

                                if (mData.hospital_id != null) {
                                    mPreference.setHospitalId(mData.hospital_id)
                                }

                            }

                            if (response.body()!!.data.message != null) {
                                Helper.toastShort(this@MainActivity, response.body()!!.data.message)

                            } else if (response.body()!!.errors != null) {
                                Helper.toastShort(this@MainActivity, response.body()!!.errors)
                            }

                        } else {
                            if (response.body()!!.data.message != null) {
                                Helper.toastShort(this@MainActivity, response.body()!!.data.message)

                            } else if (response.body()!!.errors != null) {
                                Helper.toastShort(this@MainActivity, response.body()!!.errors)
                            }
                        }

                    } else {
                        Helper.toastNetworkError(this@MainActivity)
                    }
                }

                override fun onFailure(call: Call<ResponseDoctorProfileDetails>, t: Throwable) {
                    Helper.toastShort(this@MainActivity, "${t.message}")
                }
            })
        }
    }

    private fun fetchPatientProfileDetails(request: RequestPatientProfileDetails) {
        if (Helper.isConnectedToInternet(this@MainActivity)) {
            val call: Call<ResponsePatientProfileDetails> = APIInterface.create().getPatientProfileDetails(request)
            Helper.showLog(TAG, " request :- ${Gson().toJson(request)}")
            call.enqueue(object : Callback<ResponsePatientProfileDetails> {
                override fun onResponse(
                    call: Call<ResponsePatientProfileDetails>,
                    response: Response<ResponsePatientProfileDetails>
                ) {
                    if (response.isSuccessful) {
                        Helper.showLog(TAG, "Response : ${response.body()}")
                        if (response.body()!!.success) {
                            val mData = response.body()!!.data
                            if (mData != null) {

                                if (mData.photo != null) {
                                    ImageLoader.loadCircleImageFromUrl(headerView.iv_profile_image_nav_header, mData.photo, R.color.colorTransparent)
                                }

                                if (mData.name != null) {
                                    profileName = mData.name
                                    headerView.tv_profile_name_nav_header.text = profileName
                                }

                            }

                            if (response.body()!!.data.message != null) {
                                Helper.toastShort(this@MainActivity, response.body()!!.data.message)

                            } else if (response.body()!!.errors != null) {
                                Helper.toastShort(this@MainActivity, response.body()!!.errors)
                            }

                        } else {
                            if (response.body()!!.data.message != null) {
                                Helper.toastShort(this@MainActivity, response.body()!!.data.message)

                            } else if (response.body()!!.errors != null) {
                                Helper.toastShort(this@MainActivity, response.body()!!.errors)
                            }
                        }

                    } else {
                        Helper.toastNetworkError(this@MainActivity)
                    }
                }

                override fun onFailure(call: Call<ResponsePatientProfileDetails>, t: Throwable) {
                    Helper.toastShort(this@MainActivity, "${t.message}")
                }
            })
        }
    }

    private fun fetchTopSpecialities() {
        if (Helper.isConnectedToInternet(this@MainActivity)) {
            val call: Call<ResponseDoctorDepartment> = APIInterface.create().getDoctorDepartments()
            call.enqueue(object : Callback<ResponseDoctorDepartment> {
                override fun onResponse(
                    call: Call<ResponseDoctorDepartment>,
                    response: Response<ResponseDoctorDepartment>
                ) {
                    if (response.isSuccessful) {
                        Helper.showLog(TAG, "Response : ${response.body()}")
                        if (response.body()!!.success) {
                            val mData = response.body()!!.data
                            if (mData != null) {
                                topSpecialitiesList = mData as ArrayList<com.bijumondal.doctorhealthcare.models.doctorDepartment.Data>
                                topSpecialitiesAdapter = TopSpecialistAdapter(topSpecialitiesList, this@MainActivity)
                                topSpecialitiesRecyclerView.adapter = topSpecialitiesAdapter
                                topSpecialitiesAdapter.notifyDataSetChanged()
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

                override fun onFailure(call: Call<ResponseDoctorDepartment>, t: Throwable) {
                    Helper.toastShort(this@MainActivity, "${t.message}")
                }
            })
        }

    }

    private fun fetchAllDoctorsList(request: RequestAllDoctorsList) {
        if (Helper.isConnectedToInternet(this@MainActivity)) {
            val call: Call<ResponseAllDoctorsList> = APIInterface.create().getAllDoctorsList(request)
            call.enqueue(object : Callback<ResponseAllDoctorsList> {
                override fun onResponse(
                    call: Call<ResponseAllDoctorsList>,
                    response: Response<ResponseAllDoctorsList>
                ) {
                    if (response.isSuccessful) {
                        Helper.showLog(TAG, "Response : ${response.body()}")
                        if (response.body()!!.success) {
                            val mData = response.body()!!.data
                            if (mData != null) {
                                allDoctorsList = mData as ArrayList<Data>
                                allDoctorsAdapter = AllDoctorsListAdapter(allDoctorsList, this@MainActivity)
                                allDoctorsRecyclerView.adapter = allDoctorsAdapter
                                allDoctorsAdapter.notifyDataSetChanged()
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

                override fun onFailure(call: Call<ResponseAllDoctorsList>, t: Throwable) {
                    Helper.toastShort(this@MainActivity, "${t.message}")
                }

            })
        }
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

                            if (response.body()!!.data.message != null) {
                                Helper.toastShort(this@MainActivity, response.body()!!.data.message)

                            } else if (response.body()!!.errors != null) {
                                Helper.toastShort(this@MainActivity, response.body()!!.errors)

                            }

                        } else {
                            if (response.body()!!.data.message != null) {
                                Helper.toastShort(this@MainActivity, response.body()!!.data.message)

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
        navigationView = findViewById(R.id.nav_view)
        navigationView.setNavigationItemSelectedListener(this)
        bannerSliderViewPager = findViewById(R.id.banner_slider_view_pager)
        allDoctorsRecyclerView = findViewById(R.id.rv_all_doctors)
        topSpecialitiesRecyclerView = findViewById(R.id.rv_dr_dept)
        headerView = nav_view.inflateHeaderView(R.layout.nav_header_main)
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
                    mPreference.setIsLoggedIn(false)
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
