package ch.heigvd.daa.labo5.gallery;

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import ch.heigvd.daa.labo5.R
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.withContext
import java.net.URL
import kotlin.concurrent.thread
import kotlin.coroutines.coroutineContext

class GalleryAdapter() : RecyclerView.Adapter<GalleryAdapter.ViewHolder>() {
    companion object {
        const val COUNT_IMAGES = 10;
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.list_item, parent, false))
    }

    override fun getItemCount(): Int {
        return COUNT_IMAGES
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        thread {
            val url = URL("https://daa.iict.ch/images/$position.jpg")
            val bytes = url.readBytes()
            val bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
            holder.bind(bitmap)
        }
    }

    inner class ViewHolder(view: View): RecyclerView.ViewHolder(view) {
        private val imgView = view.findViewById<ImageView>(R.id.image)
        fun bind(image: Bitmap) {

            imgView.setImageBitmap(image)
        }
    }
}
