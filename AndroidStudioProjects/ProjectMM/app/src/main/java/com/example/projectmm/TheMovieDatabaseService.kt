package com.example.projectmm

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET;
import retrofit2.http.Path

interface TheMovieDatabaseService {

    @GET("trending/all/week?api_key=4c5fc9d212f1199cb82213673620b351")
    suspend fun getMovies() : GetMoviesResult

    @GET("movie/{movie_id}?api_key=4c5fc9d212f1199cb82213673620b351")
    suspend fun getMovieById(@Path("movie_id") id : Int): MoviesAPI
}

data class GetMoviesResult(val results: List<MoviesAPI>)
data class MoviesAPI(
    val adult: Boolean,
    val backdrop_path: String,
    val budget: Int,
    val id: Int,
    val original_language: String,
    val original_title: String,
    val overview: String,
    val popularity: Double,
    val poster_path: String,
    val release_date: String,
    val revenue: Int,
    val runtime: Int,
    val title: String,
    val video: Boolean,
    val vote_average: Float,
    val vote_count: Int
){

    fun filter() {
        adult ?: false
        backdrop_path ?: "No backdrop path"
        budget ?: 0
        id ?: -99
        original_language ?: "No original language"
        original_title ?: "No original title"
        overview ?: "No overview"
        popularity ?: 0.0
        poster_path ?: "No poster path"
        release_date ?: " No release date"
        revenue ?: 0
        runtime ?: 0
        title ?: "No title"
        video ?: false
        vote_average ?: 0
        vote_count ?: 0
    }
    override fun toString(): String {
        return "MoviesAPI(adult=$adult, backdrop_path='$backdrop_path', budget=$budget, id=$id, original_language='$original_language', original_title='$original_title', overview='$overview', popularity=$popularity, poster_path='$poster_path', release_date='$release_date', revenue=$revenue, runtime=$runtime, title='$title', video=$video, vote_average=$vote_average, vote_count=$vote_count) \n"
    }
}

object RetrofitHelper {

    val baseUrl = "https://api.themoviedb.org/3/"

    fun getInstance(): Retrofit {
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

