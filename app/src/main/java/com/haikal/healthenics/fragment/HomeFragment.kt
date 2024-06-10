package com.haikal.healthenics.fragment

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.haikal.healthenics.activity.EditFoodActivity
import com.haikal.healthenics.activity.SearchActivity
import com.haikal.healthenics.adapter.HomeFoodAdapter
import com.haikal.healthenics.api.data.DetailFoodResponse
import com.haikal.healthenics.api.data.dao.ConsumedFood
import com.haikal.healthenics.api.data.dao.DbApplication
import com.haikal.healthenics.api.data.model.FoodEntity
import com.haikal.healthenics.databinding.FragmentHomeBinding
import com.haikal.healthenics.viewmodel.HomeViewModel
import com.haikal.healthenics.viewmodel.HomeViewModelFactory

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var homeViewModel: HomeViewModel
    private lateinit var homeFoodAdapter: HomeFoodAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initializeViewModel()
        setupRecyclerView()
        setupListeners()
        observeViewModel()
    }

    private fun initializeViewModel() {
        val foodDao = (requireActivity().application as DbApplication).database.foodDao()
        val viewModelFactory = HomeViewModelFactory(foodDao)
        homeViewModel = ViewModelProvider(this, viewModelFactory)[HomeViewModel::class.java]
    }

    private fun setupRecyclerView() {
        homeFoodAdapter = HomeFoodAdapter()
        binding.rvFoodList.apply {
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)
            adapter = homeFoodAdapter
        }
    }

    private fun setupListeners() {
        binding.btnSearch.setOnClickListener {
            startActivity(Intent(requireContext(), SearchActivity::class.java))
        }

        binding.swipeToRefresh.setOnRefreshListener {
            homeViewModel.findFoods()
            binding.swipeToRefresh.isRefreshing = false
        }

        homeFoodAdapter.setOnItemClickCallback(object : HomeFoodAdapter.OnItemClickCallback {
            override fun onItemClicked(data: FoodEntity) {
                val intent = Intent(requireContext(), EditFoodActivity::class.java).apply {
                    putExtra(EditFoodActivity.EXTRA_FOOD_ID, data.foodId)
                    putExtra(EditFoodActivity.EXTRA_FOOD_NAME, data.foodName)
                }
                startActivity(intent)
            }
        })
    }

    private fun updateCaloriesTotal() {
        val totalCalories = homeFoodAdapter.getTotalCalories().toInt()
        binding.tvCurCalories.text = "Calories Consumed: $totalCalories Calories"
    }
    private fun updateProteinTotal() {
        val totalProtein = homeFoodAdapter.getTotalProtein()
        binding.tvSumProtein.text = "Proteins: %.2f Gram".format(totalProtein)
    }

    private fun updateFatTotal() {
        val totalFat = homeFoodAdapter.getTotalFat()
        binding.tvSumFats.text = "Fats: %.2f Gram".format(totalFat)
    }

    private fun updateCarbohydrateTotal() {
        val totalCarbohydrate = homeFoodAdapter.getTotalCarbohydrate()
        binding.tvSumCarbs.text = "Carbohydrates: %.2f Gram".format(totalCarbohydrate)
    }
    private fun updateRemainingCalories() {
        val sharedPref = activity?.getSharedPreferences("AppSettings", Context.MODE_PRIVATE)
        val dailyCalories = sharedPref?.getInt("DailyCalories", 0) ?: 0
        val consumedCalories = homeFoodAdapter.getTotalCalories().toInt()

        val remainingCalories = dailyCalories - consumedCalories
        binding.tvRemainCalories.text = "Remaining Calories: %d Calories".format(remainingCalories)
    }

    private fun observeViewModel() {
        homeViewModel.consumedFoods.observe(viewLifecycleOwner) { consumedFoods ->
            homeFoodAdapter.setList(consumedFoods.map { it.toFoodEntity() })
            updateCaloriesTotal()
            updateProteinTotal()
            updateFatTotal()
            updateCarbohydrateTotal()
            updateRemainingCalories()
        }

        val sharedPref = activity?.getSharedPreferences("AppSettings", Context.MODE_PRIVATE)
        val dailyCalories = sharedPref?.getInt("DailyCalories", 0)
        binding.tvTargetCalories.text = "%d Daily Calories".format(dailyCalories)
    }

    private fun ConsumedFood.toFoodEntity(): FoodEntity {
        return FoodEntity(
            foodId = this.foodId,
            foodName = this.foodName,
            foodDescription = this.foodDescription,
            foodType = this.foodType,
            foodUrl = this.foodUrl,
            calories = this.calories,
            protein = this.protein,
            fat = this.fat,
            carbohydrate = this.carbohydrate
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}