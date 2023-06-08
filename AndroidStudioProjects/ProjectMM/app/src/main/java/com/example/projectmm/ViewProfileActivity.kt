package com.example.projectmm

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import com.google.android.material.bottomnavigation.BottomNavigationView

class ViewProfileActivity : HomeActivity() {
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_profile)

        setTitle("Your profile")

        super.setBottomBarListener()
        val bottomNav = findViewById<BottomNavigationView>(R.id.bottomNav)
        bottomNav.selectedItemId = R.id.action_profile

        val global = applicationContext as Global

        val textWelcome = findViewById<TextView>(R.id.profile_welcome)
        textWelcome.text = "Bienvenue " + global.getProfileId()


        val buttonDisconnect = findViewById<Button>(R.id.button_disconnect)


        buttonDisconnect.setOnClickListener {
            global.setProfileId("")
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
        }
    }
}