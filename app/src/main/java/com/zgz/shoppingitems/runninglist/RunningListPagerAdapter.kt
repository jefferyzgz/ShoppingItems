package com.zgz.shoppingitems.runninglist

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import java.lang.IllegalArgumentException

class RunningListPagerAdapter constructor(
    fragmentManager: FragmentManager,
    lifecycle: Lifecycle,
    val frameList : List<Fragment>)
    : FragmentStateAdapter(fragmentManager, lifecycle)  {

    override fun getItemCount(): Int {
        return frameList.size
    }

    override fun createFragment(position: Int): Fragment {
            if(position >= frameList.size || position < 0) {
                throw IllegalArgumentException("Invalid position $position")
            }
            return frameList.get(position)
    }
}