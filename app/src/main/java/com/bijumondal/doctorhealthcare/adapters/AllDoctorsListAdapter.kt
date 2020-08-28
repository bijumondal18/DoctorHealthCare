package com.bijumondal.doctorhealthcare.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bijumondal.doctorhealthcare.R
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
        holder.doctorAddress.text = allDoctorsList[position].hospitalname
        holder.doctorVisitAmount.text = "₹ ${allDoctorsList[position].visit_amount} Consultation fees"

        if (allDoctorsList[position].photo != null) {
            ImageLoader.loadCircleImageFromUrl(holder.doctorImage, allDoctorsList[position].photo, R.color.colorTransparent)
        }else{
            ImageLoader.loadCircleImageFromUrl(holder.doctorImage, "", R.color.colorTransparent)

        }

    }

    class AllDoctorsListAdapterViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val llParent = view.ll_parent
        val doctorImage = view.iv_doc_image
        val doctorName = view.tv_doc_name
        val doctorDept = view.tv_doc_department
        val doctorAddress = view.tv_doc_address
        val doctorVisitAmount = view.tv_visit_amount
    }
}