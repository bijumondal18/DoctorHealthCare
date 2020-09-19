package com.bijumondal.doctorhealthcare.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bijumondal.doctorhealthcare.R
import com.bijumondal.doctorhealthcare.models.addMedicineTemp.Data
import kotlinx.android.synthetic.main.item_temp_medicine_list.view.*

class TempMedicineListAdapter(
    private val tempMedicineList: ArrayList<Data>,
    val context: Context
) :
    RecyclerView.Adapter<TempMedicineListAdapter.TempMedicineListAdapterViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TempMedicineListAdapterViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_temp_medicine_list, parent, false)

        return TempMedicineListAdapterViewHolder(view)
    }

    override fun getItemCount(): Int {
        return tempMedicineList.size
    }

    override fun onBindViewHolder(holder: TempMedicineListAdapterViewHolder, position: Int) {
        holder.medicineName.text = tempMedicineList[position].medicine
        holder.frequency.text = tempMedicineList[position].frequency
        holder.duration.text = tempMedicineList[position].duration
        holder.instruction.text = tempMedicineList[position].instructions

    }

    class TempMedicineListAdapterViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val medicineName = view.tv_temp_med_name
        val frequency = view.tv_temp_med_frequency
        val duration = view.tv_temp_med_duration
        val instruction = view.tv_temp_med_instruction
        val deleteTempMedicine = view.iv_delete_temp_medicine

    }

}