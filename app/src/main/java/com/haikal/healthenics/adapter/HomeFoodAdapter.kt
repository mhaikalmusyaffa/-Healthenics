package com.haikal.healthenics.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import com.haikal.healthenics.api.data.DetailFoodResponse
import com.haikal.healthenics.api.data.model.FoodEntity
import com.haikal.healthenics.databinding.ItemDetailBinding

class HomeFoodAdapter : RecyclerView.Adapter<HomeFoodAdapter.HomeFoodViewHolder>() {

    private val _foodNutrition = MutableLiveData<DetailFoodResponse.Food.Servings.Serving?>()
    val foodNutrition: MutableLiveData<DetailFoodResponse.Food.Servings.Serving?> = _foodNutrition

    private val list = ArrayList<FoodEntity>()
    private var onItemClickCallback: OnItemClickCallback? = null

    fun setList(foods: List<FoodEntity>) {
        list.clear()
        list.addAll(foods)
        notifyDataSetChanged()
        Log.d("HomeFoodAdapter", "List set with size: ${list.size}")
    }

    fun getTotalCalories(): Double {
        return list.sumOf { foodEntity ->
            // Ekstrak hanya angka dari string kalori
            val caloriesString = foodEntity.calories.replace(Regex("[^\\d]"), "")
            // Konversi string yang sudah bersih ke integer
            caloriesString.toDoubleOrNull() ?: 0.0
        }
    }
    fun getTotalProtein(): Double {
        return list.sumOf { foodEntity ->
            val proteinString = foodEntity.protein.replace(Regex("[^\\d.]"), "")
            proteinString.toDoubleOrNull() ?: 0.0
        }
    }

    fun getTotalFat(): Double {
        return list.sumOf { foodEntity ->
            val fatString = foodEntity.fat.replace(Regex("[^\\d.]"), "")
            fatString.toDoubleOrNull() ?: 0.0
        }
    }

    fun getTotalCarbohydrate(): Double {
        return list.sumOf { foodEntity ->
            val carbohydrateString = foodEntity.carbohydrate.replace(Regex("[^\\d.]"), "")
            carbohydrateString.toDoubleOrNull() ?: 0.0
        }
    }


    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    inner class HomeFoodViewHolder(private val binding: ItemDetailBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(foodEntity: FoodEntity) {
            binding.apply {
                root.setOnClickListener {
                    onItemClickCallback?.onItemClicked(foodEntity)
                }
                tvConsumedFood.text = foodEntity.foodName
                tvFoodCalories.text = foodEntity.calories
            }
        }
    }



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeFoodViewHolder {
        val binding = ItemDetailBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return HomeFoodViewHolder(binding)
    }

    override fun onBindViewHolder(holder: HomeFoodViewHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount(): Int = list.size

    interface OnItemClickCallback {
        fun onItemClicked(data: FoodEntity)
    }
}