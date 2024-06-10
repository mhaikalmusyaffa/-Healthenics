package com.haikal.healthenics.activity

import android.os.Bundle
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import com.haikal.healthenics.R
import com.haikal.healthenics.adapter.SectionsPagerAdapter
import com.haikal.healthenics.databinding.ActivityDetailFoodBinding
import com.google.android.material.tabs.TabLayoutMediator

class DetailFoodActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailFoodBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailFoodBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val foodId = intent.getLongExtra(EXTRA_FOOD_ID, 0)
        val foodName = intent.getStringExtra(EXTRA_FOOD_NAME) ?: ""
        val bundle = Bundle()
        bundle.putString(EXTRA_FOOD_NAME, foodName)
        bundle.putLong(EXTRA_FOOD_ID, foodId)

        val sectionsPagerAdapter = SectionsPagerAdapter(this@DetailFoodActivity, bundle)
        binding.viewPager.adapter = sectionsPagerAdapter
        TabLayoutMediator(binding.tabs, binding.viewPager) { tab, position ->
            tab.text = resources.getString(TAB_TITLES[position])
        }.attach()

        binding.btnBack.setOnClickListener {
            onBackPressed()
        }
    }

    companion object {
        const val EXTRA_FOOD_ID = "extra_food_id"
        const val EXTRA_FOOD_NAME = "extra_food_name"

        @StringRes
        private val TAB_TITLES = intArrayOf(
            R.string.details_tab_title,
            R.string.nutrition_tab_title
        )
    }
}