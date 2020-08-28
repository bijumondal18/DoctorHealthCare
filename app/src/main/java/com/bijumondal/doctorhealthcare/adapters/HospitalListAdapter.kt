package com.bijumondal.doctorhealthcare.adapters

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.bijumondal.doctorhealthcare.R
import kotlinx.android.synthetic.main.item_doc_dept.view.*

class HospitalListAdapter(
    private val hospitalList: ArrayList<com.bijumondal.doctorhealthcare.models.hospitalList.Data>,
    val context: Context
) :
    RecyclerView.Adapter<HospitalListAdapter.HospitalListAdapterViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HospitalListAdapterViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_doc_dept, parent, false)
        return HospitalListAdapterViewHolder(view)
    }

    override fun getItemCount(): Int {
        return hospitalList.size
    }

    override fun onBindViewHolder(holder: HospitalListAdapterViewHolder, position: Int) {

        holder.hospitalName.text = hospitalList[position].name

        val hospitalId = hospitalList[position].id


        holder.llParent.setOnClickListener {
            val intent = Intent()
            intent.putExtra("hospitalName", hospitalList[position].name)
            intent.putExtra("hospitalId", hospitalId)
            (context as AppCompatActivity).setResult(Activity.RESULT_OK, intent)
            context.finish()
        }

    }

    class HospitalListAdapterViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val hospitalName = view.tv_doc_dept_name
        val departmentDesc = view.tv_doc_dept_desc
        val llParent = view.ll_parent
    }
}