/*
package com.bijumondal.doctorhealthcare.adapters

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bijumondal.doctorhealthcare.R
import com.bijumondal.doctorhealthcare.models.userType.UserType
import com.bijumondal.doctorhealthcare.utils.HealthCarePreference
import kotlinx.android.synthetic.main.item_user_type.view.*

class UserTypeAdapter(
    private val userTypeList: ArrayList<UserType>,
    val context: Context
) :
    RecyclerView.Adapter<UserTypeAdapter.UserTypeAdapterViewHolder>() {

    var isSelected: Boolean = false
    var selectedItem: Int = -1
    private lateinit var mPreference: HealthCarePreference

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserTypeAdapterViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_user_type, parent, false)
        mPreference = HealthCarePreference(context)
        return UserTypeAdapterViewHolder(view)
    }

    override fun getItemCount(): Int {
        return userTypeList.size
    }

    override fun onBindViewHolder(holder: UserTypeAdapterViewHolder, position: Int) {
        holder.userTypeTitle.text = userTypeList[position].title
        holder.userTypeThumbnail.setImageResource(userTypeList[position].thumbnail)

        if (selectedItem == position) {
            isSelected = true
            holder.itemView.isSelected = true
            // holder.userTypeTitle.setTextColor(Color.parseColor("#FFFFFF"))

        } else {
            isSelected = false
            holder.itemView.isSelected = false
            // holder.userTypeTitle.setTextColor(Color.parseColor("#000000"))
        }

        holder.itemView.setOnClickListener {
            if (selectedItem == 0) {
                notifyItemChanged(selectedItem)
                mPreference.setUserType(0)

            } else if (selectedItem == 1) {
                notifyItemChanged(selectedItem)
                mPreference.setUserType(1)
            }

            selectedItem = holder.adapterPosition
            notifyItemChanged(selectedItem)
        }


    }

    class UserTypeAdapterViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val userTypeThumbnail = view.iv_user_type
        val userTypeTitle = view.tv_user_type
        val llParent = view.ll_parent
    }

}*/
