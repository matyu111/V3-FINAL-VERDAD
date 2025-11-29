package com.tiendasuplementos.app.ui.main.product

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.tiendasuplementos.app.databinding.FragmentAddProductBinding

class AddProductFragment : Fragment() {

    private var _binding: FragmentAddProductBinding? = null
    private val binding get() = _binding!!

    private lateinit var productManager: ProductManager
    private var selectedImageUri: Uri? = null

    private val selectImageLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            result.data?.data?.let {
                selectedImageUri = it
                binding.imageViewProductPreview.setImageURI(it)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddProductBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        productManager = ProductManager.getInstance(requireContext())

        binding.buttonSelectImage.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            selectImageLauncher.launch(intent)
        }

        binding.buttonCreateProduct.setOnClickListener {
            val name = binding.editTextProductName.text.toString()
            val description = binding.editTextProductDescription.text.toString()
            val price = binding.editTextProductPrice.text.toString().toDoubleOrNull()
            val stock = binding.editTextProductStock.text.toString().toIntOrNull()

            if (name.isNotBlank() && description.isNotBlank() && price != null && stock != null && selectedImageUri != null) {
                productManager.createProduct(lifecycleScope, name, description, price, stock, selectedImageUri!!)
            } else {
                Toast.makeText(requireContext(), "Por favor, completa todos los campos y selecciona una imagen", Toast.LENGTH_SHORT).show()
            }
        }

        observeCreationState()
    }

    private fun observeCreationState() {
        productManager.creationState.observe(viewLifecycleOwner) { state ->
            when (state) {
                is UiState.Success -> {
                    Toast.makeText(requireContext(), "Producto creado con éxito", Toast.LENGTH_SHORT).show()
                    productManager.resetCreationState()
                    parentFragmentManager.popBackStack()
                }
                is UiState.Error -> {
                    Toast.makeText(requireContext(), state.message, Toast.LENGTH_LONG).show()
                    productManager.resetCreationState()
                }
                else -> {}
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}