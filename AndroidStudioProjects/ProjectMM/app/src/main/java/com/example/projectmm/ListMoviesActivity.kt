package com.example.projectmm

import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.runBlocking


/**
 * A simple [Fragment] subclass.
 * Use the [ListMoviesFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ListMoviesActivity : HomeActivity() {
    // TODO: Rename and change types of parameters
    lateinit var recyclerView: RecyclerView
    lateinit var swipeRefreshLayout: SwipeRefreshLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_movies)

        super.setBottomBarListener()
        val bottomNav = findViewById<BottomNavigationView>(R.id.bottomNav)
        bottomNav.selectedItemId = R.id.action_listMovies

        this.recyclerView = findViewById<RecyclerView>(R.id.movie_list_item)
        recyclerView.layoutManager = GridLayoutManager(this, 1)

        val moviesAPI = RetrofitHelper.getInstance("https://api.themoviedb.org/3/")
            .create(TheMovieDatabaseService::class.java)
        runBlocking {
            Toast.makeText(applicationContext, "Scroll up to get more !", Toast.LENGTH_SHORT).show()
            val test = moviesAPI.getMovies()
            recyclerView.adapter = MovieAdapter(test.results.take(5), this@ListMoviesActivity, R.layout.movie_list_item)
        }
    }

    override fun onStart() {
        super.onStart()
        val moviesAPI = RetrofitHelper.getInstance("https://api.themoviedb.org/3/")
            .create(TheMovieDatabaseService::class.java)

        swipeRefreshLayout = findViewById(R.id.pullToRefresh)

        swipeRefreshLayout.setOnRefreshListener {
            runBlocking {
                val test = moviesAPI.getMovies()
                recyclerView.adapter = MovieAdapter(test.results.take(20), this@ListMoviesActivity, R.layout.movie_list_item)
            }
            swipeRefreshLayout.isRefreshing = false
        }
    }
}