package com.example.projectmm

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
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
        recyclerView.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        val URL_image = "bOGkgRGdhrBYJSLpXaxhXVstddV.jpg"
        val moviesAPI = RetrofitHelper.getInstance("https://api.themoviedb.org/3/")
            .create(TheMovieDatabaseService::class.java)
        val imageAPI = RetrofitHelper.getInstance("https://image.tmdb.org/t/p/original/")
            .create(TheMovieDatabaseService::class.java)


//        val imageView = activity?.findViewById<ImageView>(R.id.movie_list_item)
//
//        // Declaring executor to parse the URL
//        val executor = Executors.newSingleThreadExecutor()
//
//        // Once the executor parses the URL
//        // and receives the image, handler will load it
//        // in the ImageView
//        val handler = Handler(Looper.getMainLooper())
//
//        // Initializing the image
//        var image: Bitmap? = null
//
//        // Only for Background process (can take time depending on the Internet speed)
//        executor.execute {
//
//            // Image URL
//            val imageURL = "https://media.geeksforgeeks.org/wp-content/cdn-uploads/gfg_200x200-min.png"
//
//            // Tries to get the image and post it in the ImageView
//            // with the help of Handler
//            try {
//                val `in` = java.net.URL(imageURL).openStream()
//                image = BitmapFactory.decodeStream(`in`)
//
//                // Only for making changes in UI
//                handler.post {
//                    imageView?.setImageBitmap(image)
//                }
//            }
//
//            // If the URL doesnot point to
//            // image or any other kind of failure
//            catch (e: Exception) {
//                e.printStackTrace()
//            }
//        }


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