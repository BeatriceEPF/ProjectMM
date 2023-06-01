package com.example.projectmm

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.RenderEffect
import android.graphics.Shader
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.projectmm.model.MovieDetail
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.runBlocking
import org.json.JSONObject
import kotlin.math.roundToInt

class MovieDetailsActivity : HomeActivity() {

    lateinit var recyclerViewRecommended: RecyclerView
    lateinit var recyclerViewSimilar: RecyclerView
    private var isFav = false
    private var movieID = 0

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movie_details)

        super.setBottomBarListener()
        val bottomNav = findViewById<BottomNavigationView>(R.id.bottomNav)

        val extras = intent.extras

        val moviesAPI = RetrofitHelper.getInstance("https://api.themoviedb.org/3/")
            .create(TheMovieDatabaseService::class.java)

        this.movieID = (extras?.get("movie_id") as? Int)!!

        val titleTextview = findViewById<TextView>(R.id.details_movie_title_textview)
        val overviewTextview = findViewById<TextView>(R.id.details_movie_overview_textview)
        val typeTextView = findViewById<TextView>(R.id.details_movie_type_textview)
        val runtimeTextView = findViewById<TextView>(R.id.details_movie_runtime_textview)
        val genreTextView = findViewById<TextView>(R.id.details_movie_genre_textview)
        val detailsTextView = findViewById<TextView>(R.id.details_movie_details_textview)

        val imageBackdrop = findViewById<ImageView>(R.id.details_movie_back_drop_imageview)

        this.recyclerViewRecommended = findViewById<RecyclerView>(R.id.recommended_movie_list_item)
        recyclerViewRecommended.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

        this.recyclerViewSimilar = findViewById<RecyclerView>(R.id.similar_movie_list_item)
        recyclerViewSimilar.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

        runBlocking {
            val movie = movieID?.let { moviesAPI.getMovieDetailsById(it) }
            titleTextview.text = movie?.title ?: movie?.name ?: "On a perdu le titre dans l'API"
            overviewTextview.text = movie?.overview

            setNoteStars(movie)

            detailsTextView.text =
                movie?.toStringAdult() + "\n" +
                "Release date : " + movie?.release_date + "\n" +
                "Budget : " + movie?.budget.toString() + " $" + "\n" +
                "Original language : " + movie?.original_language + "\n" +
                "Original title : " + movie?.original_title + "\n" +
                "Production companies : "  + movie?.production_companies.toString().substring(1, movie?.production_companies.toString().length - 1).replace(",", "") + "\n" +
                "Spoken languages : "  + movie?.spoken_languages.toString().substring(1, movie?.spoken_languages.toString().length - 1)

            typeTextView.text = movie?.tagline + "\n"

            runtimeTextView.text = movie?.runtime.toString() + " min"
            genreTextView.text =
                movie?.genres.toString().substring(1, movie?.genres.toString().length - 1)
                    .replace(",", " ")

            Glide.with(this@MovieDetailsActivity)
                .load("https://image.tmdb.org/t/p/original/" + movie?.poster_path)
                .into(findViewById(R.id.details_movie_poster_imageview))
            Glide.with(this@MovieDetailsActivity)
                .load("https://image.tmdb.org/t/p/original/" + movie?.backdrop_path)
                .into(imageBackdrop)

            imageBackdrop.setRenderEffect(
                RenderEffect.createBlurEffect(
                    10F,
                    10F,
                    Shader.TileMode.MIRROR
                )
            )

//            DownloadImageFromInternet(findViewById(R.id.details_movie_poster_imageview), applicationContext).execute("https://image.tmdb.org/t/p/original/" + movie?.poster_path)
//            DownloadImageFromInternet(findViewById(R.id.details_movie_back_drop_imageview), applicationContext).execute("https://image.tmdb.org/t/p/original/" + movie?.backdrop_path)

            val recommended = movieID?.let {
                moviesAPI.getRecommendedMovies(it)
            }
            val similar = movieID?.let {
                moviesAPI.getSimilarMovies(it)
            }
            if (recommended != null && similar != null) {
                recyclerViewRecommended.adapter = MovieAdapter(
                    moviesAPI.getRecommendedMovies(movieID).results.take(6),
                    this@MovieDetailsActivity,
                    R.layout.movie_detail_list_item
                )
                recyclerViewSimilar.adapter = MovieAdapter(
                    moviesAPI.getSimilarMovies(movieID).results.take(6),
                    this@MovieDetailsActivity,
                    R.layout.movie_detail_list_item
                )

            }
        }

        if (super.isConnected()) this.isFav = isFavMovie()
    }

    @SuppressLint("SetTextI18n")
    private fun setNoteStars(movie: MovieDetail?) {
        val starIcons = ArrayList<ImageView>()
        starIcons.add(findViewById(R.id.star_icon1))
        starIcons.add(findViewById(R.id.star_icon2))
        starIcons.add(findViewById(R.id.star_icon3))
        starIcons.add(findViewById(R.id.star_icon4))
        starIcons.add(findViewById(R.id.star_icon5))

        val noteI = (movie?.vote_average!!.roundToInt().toDouble()/2).roundToInt()
        val noteF = movie.vote_average.roundToInt().toDouble()/2

        for((cpt, star) in starIcons.withIndex()) {
            if((cpt+1) <= noteI) {
                if(noteF-cpt == 0.5) star.setImageResource(R.drawable.baseline_star_half_24)
                else star.setImageResource(R.drawable.baseline_star_24)
            }
        }

        val starTextView = findViewById<TextView>(R.id.text_note_count)
        starTextView.text = " (" + movie.vote_count + " votes)"
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_details, menu)
        
        if(super.isConnected()) {
            this.isFav = isFavMovie()

            if (this.isFav) {
                menu?.getItem(0)?.icon = ContextCompat.getDrawable(this, R.drawable.baseline_favorite_24)
            }
        }
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_addToFav -> {
                if (super.isConnected()) {
                    if (!this.isFav) {
                        this.isFav = true;
                        item.icon = ContextCompat.getDrawable(this, R.drawable.baseline_favorite_24)
                        this.addFavMovieToJSON()
                    } else {
                        this.isFav = false;
                        item.icon =
                            ContextCompat.getDrawable(this, R.drawable.baseline_favorite_border_24)
                        removeFavMovieFromJSON()
                    }
                } else {
                    val intent = Intent(this, ConnectProfileActivity::class.java)
                    intent.putExtra("modeConnect", "log")
                    startActivity(intent)
                }
            }

            R.id.action_returnHome -> {
                val intent = Intent(this, HomeActivity::class.java)
                startActivity(intent)
            }
        }
        return super.onOptionsItemSelected(item)
    }


    fun isFavMovie(): Boolean {
        val global = applicationContext as Global
        val nbProfiles = global.getProfilesJSON().getString("nb_profile").toInt()

        for (i in 0 until nbProfiles) {
            val profile = JSONObject(global.getProfilesJSON().getString("profile$i"))

            if (profile.getString("profile_id") == global.getProfileId()) {
                val favMoviesString = profile.getString("movie")

                if (favMoviesString != "[]") {
                    val moviesIdStr =
                        favMoviesString.substring(1, favMoviesString.length - 1).split(",")
                            .map { it.trim() }
                    val moviesId = moviesIdStr.map { it.toInt() }.toMutableList()

                    for (movieInt in moviesId) {
                        if (movieInt == movieID) return true
                    }
                }
            }
        }
        return false;
    }

    private fun removeFavMovieFromJSON() {

        val global = applicationContext as Global
        val profileObject = JSONObject()
        val nbProfiles = global.getProfilesJSON().getString("nb_profile").toInt()

        val rootObject = JSONObject();
        rootObject.put("nb_profile", nbProfiles.toString());

        for (i in 0 until nbProfiles) {
            val profile = JSONObject(global.getProfilesJSON().getString("profile$i"))

            if (profile.getString("profile_id") == global.getProfileId()) {
                profileObject.put("profile_id", profile.getString("profile_id"));
                profileObject.put("profile_passwd", profile.getString("profile_passwd"));

                val favMoviesString = profile.getString("movie")

                val moviesIdStr =
                    favMoviesString.substring(1, favMoviesString.length - 1).split(",")
                        .map { it.trim() }
                val moviesId = moviesIdStr.map { it.toInt() }.toMutableList()

                val newMovies = ArrayList<String>()
                for (movieInt in moviesId) {
                    if (movieInt != movieID) newMovies.add(movieInt.toString())
                }
                profileObject.put("movie", newMovies)
                rootObject.put("profile$i", profileObject)
            } else {
                rootObject.put("profile$i", profile)
            }
        }
        global.setProfile(profileObject)
        global.setProfilesJSON(rootObject)
    }

    private fun addFavMovieToJSON() {

        val global = applicationContext as Global
        val profileObject = JSONObject()
        val nbProfiles = global.getProfilesJSON().getString("nb_profile").toInt()

        val rootObject = JSONObject();
        rootObject.put("nb_profile", nbProfiles.toString());

        for (i in 0 until nbProfiles) {
            val profile = JSONObject(global.getProfilesJSON().getString("profile$i"))

            if (profile.getString("profile_id") == global.getProfileId()) {
                profileObject.put("profile_id", profile.getString("profile_id"));
                profileObject.put("profile_passwd", profile.getString("profile_passwd"));

                val favMoviesString = profile.getString("movie")

                if (favMoviesString != "[]") {
                    val moviesIdStr =
                        favMoviesString.substring(1, favMoviesString.length - 1).split(",")
                            .map { it.trim() }
                    val moviesId = moviesIdStr.toMutableList()

                    moviesId.add(movieID.toString())
                    profileObject.put("movie", moviesId)
                } else {
                    val arrayMovie = ArrayList<Int>()
                    arrayMovie.add(movieID)
                    profileObject.put("movie", arrayMovie)
                }
                rootObject.put("profile$i", profileObject)
            } else {
                rootObject.put("profile$i", profile)
            }
        }

        global.setProfile(profileObject)
        global.setProfilesJSON(rootObject)
    }

}