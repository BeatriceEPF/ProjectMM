package com.example.projectmm.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Movie(
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
) : Parcelable {

}

/**
 *
 * Doc: https://developer.android.com/kotlin/parcelize?hl=fr
 *
 */

