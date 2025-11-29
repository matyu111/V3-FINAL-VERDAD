package com.tiendasuplementos.app.ui.main.product

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.tiendasuplementos.app.data.remote.dto.Product
import com.tiendasuplementos.app.databinding.FragmentAddProductBinding

class EditProductFragment(private val product: Product) : Fragment() {

    private var _binding: FragmentAddProductBinding? = null
    private val binding get() = _binding!!

    private lateinit var productManager: ProductManager

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

        binding.titleTextView.text = "Editar Producto"
        binding.buttonCreateProduct.text = "Guardar Cambios"

        binding.editTextProductName.setText(product.name)
        binding.editTextProductDescription.setText(product.description)
        binding.editTextProductPrice.setText(product.price.toString())
        binding.editTextProductStock.setText(product.stock.toString())

        binding.buttonSelectImage.visibility = View.GONE
        binding.imageViewProductPreview.visibility = View.GONE

        productManager.updateState.observe(viewLifecycleOwner) { state ->
            when (state) {
                is UiState.Success -> {
                    Toast.makeText(requireContext(), "Producto actualizado con éxito", Toast.LENGTH_SHORT).show()
                    productManager.resetUpdateState()
                    parentFragmentManager.popBackStack()
                }
                is UiState.Error -> {
                    Toast.makeText(requireContext(), state.message, Toast.LENGTH_SHORT).show()
                    productManager.resetUpdateState()
                }
                else -> { /* No hacemos nada en Idle o Loading */ }
            }
        }

        binding.buttonCreateProduct.setOnClickListener {
            val name = binding.editTextProductName.text.toString()
            val description = binding.editTextProductDescription.text.toString()
            val price = binding.editTextProductPrice.text.toString().toDoubleOrNull()
            val stock = binding.editTextProductStock.text.toString().toIntOrNull()

            if (name.isNotBlank() && description.isNotBlank() && price != null && stock != null) {
                productManager.updateProduct(lifecycleScope, product.id, name, description, price, stock)
            } else {
                Toast.makeText(requireContext(), "Por favor, completa todos los campos", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}