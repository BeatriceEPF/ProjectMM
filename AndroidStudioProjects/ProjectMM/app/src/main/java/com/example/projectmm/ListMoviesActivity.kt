package com.example.projectmm

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.runBlocking


/**
 * A simple [Fragment] subclass.
 * Use the [ListMoviesFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ListMoviesActivity : AppCompatActivity() {
    // TODO: Rename and change types of parameters
    lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_movies)
        this.recyclerView = findViewById<RecyclerView>(R.id.movie_list_item)
        recyclerView.layoutManager = GridLayoutManager(this, 1)

        val moviesAPI = RetrofitHelper.getInstance("https://api.themoviedb.org/3/")
            .create(TheMovieDatabaseService::class.java)

        //val pullToRefresh = findViewById<>(R.id.pullToRefresh);


        runBlocking {
            val test = moviesAPI.getMovies()
            Log.d("API requests", test.results.take(10).toString())
            recyclerView.adapter = MovieAdapter(test.results.take(10), this@ListMoviesActivity)
        }

    }
}