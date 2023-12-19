package ch.heigvd.daa.labo5.works

import android.content.Context
import android.util.Log
import androidx.work.Worker
import androidx.work.WorkerParameters

class ImageCacheCleaner(appContext: Context, workerParams: WorkerParameters) : Worker(appContext, workerParams)  {
    override fun doWork(): Result {
        Log.i("Cache cleaning","Started")
        // Stuffs
        Log.i("Cache cleaning","Ended")
        return Result.success()
    }
}