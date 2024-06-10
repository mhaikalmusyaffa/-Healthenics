package com.haikal.healthenics.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.haikal.healthenics.api.ApiClient
import com.haikal.healthenics.api.data.DetailFoodResponse
import com.haikal.healthenics.api.data.FoodItem
import com.haikal.healthenics.api.data.dao.ConsumedFood
import com.haikal.healthenics.api.data.dao.DbApplication
import com.haikal.healthenics.api.data.dao.FoodDao
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailFoodViewModel(private val foodDao: FoodDao) : ViewModel() {


    private val _foodDetails = MutableLiveData<FoodItem>()
    val foodDetails: LiveData<FoodItem> = _foodDetails

    private val _foodNutrition = MutableLiveData<DetailFoodResponse.Food.Servings.Serving?>()
    val foodNutrition: MutableLiveData<DetailFoodResponse.Food.Servings.Serving?> = _foodNutrition

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _onFailure = MutableLiveData<Boolean>()
    val onFailure: LiveData<Boolean> = _onFailure

    fun setFoodDetails(foodId: String) {
        _isLoading.value = true
        ApiClient.apiService.getFoodDetails(foodId).enqueue(object : Callback<DetailFoodResponse> {
            override fun onResponse(call: Call<DetailFoodResponse>, response: Response<DetailFoodResponse>) {
                _isLoading.value = false
                if (response.isSuccessful && response.body() != null) {
                    val foodItem = FoodItem(
                        foodId = response.body()!!.food.foodId.toLong(),
                        foodName = response.body()!!.food.foodName,
                        foodDescription = "", // Sesuaikan jika ada field yang sesuai
                        foodType = response.body()!!.food.foodType,
                        foodUrl = response.body()!!.food.foodUrl,
                    )
                    _foodDetails.postValue(foodItem)
                } else {
                    _onFailure.value = true
                }
            }

            override fun onFailure(call: Call<DetailFoodResponse>, t: Throwable) {
                _isLoading.value = false
                _onFailure.value = true
                Log.e("DetailFoodViewModel", "Error fetching food details: ${t.message}")
            }
        })
    }

    fun insertFoodItem(foodItem: FoodItem) {
        viewModelScope.launch(Dispatchers.IO) {
            try {

                val consumedFood = ConsumedFood(
                    foodId = foodItem.foodId,
                    foodName = foodItem.foodName,
                    foodDescription = foodItem.foodDescription,
                    foodType = foodItem.foodType,
                    foodUrl = foodItem.foodUrl,
                    calories = "Calories : ${foodNutrition.value!!.calories.toInt()} Calories",
                    protein = foodNutrition.value!!.protein,
                    fat = foodNutrition.value!!.fat,
                    carbohydrate = foodNutrition.value!!.carbohydrate
                ) // Sesuaikan kalori jika perlu
                foodDao.insertConsumedFood(consumedFood)
            } catch (e: Exception) {
                Log.e("ViewModel", "Error inserting food item", e)
            }
        }
    }

    fun setFoodNutrition(foodId: String) {
        _isLoading.value = true
        ApiClient.apiService.getFoodDetails(foodId).enqueue(object : Callback<DetailFoodResponse> {
            override fun onResponse(call: Call<DetailFoodResponse>, response: Response<DetailFoodResponse>) {
                _isLoading.value = false
                if (response.isSuccessful && response.body() != null) {
                    val foodNutritionItem = response.body()!!.food.servings.serving.firstOrNull()
                    if (foodNutritionItem != null) {
                        _foodNutrition.postValue(foodNutritionItem)
                    } else {
                        _onFailure.value = true
                    }
                } else {
                    _onFailure.value = true
                }
            }

            override fun onFailure(call: Call<DetailFoodResponse>, t: Throwable) {
                _isLoading.value = false
                _onFailure.value = true
                Log.e("DetailFoodViewModel", "Error fetching food nutrition: ${t.message}")
            }
        })
    }

}