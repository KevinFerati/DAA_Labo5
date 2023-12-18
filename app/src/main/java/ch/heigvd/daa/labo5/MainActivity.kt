package ch.heigvd.daa.labo5

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ch.heigvd.daa.labo5.gallery.GalleryAdapter

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val adapter = GalleryAdapter()
        val recycler = findViewById<RecyclerView>(R.id.gallery_view)

        recycler.layoutManager = GridLayoutManager(this, 3)
        recycler.adapter = adapter

    }
}