package ch.heigvd.daa.labo5.gallery

import android.graphics.BitmapFactory
import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.net.URL
import java.time.Instant
import kotlin.time.Duration

class ImageHandler(private val cacheLocation: File,
                   private val cacheDuration: Duration)  {
    suspend fun deleteImagesCache() = withContext(Dispatchers.IO) {
        cacheLocation.list()?.onEach {
            Log.d("ImageDownloader - Cache", "deleting $it")
            File(cacheLocation, it).delete()
        }
    }

    suspend fun decodeImage(bytes: ByteArray?) = withContext(Dispatchers.Default) {
        BitmapFactory.decodeByteArray(bytes, 0, bytes?.size ?: 0)
    }

    suspend fun getOrCacheImage(position: Int) : ByteArray = withContext(Dispatchers.IO) {
        val cachedImage = File(cacheLocation, "$position")
        // lastModified returns 0 if it does not exist
        val expirationEpoch = cachedImage.lastModified() + cacheDuration.inWholeMilliseconds
        val currentEpoch = Instant.now().toEpochMilli()

        // cache has expired or file does not exist => download it and cache it
        if (expirationEpoch < currentEpoch)  {
            Log.d("ImageDownloader - Cache", "Cache miss for $position")
            val img = URL("https://daa.iict.ch/images/$position.jpg").readBytes()
            cachedImage.writeBytes(img)
            return@withContext img
        }

        // did not expire => returns it
        Log.d("ImageDownloader - Cache", "Cache hit for $position")
        return@withContext cachedImage.readBytes()
    }


}