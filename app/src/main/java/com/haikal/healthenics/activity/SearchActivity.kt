package com.haikal.healthenics.activity

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.haikal.healthenics.R
import com.haikal.healthenics.adapter.FoodAdapter
import com.haikal.healthenics.api.data.model.FoodEntity
import com.haikal.healthenics.databinding.ActivitySearchBinding
import com.haikal.healthenics.utils.DataMapper
import com.haikal.healthenics.viewmodel.SearchFoodViewModel

class SearchActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySearchBinding
    private lateinit var searchFoodViewModel: SearchFoodViewModel
    private lateinit var foodAdapter: FoodAdapter

    private lateinit var etQuery: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(
            layoutInflater
        )
        setContentView(binding.root)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = ""

        // Inisialisasi foodAdapter sebelum digunakan
        foodAdapter = FoodAdapter()

        // Inisialisasi ViewModel
        searchFoodViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory())[SearchFoodViewModel::class.java]

        // Setup RecyclerView
        binding.rvFoods.layoutManager = LinearLayoutManager(this)
        binding.rvFoods.adapter = foodAdapter

        // Cek intent extra dan lakukan pencarian jika ada query
        val query = intent.getStringExtra("query")
        if (query != null && query.isNotEmpty()) {
            searchFood(query)
        }

        foodAdapter = FoodAdapter()
        searchFoodViewModel = ViewModelProvider(
            this,
            ViewModelProvider.NewInstanceFactory()
        )[SearchFoodViewModel::class.java]
        foodAdapter.notifyDataSetChanged()

        foodAdapter.setOnItemClickCallback(object :
            FoodAdapter.OnItemClickCallback {
            override fun onItemClicked(data: FoodEntity) {
                Intent(
                    this@SearchActivity,
                    DetailFoodActivity::class.java
                ).also {
                    it.putExtra(
                        DetailFoodActivity.EXTRA_FOOD_NAME,
                        data.foodName
                    )
                    it.putExtra(
                        DetailFoodActivity.EXTRA_FOOD_ID,
                        data.foodId
                    )
                    startActivity(it)
                }
            }
        })

        binding.apply {
            rvFoods.layoutManager =
                LinearLayoutManager(this@SearchActivity)
            rvFoods.setHasFixedSize(true)
            rvFoods.adapter = foodAdapter
        }


        searchFoodViewModel.getSearchedFood()
            .observe(this) {
                if (it != null) {
                    foodAdapter.setList(
                        DataMapper.mapResponsesToEntities(
                            it
                        )
                    )
                }
            }
        refreshApp()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.search_menu, menu)

        val searchManager =
            getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchView =
            menu.findItem(R.id.search).actionView as SearchView

        searchView.apply {
            setSearchableInfo(
                searchManager.getSearchableInfo(
                    componentName
                )
            )
            queryHint =
                resources.getString(R.string.searchbar_hint)
            setIconifiedByDefault(false)
            onActionViewExpanded()

            setOnQueryTextListener(object :
                SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(
                    query: String
                ): Boolean {
                    etQuery = query
                    searchFood(query)
                    clearFocus()
                    return true
                }

                override fun onQueryTextChange(
                    newText: String
                ): Boolean {
                    etQuery = newText
                    searchFood(newText)
                    return false
                }
            })
        }
        return true
    }

    private fun searchFood(query: String) {
        binding.apply {
            rvFoods.adapter = foodAdapter
            foodAdapter.clearList()
            searchFoodViewModel.setSearchFood(
                query
            )
        }
    }

    private fun totalFoodCheck(foodFound: Int?) {
        binding.apply {
            if (foodFound == 0) {
                tvNoData.visibility = View.VISIBLE
            } else {
                tvNoData.visibility = View.GONE
            }
        }
    }

    private fun refreshApp() {
        binding.apply {
            swipeToRefresh.setOnRefreshListener {
                if (etQuery.isNotEmpty()) {
                    searchFood(etQuery)  // Gunakan etQuery yang tersimpan sebagai parameter
                }
                swipeToRefresh.isRefreshing =
                    false
            }
        }
    }
}