package com.example.projectmm

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView

class ViewMovieActivity : AppCompatActivity()
{
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_movie)

        val extras = intent.extras

        val movieId = extras?.get("movie_id") as String

        val textview = findViewById<TextView>(R.id.id_holder_textview)
        textview.text = (movieId ?: "none")

    }

}