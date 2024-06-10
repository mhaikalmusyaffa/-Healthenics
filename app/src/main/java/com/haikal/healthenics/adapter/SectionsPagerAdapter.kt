package com.haikal.healthenics.adapter

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.haikal.healthenics.activity.DetailFoodActivity
import com.haikal.healthenics.ui.fragment.DetailFoodFragment



class SectionsPagerAdapter(activity: AppCompatActivity, data : Bundle) : FragmentStateAdapter(activity) {
    private var fragmentBundle: Bundle = data

    override fun createFragment(position: Int): Fragment {
        var fragment: Fragment? = null
        when (position) {
            0 -> fragment = DetailFoodFragment()
        }
        fragment?.arguments = this.fragmentBundle

        return fragment as Fragment
    }

    override fun getItemCount(): Int {
        return 1
    }
}