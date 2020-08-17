package com.bijumondal.doctorhealthcare.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.bijumondal.doctorhealthcare.R


class HomeFragment : Fragment() {

    companion object {
        fun newInstance(): HomeFragment = HomeFragment()
        private const val TAG: String = "HomeFragment"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

}
