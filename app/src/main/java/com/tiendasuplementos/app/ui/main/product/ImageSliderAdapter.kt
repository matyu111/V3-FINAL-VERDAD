package com.tiendasuplementos.app.ui.main.product

import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.tiendasuplementos.app.R

class ImageSliderAdapter(private val images: List<Any>) : RecyclerView.Adapter<ImageSliderAdapter.ImageViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val imageView = LayoutInflater.from(parent.context).inflate(R.layout.item_image_slider, parent, false) as ImageView
        return ImageViewHolder(imageView)
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        holder.bind(images[position])
    }

    override fun getItemCount(): Int = images.size

    inner class ImageViewHolder(private val imageView: ImageView) : RecyclerView.ViewHolder(imageView) {
        private val BASE_IMAGE_URL = "https://x8ki-letl-twmt.n7.xano.io"

        fun bind(image: Any) {
            when (image) {
                is Uri -> imageView.load(image)
                is String -> {
                    val fullImageUrl = if (image.startsWith("http")) image else BASE_IMAGE_URL + image
                    imageView.load(fullImageUrl) { 
                        placeholder(R.drawable.ic_placeholder)
                        error(R.drawable.ic_error)
                    }
                }
            }
        }
    }
}
