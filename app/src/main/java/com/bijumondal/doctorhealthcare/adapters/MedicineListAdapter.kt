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
import com.bijumondal.doctorhealthcare.models.medicineList.Data
import kotlinx.android.synthetic.main.item_doc_dept.view.*

class MedicineListAdapter(
    private val medicineList: ArrayList<Data>,
    val context: Context
) :
    RecyclerView.Adapter<MedicineListAdapter.MedicineListAdapterViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MedicineListAdapterViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_doc_dept, parent, false)
        return MedicineListAdapterViewHolder(view)
    }

    override fun getItemCount(): Int {
        return medicineList.size
    }

    override fun onBindViewHolder(holder: MedicineListAdapterViewHolder, position: Int) {

        val medicineGenericName = "${medicineList[position].generic} (${medicineList[position].name})"
        holder.medicineName.text = medicineGenericName

        holder.llParent.setOnClickListener {
            val intent = Intent()
            intent.putExtra("medicineGenericName", medicineGenericName)
            (context as AppCompatActivity).setResult(Activity.RESULT_OK, intent)
            context.finish()
        }

    }

    class MedicineListAdapterViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val medicineName = view.tv_doc_dept_name
        val departmentDesc = view.tv_doc_dept_desc
        val llParent = view.ll_parent
    }
}