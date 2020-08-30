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
) :
    RecyclerView.Adapter<DoctorTimeSlotsAdapter.DoctorTimeSlotsAdapterViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DoctorTimeSlotsAdapterViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_timeslots, parent, false)
        return DoctorTimeSlotsAdapterViewHolder(view)
    }

    override fun getItemCount(): Int {
        return timeSlotsList.size
    }

    override fun onBindViewHolder(holder: DoctorTimeSlotsAdapterViewHolder, position: Int) {

        holder.timeSlotsName.text = timeSlotsList[position].weekday

        val timeSlotsId = timeSlotsList[position].id

        /*holder.llParent.setOnClickListener {
            val intent = Intent()
            intent.putExtra("hospitalName", hospitalList[position].name)
            intent.putExtra("hospitalId", hospitalId)
            (context as AppCompatActivity).setResult(Activity.RESULT_OK, intent)
            context.finish()
        }*/

    }

    class DoctorTimeSlotsAdapterViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val timeSlotsName = view.tv_time_slots
        val llParent = view.ll_parent_timeslots
    }
}