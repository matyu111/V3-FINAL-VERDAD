package com.tiendasuplementos.app.ui.main.product

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.tiendasuplementos.app.data.remote.dto.Product
import com.tiendasuplementos.app.databinding.FragmentEditProductBinding
import com.tiendasuplementos.app.ui.state.UiState

class EditProductFragment : Fragment() {

    private var _binding: FragmentEditProductBinding? = null
    private val binding get() = _binding!!

    private val args: EditProductFragmentArgs by navArgs()
    private var existingProduct: Product? = null

    private lateinit var productManager: ProductManager
    private val selectedImageUris = mutableListOf<Uri>()
    private lateinit var imageSliderAdapter: ImageSliderAdapter

    private val selectImagesLauncher = registerForActivityResult(ActivityResultContracts.GetMultipleContents()) { uris: List<Uri> ->
        if (uris.isNotEmpty()) {
            selectedImageUris.clear()
            selectedImageUris.addAll(uris)
            updateImageSlider()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEditProductBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        existingProduct = args.product
        productManager = ProductManager.getInstance(requireContext())

        setupViews()
        setupObservers()
    }

    private fun setupViews() {
        if (existingProduct != null) {
            // Modo Edición
            binding.textFieldName.editText?.setText(existingProduct!!.name)
            binding.textFieldDescription.editText?.setText(existingProduct!!.description)
            binding.textFieldPrice.editText?.setText(existingProduct!!.price.toString())
            binding.textFieldStock.editText?.setText(existingProduct!!.stock.toString())
            val imageUrls = existingProduct?.image?.url?.let { listOf(it) } ?: emptyList()
            imageSliderAdapter = ImageSliderAdapter(imageUrls)
            binding.buttonDeleteProduct.visibility = View.VISIBLE
        } else {
            // Modo Creación
            imageSliderAdapter = ImageSliderAdapter(emptyList())
            binding.buttonDeleteProduct.visibility = View.GONE
        }

        binding.viewPagerImages.adapter = imageSliderAdapter

        binding.buttonSelectImages.setOnClickListener {
            selectImagesLauncher.launch("image/*")
        }

        binding.buttonSaveProduct.setOnClickListener {
            saveChanges()
        }

        binding.buttonDeleteProduct.setOnClickListener {
            showDeleteConfirmationDialog()
        }
    }

    private fun setupObservers() {
        // Observador para creación
        productManager.creationState.observe(viewLifecycleOwner) { state ->
             when (state) {
                is UiState.Loading -> {
                    // Opcional: mostrar un ProgressBar
                }
                is UiState.Success -> {
                    Toast.makeText(requireContext(), "producto creado", Toast.LENGTH_SHORT).show()
                    productManager.resetCreationState()
                    findNavController().popBackStack()
                }
                is UiState.Error -> {
                    Toast.makeText(requireContext(), "Error al crear: ${state.message}", Toast.LENGTH_LONG).show()
                    productManager.resetCreationState()
                }
                else -> { /* No-op */ }
            }
        }

        // Observador para actualización
        productManager.updateState.observe(viewLifecycleOwner) { state ->
            when (state) {
                is UiState.Loading -> {
                     // Opcional: mostrar un ProgressBar
                }
                is UiState.Success -> {
                    Toast.makeText(requireContext(), "Producto actualizado", Toast.LENGTH_SHORT).show()
                    productManager.resetUpdateState()
                    findNavController().popBackStack()
                }
                 is UiState.Error -> {
                    Toast.makeText(requireContext(), "Error al actualizar: ${state.message}", Toast.LENGTH_LONG).show()
                    productManager.resetUpdateState()
                }
                else -> { /* No-op */ }
            }
        }

        // Observador para borrado
        productManager.deletionState.observe(viewLifecycleOwner) { state ->
             when (state) {
                is UiState.Loading -> {
                     // Opcional: mostrar un ProgressBar
                }
                is UiState.Success -> {
                    Toast.makeText(requireContext(), "Producto eliminado con éxito", Toast.LENGTH_SHORT).show()
                    productManager.resetDeletionState()
                    findNavController().popBackStack()
                }
                 is UiState.Error -> {
                    Toast.makeText(requireContext(), "Error al eliminar: ${state.message}", Toast.LENGTH_LONG).show()
                    productManager.resetDeletionState()
                }
                else -> { /* No-op */ }
            }
        }
    }

    private fun updateImageSlider() {
        imageSliderAdapter = ImageSliderAdapter(selectedImageUris)
        binding.viewPagerImages.adapter = imageSliderAdapter
    }

    private fun saveChanges() {
        val name = binding.textFieldName.editText?.text.toString().trim()
        val description = binding.textFieldDescription.editText?.text.toString().trim()
        val price = binding.textFieldPrice.editText?.text.toString().toDoubleOrNull()
        val stock = binding.textFieldStock.editText?.text.toString().toIntOrNull()

        if (name.isNotBlank() && description.isNotBlank() && price != null && stock != null) {
            if (existingProduct != null) {
                // Actualizar producto existente
                productManager.updateProduct(lifecycleScope, existingProduct!!.id, name, description, price, stock, selectedImageUris)
            } else {
                // Crear nuevo producto (si se llega a este fragment sin un producto)
                productManager.createProduct(lifecycleScope, name, description, price, stock, selectedImageUris)
            }
        } else {
            Toast.makeText(requireContext(), "Por favor, completa todos los campos", Toast.LENGTH_SHORT).show()
        }
    }

    private fun showDeleteConfirmationDialog() {
        AlertDialog.Builder(requireContext())
            .setTitle("Confirmar Eliminación")
            .setMessage("¿Estás seguro de que deseas eliminar este producto?")
            .setPositiveButton("Eliminar") { _, _ ->
                existingProduct?.let { productManager.deleteProduct(lifecycleScope, it.id) }
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        productManager.resetCreationState()
        productManager.resetUpdateState()
        productManager.resetDeletionState()
    }
}