package com.haikal.healthenics.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.haikal.healthenics.api.data.dao.DbApplication
import com.haikal.healthenics.api.data.dao.FoodDao
import com.haikal.healthenics.databinding.ActivityEditFoodBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class EditFoodActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEditFoodBinding
    private lateinit var foodDao: FoodDao

    companion object {
        const val EXTRA_FOOD_ID = "extra_food_id"
        const val EXTRA_FOOD_NAME = "extra_food_name"
        const val EXTRA_FOOD_CALORIES = "extra_food_calories"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditFoodBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val foodId = intent.getIntExtra(EXTRA_FOOD_ID, 0)

        foodDao = (application as DbApplication).database.foodDao()

        binding.apply {
            btnDelete.setOnClickListener {
                CoroutineScope(Dispatchers.IO).launch {
                    foodDao.deleteFoodById(foodId)
                    finish()
                }
            }
        }
    }
}