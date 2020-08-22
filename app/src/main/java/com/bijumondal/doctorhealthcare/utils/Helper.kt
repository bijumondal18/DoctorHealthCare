package com.bijumondal.doctorhealthcare.utils

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.view.animation.LinearInterpolator
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationManagerCompat
import com.bijumondal.doctorhealthcare.Constants
import com.bijumondal.doctorhealthcare.R
import com.google.android.material.snackbar.Snackbar
import java.text.SimpleDateFormat
import java.util.*
import java.util.regex.Pattern

class Helper {

    companion object {
        private const val TAG = "PurocanHelper"
        private const val PLAY_SERVICES_RESOLUTION_REQUEST = 9000
        private lateinit var progressDialogBuilder: AlertDialog.Builder
        private lateinit var progressDialog: AlertDialog
        private var mPreference: HealthCarePreference? = null


        fun toastShort(context: Context, msg: String) {
            Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
        }

        fun toastNetworkError(context: Context) {
            Toast.makeText(context, "Something went wrong!", Toast.LENGTH_SHORT).show()
        }

        fun toastLong(context: Context, msg: String) {
            Toast.makeText(context, msg, Toast.LENGTH_LONG).show()
        }

        fun showLog(tag: String, msg: String) {
            Log.d(tag, msg)
        }

        fun showLogError(tag: String, msg: String) {
            Log.e(tag, msg)
        }

        fun getOSVersion(): String {
            return Build.VERSION.RELEASE
        }

        fun getDeviceSDKVersion(): Int {
            return Build.VERSION.SDK_INT
        }

        fun getDeviceName(): String? {
            return Build.MODEL
        }

//        fun getAppVersion(): String {
//            return BuildConfig.VERSION_NAME
//        }

        @SuppressLint("HardwareIds")
        fun getDeviceId(context: Context): String? {
            return Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID)
        }

        fun isNotificationEnabled(context: Context): Boolean {
            return NotificationManagerCompat.from(context).areNotificationsEnabled()
        }


        fun getErrorCode(type: Int): String {
            var errorCode: String? = null
            when (type) {
                101 -> errorCode = "ERROR_FAILED"
                102 -> errorCode = "ERROR_SERVER"
                103 -> errorCode = "ERROR_UNKNOWN"
                104 -> errorCode = "ERROR_VALIDATION"
                105 -> errorCode = "ERROR_INVALID_DATA"
                106 -> errorCode = "ERROR_NO_RECORDS_FOUND"
                107 -> errorCode = "ERROR_REQUEST_CANNOT_PROCEED"
                108 -> errorCode = "ERROR_UNAUTHORISED"

            }
            return errorCode!!
        }

        fun getDeviceType(type: Int): String {
            var deviceType: String? = null
            when (type) {
                3 -> deviceType = "Platform_Android"
                4 -> deviceType = "Platform_Ios"
            }
            return deviceType!!
        }

        fun userType(type: Int): String {
            var userType: String? = null
            when (type) {
                1 -> userType = "Patient"
                2 -> userType = "Doctor"
            }
            return userType!!
        }

        fun getGender(type: Int): String {
            var getgender: String? = null
            when (type) {
                1 -> getgender = "Male"
                2 -> getgender = "Female"
                3 -> getgender = "Others"
            }
            return getgender!!
        }

        fun bloodGroup(type: Int): String {
            var bloodGroup: String? = null
            when (type) {
                1 -> bloodGroup = "A+"
                2 -> bloodGroup = "A-"
                3 -> bloodGroup = "B+"
                4 -> bloodGroup = "B-"
                5 -> bloodGroup = "AB+"
                6 -> bloodGroup = "AB-"
                7 -> bloodGroup = "O+"
                8 -> bloodGroup = "O-"
            }
            return bloodGroup!!
        }


        @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
        fun showLoading(context: Context) {
            progressDialogBuilder = AlertDialog.Builder(context)
            progressDialogBuilder.setCancelable(false) // if you want user to wait for some process to finish,
            progressDialogBuilder.setView(R.layout.layout_loading_dialog)
            progressDialog = progressDialogBuilder.create()
            progressDialog.show()
        }


        fun hideLoading() {
            try {

                if (progressDialog.isShowing)
                    progressDialog.dismiss()

            } catch (ex: java.lang.Exception) {
                Log.e(TAG, ex.toString())
            }
        }

