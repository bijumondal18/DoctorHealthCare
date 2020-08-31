package com.bijumondal.doctorhealthcare.adapters

import android.content.Context
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bijumondal.doctorhealthcare.R
import com.bijumondal.doctorhealthcare.api.APIInterface
import com.bijumondal.doctorhealthcare.models.deleteDoctorHolidays.RequestDeleteDoctorHolidays
import com.bijumondal.doctorhealthcare.models.deleteDoctorHolidays.ResponseDeleteDoctorHolidays
import com.bijumondal.doctorhealthcare.models.doctorHolidaysList.Data
import com.bijumondal.doctorhealthcare.utils.Helper
import com.google.gson.Gson
import kotlinx.android.synthetic.main.item_holidays_list.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HolidaysListAdapter(
    private val holidaysList: ArrayList<Data>,
    val context: Context
) :
    RecyclerView.Adapter<HolidaysListAdapter.HolidaysListAdapterViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HolidaysListAdapterViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_holidays_list, parent, false)
        return HolidaysListAdapterViewHolder(view)
    }

    override fun getItemCount(): Int {
        return holidaysList.size
    }

    override fun onBindViewHolder(holder: HolidaysListAdapterViewHolder, position: Int) {

        holder.holidaysDate.text = holidaysList[position].date

        val holidaysId = holidaysList[position].id

        holder.btnDelete.setOnClickListener {

            val request = RequestDeleteDoctorHolidays(holidaysId)
            deleteHolidays(request)

        }

    }

    class HolidaysListAdapterViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val holidaysDate = view.tv_holidays_date
        val btnDelete = view.btn_delete_holiday
    }


    private fun deleteHolidays(request: RequestDeleteDoctorHolidays) {
        if (Helper.isConnectedToInternet(context)) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                Helper.showLoading(context)
            }
            val call: Call<ResponseDeleteDoctorHolidays> = APIInterface.create().deleteDoctorHolidays(request)
            Helper.showLog("TAG", " request :- ${Gson().toJson(request)}")
            call.enqueue(object : Callback<ResponseDeleteDoctorHolidays> {
                override fun onResponse(
                    call: Call<ResponseDeleteDoctorHolidays>,
                    response: Response<ResponseDeleteDoctorHolidays>
                ) {
                    Helper.hideLoading()
                    if (response.isSuccessful) {
                        Helper.showLog("TAG", "Response : ${response.body()}")
                        if (response.body()!!.success) {
                            val mData = response.body()!!.data
                            if (mData != null) {

                                Helper.toastShort(context, mData.message)

                            }

                            if (response.body()!!.message != null) {
                                Helper.toastShort(context, response.body()!!.message)

                            } else if (response.body()!!.errors != null) {
                                Helper.toastShort(context, response.body()!!.errors)
                            }

                        } else {
                            if (response.body()!!.message != null) {
                                Helper.toastShort(context, response.body()!!.message)

                            } else if (response.body()!!.errors != null) {
                                Helper.toastShort(context, response.body()!!.errors)
                            }
                        }

                    } else {
                        Helper.toastNetworkError(context)
                    }
                }

                override fun onFailure(call: Call<ResponseDeleteDoctorHolidays>, t: Throwable) {
                    Helper.toastShort(context, "${t.message}")
                    Helper.hideLoading()
                }
            })
        }


    }
}