package ch.heigvd.daa.labo5.works

import android.content.Context
import android.util.Log
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.lifecycle.lifecycleScope
import androidx.work.Worker
import androidx.work.WorkerParameters
import ch.heigvd.daa.labo5.gallery.GalleryAdapter
import ch.heigvd.daa.labo5.gallery.ImageHandler
import kotlinx.coroutines.launch

class ImageCacheCleaner(
    appContext: Context, workerParams: WorkerParameters
) : Worker(appContext, workerParams) {
    override fun doWork(): Result {
        Log.i("Cache cleaning", "Started")

        ImageHandler.deleteImagesCache()

        Log.i("Cache cleaning", "Ended")
        return Result.success()
    }
}