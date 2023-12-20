package ch.heigvd.daa.labo5.gallery;

import android.graphics.Bitmap
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.recyclerview.widget.RecyclerView
import ch.heigvd.daa.labo5.R
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class GalleryAdapter(private val scope: LifecycleCoroutineScope, private val handler: ImageHandler) : RecyclerView.Adapter<GalleryAdapter.ViewHolder>() {
    companion object {
        const val COUNT_IMAGES = 10_000;
    }

    public fun cancelAll() {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.list_item, parent, false))
    }

    override fun getItemCount(): Int {
        return COUNT_IMAGES
    }

    override fun onViewRecycled(holder: ViewHolder) {
        super.onViewRecycled(holder)
        if (holder.currentJob?.isCompleted != true) {
            holder.currentJob?.cancel()
            Log.d("GalleryAdapter", "Job cancelled for ${holder.adapterPosition}")
        }
    }
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Log.d("GalleryAdapter", "Job started for ${holder.adapterPosition}")
        holder.currentJob = scope.launch {
            val imgBytes = handler.getOrCacheImage(position)
            val bitmap = handler.decodeImage(imgBytes)
            holder.bind(bitmap)
        }
    }

    inner class ViewHolder(view: View): RecyclerView.ViewHolder(view) {
        private val imgView = view.findViewById<ImageView>(R.id.image)
        private val progressBar = view.findViewById<ProgressBar>(R.id.progressbar)
        var currentJob: Job? = null;

        fun bind(image: Bitmap) {
            imgView.setImageBitmap(image)
            progressBar.visibility = View.GONE
            imgView.visibility = View.VISIBLE
        }
    }
}