        fun setBoldTypeface(context: Context, textView: TextView) {
            textView.typeface = Typeface.createFromAsset(context.assets, "fonts/roboto_bold.ttf")
        }

        fun setMediumTypeface(context: Context, textView: TextView) {
            textView.typeface = Typeface.createFromAsset(context.assets, "fonts/roboto_medium.ttf")
        }

        fun setMediumTypefaceButton(context: Context, btn: Button) {
            btn.typeface = Typeface.createFromAsset(context.assets, "fonts/roboto_medium.ttf")
        }

        fun setMediumTypefaceRadioButton(context: Context, btn: RadioButton) {
            btn.typeface = Typeface.createFromAsset(context.assets, "fonts/roboto_medium.ttf")
        }

        fun setRegularTypefaceButton(context: Context, btn: Button) {
            btn.typeface = Typeface.createFromAsset(context.assets, "fonts/roboto_regular.ttf")
        }

        fun setRegularTypeface(context: Context, textView: TextView) {
            textView.typeface = Typeface.createFromAsset(context.assets, "fonts/roboto_regular.ttf")
        }

        /*fun showRefresh(refresh: SwipeRefreshLayout) {
            if (refresh != null) {
                refresh.isRefreshing = true
                refresh.isEnabled = true
            }
        }

        fun hideRefresh(refresh: SwipeRefreshLayout) {
            if (refresh != null) {
                refresh.isRefreshing = false
                refresh.isEnabled = false
            }
        }*/

        fun showSNACK(view: View, msg: String) {
            val snackBar = Snackbar.make(view, msg, Snackbar.LENGTH_INDEFINITE)
            snackBar.setActionTextColor(Color.WHITE)
            val snackBarView = snackBar.view
            snackBarView.setBackgroundColor(Color.DKGRAY)
            val textView =
                snackBarView.findViewById(com.google.android.material.R.id.snackbar_text) as TextView
            textView.setTextColor(Color.LTGRAY)
            textView.textSize = 15f
            snackBar.setAction("OK") { snackBar.dismiss() }
            snackBar.show()
        }

        fun setTextOnTextView(tv: TextView, str: String) {
            if (!TextUtils.isEmpty(str)) {
                tv.text = str
            } else {
                tv.text = "NA"
            }
        }

        fun setTextOnEditText(edt: EditText, str: String) {
            if (!TextUtils.isEmpty(str)) {
                edt.setText(str)
            } else {
                edt.setText("NA")
            }
        }

        fun isValidEmail(str: String): Boolean {
            return !TextUtils.isEmpty(str) && Pattern.compile(
                "^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]{1}|[\\w-]{2,}))@"
                        + "((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                        + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\."
                        + "([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                        + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])){1}|"
                        + "([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$"
            ).matcher(str).matches()
        }


        fun isValidPhoneNumber(str: String): Boolean {
            return !TextUtils.isEmpty(str)

        }

        fun isValidString(str: String): Boolean {
            return !TextUtils.isEmpty(str)

        }

        fun isValidPickupDropLocation(str: String, isProvideLater: Boolean): Boolean {
            return if (isProvideLater) {
                true
            } else {
                !TextUtils.isEmpty(str)
            }
        }

        /* fun showAppCloseAlert(context: Context) {
             val dialog = Dialog(context)
             dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
             dialog.setContentView(R.layout.dialog_alert)
             dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
             dialog.show()

             dialog.findViewById<Button>(R.id.btn_positive).setOnClickListener {
                 dialog.dismiss()
                 (context as AppCompatActivity).finish()
             }
             dialog.findViewById<Button>(R.id.btn_negative).setOnClickListener {
                 dialog.dismiss()
             }
         }

         fun showMessageDialogOnly(context: Context, msg: String) {
             val dialog = Dialog(context)
             dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
             dialog.setContentView(R.layout.dialog_alert)
             dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
             dialog.show()
             dialog.tv_title.text = msg
             dialog.btn_negative.visibility = View.GONE
             dialog.findViewById<Button>(R.id.btn_positive).setOnClickListener {
                 dialog.dismiss()
                 (context as AppCompatActivity).finish()
             }

             *//*dialog.findViewById<Button>(R.id.btn_negative).setOnClickListener {
                dialog.dismiss()
            }*//*
        }*/


