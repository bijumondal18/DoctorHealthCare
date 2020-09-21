package com.bijumondal.doctorhealthcare.activities

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bijumondal.doctorhealthcare.R
import com.bijumondal.doctorhealthcare.adapters.TempMedicineListAdapter
import com.bijumondal.doctorhealthcare.api.APIInterface
import com.bijumondal.doctorhealthcare.models.addDoctorPrescriptions.RequestAddPrescriptions
import com.bijumondal.doctorhealthcare.models.addDoctorPrescriptions.ResponseAddPrescriptions
import com.bijumondal.doctorhealthcare.models.addMedicineTemp.Data
import com.bijumondal.doctorhealthcare.models.addMedicineTemp.RequestAddMedicineTemp
import com.bijumondal.doctorhealthcare.models.addMedicineTemp.ResponseAddMedicineTemp
import com.bijumondal.doctorhealthcare.models.delTempMedicine.RequestDeleteTempMedicine
import com.bijumondal.doctorhealthcare.models.delTempMedicine.ResponseDeleteTempMedicine
import com.bijumondal.doctorhealthcare.utils.ClickListener
import com.bijumondal.doctorhealthcare.utils.HealthCarePreference
import com.bijumondal.doctorhealthcare.utils.Helper
import com.bijumondal.doctorhealthcare.utils.RecyclerTouchListener
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_add_prescriptions.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.StringBuilder
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class AddPrescriptionsActivity : AppCompatActivity() {

    companion object {
        private const val TAG: String = "AddPrescriptionsActivity"
        private const val REQUEST_CODE = 342
    }

    private lateinit var mPreference: HealthCarePreference
    private lateinit var mRecyclerView: RecyclerView
    private lateinit var tempMedicineListAdapter: TempMedicineListAdapter
    private var tempMedicineList: ArrayList<Data> = ArrayList()

    private lateinit var spinnerFrequency: Spinner
    var frequencyList = arrayOf(
        "Morning only",
        "Morning + Day",
        "Morning + Day + Night",
        "Morning + Night",
        "Day only",
        "Day + Night",
        "Night only"
    )
    var selectedfrequency: Int? = null
    var frequency = ""
    private lateinit var spinnerDuration: Spinner
    var durationList = arrayOf(
        "1 Day",
        "2 Days",
        "3 Days",
        "5 Days",
        "7 Days",
        "10 Days",
        "15 Days",
        "30 Days"
    )
    var selectedDuration: Int? = null
    var duration = ""

    private lateinit var spinnerInstruction: Spinner
    var instructionList = arrayOf(
        "Before food",
        "After food"
    )
    var selectedInstruction: Int? = null
    var instruction = ""

    private var medicineName: String = ""
    private var medName: String = ""
    private var symptom: String = ""
    private var advice: String = ""
    private var note: String = ""
    private var patientId: String = ""
    private var doctorId: String = ""
    private var prescriptionDate: String = ""


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_prescriptions)

        initViews()

        setupToolbar()

        tv_choose_medicine_name.setOnClickListener {
            startActivityForResult(Intent(this@AddPrescriptionsActivity, MedicineListActivity::class.java), REQUEST_CODE)
        }

        setupFrequencySpinner()

        setupDurationSpinner()

        setupInstructionSpinner()

        validateFields()

        val layoutManager = LinearLayoutManager(this@AddPrescriptionsActivity, LinearLayoutManager.VERTICAL, false)
        mRecyclerView.layoutManager = layoutManager
        val requestAddMedicineTemp = RequestAddMedicineTemp(prescriptionDate, doctorId, "", "", "", "", patientId, "1")
        addTempMedicine(requestAddMedicineTemp)

        btn_add_temp_medicine.setOnClickListener {
            if (!TextUtils.isEmpty(medicineName)) {
                medName = medicineName

                val requestAddMedicineTemp = RequestAddMedicineTemp(prescriptionDate, doctorId, duration, frequency, instruction, medName, patientId, "2")
                addTempMedicine(requestAddMedicineTemp)

            } else {
                Helper.toastShort(this@AddPrescriptionsActivity, "Please add some Medicines!")
            }

        }

        btn_save_prescription.setOnClickListener {
            doAddMedicineValidation()
        }


    }

    private fun addTempMedicine(requestAddMedicineTemp: RequestAddMedicineTemp) {
        if (Helper.isConnectedToInternet(this@AddPrescriptionsActivity)) {
            /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                Helper.showLoading(this@AddPrescriptionsActivity)
            }*/
            val call: Call<ResponseAddMedicineTemp> = APIInterface.create().addTempMedicine(requestAddMedicineTemp)
            Helper.showLog(TAG, " request :- ${Gson().toJson(requestAddMedicineTemp)}")
            call.enqueue(object : Callback<ResponseAddMedicineTemp> {
                override fun onResponse(
                    call: Call<ResponseAddMedicineTemp>,
                    response: Response<ResponseAddMedicineTemp>
                ) {
                    Helper.hideLoading()
                    if (response.isSuccessful) {
                        Helper.showLog(TAG, "Response : ${response.body()}")
                        if (response.body()!!.success) {
                            val mData = response.body()!!.data
                            if (mData != null) {

                                tempMedicineList = mData as ArrayList<Data>
                                if (tempMedicineList.size > 0) {
                                    btn_add_temp_medicine.text = "Add More"
                                    btn_save_prescription.visibility = View.VISIBLE
                                } else {
                                    btn_add_temp_medicine.text = "Add Medicines"
                                    btn_save_prescription.visibility = View.GONE
                                }
                                tv_choose_medicine_name.text = ""

                                tempMedicineListAdapter = TempMedicineListAdapter(tempMedicineList, this@AddPrescriptionsActivity)
                                mRecyclerView.adapter = tempMedicineListAdapter
                                tempMedicineListAdapter.notifyDataSetChanged()

                                mRecyclerView.addOnItemTouchListener(RecyclerTouchListener(this@AddPrescriptionsActivity, mRecyclerView, object : ClickListener {
                                    override fun onClick(view: View?, position: Int) {

                                        val medicineId = tempMedicineList[position].id
                                        val requestDeleteTempMedicine = RequestDeleteTempMedicine(medicineId)
                                        deleteTempMedicine(requestDeleteTempMedicine)

                                    }

                                    override fun onLongClick(view: View?, position: Int) {
                                        //Helper.toastShort(this@BookingActivity, timeSlotsList[position].weekday)
                                    }

                                }))

                            }

                            if (response.body()!!.message != null) {
                                Helper.toastShort(this@AddPrescriptionsActivity, response.body()!!.message)

                            } else if (response.body()!!.errors != null) {
                                Helper.toastShort(this@AddPrescriptionsActivity, response.body()!!.errors)
                            }

                        } else {
                            if (response.body()!!.message != null) {
                                Helper.toastShort(this@AddPrescriptionsActivity, response.body()!!.message)

                            } else if (response.body()!!.errors != null) {
                                Helper.toastShort(this@AddPrescriptionsActivity, response.body()!!.errors)
                            }
                        }

                    } else {
                        Helper.toastNetworkError(this@AddPrescriptionsActivity)
                    }
                }

                override fun onFailure(call: Call<ResponseAddMedicineTemp>, t: Throwable) {
                    Helper.toastShort(this@AddPrescriptionsActivity, "${t.message}")
                    Helper.hideLoading()
                }
            })
        }

    }

    private fun deleteTempMedicine(requestDeleteTempMedicine: RequestDeleteTempMedicine) {
        if (Helper.isConnectedToInternet(this@AddPrescriptionsActivity)) {
            /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                Helper.showLoading(this@AddPrescriptionsActivity)
            }*/
            val call: Call<ResponseDeleteTempMedicine> = APIInterface.create().delTempMedicine(requestDeleteTempMedicine)
            Helper.showLog(TAG, " request :- ${Gson().toJson(requestDeleteTempMedicine)}")
            call.enqueue(object : Callback<ResponseDeleteTempMedicine> {
                override fun onResponse(
                    call: Call<ResponseDeleteTempMedicine>,
                    response: Response<ResponseDeleteTempMedicine>
                ) {
                    Helper.hideLoading()
                    if (response.isSuccessful) {
                        Helper.showLog(TAG, "Response : ${response.body()}")
                        if (response.body()!!.success) {
                            val mData = response.body()!!.data
                            if (mData != null) {

                                val requestAddMedicineTemp = RequestAddMedicineTemp(prescriptionDate, doctorId, duration, frequency, instruction, medName, patientId, "1")
                                addTempMedicine(requestAddMedicineTemp)
                            }

                            if (response.body()!!.data.message != null) {
                                //Helper.toastShort(this@AddPrescriptionsActivity, response.body()!!.data.message)
                                Helper.showLog(TAG, response.body()!!.data.message)

                            } else if (response.body()!!.errors != null) {
                                // Helper.toastShort(this@AddPrescriptionsActivity, response.body()!!.errors)
                                Helper.showLog(TAG, response.body()!!.errors)

                            }

                        } else {
                            if (response.body()!!.data.message != null) {
                                Helper.toastShort(this@AddPrescriptionsActivity, response.body()!!.data.message)

                            } else if (response.body()!!.errors != null) {
                                Helper.toastShort(this@AddPrescriptionsActivity, response.body()!!.errors)
                            }
                        }

                    } else {
                        Helper.toastNetworkError(this@AddPrescriptionsActivity)
                    }
                }

                override fun onFailure(call: Call<ResponseDeleteTempMedicine>, t: Throwable) {
                    Helper.toastShort(this@AddPrescriptionsActivity, "${t.message}")
                    Helper.hideLoading()
                }
            })
        }

    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun initViews() {
        mPreference = HealthCarePreference(this@AddPrescriptionsActivity)
        mRecyclerView = findViewById(R.id.rv_temp_medicine_list)

        if (intent.hasExtra("patientId") != null) {
            patientId = intent.getStringExtra("patientId").toString()
        }
        if (mPreference.getUserId() != null) {
            doctorId = mPreference.getUserId().toString()
        }

        prescriptionDate = Helper.getCurrentDate().toString()

        if (intent.hasExtra("advice") != null) {
            edt_advice.setText(intent.getStringExtra("advice"))
            advice = intent.getStringExtra("advice").toString()
        }

        if (intent.hasExtra("note") != null) {
            edt_note.setText(intent.getStringExtra("note"))
            note = intent.getStringExtra("note").toString()
        }

        if (intent.hasExtra("symptom") != null) {
            edt_symptom.setText(intent.getStringExtra("symptom"))
            symptom = intent.getStringExtra("symptom").toString()
        }

    }

    private fun setupInstructionSpinner() {
        spinnerInstruction = findViewById<Spinner>(R.id.spinner_instruction)
        val instructionListAdapter = ArrayAdapter(this@AddPrescriptionsActivity, R.layout.support_simple_spinner_dropdown_item, instructionList)
        instructionListAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item)
        spinnerInstruction.adapter = instructionListAdapter

        spinnerInstruction.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("Not yet implemented")
            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                selectedInstruction = spinnerInstruction.selectedItemPosition + 1.toString().toInt()
                Helper.instructionsList(selectedInstruction!!)
                instruction = Helper.instructionsList(selectedInstruction!!) // bloodGroup will pass on request parameters
            }

        }
    }

    private fun setupDurationSpinner() {
        spinnerDuration = findViewById<Spinner>(R.id.spinner_duration)
        val durationListAdapter = ArrayAdapter(this@AddPrescriptionsActivity, R.layout.support_simple_spinner_dropdown_item, durationList)
        durationListAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item)
        spinnerDuration.adapter = durationListAdapter

        spinnerDuration.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("Not yet implemented")
            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                selectedDuration = spinnerDuration.selectedItemPosition + 1.toString().toInt()
                Helper.durationList(selectedDuration!!)
                duration = Helper.durationList(selectedDuration!!) // bloodGroup will pass on request parameters
            }

        }
    }

    private fun setupFrequencySpinner() {
        spinnerFrequency = findViewById<Spinner>(R.id.spinner_frequency)
        val frequencyListAdapter = ArrayAdapter(this@AddPrescriptionsActivity, R.layout.support_simple_spinner_dropdown_item, frequencyList)
        frequencyListAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item)
        spinnerFrequency.adapter = frequencyListAdapter

        spinnerFrequency.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("Not yet implemented")
            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                selectedfrequency = spinnerFrequency.selectedItemPosition + 1.toString().toInt()
                Helper.frequencyList(selectedfrequency!!)
                frequency = Helper.frequencyList(selectedfrequency!!) // bloodGroup will pass on request parameters
            }

        }

    }


    private fun doAddMedicineValidation() {
        if (!TextUtils.isEmpty(advice) &&
            !TextUtils.isEmpty(symptom) &&
            !TextUtils.isEmpty(note)
        ) {

            val request = RequestAddPrescriptions(advice, mPreference.getUserId().toString(), "medName", note, patientId, symptom)
            doSubmitPrescriptions(request)

        } else {
            Helper.toastLong(this@AddPrescriptionsActivity, "Fields shouldn't be empty!")
        }

    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                medicineName = data!!.getStringExtra("medicineGenericName")!!
                tv_choose_medicine_name.text = medicineName
            }
        }

    }


    private fun doSubmitPrescriptions(request: RequestAddPrescriptions) {
        if (Helper.isConnectedToInternet(this@AddPrescriptionsActivity)) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                Helper.showLoading(this@AddPrescriptionsActivity)
            }
            val call: Call<ResponseAddPrescriptions> = APIInterface.create().addPrescription(request)
            Helper.showLog(TAG, " request :- ${Gson().toJson(request)}")
            call.enqueue(object : Callback<ResponseAddPrescriptions> {
                override fun onResponse(
                    call: Call<ResponseAddPrescriptions>,
                    response: Response<ResponseAddPrescriptions>
                ) {
                    Helper.hideLoading()
                    if (response.isSuccessful) {
                        Helper.showLog(TAG, "Response : ${response.body()}")
                        if (response.body()!!.success) {
                            val mData = response.body()!!.data
                            if (mData != null) {

                                startActivity(Intent(this@AddPrescriptionsActivity, MyPrescriptionsActivity::class.java))
                                finish()

                            }

                            if (response.body()!!.data.message != null) {
                                Helper.toastShort(this@AddPrescriptionsActivity, response.body()!!.data.message)

                            } else if (response.body()!!.errors != null) {
                                Helper.toastShort(this@AddPrescriptionsActivity, response.body()!!.errors)
                            }

                        } else {
                            if (response.body()!!.data.message != null) {
                                Helper.toastShort(this@AddPrescriptionsActivity, response.body()!!.data.message)

                            } else if (response.body()!!.errors != null) {
                                Helper.toastShort(this@AddPrescriptionsActivity, response.body()!!.errors)
                            }
                        }

                    } else {
                        Helper.toastNetworkError(this@AddPrescriptionsActivity)
                    }
                }

                override fun onFailure(call: Call<ResponseAddPrescriptions>, t: Throwable) {
                    Helper.toastShort(this@AddPrescriptionsActivity, "${t.message}")
                    Helper.hideLoading()
                }
            })
        }

    }

    private fun validateFields() {

        edt_symptom.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                symptom = edt_symptom.text.trim().toString()
            }

        })

        edt_advice.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                advice = edt_advice.text.trim().toString()
            }

        })

        edt_note.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                note = edt_note.text.trim().toString()
            }

        })


    }

    private fun setupToolbar() {
        setSupportActionBar(toolbar_add_prescriptions_activity)
        val actionBar = supportActionBar
        actionBar!!.title = "Add Prescriptions"
        actionBar!!.setDisplayHomeAsUpEnabled(true)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}
