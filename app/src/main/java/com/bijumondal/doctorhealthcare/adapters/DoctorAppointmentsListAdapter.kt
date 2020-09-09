package com.bijumondal.doctorhealthcare.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bijumondal.doctorhealthcare.R
import com.bijumondal.doctorhealthcare.activities.AddPrescriptionsActivity
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
        holder.patientName.text = appointmentsList[position].name
        holder.patientBloodGroup.text = appointmentsList[position].bloodgroup
        holder.patientBookingDate.text = "Date\n${appointmentsList[position].add_date}"
        holder.patientPhoneNumber.text = appointmentsList[position].phone
        holder.patientBookingStatus.text = appointmentsList[position].status
        holder.patientGender.text = appointmentsList[position].sex
        holder.patientBookingTimeSlots.text = "Time\n${appointmentsList[position].time_slot}"

        if (appointmentsList[position].photo != null) {
            ImageLoader.loadImageFromUrl(holder.patientImage, appointmentsList[position].photo, R.color.colorTransparent)
        }

        if (mPreference.getUserType() == 1) {
            holder.btnAddPrescriptions.visibility = View.GONE
        } else if (mPreference.getUserType() == 2) {
            holder.btnAddPrescriptions.visibility = View.VISIBLE
        }

        holder.btnAddPrescriptions.setOnClickListener {
            context.startActivity(
                Intent(context!!, AddPrescriptionsActivity::class.java)

            )
        }

        //todo putExtra to send appointed user id to next screen

    }

    class DoctorAppointmentsListAdapterViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val patientImage = view.iv_patient_image
        val patientName = view.tv_patient_name
        val patientBloodGroup = view.tv_patient_blood_group
        val patientBookingDate = view.tv_patient_booking_date
        val patientPhoneNumber = view.tv_patient_phone
        val patientBookingStatus = view.tv_patient_booking_status
        val patientGender = view.tv_patient_gender
        val patientBookingTimeSlots = view.tv_patient_booking_time
        val btnAddPrescriptions = view.btn_add_prescription

    }
}