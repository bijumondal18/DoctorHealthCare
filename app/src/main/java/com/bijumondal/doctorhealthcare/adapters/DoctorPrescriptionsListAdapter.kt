package com.bijumondal.doctorhealthcare.adapters

import android.content.Context
import android.content.Intent
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bijumondal.doctorhealthcare.R
import com.bijumondal.doctorhealthcare.activities.AddPrescriptionsActivity
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

        val patientName = "You prescribed to <b>${prescriptionsListForDoctor[position].name}</b> for"
        holder.patientName.text = Html.fromHtml(patientName)
        holder.patientSymptom.text = "${prescriptionsListForDoctor[position].symptom}"
        val medicineName = prescriptionsListForDoctor[position].medicine
        holder.medicine.text = Html.fromHtml(medicineName)
        holder.note.text = prescriptionsListForDoctor[position].note
        holder.advice.text = prescriptionsListForDoctor[position].advice

        if (prescriptionsListForDoctor[position].photo != null) {
            ImageLoader.loadImageFromUrl(holder.patientImage, prescriptionsListForDoctor[position].photo, R.drawable.ic_avatar)
        }

        holder.btnEditPrescription.setOnClickListener {
            context.startActivity(
                Intent(context, AddPrescriptionsActivity::class.java)
                    .putExtra("patientId", prescriptionsListForDoctor[position].patient_id)
                    .putExtra("note", prescriptionsListForDoctor[position].note)
                    .putExtra("advice", prescriptionsListForDoctor[position].advice)
                    .putExtra("symptom", prescriptionsListForDoctor[position].symptom)
            )
        }

    }

    class DoctorPrescriptionsListAdapterViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val patientImage = view.iv_doctor_image
        val patientName = view.tv_doctor_name
        val patientSymptom = view.tv_patient_symptom
        val medicine = view.tv_medicine
        val advice = view.tv_advice
        val note = view.tv_note
        val btnEditPrescription = view.btn_edit_prescription

    }
}