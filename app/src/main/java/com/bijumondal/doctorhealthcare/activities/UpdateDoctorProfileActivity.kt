package com.bijumondal.doctorhealthcare.activities

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.media.MediaScannerConnection
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.text.TextUtils
import android.widget.ImageView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.bijumondal.doctorhealthcare.Constants
import com.bijumondal.doctorhealthcare.R
import com.bijumondal.doctorhealthcare.api.APIInterface
import com.bijumondal.doctorhealthcare.models.createDoctorProfile.RequestCreateDoctorProfile
import com.bijumondal.doctorhealthcare.models.createDoctorProfile.ResponseCreateDoctorProfile
import com.bijumondal.doctorhealthcare.models.doctorPhoto.RequestDoctorPhoto
import com.bijumondal.doctorhealthcare.models.doctorPhoto.ResponseDoctorPhoto
import com.bijumondal.doctorhealthcare.models.patientPhoto.ResponsePatientPhoto
import com.bijumondal.doctorhealthcare.utils.CaptureImage
import com.bijumondal.doctorhealthcare.utils.HealthCarePreference
import com.bijumondal.doctorhealthcare.utils.Helper
import com.bijumondal.doctorhealthcare.utils.ImageLoader
import com.google.gson.Gson
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.activity_update_doctor_profile.*
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.util.*

class UpdateDoctorProfileActivity : AppCompatActivity() {

    companion object {
        private const val TAG: String = "UpdateDoctorProfileActivity"
        private const val REQUEST_CODE = 212
        private const val REQUEST_CODE1 = 213
    }

    private lateinit var mPreference: HealthCarePreference

