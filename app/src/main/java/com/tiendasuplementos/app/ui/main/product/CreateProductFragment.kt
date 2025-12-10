package com.tiendasuplementos.app.ui.main.product

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.tiendasuplementos.app.databinding.FragmentCreateProductBinding
import com.tiendasuplementos.app.ui.state.UiState

class CreateProductFragment : Fragment() {

    private var _binding: FragmentCreateProductBinding? = null
    private val binding get() = _binding!!

    private lateinit var productManager: ProductManager
    private val selectedImageUris = mutableListOf<Uri>()

    private val selectImagesLauncher = registerForActivityResult(ActivityResultContracts.GetMultipleContents()) { uris: List<Uri> ->
        if (uris.isNotEmpty()) {
            selectedImageUris.clear()
            selectedImageUris.addAll(uris)
            showImagePreviews()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCreateProductBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        productManager = ProductManager.getInstance(requireContext())

        binding.buttonSelectImages.setOnClickListener {
            selectImagesLauncher.launch("image/*")
        }

        binding.buttonSaveProduct.setOnClickListener {
            saveProduct()
        }

        productManager.creationState.observe(viewLifecycleOwner) { state ->
            when (state) {
                is UiState.Success -> {
                    Toast.makeText(requireContext(), "Producto creado con éxito", Toast.LENGTH_SHORT).show()
                    parentFragmentManager.popBackStack()
                }
                is UiState.Error -> {
                    Toast.makeText(requireContext(), "Error: ${state.message}", Toast.LENGTH_SHORT).show()
                }
                else -> { /* No-op */ }
            }
        }
    }

    private fun showImagePreviews() {
        binding.imagePreviewContainer.removeAllViews()
        for (uri in selectedImageUris) {
            val imageView = ImageView(requireContext()).apply {
                layoutParams = ViewGroup.LayoutParams(200, 200)
                scaleType = ImageView.ScaleType.CENTER_CROP
                setImageURI(uri)
            }
            binding.imagePreviewContainer.addView(imageView)
        }
    }

    private fun saveProduct() {
        val name = binding.textFieldName.editText?.text.toString().trim()
        val description = binding.textFieldDescription.editText?.text.toString().trim()
        val price = binding.textFieldPrice.editText?.text.toString().toDoubleOrNull()
        val stock = binding.textFieldStock.editText?.text.toString().toIntOrNull()

        Log.d("ProductCreation", "Attempting to save product:")
        Log.d("ProductCreation", "Name: '$name' (isEmpty: ${name.isEmpty()})")
        Log.d("ProductCreation", "Description: '$description' (isEmpty: ${description.isEmpty()})")
        Log.d("ProductCreation", "Price: $price (isNull: ${price == null})")
        Log.d("ProductCreation", "Stock: $stock (isNull: ${stock == null})")
        Log.d("ProductCreation", "Image URIs count: ${selectedImageUris.size} (isEmpty: ${selectedImageUris.isEmpty()})")

        if (name.isEmpty() || description.isEmpty() || price == null || stock == null || selectedImageUris.isEmpty()) {
            Log.d("ProductCreation", "Validation failed. Showing toast.")
            Toast.makeText(requireContext(), "Por favor, completa todos los campos y selecciona al menos una imagen", Toast.LENGTH_LONG).show()
            return
        }

        Log.d("ProductCreation", "Validation passed. Calling createProduct.")
        // El ProductManager se actualizará para manejar múltiples URIs
        productManager.createProduct(lifecycleScope, name, description, price, stock, selectedImageUris)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        productManager.resetCreationState()
    }
}
