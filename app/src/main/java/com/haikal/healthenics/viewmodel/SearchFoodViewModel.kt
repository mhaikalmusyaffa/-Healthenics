package com.haikal.healthenics.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.haikal.healthenics.api.ApiClient
import com.haikal.healthenics.api.data.FoodItem
import com.haikal.healthenics.api.data.FoodResponse
import kotlinx.coroutines.launch

class SearchFoodViewModel : ViewModel() {
    private val listFoods = MutableLiveData<List<FoodItem>>()

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _onFailure = MutableLiveData<Boolean>()
    val onFailure: LiveData<Boolean> = _onFailure

    private val _totalFoodFound = MutableLiveData<Int>()
    val totalFoodFound: LiveData<Int> = _totalFoodFound

    fun setSearchFood(query: String) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                _onFailure.value = false
                val response = ApiClient.apiService.getSearchedFood(query, 0, 30)
                if (response.foods.food.isNotEmpty()) {
                    listFoods.postValue(response.foods.food)
                    _totalFoodFound.postValue(response.foods.food.size)
                } else {
                    _onFailure.postValue(true)
                }
                _isLoading.postValue(false)
            } catch (e: Exception) {
                Log.e("SearchResult", "onFailure: ${e.message}")
                _isLoading.postValue(false)
                _onFailure.postValue(true)
            }
        }
    }

    fun getSearchedFood(): LiveData<List<FoodItem>> {
        return listFoods
    }
}