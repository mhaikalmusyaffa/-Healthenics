package com.haikal.healthenics.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.haikal.healthenics.R
import com.haikal.healthenics.api.data.DetailFoodResponse
import com.haikal.healthenics.api.data.FoodItem
import com.haikal.healthenics.api.data.dao.DbApplication
import com.haikal.healthenics.databinding.FragmentDetailFoodBinding
import com.haikal.healthenics.activity.DetailFoodActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import com.haikal.healthenics.viewmodel.DetailFoodViewModel
import com.haikal.healthenics.viewmodel.DetailFoodViewModelFactory

class DetailFoodFragment : Fragment() {

    private var _binding: FragmentDetailFoodBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: DetailFoodViewModel
    private lateinit var foodItem: FoodItem

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailFoodBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val foodDao = (requireActivity().application as DbApplication).database.foodDao()
        val viewModelFactory = DetailFoodViewModelFactory(foodDao)
        viewModel = ViewModelProvider(this, viewModelFactory).get(DetailFoodViewModel::class.java)


        showLoading(true)
        val foodId = requireActivity().intent.getLongExtra(DetailFoodActivity.EXTRA_FOOD_ID, 0)

        binding.apply {

            viewModel.setFoodDetails(foodId.toString())
            viewModel.setFoodNutrition(foodId.toString()) // Panggil API untuk mendapatkan data nutrisi

            viewModel.isLoading.observe(requireActivity()) {
                showLoading(it)
            }
            viewModel.onFailure.observe(requireActivity()) {
                onFailure(it)
            }
            viewModel.foodNutrition.observe(requireActivity()) {
                if (it != null) {
                    binding.apply {
                        tvCalories.text = "Calories: ${it.calories} Calories"
                        tvCarbs.text = "Carbohydrates: ${it.carbohydrate} Gram"
                        tvProteins.text = "Proteins: ${it.protein} Gram"
                        tvFats.text = "Fats: ${it.fat} Gram"
                    }
                }
            }
            viewModel.foodDetails.observe(requireActivity()) {
                if (it != null) {
                    foodItem = it

                    binding.apply {
                        tvFoodName.text = it.foodName
                        // Set other food details if needed
                    }
                }
            }

            binding.btnAddFood.setOnClickListener {
                viewModel.insertFoodItem(foodItem)
            }

            binding.btnRefresh.setOnClickListener {
                viewModel.setFoodDetails(foodId.toString())
                viewModel.setFoodNutrition(foodId.toString())
            }
        }
    }


    private fun showLoading(state: Boolean) {
        binding.apply {
            if (state) {
                progressBar.visibility = View.VISIBLE
            } else {
                progressBar.visibility = View.GONE
            }
        }
    }

    private fun onFailure(fail: Boolean) {
        binding.apply {
            if (fail) {
                tvOnFailMsg.visibility = View.VISIBLE
            } else {
                tvOnFailMsg.visibility = View.GONE
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}