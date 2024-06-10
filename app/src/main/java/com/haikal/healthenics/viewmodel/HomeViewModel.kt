package com.haikal.healthenics.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.haikal.healthenics.api.ApiClient
import com.haikal.healthenics.api.data.FoodItem
import com.haikal.healthenics.api.data.dao.ConsumedFood
import com.haikal.healthenics.api.data.dao.FoodDao
import kotlinx.coroutines.launch

class HomeViewModel(private val foodDao: FoodDao) : ViewModel() {

    private val listFoods = MutableLiveData<List<FoodItem>>()

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _onFailure = MutableLiveData<Boolean>()
    val onFailure: LiveData<Boolean> = _onFailure

    // Inisialisasi LiveData untuk mengambil data makanan yang dikonsumsi
    val consumedFoods: LiveData<List<ConsumedFood>> = foodDao.getAllConsumedFoods()

    companion object {
        private const val TAG = "HomeViewModel"
    }

    init {
        findFoods()
    }

    fun findFoods() {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                _onFailure.value = false
                val response = ApiClient.apiService.getSearchedFood("ayam", 0, 30)
                listFoods.postValue(response.foods.food)  // Pastikan ini adalah List<FoodItem>
                _isLoading.value = false
            } catch (e: Exception) {
                _isLoading.value = false
                _onFailure.value = true
                Log.e(TAG, "Error: ${e.message}")
            }
        }
    }

    fun getListFoods(): LiveData<List<FoodItem>> {
        return listFoods
    }

    // Fungsi untuk menyisipkan makanan yang dikonsumsi
    fun insertConsumedFood(consumedFood: ConsumedFood) {
        viewModelScope.launch {
            try {
                foodDao.insertConsumedFood(consumedFood)
            } catch (e: Exception) {
                Log.e(TAG, "Error inserting consumed food: ${e.message}")
            }
        }
    }
}