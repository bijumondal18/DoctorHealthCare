package com.bijumondal.doctorhealthcare.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bijumondal.doctorhealthcare.R
import com.bijumondal.doctorhealthcare.models.appointmentsListForDoctor.Data
import kotlinx.android.synthetic.main.item_appointments_list_for_doctor.view.*

class PatientAppointmentsListAdapter(
    private val appointmentsList: ArrayList<com.bijumondal.doctorhealthcare.models.appointmentListForPatient.Data>,
    val context: Context
) :
    RecyclerView.Adapter<PatientAppointmentsListAdapter.PatientAppointmentsListAdapterViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PatientAppointmentsListAdapterViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_appointments_list_for_doctor, parent, false)
        return PatientAppointmentsListAdapterViewHolder(view)
    }

    override fun getItemCount(): Int {
        return appointmentsList.size
    }

    override fun onBindViewHolder(holder: PatientAppointmentsListAdapterViewHolder, position: Int) {
        holder.patientName.text =  appointmentsList[position].name
        holder.patientBloodGroup.text = appointmentsList[position].bloodgroup
        holder.patientBookingDate.text = "Date\n${appointmentsList[position].add_date}"
        holder.patientPhoneNumber.text = appointmentsList[position].phone
        holder.patientBookingStatus.text = appointmentsList[position].status
        holder.patientGender.text = appointmentsList[position].sex
        holder.patientBookingTimeSlots.text = "Time\n${appointmentsList[position].time_slot}"

        /*if (appointmentsList[position].photo != null) {
            ImageLoader.loadImageFromUrl(holder.patientImage, appointmentsList[position].photo, R.color.colorTransparent)
        }*/


    }

    class PatientAppointmentsListAdapterViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val patientImage = view.iv_patient_image
        val patientName = view.tv_patient_name
        val patientBloodGroup = view.tv_patient_blood_group
        val patientBookingDate = view.tv_patient_booking_date
        val patientPhoneNumber = view.tv_patient_phone
        val patientBookingStatus = view.tv_patient_booking_status
        val patientGender = view.tv_patient_gender
        val patientBookingTimeSlots = view.tv_patient_booking_time
    }
}