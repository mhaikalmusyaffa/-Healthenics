package com.haikal.healthenics.fragment

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.haikal.healthenics.databinding.FragmentScanBinding
import android.content.Intent
import android.provider.MediaStore
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.haikal.healthenics.R
import com.haikal.healthenics.activity.SearchActivity
import com.haikal.healthenics.ml.FoodModel
import com.haikal.healthenics.viewmodel.ScanViewModel
import com.yalantis.ucrop.UCrop
import kotlinx.coroutines.launch
import org.tensorflow.lite.support.image.TensorImage
import java.io.File

class ScanFragment : Fragment() {
    private var _binding: FragmentScanBinding? = null
    private val binding get() = _binding!!
    private val viewModel: ScanViewModel by viewModels()
    private var currentImageUri: Uri? = null

    private val cropLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val data: Intent? = result.data
            data?.let {
                UCrop.getOutput(it)?.let { uri ->
                    showImage(uri)
                    currentImageUri = uri
                } ?: showToast(getString(R.string.ucrop_null))
            } ?: showToast(getString(R.string.failed))
        } else if (result.resultCode == Activity.RESULT_CANCELED) {
            showToast("Cropping cancelled.")
        } else if (result.resultCode == UCrop.RESULT_ERROR) {
            showToast(getString(R.string.failed))
        }
    }

    private val takePictureLauncher = registerForActivityResult(ActivityResultContracts.TakePicturePreview()) { bitmap ->
        bitmap?.let {
            viewModel.processImage(it)
            binding.imageView.setImageBitmap(it)
        } ?: showToast("Failed to capture image.")
    }

    private val launcherGallery = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let {
            currentImageUri = it
            startUCropActivity(it)
        } ?: Log.d("Photo Picker", "No media selected")
    }

    private val requestCameraPermissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
        if (granted) {
            takePictureLauncher.launch(null)
        } else {
            showToast("Camera permission denied.")
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentScanBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnSearchscan.setOnClickListener {
            val outputText = binding.tvOutput.text.toString()
            val intent = Intent(requireContext(), SearchActivity::class.java)
            intent.putExtra("query", outputText)
            startActivity(intent)
        }

        setupListeners()
        observeViewModel()
    }

    private fun setupListeners() {
        binding.btnCaptureImage.setOnClickListener {
            if (ContextCompat.checkSelfPermission(requireContext(), android.Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                takePictureLauncher.launch(null)
            } else {
                requestCameraPermissionLauncher.launch(android.Manifest.permission.CAMERA)
            }
        }

        binding.btnLoadImage.setOnClickListener {
            startGallery()
        }
        binding.btnAnalys.setOnClickListener {
            currentImageUri?.let {
                outputGeneratorFromUri(it)
            } ?: run { showToast(getString(R.string.insert_image))
            }
        }

    }

    private fun outputGeneratorFromUri(currentImageUri: Uri) {
        try {
            // Menggunakan requireContext() untuk mendapatkan ContentResolver
            val inputStream = requireContext().contentResolver.openInputStream(currentImageUri)
            val bitmap = BitmapFactory.decodeStream(inputStream)

            // Pastikan bitmap tidak null sebelum memproses
            bitmap?.let {
                outputGenerator(it)
            } ?: showToast("Gagal memuat gambar dari URI.")
        } catch (e: Exception) {
            // Tangani kesalahan yang mungkin terjadi saat membuka stream atau memproses gambar
            showToast("Error saat memproses gambar: ${e.message}")
            Log.e("ScanFragment", "Error processing image", e)
        }
    }

    private fun outputGenerator(bitmap: Bitmap) {
        var foodModel: FoodModel? = null
        try {
            val foodModel = FoodModel.newInstance(
                requireContext()
            )
            val newBitmap = bitmap.copy(
                Bitmap.Config.ARGB_8888,
                true
            )
            val tfImage =
                TensorImage.fromBitmap(newBitmap)

            val outputs =
                foodModel.process(tfImage)
                    .probabilityAsCategoryList.apply {
                        sortByDescending { it.score }
                    }
            val highProbability = outputs[0]

            viewLifecycleOwner.lifecycleScope.launch {
                binding.tvOutput.text =
                    highProbability.label
            }

            Log.i(
                "TAG",
                "outputGenerator $highProbability"
            )
            viewModel.processImage(bitmap)
        } finally {
            foodModel?.close()
        }
    }

    private fun startGallery() {
        launcherGallery.launch("image/*")
    }


    private fun startUCropActivity(uri: Uri) {
        val fileName = "cropped_image_${System.currentTimeMillis()}.jpg"
        val file = File(requireContext().filesDir, fileName)
        val outputUri = Uri.fromFile(file)
        val uCrop = UCrop.of(uri, outputUri).withAspectRatio(4f, 3f)
        cropLauncher.launch(uCrop.getIntent(requireContext()))
    }

    private fun observeViewModel() {
        viewModel.currentImageUri.observe(viewLifecycleOwner) { uri ->
            uri?.let { binding.imageView.setImageURI(it) }
        }
        viewModel.outputText.observe(viewLifecycleOwner) { text ->
            binding.tvOutput.text = text
        }
    }

    private fun showImage(imageUri: Uri?) {
        imageUri?.let {
            Glide.with(this)
                .load(it)
                .placeholder(R.drawable.ic_place_holder)  // Contoh placeholder
                .skipMemoryCache(true)  // Lewati cache memori
                .diskCacheStrategy(DiskCacheStrategy.NONE)  // Lewati cache disk
                .into(binding.imageView)
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}