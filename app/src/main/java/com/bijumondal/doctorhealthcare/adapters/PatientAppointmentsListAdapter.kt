package com.bijumondal.doctorhealthcare.adapters

import android.content.Context
import android.content.Intent
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bijumondal.doctorhealthcare.R
import com.bijumondal.doctorhealthcare.activities.AppointmentDetailsActivity
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
        val doctorName = "Appointment with <b>${appointmentsList[position].doctor}</b>"
        holder.patientName.text = Html.fromHtml(doctorName)
        holder.hospitalDetails.text = "${appointmentsList[position].hospital_name} \u2022 ${appointmentsList[position].hospital_address}"
        holder.patientBookingDate.text = "${appointmentsList[position].add_date} at ${appointmentsList[position].time_slot}"
        //holder.patientBookingStatus.text = appointmentsList[position].status

        if (appointmentsList[position].photo != null) {
            ImageLoader.loadImageFromUrl(holder.patientImage, appointmentsList[position].photo, R.drawable.ic_avatar)
        }

        holder.llParent.setOnClickListener {
            context.startActivity(
                Intent(context, AppointmentDetailsActivity::class.java)
                    .putExtra("doctorName", appointmentsList[position].doctor)
                    .putExtra("doctorImage", appointmentsList[position].photo)
                    .putExtra("appointmentDate", appointmentsList[position].add_date)
                    .putExtra("appointmentTime", appointmentsList[position].time_slot)
                    .putExtra("hospitalName", appointmentsList[position].hospital_name)
                    .putExtra("hospitalAddress", appointmentsList[position].hospital_address)
                    .putExtra("hospitalNumber", appointmentsList[position].hospital_phone)
                    .putExtra("appointmentForName", appointmentsList[position].name)
            )
        }

    }

    class PatientAppointmentsListAdapterViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val patientImage = view.iv_doctor_image
        val patientName = view.tv_doctor_name
        val patientBookingDate = view.tv_doctor_booking_date
        val hospitalDetails = view.tv_hospital_name_and_address
        val llParent = view.ll_parent_appointment_list
        // val patientBookingStatus = view.tv_patient_booking_status

    }

}