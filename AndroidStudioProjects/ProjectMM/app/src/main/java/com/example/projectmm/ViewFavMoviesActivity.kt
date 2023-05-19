package com.example.projectmm

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.projectmm.model.Movie
import com.example.projectmm.model.MovieDetail
import kotlinx.coroutines.runBlocking
import org.json.JSONObject

class ViewFavMoviesActivity : HomeActivity() {

    lateinit var recyclerView: RecyclerView
    lateinit var swipeRefreshLayout: SwipeRefreshLayout
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_fav_movies)

        super.setBottomBarListener()

        this.recyclerView = findViewById<RecyclerView>(R.id.movie_list_item)
        recyclerView.layoutManager = GridLayoutManager(this, 1)

        this.recyclerView = findViewById<RecyclerView>(R.id.movie_list_item)
        recyclerView.layoutManager = GridLayoutManager(this, 1)

        val moviesAPI = RetrofitHelper.getInstance("https://api.themoviedb.org/3/")
            .create(TheMovieDatabaseService::class.java)

        runBlocking {
            val moviesId = getFavMovies()

            if(moviesId.isNotEmpty()) {
                val movies = moviesId.map { moviesAPI.getMoviesById(it)}
                recyclerView.adapter = MovieAdapter(movies, this@ViewFavMoviesActivity)
            }
        }
    }

    private fun getFavMovies(): List<Int> {

        val global = applicationContext as Global
        val currentProfile = global.getProfile()
        val favMoviesString = currentProfile.getString("movie")
        var moviesId : List<Int>

        if(favMoviesString != "[]") {
            val moviesIdStr= favMoviesString.substring(1, favMoviesString.length-1).split(",").map { it.trim() }
            moviesId = moviesIdStr.map { it.toInt() }
        }
        else {
            moviesId = emptyList()
        }

        return moviesId
    }
}