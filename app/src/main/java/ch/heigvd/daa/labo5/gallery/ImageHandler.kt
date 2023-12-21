package ch.heigvd.daa.labo5.gallery

import android.graphics.BitmapFactory
import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.coroutines.yield
import java.io.File
import java.net.URL
import java.time.Instant
import kotlin.time.Duration
import kotlin.time.Duration.Companion.minutes

object ImageHandler {
    private lateinit var cacheLocation: File
    private var cacheDuration: Duration = 5.minutes

    fun setCacheLocation(cacheLocation: File) {
        this.cacheLocation = cacheLocation
    }

    fun deleteImagesCache() {
        cacheLocation.list()?.onEach {
            Log.d("ImageDownloader - Cache", "deleting $it")
            File(cacheLocation, it).delete()
        }
    }

    suspend fun decodeImage(bytes: ByteArray?) = withContext(Dispatchers.Default) {
        BitmapFactory.decodeByteArray(bytes, 0, bytes?.size ?: 0)
    }

    suspend fun getOrCacheImage(position: Int): ByteArray = withContext(Dispatchers.IO) {
        val cachedImage = File(cacheLocation, "$position")
        // lastModified returns 0 if it does not exist
        val expirationEpoch = cachedImage.lastModified() + cacheDuration.inWholeMilliseconds
        val currentEpoch = Instant.now().toEpochMilli()
        yield()
        // cache has expired or file does not exist => download it and cache it
        if (expirationEpoch < currentEpoch) {
            Log.d("ImageDownloader - Cache", "Cache miss for $position")
            val img = URL("https://daa.iict.ch/images/$position.jpg").readBytes()
            yield()
            cachedImage.writeBytes(img)
            return@withContext img
        }

        // did not expire => returns it
        Log.d("ImageDownloader - Cache", "Cache hit for $position")
        return@withContext cachedImage.readBytes()
    }


}