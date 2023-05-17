package com.example.projectmm

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class ViewProfileActivity : HomeActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_profile)

        super.setBottomBarListener()
    }
}