package com.example.chattapp.adapters

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.chattapp.ChatsFragment
import com.example.chattapp.GroupsFragment

class ViewPagerAdapters(fragment: Fragment) : FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        return if (position == 0) {
            ChatsFragment()
        } else {
            GroupsFragment()
        }
    }
}