    var userId = ""
    private lateinit var imgProfilePic: CircleImageView
    private lateinit var imgEditProfilePic: ImageView
    var firstname = ""
    var lastname = ""
    var email = ""
    var phone = ""
    var address = ""
    var docDept = ""
    var visitAmount = ""
    var photo = ""
    var hospitalName = ""
    var hospitalId = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update_doctor_profile)

        initViews()
        setupToolbar()

    }

    private fun initViews() {
        mPreference = HealthCarePreference(this@UpdateDoctorProfileActivity)
        imgProfilePic = findViewById(R.id.iv_user_image_doc)
        imgEditProfilePic = findViewById(R.id.iv_edit_profile_picture_doc)

        if (mPreference.getUserId() != null) {
            userId = mPreference.getUserId().toString()
        }

        if (mPreference.getFirstName() != null) {
            edt_first_name_doc.setText(mPreference.getFirstName())
        }

        if (mPreference.getLastName() != null) {
            edt_last_name_doc.setText(mPreference.getLastName())
        }

        if (mPreference.getEmail() != null) {
            edt_email_doc.setText(mPreference.getEmail())
        }

        if (mPreference.getAddress() != null) {
            edt_address_doc.setText(mPreference.getAddress())
        }

        if (mPreference.getNumber() != null) {
            edt_phone_number_doc.setText(mPreference.getNumber())
        }

        if (mPreference.getDoctorDept() != null) {
            edt_department_doc.text = mPreference.getDoctorDept()
        }

        if (mPreference.getHospitalName() != null) {
            edt_hospital_doc.text = mPreference.getHospitalName()
        }

        if (mPreference.getVisitAmount() != null) {
            edt_visit_amount_doc.setText(mPreference.getVisitAmount())
        }

        imgEditProfilePic.setOnClickListener {
            CaptureImage.showPictureDialog(this@UpdateDoctorProfileActivity)
        }

        edt_department_doc.setOnClickListener {
            startActivityForResult(Intent(this@UpdateDoctorProfileActivity, DoctorDepartmentActivity::class.java), REQUEST_CODE)
        }

        edt_hospital_doc.setOnClickListener {
            startActivityForResult(Intent(this@UpdateDoctorProfileActivity, HospitalListActivity::class.java), REQUEST_CODE1)

        }

        btn_update_doc_profile.setOnClickListener {
            validateProfile()
        }

    }

    private fun validateProfile() {
        firstname = edt_first_name_doc.text.trim().toString()
        lastname = edt_last_name_doc.text.trim().toString()
        email = edt_email_doc.text.trim().toString()
        phone = edt_phone_number_doc.text.trim().toString()
        address = edt_address_doc.text.trim().toString()
        docDept = edt_department_doc.text.trim().toString()
        userId = mPreference.getUserId().toString()
        visitAmount = edt_visit_amount_doc.text.trim().toString()
        hospitalName = edt_hospital_doc.text.trim().toString()
        hospitalId = mPreference.getHospitalId().toString()

        if (!TextUtils.isEmpty(firstname) &&
            !TextUtils.isEmpty(lastname) &&
            !TextUtils.isEmpty(phone) &&
            !TextUtils.isEmpty(docDept)
        ) {

            val request = RequestCreateDoctorProfile(address, docDept, userId, email, photo, phone, "${firstname} ${lastname}", "", hospitalId, visitAmount)
            updateDoctorProfile(request)

        } else {
            Helper.toastLong(this@UpdateDoctorProfileActivity, "Fields shouldn't be empty!")
        }
    }

    private fun updateDoctorProfile(request: RequestCreateDoctorProfile) {
        if (Helper.isConnectedToInternet(this@UpdateDoctorProfileActivity)) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                Helper.showLoading(this)
            }
            val call: Call<ResponseCreateDoctorProfile> = APIInterface.create().createDoctorProfile(request)
            Helper.showLog(TAG, " request :- ${Gson().toJson(request)}")
            call.enqueue(object : Callback<ResponseCreateDoctorProfile> {
                override fun onResponse(
                    call: Call<ResponseCreateDoctorProfile>,
                    response: Response<ResponseCreateDoctorProfile>
                ) {
                    Helper.hideLoading()
                    if (response.isSuccessful) {
                        Helper.showLog(TAG, "Response : ${response.body()}")
                        if (response.body()!!.success) {
                            val mData = response.body()!!.data
                            if (mData != null) {

                                mPreference.setFirstName(firstname)
                                mPreference.setLastName(lastname)
                                mPreference.setEmail(email)
                                mPreference.setNumber(phone)
                                mPreference.setDoctorDept(docDept)
                                mPreference.setVisitAmount(visitAmount)
                                mPreference.setHospitalName(hospitalName)

                                val intent = Intent()
                                intent.putExtra("firstName", firstname)
                                intent.putExtra("lastName", lastname)
                                intent.putExtra("email", email)
                                intent.putExtra("phone", phone)
                                intent.putExtra("address", address)
                                intent.putExtra("docDept", docDept)
                                intent.putExtra("docVisitAmount", visitAmount)
                                intent.putExtra("docHospitalName", hospitalName)
                                intent.putExtra("",mPreference.getProfileImage())
                                setResult(RESULT_OK, intent)
                                finish()

                            }

                            if (response.body()!!.data.message != null) {
                                Helper.toastShort(this@UpdateDoctorProfileActivity, response.body()!!.data.message)

                            } else if (response.body()!!.errors != null) {
                                Helper.toastShort(this@UpdateDoctorProfileActivity, response.body()!!.errors)
                            }

                        } else {
                            if (response.body()!!.data.message != null) {
                                Helper.toastShort(this@UpdateDoctorProfileActivity, response.body()!!.data.message)

                            } else if (response.body()!!.errors != null) {
                                Helper.toastShort(this@UpdateDoctorProfileActivity, response.body()!!.errors)
                            }
                        }

                    } else {
                        Helper.toastNetworkError(this@UpdateDoctorProfileActivity)
                    }
                }

                override fun onFailure(call: Call<ResponseCreateDoctorProfile>, t: Throwable) {
                    Helper.toastShort(this@UpdateDoctorProfileActivity, "${t.message}")
                    Helper.hideLoading()
                }
            })
        }

    }

    private fun saveImage(myBitmap: Bitmap): String {
        val bytes = ByteArrayOutputStream()
        myBitmap.compress(Bitmap.CompressFormat.JPEG, 90, bytes)
        val wallpaperDirectory = File(
            (Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)).toString() + Constants.LOCAL_IMAGE_DIRECTORY
        )
        // have the object build the directory structure, if needed.
        Helper.showLog("fee", wallpaperDirectory.toString())
        if (!wallpaperDirectory.exists()) {
            wallpaperDirectory.mkdirs()
        }

        try {
            Helper.showLog("heel", wallpaperDirectory.toString())
            val f = File(
                wallpaperDirectory, ((Calendar.getInstance()
                    .timeInMillis).toString() + ".jpg")
            )
            f.createNewFile()
            val fo = FileOutputStream(f)
            fo.write(bytes.toByteArray())
            MediaScannerConnection.scanFile(
                this,
                arrayOf(f.path),
                arrayOf("photo/jpeg"), null
            )
            fo.close()
            Helper.showLog(TAG, "File Saved::--->" + f.absolutePath)

            return f.absolutePath
        } catch (e1: IOException) {
            e1.printStackTrace()
        }

        return ""
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                docDept = data!!.getStringExtra("docDept")!!
                edt_department_doc.text = docDept
            }

        } else if (requestCode == REQUEST_CODE1) {
            if (resultCode == RESULT_OK) {
                hospitalName = data!!.getStringExtra("hospitalName")!!
                edt_hospital_doc.text = hospitalName
                hospitalId = data!!.getStringExtra("hospitalId")!!
                mPreference.setHospitalId(hospitalId)
            }
        }

        if (requestCode == Constants.GALLERY) {
            if (data != null) {
                val contentURI = data.data
                try {
                    val bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, contentURI)
                    val file = File(saveImage(bitmap))
                    imgProfilePic.setImageURI(contentURI)
                    uploadImage(this@UpdateDoctorProfileActivity, imgProfilePic, file)

                } catch (e: IOException) {
                    e.printStackTrace()
                    Helper.toastShort(this@UpdateDoctorProfileActivity, "Failed!")
                }

            }

        } else if (requestCode == Constants.CAMERA) {
            if (data != null) {
                val thumbnail = data!!.extras!!.get("data") as Bitmap
                // saveImage(thumbnail)
                val file = File(saveImage(thumbnail))
                imgProfilePic.setImageBitmap(thumbnail)
                uploadImage(this@UpdateDoctorProfileActivity, imgProfilePic, file)
                Helper.toastShort(this@UpdateDoctorProfileActivity, "Image Saved")
            }
        }

    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    fun uploadImage(context: Context, imgView: CircleImageView, file: File) {
        val mFile = RequestBody.create(MediaType.parse("photo/*"), file)
        val fileToUpload: MultipartBody.Part = MultipartBody.Part.createFormData("photo", file.name, mFile)
        Helper.showLoading(this@UpdateDoctorProfileActivity)
        val call: Call<ResponseDoctorPhoto> = APIInterface.create().getDoctorProfilePhoto(fileToUpload, mFile)
        call.enqueue(object : Callback<ResponseDoctorPhoto> {
            override fun onResponse(
                call: Call<ResponseDoctorPhoto>,
                response: Response<ResponseDoctorPhoto>
            ) {
                Helper.hideLoading()
                if (response.isSuccessful) {
                    Helper.showLog(TAG, "Response : ${response.body()!!}")
                    if (response.body()!!.success) {
                        val mData = response.body()!!.data
                        if (mData != null) {
                            if (mData.photo != null) {
                                photo = mData.photo
                                mPreference.setProfileImage(photo)

                            }

                            Helper.hideLoading()

                        } else {
                            if (response.body()!!.message != null) {
                                Helper.toastShort(context, response.body()!!.message)

                            } else if (response.body()!!.errors != null && response.body()!!.errors[0] != null) {
                                Helper.toastShort(context, response.body()!!.errors[0])
                            } else {
                                Helper.toastShort(context, "Data not saved, response : Failed!")
                            }
                        }
                    } else {
                        if (response.body()!!.message != null) {
                            Helper.toastShort(context, response.body()!!.message)

                        } else if (response.body()!!.errors != null && response.body()!!.errors[0] != null) {
                            Helper.toastShort(context, response.body()!!.errors[0])
                        } else {
                            Helper.toastShort(context, "Response : Failed")
                        }

                    }

                } else {
                    Helper.toastNetworkError(context)
                }
            }

            override fun onFailure(call: Call<ResponseDoctorPhoto>, t: Throwable) {
                Helper.hideLoading()
                if (t.message != null) {
                    Helper.showLog(TAG, t.message!!)
                    Helper.toastShort(context, t.message!!)
                }

            }

        })
    }

    private fun setupToolbar() {
        setSupportActionBar(toolbar_update_doctor_profile_activity)
        val actionBar = supportActionBar
        actionBar!!.title = "Update Profile"
        actionBar.setDisplayHomeAsUpEnabled(true)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

}
