package com.haikal.healthenics.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.haikal.healthenics.api.data.dao.FoodDao

class HomeViewModelFactory(private val foodDao: FoodDao) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return HomeViewModel(foodDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

class DetailFoodViewModelFactory(private val foodDao: FoodDao) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DetailFoodViewModel::class.java)) {
            return DetailFoodViewModel(foodDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}