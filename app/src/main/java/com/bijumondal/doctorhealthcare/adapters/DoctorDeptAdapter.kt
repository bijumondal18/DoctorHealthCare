package com.bijumondal.doctorhealthcare.adapters

import android.app.Activity.RESULT_OK
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.bijumondal.doctorhealthcare.R
import com.bijumondal.doctorhealthcare.models.doctorDepartment.Data
import kotlinx.android.synthetic.main.item_doc_dept.view.*

class DoctorDeptAdapter(
    private val deptList: ArrayList<Data>,
    val context: Context
) :
    RecyclerView.Adapter<DoctorDeptAdapter.DoctorDeptAdapterViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DoctorDeptAdapterViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_doc_dept, parent, false)
        return DoctorDeptAdapterViewHolder(view)
    }

    override fun getItemCount(): Int {
        return deptList.size
    }

    override fun onBindViewHolder(holder: DoctorDeptAdapterViewHolder, position: Int) {

        holder.departmentName.text = deptList[position].name
        holder.departmentDesc.text = deptList[position].description

        holder.llParent.setOnClickListener {
            val intent = Intent()
            intent.putExtra("docDept", deptList[position].name)
            (context as AppCompatActivity).setResult(RESULT_OK, intent)
            context.finish()
        }


    }

    class DoctorDeptAdapterViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val departmentName = view.tv_doc_dept_name
        val departmentDesc = view.tv_doc_dept_desc
        val llParent = view.ll_parent
    }
}