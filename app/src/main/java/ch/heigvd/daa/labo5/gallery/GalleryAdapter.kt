package ch.heigvd.daa.labo5.gallery;

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.transition.Visibility
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.recyclerview.widget.RecyclerView
import ch.heigvd.daa.labo5.R
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.net.URL
import kotlin.concurrent.thread
import kotlin.coroutines.coroutineContext

class GalleryAdapter(private val scope: LifecycleCoroutineScope) : RecyclerView.Adapter<GalleryAdapter.ViewHolder>() {
    companion object {
        const val COUNT_IMAGES = 10_000;

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.list_item, parent, false))
    }

    override fun getItemCount(): Int {
        return COUNT_IMAGES
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val downloader = ImageDownloader()
        scope.launch {
            val img = downloader.getImage(position)
            holder.bind(img)
        }
    }

    inner class ViewHolder(view: View): RecyclerView.ViewHolder(view) {
        private val imgView = view.findViewById<ImageView>(R.id.image)
        private val progressBar = view.findViewById<ProgressBar>(R.id.progressbar);
        fun bind(image: Bitmap) {
            imgView.setImageBitmap(image)
            progressBar.visibility = View.GONE
            imgView.visibility = View.VISIBLE
        }
    }
}
