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

    }

    class HolidaysListAdapterViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val holidaysDate = view.tv_holidays_date
    }

}