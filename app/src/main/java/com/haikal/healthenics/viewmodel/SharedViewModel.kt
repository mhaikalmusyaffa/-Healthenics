package com.haikal.healthenics.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SharedViewModel : ViewModel() {
    val targetCalories = MutableLiveData<String>()
}