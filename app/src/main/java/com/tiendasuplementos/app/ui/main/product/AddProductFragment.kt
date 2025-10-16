package com.tiendasuplementos.app.ui.main.product

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.tiendasuplementos.app.R

class AddProductFragment : Fragment() {

    private val productViewModel: ProductViewModel by activityViewModels()
    private lateinit var imageViewPreview: ImageView
    private var selectedImageUri: Uri? = null

    private val selectImageLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            result.data?.data?.let {
                selectedImageUri = it
                imageViewPreview.setImageURI(it)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_add_product, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        imageViewPreview = view.findViewById(R.id.imageViewProductPreview)
        val selectImageButton: Button = view.findViewById(R.id.buttonSelectImage)
        val nameEditText: EditText = view.findViewById(R.id.editTextProductName)
        val priceEditText: EditText = view.findViewById(R.id.editTextProductPrice)
        val stockEditText: EditText = view.findViewById(R.id.editTextProductStock)
        val createButton: Button = view.findViewById(R.id.buttonCreateProduct)

        selectImageButton.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            selectImageLauncher.launch(intent)
        }

        createButton.setOnClickListener {
            val name = nameEditText.text.toString()
            val price = priceEditText.text.toString().toDoubleOrNull()
            val stock = stockEditText.text.toString().toIntOrNull()

            if (name.isNotBlank() && price != null && stock != null && selectedImageUri != null) {
                productViewModel.createProduct(name, price, stock, selectedImageUri!!)
            } else {
                Toast.makeText(requireContext(), "Por favor, completa todos los campos y selecciona una imagen", Toast.LENGTH_SHORT).show()
            }
        }

        observeCreationState()
    }

    private fun observeCreationState() {
        productViewModel.creationState.observe(viewLifecycleOwner) { state ->
            when (state) {
                is CreationState.Success -> {
                    Toast.makeText(requireContext(), "Producto creado con éxito", Toast.LENGTH_SHORT).show()
                    productViewModel.resetCreationState() // Resetea el estado
                    parentFragmentManager.popBackStack() // Vuelve a la lista
                }
                is CreationState.Error -> {
                    Toast.makeText(requireContext(), state.message, Toast.LENGTH_LONG).show()
                    productViewModel.resetCreationState() // Resetea el estado
                }
                else -> {}
            }
        }
    }
}