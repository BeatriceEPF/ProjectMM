package com.example.projectmm

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.projectmm.fragments.*
import com.google.android.material.bottomnavigation.BottomNavigationView

@Suppress("DEPRECATED_IDENTITY_EQUALS")
open class HomeActivity : AppCompatActivity() {

    private val ZXING_CAMERA_PERMISSION = 1
    private var mClss: Class<*>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        setBottomBarListener()
    }

    fun setGlobalVarTests() {
        // TESTS ON GLOBAL VAR
        val global = applicationContext as Global
        val profileId = global.getProfileId();
        Log.d("GLOBAL_VAR_HOME_TEST", profileId.toString())
        global.setProfileId(true);
    }

    fun setBottomBarListener() {
        //loadFragment(HomeFragment())
        val bottomNav = findViewById<BottomNavigationView>(R.id.bottomNav);

        bottomNav.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.action_listMovies -> {
                    if(this.localClassName != "ListMoviesActivity") {
                        val intent = Intent(this, ListMoviesActivity::class.java)
                        startActivity(intent)
                    }
                    true
                }
                R.id.action_searchMovies -> {
                    //loadFragment(SearchMoviesFragment())
                    if(this.localClassName != "SearchMovieActivity") {
                        val intent = Intent(this, SearchMovieActivity::class.java)
                        startActivity(intent)
                    }
                    true
                }
                R.id.action_favFilms -> {
                    //loadFragment(ViewFavMoviesFragment())
                    if(this.localClassName != "ViewFavMoviesActivity") {
                        val intent = Intent(this, ViewFavMoviesActivity::class.java)
                        startActivity(intent)
                    }
                    true
                }
                R.id.action_profile -> {
                    //loadFragment(ViewProfileFragment())
                    if(this.localClassName != "ViewProfileActivity") {
                        val intent = Intent(this, ViewProfileActivity::class.java)
                        startActivity(intent)
                    }
                    true
                }
                R.id.action_scanQRCode -> {
                    launchActivity(ScanQRActivity::class.java)
                    true
                }
                else -> {
                    true
                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_home,menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.action_returnHome -> {
                if(this.localClassName != "HomeActivity") {
                    val intent = Intent(this, HomeActivity::class.java)
                    startActivity(intent)
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private  fun loadFragment(fragment: Fragment){
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.homeContainer,fragment)
        transaction.commit()
    }


    private fun launchActivity(clss: Class<*>) {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
            !== PackageManager.PERMISSION_GRANTED
        ) {
            mClss = clss
            ActivityCompat.requestPermissions(this,
                arrayOf(Manifest.permission.CAMERA), ZXING_CAMERA_PERMISSION)
        }
        else {
            val intent = Intent(this, clss)
            startActivity(intent)
        }
    }
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            ZXING_CAMERA_PERMISSION -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (mClss != null) {
                        val intent = Intent(this, mClss)
                        startActivity(intent)
                    }
                } else {
                    Toast.makeText(
                        this,
                        "Please grant camera permission to use the QR Scanner",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                return
            }
        }
    }

}