        /*
        fun showNetworkErrorAlert(context: Context, isForceToFinish: Boolean) {
            val dialog = Dialog(context)
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog.setContentView(R.layout.dialog_internet_error)
            dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog.show()

            //dialog.tv_msg.text = context.resources.getString(R.string.internet_error_msg)

            dialog.findViewById<Button>(R.id.btn_positive).setOnClickListener {
                if (isForceToFinish) {
                    (context as AppCompatActivity).finish()
                    dialog.dismiss()
                } else {
                    dialog.dismiss()
                }
            }

        }*/


        fun isConnectedToInternet(context: Context): Boolean {

            val manager =
                context.getSystemService(Context.CONNECTIVITY_SERVICE) as? ConnectivityManager
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                val allNetworks = manager?.allNetworks?.let { it } ?: return false
                allNetworks.forEach { network ->
                    val info = manager.getNetworkInfo(network)

                    if (info!!.state == NetworkInfo.State.CONNECTED) return true
                }
            } else {
                val allNetworkInfo = manager?.allNetworkInfo?.let { it } ?: return false
                allNetworkInfo.forEach { info ->
                    if (info.state == NetworkInfo.State.CONNECTED) return true
                }
            }
            return false
        }

        @SuppressLint("SimpleDateFormat")
        fun getAnyDate(givenDateString: String, dateFormat: String): String {
            val formatter = SimpleDateFormat(Constants.API_DATE_FORMAT)
            val mDate: Date = formatter.parse(givenDateString)!!
            return android.text.format.DateFormat.format(dateFormat, mDate).toString()
        }

        @SuppressLint("SimpleTimeFormat")
        fun getAnyTime(givenTimeString: String, timeFormat: String): String {
            val formatter = SimpleDateFormat(Constants.API_TIME_FORMAT)
            val mTime: Date = formatter.parse(givenTimeString)!!
            return android.text.format.DateFormat.format(timeFormat, mTime).toString()
        }

        fun appShare(context: Context) {
            val shareIntent = Intent()
            shareIntent.action = Intent.ACTION_SEND
            shareIntent.type = "text/plain"
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, context.getString(R.string.text_choose_one))
            shareIntent.putExtra(
                Intent.EXTRA_TEXT,
                "\nLet me recommend you this application\n\n" + "https://play.google.com/store/apps/details?id=${context.packageName}" + "\n\n"
            )
            context.startActivity(
                Intent.createChooser(
                    shareIntent,
                    context.getString(R.string.text_choose_one)
                )
            )
        }

        fun openPlayStore(context: Context) {
            val uri = Uri.parse("market://details?id=${context.packageName}")
            val goToMarket = Intent(Intent.ACTION_VIEW, uri)
            // To count with Play market backstack, After pressing back button,
            // to taken back to our application, we need to add following flags to intent.
            goToMarket.addFlags(
                Intent.FLAG_ACTIVITY_NO_HISTORY or
                        Intent.FLAG_ACTIVITY_NEW_DOCUMENT or
                        Intent.FLAG_ACTIVITY_MULTIPLE_TASK
            )
            try {
                context.startActivity(goToMarket)
            } catch (e: ActivityNotFoundException) {
                context.startActivity(
                    Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse("http://play.google.com/store/apps/details?id=${context.packageName}")
                    )
                )
            }

        }

        /*fun getSymbol(context: Context, symbol: String, textSize: Float, color: Int): Drawable {
            val paint = Paint(ANTI_ALIAS_FLAG)
            paint.textSize = textSize
            paint.color = color
            paint.textAlign = Paint.Align.LEFT
            val baseline = -paint.ascent() // ascent() is negative
            val width = (paint.measureText(symbol) + 2).toInt() // r
            // ound
            val height = (baseline + paint.descent() + 2).toInt()
            val image = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
            val canvas = Canvas(image)
            canvas.drawText(symbol, 0.0f, baseline, paint)
            return BitmapDrawable(context.resources, image)
        }*/

        //to rotate an imageView in clockwise
        fun rotateImage(img: ImageView) {
            img.animate().rotationBy(180f).setDuration(500).setInterpolator(LinearInterpolator())
                .start()
        }


    }

}