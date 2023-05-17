package com.example.projectmm

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.runBlocking

class MovieDetailsActivity : AppCompatActivity() {

    lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movie_details)

        val extras = intent.extras

        val moviesAPI = RetrofitHelper.getInstance("https://api.themoviedb.org/3/")
            .create(TheMovieDatabaseService::class.java)

        val movieID = extras?.get("movie_id") as? Int
        val titleTextview = findViewById<TextView>(R.id.details_movie_title_textview)
        val overviewTextview = findViewById<TextView>(R.id.details_movie_overview_textview)
        val typeTextView = findViewById<TextView>(R.id.details_movie_type_textview)
        val runtimeTextView = findViewById<TextView>(R.id.details_movie_runtime_textview)
        val genreTextView = findViewById<TextView>(R.id.details_movie_genre_textview)
        val detailsTextView = findViewById<TextView>(R.id.details_movie_details_textview)

        this.recyclerView = findViewById<RecyclerView>(R.id.movie_list_item)
        recyclerView.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false)


        runBlocking {
            val movie = movieID?.let { moviesAPI.getMovieById(it) }
            titleTextview.text = movie?.title ?: movie?.name ?: "On a perdu le titre dans l'API"
            overviewTextview.text = movie?.overview

            runtimeTextView.text = movie?.runtime.toString()
            genreTextView.text = movie?.genres.toString()
            DownloadImageFromInternet(findViewById(R.id.details_movie_poster_imageview), applicationContext).execute("https://image.tmdb.org/t/p/original/" + movie?.poster_path)
            DownloadImageFromInternet(findViewById(R.id.details_movie_back_drop_imageview), applicationContext).execute("https://image.tmdb.org/t/p/original/" + movie?.backdrop_path)

            val recommended = movieID?.let {
                moviesAPI.getRecommendedMovies(it)
                Log.d("Test recommended",  moviesAPI.getRecommendedMovies(it).results.toString())
            }
            if (recommended != null) {
                recyclerView.adapter = MovieAdapter(moviesAPI.getRecommendedMovies(movieID).results.take(6), this@MovieDetailsActivity)
            }
        }
    }
}