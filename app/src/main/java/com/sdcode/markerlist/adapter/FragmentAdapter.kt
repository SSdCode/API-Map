package com.sdcode.markerlist.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.sdcode.markerlist.fragments.MapFragment
import com.sdcode.markerlist.fragments.MarkerListFragment

class FragmentAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle) :
    FragmentStateAdapter(fragmentManager, lifecycle) {
    override fun createFragment(position: Int): Fragment {
        return if (position == 1) {
            MapFragment()
        } else MarkerListFragment()
    }

    override fun getItemCount(): Int {
        return 2
    }
}