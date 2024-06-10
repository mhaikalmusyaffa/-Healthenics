package com.haikal.healthenics.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.haikal.healthenics.R

class CalorieCalculatorFragment : Fragment() {

    private lateinit var etHeight: EditText
    private lateinit var etWeight: EditText
    private lateinit var spActivityLevel: Spinner
    private lateinit var btnCalculate: Button
    private lateinit var tvResult: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_calorie_calculator, container, false)

        etHeight = view.findViewById(R.id.etHeight)
        etWeight = view.findViewById(R.id.etWeight)
        spActivityLevel = view.findViewById(R.id.spActivityLevel)
        btnCalculate = view.findViewById(R.id.btnCalculate)
        tvResult = view.findViewById(R.id.tvResult)

        // Set up spinner
        val adapter = ArrayAdapter.createFromResource(
            requireContext(),
            R.array.activity_levels,
            android.R.layout.simple_spinner_item
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spActivityLevel.adapter = adapter

        btnCalculate.setOnClickListener {
            calculateCalories()
        }

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Membaca nilai DailyCalories dari SharedPreferences
        val sharedPref = activity?.getSharedPreferences("AppSettings", Context.MODE_PRIVATE)
        val dailyCalories = sharedPref?.getInt("DailyCalories", 0)
        if (dailyCalories != 0) {
            tvResult.text = "Kebutuhan Kalori Harian: %d Calories".format(dailyCalories)
        }
    }

    private fun calculateCalories() {
        val height = etHeight.text.toString().toIntOrNull()
        val weight = etWeight.text.toString().toIntOrNull()
        val activityLevel = spActivityLevel.selectedItemPosition

        if (height != null && weight != null) {
            val bmi = weight / ((height / 100) * (height / 100))
            val bmr = 10 * weight + 6.25 * height - 5 * 25 + 5 // Contoh rumus BMR untuk pria

            val activityMultiplier = when (activityLevel) {
                0 -> 1.2 // Sangat Sedikit
                1 -> 1.375 // Ringan
                2 -> 1.55 // Sedang
                3 -> 1.725 // Berat
                4 -> 1.9 // Sangat Berat
                else -> 1.0
            }

            val dailyCalories = bmr * activityMultiplier
            tvResult.text = "Kebutuhan Kalori Harian: %.0f Calories".format(dailyCalories)

            // Simpan ke SharedPreferences
            val sharedPref = activity?.getSharedPreferences("AppSettings", Context.MODE_PRIVATE)
            sharedPref?.edit()?.putInt("DailyCalories", dailyCalories.toInt())?.apply()
        } else {
            tvResult.text = "Masukkan tinggi dan berat badan yang valid"
        }
    }
}