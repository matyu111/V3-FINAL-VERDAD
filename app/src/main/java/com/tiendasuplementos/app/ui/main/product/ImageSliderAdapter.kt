package com.tiendasuplementos.app.ui.main.product

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.tiendasuplementos.app.R
import com.tiendasuplementos.app.data.remote.dto.ImageUrl

class ImageSliderAdapter(private val images: List<ImageUrl>) : RecyclerView.Adapter<ImageSliderAdapter.ImageViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_image_preview, parent, false)
        return ImageViewHolder(view)
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        images[position].url?.let {
            holder.imageView.load(it)
        }
    }

    override fun getItemCount(): Int = images.size

    class ImageViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imageView: ImageView = view.findViewById(R.id.previewImageView)
    }
}