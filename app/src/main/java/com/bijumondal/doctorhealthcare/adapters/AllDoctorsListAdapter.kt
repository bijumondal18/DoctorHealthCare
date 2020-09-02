package com.bijumondal.doctorhealthcare.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bijumondal.doctorhealthcare.R
import com.bijumondal.doctorhealthcare.activities.BookingActivity
import com.bijumondal.doctorhealthcare.models.allDoctorsList.Data
import com.bijumondal.doctorhealthcare.utils.ImageLoader
import kotlinx.android.synthetic.main.item_all_doctors_list.view.*

class AllDoctorsListAdapter(
    private val allDoctorsList: ArrayList<Data>,
    val context: Context
) :
    RecyclerView.Adapter<AllDoctorsListAdapter.AllDoctorsListAdapterViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AllDoctorsListAdapterViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_all_doctors_list, parent, false)
        return AllDoctorsListAdapterViewHolder(view)
    }

    override fun getItemCount(): Int {
        return allDoctorsList.size
    }

    override fun onBindViewHolder(holder: AllDoctorsListAdapterViewHolder, position: Int) {
        holder.doctorName.text = allDoctorsList[position].name
        holder.doctorDept.text = allDoctorsList[position].department
        holder.doctorAddress.text = allDoctorsList[position].address
        holder.doctorVisitAmount.text = "~ ₹ ${allDoctorsList[position].visit_amount} Consultation fees"
        holder.hospitalNameAndAddress.text = "${allDoctorsList[position].hospitalname} \u25CF ${allDoctorsList[position].hospitaladdress}"

        if (allDoctorsList[position].photo != null) {
            ImageLoader.loadImageFromUrl(holder.doctorImage, allDoctorsList[position].photo, R.drawable.ic_avatar)
        }

        val doctorId = allDoctorsList[position].id  //doctorId

        holder.llParent.setOnClickListener {
            context.startActivity(
                Intent(context!!, BookingActivity::class.java)
                    .putExtra("doctorPhoto", allDoctorsList[position].photo)
                    .putExtra("doctorId", doctorId)
                    .putExtra("doctorName", allDoctorsList[position].name)
                    .putExtra("doctorPhone", allDoctorsList[position].phone)
                    .putExtra("doctorAddress", allDoctorsList[position].address)
                    .putExtra("doctorDept", allDoctorsList[position].department)
                    .putExtra("hospitalPhone", allDoctorsList[position].hospitalphoneno)
                    .putExtra("doctorVisitAmount", "₹ ${allDoctorsList[position].visit_amount}")
                    .putExtra("hospitalNameAndAddress", "${allDoctorsList[position].hospitalname} \u25CF ${allDoctorsList[position].hospitaladdress}")
            )
        }

        holder.btnBook.setOnClickListener {
            context.startActivity(
                Intent(context!!, BookingActivity::class.java)
                    .putExtra("doctorPhoto", allDoctorsList[position].photo)
                    .putExtra("doctorName", allDoctorsList[position].name)
                    .putExtra("doctorId", doctorId)
                    .putExtra("doctorPhone", allDoctorsList[position].phone)
                    .putExtra("doctorAddress", allDoctorsList[position].address)
                    .putExtra("doctorDept", allDoctorsList[position].department)
                    .putExtra("hospitalPhone", allDoctorsList[position].hospitalphoneno)
                    .putExtra("doctorVisitAmount", "₹ ${allDoctorsList[position].visit_amount}")
                    .putExtra("hospitalNameAndAddress", "${allDoctorsList[position].hospitalname} \u25CF ${allDoctorsList[position].hospitaladdress}")
            )
        }

    }

    class AllDoctorsListAdapterViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val llParent = view.ll_parent
        val btnBook = view.btn_book_appointment
        val doctorImage = view.iv_doc_image_booking
        val doctorName = view.tv_doc_name_booking
        val doctorDept = view.tv_doc_department_booking
        val doctorAddress = view.tv_doc_address_booking
        val doctorVisitAmount = view.tv_visit_amount_booking
        val hospitalNameAndAddress = view.tv_hospital_name_and_address_booking
    }
}