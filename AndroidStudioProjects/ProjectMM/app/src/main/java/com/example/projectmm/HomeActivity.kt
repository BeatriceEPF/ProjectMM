package com.example.projectmm

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
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
class HomeActivity : AppCompatActivity() {

    private val ZXING_CAMERA_PERMISSION = 1
    private var mClss: Class<*>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)


        loadFragment(HomeFragment())
        val bottomNav = findViewById<BottomNavigationView>(R.id.bottomNav);

        /*
        bottomNav.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.action_scanQRCode -> {
                    val intent = Intent(this, ScanQRCodeActivity::class.java)
                    startActivity(intent)
                    true
                }
                R.id.action_listFilms -> {
                    val intent = Intent(this, ListMoviesActivity::class.java)
                    startActivity(intent)
                    true
                }
                R.id.action_searchMovies -> {
                    val intent = Intent(this, SearchMoviesActivity::class.java)
                    startActivity(intent)
                    true
                }
                R.id.action_favFilms -> {
                    val intent = Intent(this, ViewFavMoviesActivity::class.java)
                    startActivity(intent)
                    true
                }
                R.id.action_profile -> {
                    val intent = Intent(this, ViewProfileActivity::class.java)
                    startActivity(intent)
                    true
                }
                else -> {
                    true
                }
            }
        }

         */

        bottomNav.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.action_listMovies -> {
                    val intent = Intent(this, ListMoviesActivity::class.java)
                    startActivity(intent)
                    true
                }
                R.id.action_searchMovies -> {
                    //loadFragment(SearchMoviesFragment())
                    val intent = Intent(this, SearchMovieActivity::class.java)
                    startActivity(intent)
                    true
                }
                R.id.action_favFilms -> {
                    loadFragment(ViewFavMoviesFragment())
                    true
                }
                R.id.action_profile -> {
                    loadFragment(ViewProfileFragment())
                    true
                }
                R.id.action_scanQRCode -> {
                    //val intent = Intent(this, SimpleScannerActivity::class.java)
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
        menuInflater.inflate(R.menu.home_menu,menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.action_returnHome -> {
                loadFragment(HomeFragment())
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
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray)
    {
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