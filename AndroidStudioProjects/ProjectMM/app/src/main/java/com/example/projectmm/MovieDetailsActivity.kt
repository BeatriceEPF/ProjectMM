package com.example.projectmm

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.example.projectmm.model.Movie

class MovieDetailsActivity : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movie_details)

        val extras = intent.extras

        val movieExtra = extras?.get("movie") as? Movie

        val titleTextview = findViewById<TextView>(R.id.details_movie_title_textview)

        titleTextview.text = movieExtra?.title ?: movieExtra?.name ?: "On a perdu le titre dans l'API"
        DownloadImageFromInternet(findViewById(R.id.details_movie_poster_imageview), applicationContext).execute("https://image.tmdb.org/t/p/original/" + movieExtra?.poster_path)
    }
}