package com.bijumondal.doctorhealthcare.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.bijumondal.doctorhealthcare.R


class MyAppointmentFragment : Fragment() {

    companion object {
        fun newInstance(): MyAppointmentFragment = MyAppointmentFragment()
        private const val TAG: String = "MyAppointmentFragment"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_my_appointment, container, false)
    }

}
