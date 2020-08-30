package com.bijumondal.doctorhealthcare.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bijumondal.doctorhealthcare.R
import com.bijumondal.doctorhealthcare.models.doctorHolidaysList.Data
import kotlinx.android.synthetic.main.item_holidays_list.view.*

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

        holder.btnDelete.setOnClickListener {


        }

    }

    class HolidaysListAdapterViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val holidaysDate = view.tv_holidays_date
        val btnDelete = view.btn_delete_holiday
    }
}