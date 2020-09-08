package com.bijumondal.doctorhealthcare.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.bijumondal.doctorhealthcare.fragments.CompletedAppointmentsFragment
import com.bijumondal.doctorhealthcare.fragments.UpcomingAppointmentsFragment

class AppointmentTypePagerAdapter(manager: FragmentManager) : FragmentPagerAdapter(manager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> {
                UpcomingAppointmentsFragment()
            }
            1 -> {
                CompletedAppointmentsFragment()
            }

            else -> {
                return UpcomingAppointmentsFragment()
            }

        }
    }

    override fun getCount(): Int {
        return 2
    }

    override fun getPageTitle(position: Int): CharSequence {
        return when (position) {
            0 -> "Upcoming"
            1 -> "Completed"
            else -> {
                return "Upcoming"
            }
        }
    }


}