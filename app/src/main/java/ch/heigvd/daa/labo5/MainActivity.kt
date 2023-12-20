package ch.heigvd.daa.labo5

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.work.BackoffPolicy
import androidx.work.Constraints
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.PeriodicWorkRequest
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import ch.heigvd.daa.labo5.gallery.GalleryAdapter
import ch.heigvd.daa.labo5.gallery.ImageDownloader
import ch.heigvd.daa.labo5.works.ImageCacheCleaner
import java.util.concurrent.TimeUnit
import kotlin.time.Duration
import kotlin.time.Duration.Companion.minutes

class MainActivity : AppCompatActivity() {
    companion object{
        lateinit var workManager: WorkManager
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val recycler = findViewById<RecyclerView>(R.id.gallery_view)
        recycler.layoutManager = GridLayoutManager(this, 3)
        recycler.adapter = GalleryAdapter(lifecycleScope, ImageDownloader(cacheDir, 5.minutes))

        workManager = WorkManager.getInstance(applicationContext)
        startPeriodicalImageCacheCleaningWork()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.main_menu_refresh -> {
                refresh()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun refresh() {
        val imageCacheCleaner = OneTimeWorkRequestBuilder<ImageCacheCleaner>().build()
        workManager.enqueue(imageCacheCleaner)
    }

    private fun startPeriodicalImageCacheCleaningWork(){
        Log.i("Cache cleaning", "Starting periodic work")
        val imageCacheCleaningPeriodicWork = PeriodicWorkRequestBuilder<ImageCacheCleaner>(15, TimeUnit.MINUTES)
            .build()
        workManager.enqueue(imageCacheCleaningPeriodicWork)
        Log.i("Cache cleaning", "Started periodic work")
    }
}