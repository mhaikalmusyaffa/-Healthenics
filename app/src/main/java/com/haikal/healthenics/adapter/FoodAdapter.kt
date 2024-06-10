package com.haikal.healthenics.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.haikal.healthenics.api.data.model.FoodEntity
import com.haikal.healthenics.databinding.ItemDetailBinding

class FoodAdapter : RecyclerView.Adapter<FoodAdapter.FoodViewHolder>() {

    private val list = ArrayList<FoodEntity>()
    private var onItemClickCallback: OnItemClickCallback? = null

    fun setList(foods: List<FoodEntity>) {
        list.clear()
        list.addAll(foods)
        notifyDataSetChanged()
    }

    fun clearList() {
        list.clear()
    }

    inner class FoodViewHolder(private val binding: ItemDetailBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(foodEntity: FoodEntity) {
            binding.apply {
                root.setOnClickListener {
                    onItemClickCallback?.onItemClicked(foodEntity)
                }
                tvConsumedFood.text = foodEntity.foodName
                tvFoodCalories.text = foodEntity.foodDescription
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FoodViewHolder {
        val view = ItemDetailBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FoodViewHolder(view)
    }

    override fun onBindViewHolder(holder: FoodViewHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount(): Int = list.size

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    interface OnItemClickCallback {
        fun onItemClicked(data: FoodEntity)
    }
}