package com.example.projectmm

import android.content.Intent
import android.graphics.Color
import android.graphics.RenderEffect
import android.graphics.Shader
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kotlinx.coroutines.runBlocking

class MovieDetailsActivity : HomeActivity() {

    lateinit var recyclerViewRecommended: RecyclerView
    lateinit var recyclerViewSimilar: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movie_details)

        super.setBottomBarListener()

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

        val imageBackdrop = findViewById<ImageView>(R.id.details_movie_back_drop_imageview)

        this.recyclerViewRecommended = findViewById<RecyclerView>(R.id.recommended_movie_list_item)
        recyclerViewRecommended.layoutManager = GridLayoutManager(this, 2)

        this.recyclerViewSimilar = findViewById<RecyclerView>(R.id.similar_movie_list_item)
        recyclerViewSimilar.layoutManager = GridLayoutManager(this, 2)


        runBlocking {
            val movie = movieID?.let { moviesAPI.getMovieById(it) }
            titleTextview.text = movie?.title ?: movie?.name ?: "On a perdu le titre dans l'API"
            overviewTextview.text = movie?.overview

            runtimeTextView.text = movie?.runtime.toString() + " min"
            genreTextView.text = movie?.genres.toString().substring(1, movie?.genres.toString().length-1).replace(","," ")

            Glide.with(this@MovieDetailsActivity)
                .load("https://image.tmdb.org/t/p/original/" + movie?.poster_path)
                .into(findViewById(R.id.details_movie_poster_imageview))
            Glide.with(this@MovieDetailsActivity)
                .load("https://image.tmdb.org/t/p/original/" + movie?.backdrop_path)
                .into(imageBackdrop)

            imageBackdrop.setRenderEffect(RenderEffect.createBlurEffect(10F, 10F, Shader.TileMode.MIRROR))

//            DownloadImageFromInternet(findViewById(R.id.details_movie_poster_imageview), applicationContext).execute("https://image.tmdb.org/t/p/original/" + movie?.poster_path)
//            DownloadImageFromInternet(findViewById(R.id.details_movie_back_drop_imageview), applicationContext).execute("https://image.tmdb.org/t/p/original/" + movie?.backdrop_path)

            val recommended = movieID?.let {
                moviesAPI.getRecommendedMovies(it)
            }
            val similar = movieID?.let {
                moviesAPI.getSimilarMovies(it)
            }
            if (recommended != null && similar != null) {
                recyclerViewRecommended.adapter = MovieAdapter(moviesAPI.getRecommendedMovies(movieID).results.take(6), this@MovieDetailsActivity)
                recyclerViewSimilar.adapter = MovieAdapter(moviesAPI.getSimilarMovies(movieID).results.take(6), this@MovieDetailsActivity)

            }
        }
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_details,menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.action_addToFav -> {
                // TODO: Add movie to Favorites
            }
            R.id.action_viewQRCode -> {
                // TODO: Show movie QR Code
            }
            R.id.action_returnHome -> {
                val intent = Intent(this, HomeActivity::class.java)
                startActivity(intent)
            }
        }
        return super.onOptionsItemSelected(item)
    }
}