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
import com.bijumondal.doctorhealthcare.models.deleteDoctorTimeSlots.RequestDeleteDoctorTimeSlots
import com.bijumondal.doctorhealthcare.models.deleteDoctorTimeSlots.ResponseDeleteDoctorTimeSlots
import com.bijumondal.doctorhealthcare.models.doctorHolidaysList.Data
import com.bijumondal.doctorhealthcare.utils.Helper
import com.google.gson.Gson
import kotlinx.android.synthetic.main.item_holidays_list.view.*
import kotlinx.android.synthetic.main.item_time_slots_list.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TimeSlotsListAdapter(
    private val timeSlotsList: ArrayList<com.bijumondal.doctorhealthcare.models.doctorTimeSlotsList.Data>,
    val context: Context
) :
    RecyclerView.Adapter<TimeSlotsListAdapter.TimeSlotsListAdapterViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TimeSlotsListAdapterViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_time_slots_list, parent, false)
        return TimeSlotsListAdapterViewHolder(view)
    }

    override fun getItemCount(): Int {
        return timeSlotsList.size
    }

    override fun onBindViewHolder(holder: TimeSlotsListAdapterViewHolder, position: Int) {

        holder.timeslotsName.text = "${timeSlotsList[position].weekday} (${timeSlotsList[position].starttime} - ${timeSlotsList[position].endtime})"

    }

    class TimeSlotsListAdapterViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val timeslotsName = view.tv_timeslots
        val btnDelete = view.btn_delete_timeslots
    }

}