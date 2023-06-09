package com.example.projectmm

//import coil.ImageLoader

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.AsyncTask
import android.util.Log
import android.widget.ImageView
import android.widget.Toast
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import com.example.projectmm.model.Movie
import com.example.projectmm.model.MovieDetail
import retrofit2.http.Query


interface TheMovieDatabaseService {

    @GET("trending/movie/day?api_key=4c5fc9d212f1199cb82213673620b351")
    suspend fun getMovies(): GetMoviesResult

    @GET("movie/{movie_id}?api_key=4c5fc9d212f1199cb82213673620b351")
    suspend fun getMovieDetailsById(@Path("movie_id") id: Int): MovieDetail

    @GET("movie/{movie_id}?api_key=4c5fc9d212f1199cb82213673620b351")
    suspend fun getMoviesById(@Path("movie_id") id: Int): Movie

    @GET("movie/{movie_id}/similar?api_key=4c5fc9d212f1199cb82213673620b351")
    suspend fun getSimilarMovies(@Path("movie_id") id: Int): GetMoviesResult

    @GET("movie/{movie_id}/recommendations?api_key=4c5fc9d212f1199cb82213673620b351")
    suspend fun getRecommendedMovies(@Path("movie_id") id: Int): GetMoviesResult

    @GET("search/movie?api_key=4c5fc9d212f1199cb82213673620b351")
    suspend fun getSearchMovies(@Query("query") search: String): GetMoviesResult
}
data class GetMoviesResult(val results: List<Movie>)


object RetrofitHelper {
    //https://image.tmdb.org/t/p/original/ + URLimage

    //val baseUrl = "https://api.themoviedb.org/3/"

    fun getInstance(baseUrl: String): Retrofit {
        val httpLoggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
        val client = OkHttpClient.Builder()
            .addInterceptor(httpLoggingInterceptor)
            .build()

        return Retrofit.Builder().baseUrl(baseUrl)
            .addConverterFactory(MoshiConverterFactory.create())
            .client(client)
            .build()
    }
}
@SuppressLint("StaticFieldLeak")
@Suppress("DEPRECATION")
class DownloadImageFromInternet(var imageView: ImageView, applicationContext: Context) : AsyncTask<String, Void, Bitmap?>() {
    init {
        Toast.makeText(applicationContext, "Please wait, it may take a few seconds...",     Toast.LENGTH_SHORT).show()
    }
    override fun doInBackground(vararg urls: String): Bitmap? {
        val imageURL = urls[0]
        var image: Bitmap? = null
        try {
            val `in` = java.net.URL(imageURL).openStream()
            image = BitmapFactory.decodeStream(`in`)
        }
        catch (e: Exception) {
            Log.e("Error Message", e.message.toString())
            e.printStackTrace()
        }
        return image
    }
    override fun onPostExecute(result: Bitmap?) {
        imageView.setImageBitmap(result)
    }
}



