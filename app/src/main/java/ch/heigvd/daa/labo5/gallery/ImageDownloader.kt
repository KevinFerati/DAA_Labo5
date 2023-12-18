package ch.heigvd.daa.labo5.gallery

import android.graphics.BitmapFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.net.URL

class ImageDownloader private constructor() {
    companion object {
        val instance: ImageDownloader by lazy {
            ImageDownloader()
        }
    }

    private suspend fun downloadImage(imgUrl: String) = withContext(Dispatchers.IO) {
        val url = URL(imgUrl)
        url.readBytes()
    }

    private suspend fun decodeImage(bytes: ByteArray?) = withContext(Dispatchers.Default) {
        BitmapFactory.decodeByteArray(bytes, 0, bytes?.size ?: 0)
    }





}