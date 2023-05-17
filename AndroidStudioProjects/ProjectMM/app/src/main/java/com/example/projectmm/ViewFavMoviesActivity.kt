package com.example.projectmm

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class ViewFavMoviesActivity : HomeActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_fav_movies)

        super.setBottomBarListener()
    }
}