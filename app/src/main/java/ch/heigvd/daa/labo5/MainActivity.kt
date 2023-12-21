package ch.heigvd.daa.labo5

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.work.Data
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import ch.heigvd.daa.labo5.gallery.GalleryAdapter
import ch.heigvd.daa.labo5.gallery.ImageHandler
import ch.heigvd.daa.labo5.works.ImageCacheCleaner
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit
import kotlin.time.Duration.Companion.minutes

class MainActivity : AppCompatActivity() {
    companion object{
        lateinit var workManager: WorkManager
    }

    private lateinit var galleryAdapter: GalleryAdapter;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        ImageHandler.setCacheLocation(cacheDir)

        galleryAdapter = GalleryAdapter(lifecycleScope)
        val recycler = findViewById<RecyclerView>(R.id.gallery_view)
        recycler.layoutManager = GridLayoutManager(this, 3)
        recycler.adapter = galleryAdapter

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

    override fun onDestroy() {
        super.onDestroy()
        galleryAdapter.cancelAll()
    }

    private fun refresh() {
        val imageCacheCleaner = OneTimeWorkRequestBuilder<ImageCacheCleaner>().build()
        workManager.enqueue(imageCacheCleaner)
        galleryAdapter.notifyDataSetChanged()
    }

    private fun startPeriodicalImageCacheCleaningWork(){
        Log.i("Cache cleaning", "Starting periodic work")
        val imageCacheCleaningPeriodicWork = PeriodicWorkRequestBuilder<ImageCacheCleaner>(15, TimeUnit.MINUTES)
            .build()
        workManager.enqueue(imageCacheCleaningPeriodicWork)
        Log.i("Cache cleaning", "Started periodic work")
    }
}