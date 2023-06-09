package com.example.projectmm

import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.widget.EditText
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.runBlocking

class SearchMovieActivity : HomeActivity() {

    lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_movie)
        setTitle("Search")

        super.setBottomBarListener()
        val bottomNav = findViewById<BottomNavigationView>(R.id.bottomNav)
        bottomNav.selectedItemId = R.id.action_searchMovies

        this.recyclerView = findViewById<RecyclerView>(R.id.movie_list_item)
        recyclerView.layoutManager = GridLayoutManager(this, 1)


        val searchEdittext = findViewById<EditText>(R.id.search_movie_edittext)
        val moviesAPI = RetrofitHelper.getInstance("https://api.themoviedb.org/3/")
            .create(TheMovieDatabaseService::class.java)

        searchEdittext.setOnKeyListener { v, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_DOWN) {
                runBlocking {
                    val searchResult = moviesAPI.getSearchMovies(searchEdittext.text.toString())
                    recyclerView.adapter = MovieAdapter(searchResult.results.take(20), this@SearchMovieActivity, R.layout.movie_list_item)  // Limit to 10 movies
                }
                return@setOnKeyListener true
            }
            else {
                // Trying to see which one is the enter ON PHONE KEYBOARD
                Log.d("keyCode", keyCode.toString())
                false
            }
        }

    }
}