package com.example.projectmm

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.fragment.app.Fragment
import com.example.projectmm.fragments.*
import com.google.android.material.bottomnavigation.BottomNavigationView

class HomeActivity : AppCompatActivity() {

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
                    loadFragment(ListMoviesFragment())
                    true
                }
                R.id.action_searchMovies -> {
                    loadFragment(SearchMoviesFragment())
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

}