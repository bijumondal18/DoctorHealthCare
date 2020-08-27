package com.bijumondal.doctorhealthcare.adapters

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.solver.widgets.Helper
import androidx.recyclerview.widget.RecyclerView
import com.bijumondal.doctorhealthcare.R
import com.bijumondal.doctorhealthcare.activities.DoctorListActivity
import com.bijumondal.doctorhealthcare.models.doctorDepartment.Data
import com.bijumondal.doctorhealthcare.utils.ImageLoader
import kotlinx.android.synthetic.main.item_doc_dept.view.*
import kotlinx.android.synthetic.main.item_top_specialities.view.*

class TopSpecialistAdapter(
    private val deptList: ArrayList<Data>,
    val context: Context
) :
    RecyclerView.Adapter<TopSpecialistAdapter.TopSpecialistAdapterViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TopSpecialistAdapterViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_top_specialities, parent, false)
        return TopSpecialistAdapterViewHolder(view)
    }

    override fun getItemCount(): Int {
        return deptList.size
    }

    override fun onBindViewHolder(holder: TopSpecialistAdapterViewHolder, position: Int) {

        holder.departmentName.text = deptList[position].name

        if (deptList[position].icon != null) {
            ImageLoader.loadCircleImageFromUrl(holder.departmentIcon, deptList[position].icon, R.color.colorTransparent)
        }

        holder.llParent.setOnClickListener {
            context.startActivity(
                Intent(context!!, DoctorListActivity::class.java)
                    .putExtra("deptName", "${deptList[position].name}")
            )
        }


    }

    class TopSpecialistAdapterViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val departmentName = view.tv_dept_title
        val departmentIcon = view.iv_dept_icon
        val llParent = view.ll_parent_dept
    }
}