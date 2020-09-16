package com.bijumondal.doctorhealthcare.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bijumondal.doctorhealthcare.R
import com.bijumondal.doctorhealthcare.utils.HealthCarePreference
import com.bijumondal.doctorhealthcare.utils.ImageLoader
import kotlinx.android.synthetic.main.item_appointment_list_for_patient.view.*
import kotlinx.android.synthetic.main.item_appointments_list_for_doctor.view.*
import kotlinx.android.synthetic.main.item_appointments_list_for_doctor.view.iv_doctor_image
import kotlinx.android.synthetic.main.item_appointments_list_for_doctor.view.tv_doctor_booking_date
import kotlinx.android.synthetic.main.item_appointments_list_for_doctor.view.tv_doctor_name

class PatientAppointmentsListAdapter(
    private val appointmentsList: ArrayList<com.bijumondal.doctorhealthcare.models.appointmentListForPatient.Data>,
    val context: Context
) :
    RecyclerView.Adapter<PatientAppointmentsListAdapter.PatientAppointmentsListAdapterViewHolder>() {

    private lateinit var mPreference: HealthCarePreference

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PatientAppointmentsListAdapterViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_appointment_list_for_patient, parent, false)
        mPreference = HealthCarePreference(context!!)

        return PatientAppointmentsListAdapterViewHolder(view)
    }

    override fun getItemCount(): Int {
        return appointmentsList.size
    }

    override fun onBindViewHolder(holder: PatientAppointmentsListAdapterViewHolder, position: Int) {
        holder.patientName.text = "Appointment with ${appointmentsList[position].doctor}"
        holder.hospitalDetails.text = "${appointmentsList[position].hospital_name} \u2022 ${appointmentsList[position].hospital_address}"
        holder.patientBookingDate.text = "Appointment Date - ${appointmentsList[position].add_date} at ${appointmentsList[position].time_slot}"

        //holder.patientBookingStatus.text = appointmentsList[position].status

        if (appointmentsList[position].photo != null) {
            ImageLoader.loadImageFromUrl(holder.patientImage, appointmentsList[position].photo, R.drawable.ic_avatar)
        }

    }

    class PatientAppointmentsListAdapterViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val patientImage = view.iv_doctor_image
        val patientName = view.tv_doctor_name
        val patientBookingDate = view.tv_doctor_booking_date
        val hospitalDetails = view.tv_hospital_name_and_address
        // val patientBookingStatus = view.tv_patient_booking_status

    }

}