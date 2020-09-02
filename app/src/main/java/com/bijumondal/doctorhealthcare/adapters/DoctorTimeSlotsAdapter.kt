package com.bijumondal.doctorhealthcare.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bijumondal.doctorhealthcare.R
import com.bijumondal.doctorhealthcare.models.doctorTimeSlotsList.Data
import kotlinx.android.synthetic.main.item_timeslots.view.*

class DoctorTimeSlotsAdapter(
    private val timeSlotsList: ArrayList<Data>,
    val context: Context
    // listener : OnTimeSlotItemClick

) :
    RecyclerView.Adapter<DoctorTimeSlotsAdapter.DoctorTimeSlotsAdapterViewHolder>() {
    // private val mCallback: OnTimeSlotItemClick? = listener

    var selectedItem: Int = -1
    var isSelected: Boolean = false

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DoctorTimeSlotsAdapterViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_timeslots, parent, false)
        return DoctorTimeSlotsAdapterViewHolder(view)
    }

    override fun getItemCount(): Int {
        return timeSlotsList.size
    }

    override fun onBindViewHolder(holder: DoctorTimeSlotsAdapterViewHolder, position: Int) {

        holder.timeSlotsName.text = "${timeSlotsList[position].weekday}\n${timeSlotsList[position].starttime} - ${timeSlotsList[position].endtime}"

        val timeSlotsId = timeSlotsList[position].id

        if (selectedItem == position) holder.itemView.isSelected = true
        else holder.itemView.isSelected = false


        holder.itemView.setOnClickListener {

            if (selectedItem >= 0)
                notifyItemChanged(selectedItem)

            selectedItem = holder.adapterPosition
            notifyItemChanged(selectedItem)

        }

    }

    class DoctorTimeSlotsAdapterViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val timeSlotsName = view.tv_time_slots
        val llParent = view.ll_parent_timeslots
    }
}