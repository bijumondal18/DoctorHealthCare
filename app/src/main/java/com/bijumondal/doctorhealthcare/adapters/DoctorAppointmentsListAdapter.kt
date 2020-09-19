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
import com.bijumondal.doctorhealthcare.activities.AppointmentDetailsActivity
import com.bijumondal.doctorhealthcare.models.appointmentsListForDoctor.Data
import com.bijumondal.doctorhealthcare.utils.HealthCarePreference
import com.bijumondal.doctorhealthcare.utils.ImageLoader
import kotlinx.android.synthetic.main.item_appointments_list_for_doctor.view.*

class DoctorAppointmentsListAdapter(
    private val appointmentsList: ArrayList<Data>,
    val context: Context
) :
    RecyclerView.Adapter<DoctorAppointmentsListAdapter.DoctorAppointmentsListAdapterViewHolder>() {
    private lateinit var mPreference: HealthCarePreference


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DoctorAppointmentsListAdapterViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_appointments_list_for_doctor, parent, false)
        mPreference = HealthCarePreference(context!!)

        return DoctorAppointmentsListAdapterViewHolder(view)
    }

    override fun getItemCount(): Int {
        return appointmentsList.size
    }

    override fun onBindViewHolder(holder: DoctorAppointmentsListAdapterViewHolder, position: Int) {
        if (appointmentsList[position].photo != null) {
            ImageLoader.loadImageFromUrl(holder.patientImage, appointmentsList[position].photo, R.drawable.ic_avatar)
        }
        val patientName = "Appointment with <b>${appointmentsList[position].name}</b>"
        holder.patientName.text = Html.fromHtml(patientName)
        holder.hospitalAddress.text = "${appointmentsList[position].hospital_name} \u2022 ${appointmentsList[position].hospital_address}"
        holder.patientBookingDate.text = "${appointmentsList[position].add_date} at ${appointmentsList[position].time_slot}"
        //holder.patientBookingStatus.text = appointmentsList[position].status

        val patient_id = appointmentsList[position].patient_id

        holder.btnAddPrescriptions.setOnClickListener {
            context.startActivity(
                Intent(context!!, AddPrescriptionsActivity::class.java)
                    .putExtra("patientId", patient_id)
            )
        }

        holder.llParent.setOnClickListener {
            context.startActivity(
                Intent(context, AppointmentDetailsActivity::class.java)
                    .putExtra("doctorName", appointmentsList[position].name)
                    .putExtra("doctorImage", appointmentsList[position].photo)
                    .putExtra("patientId", patient_id)
                    .putExtra("appointmentDate", appointmentsList[position].add_date)
                    .putExtra("appointmentTime", appointmentsList[position].time_slot)
                    .putExtra("hospitalName", appointmentsList[position].hospital_name)
                    .putExtra("hospitalAddress", appointmentsList[position].hospital_address)
                    .putExtra("hospitalNumber", appointmentsList[position].hospital_phone)
                    .putExtra("appointmentForName", appointmentsList[position].name)
            )
        }

    }

    class DoctorAppointmentsListAdapterViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val patientImage = view.iv_doctor_image
        val patientName = view.tv_doctor_name
        val patientBookingDate = view.tv_doctor_booking_date
        val hospitalAddress = view.tv_hospital_name_and_address_dor_doctor

        //val patientBookingStatus = view.tv_patient_booking_status
        val btnAddPrescriptions = view.btn_add_prescription
        val llParent = view.ll_parent_appointment_list_for_doctor

    }
}