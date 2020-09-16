package com.bijumondal.doctorhealthcare.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bijumondal.doctorhealthcare.R
import com.bijumondal.doctorhealthcare.models.doctorPrescriptions.Data
import com.bijumondal.doctorhealthcare.utils.ImageLoader
import kotlinx.android.synthetic.main.item_prescription_list_for_doctor.view.*

class DoctorPrescriptionsListAdapter(
    private val prescriptionsListForDoctor: ArrayList<Data>,
    val context: Context
) :
    RecyclerView.Adapter<DoctorPrescriptionsListAdapter.DoctorPrescriptionsListAdapterViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DoctorPrescriptionsListAdapterViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_prescription_list_for_doctor, parent, false)
        return DoctorPrescriptionsListAdapterViewHolder(view)
    }

    override fun getItemCount(): Int {
        return prescriptionsListForDoctor.size
    }

    override fun onBindViewHolder(holder: DoctorPrescriptionsListAdapterViewHolder, position: Int) {
        holder.patientName.text = "You prescribed for ${prescriptionsListForDoctor[position].name}"
        holder.patientSymptom.text = "Symptom - ${prescriptionsListForDoctor[position].symptom}"
        holder.medicine.text = prescriptionsListForDoctor[position].medicine
        holder.note.text = prescriptionsListForDoctor[position].note
        holder.advice.text = prescriptionsListForDoctor[position].advice

        if (prescriptionsListForDoctor[position].photo != null) {
            ImageLoader.loadImageFromUrl(holder.patientImage, prescriptionsListForDoctor[position].photo, R.drawable.ic_avatar)
        }


    }

    class DoctorPrescriptionsListAdapterViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val patientImage = view.iv_doctor_image
        val patientName = view.tv_doctor_name
        val patientSymptom = view.tv_patient_symptom
        val medicine = view.tv_medicine
        val advice = view.tv_advice
        val note = view.tv_note

    }
}