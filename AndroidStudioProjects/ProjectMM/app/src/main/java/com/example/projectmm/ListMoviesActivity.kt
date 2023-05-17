package com.example.projectmm

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.runBlocking
import com.example.projectmm.DownloadImageFromInternet

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
        recyclerView.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        val URL_image = "bOGkgRGdhrBYJSLpXaxhXVstddV.jpg"
        val moviesAPI = RetrofitHelper.getInstance("https://api.themoviedb.org/3/")
            .create(TheMovieDatabaseService::class.java)
//        val imageAPI = RetrofitHelper.getInstance("https://image.tmdb.org/t/p/original/")
//            .create(TheMovieDatabaseService::class.java)





        runBlocking {
//            val test_image = imageAPI.getImageMovie(URL_image)
            val test = moviesAPI.getMovies()
            val test_id = moviesAPI.getMovieById(507250)
            Log.d("Test API", test.toString())
            Log.d("Test API", test_id.toString())
            recyclerView.adapter = MovieAdapter(test.results, this@ListMoviesActivity)
        }

    }
}