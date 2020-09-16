package com.bijumondal.doctorhealthcare.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bijumondal.doctorhealthcare.R
import com.bijumondal.doctorhealthcare.utils.ImageLoader
import kotlinx.android.synthetic.main.item_prescription_list_for_doctor.view.*

class PatientPrescriptionsListAdapter(
    private val prescriptionsListForPatient: ArrayList<com.bijumondal.doctorhealthcare.models.patientPrescriptions.Data>,
    val context: Context
) :
    RecyclerView.Adapter<PatientPrescriptionsListAdapter.PatientPrescriptionsListAdapterViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PatientPrescriptionsListAdapterViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_prescription_list_for_doctor, parent, false)
        return PatientPrescriptionsListAdapterViewHolder(view)
    }

    override fun getItemCount(): Int {
        return prescriptionsListForPatient.size
    }

    override fun onBindViewHolder(holder: PatientPrescriptionsListAdapterViewHolder, position: Int) {
        holder.patientName.text = "Prescribed by ${prescriptionsListForPatient[position].doctor}"
        holder.patientSymptom.text = "Symptom - ${prescriptionsListForPatient[position].symptom}"
        holder.medicine.text = prescriptionsListForPatient[position].medicine
        holder.note.text = prescriptionsListForPatient[position].note
        holder.advice.text = prescriptionsListForPatient[position].advice

        if (prescriptionsListForPatient[position].photo != null) {
            ImageLoader.loadImageFromUrl(holder.patientImage, prescriptionsListForPatient[position].photo, R.drawable.ic_avatar)
        }

    }

    class PatientPrescriptionsListAdapterViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val patientImage = view.iv_doctor_image
        val patientName = view.tv_doctor_name
        val patientSymptom = view.tv_patient_symptom
        val medicine = view.tv_medicine
        val advice = view.tv_advice
        val note = view.tv_note
    }

}