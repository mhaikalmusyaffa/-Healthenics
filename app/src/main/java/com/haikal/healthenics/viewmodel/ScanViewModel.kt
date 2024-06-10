package com.haikal.healthenics.viewmodel

import android.app.Application
import android.graphics.Bitmap
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.haikal.healthenics.ml.FoodModel
import org.tensorflow.lite.support.image.TensorImage

class ScanViewModel(application: Application) : AndroidViewModel(application) {
    val currentImageUri: MutableLiveData<Uri?> = MutableLiveData()
    val outputText: MutableLiveData<String> = MutableLiveData()

    fun processImage(bitmap: Bitmap) {
        val foodModel = FoodModel.newInstance(getApplication())
        val tfImage = TensorImage.fromBitmap(bitmap)
        val outputs = foodModel.process(tfImage)
            .probabilityAsCategoryList.apply {
                sortByDescending { it.score }
            }
        val highProbability = outputs[0]
        outputText.postValue(highProbability.label)
        foodModel.close()
    }

}