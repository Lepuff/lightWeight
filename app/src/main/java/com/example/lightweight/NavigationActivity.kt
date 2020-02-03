package com.example.lightweight

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class NavigationActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_navigation)
        val bottomNav = findViewById<BottomNavigationView>(R.id.bottom_navigation)

        loadFragment(FeedFragment())
        bottomNav.setOnNavigationItemSelectedListener {
            when (it.itemId){
                R.id.nav_analytics ->{
                loadFragment(AnalyticsFragment())
                    return@setOnNavigationItemSelectedListener true

                }
                R.id.nav_feed -> {
                    loadFragment(FeedFragment())
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.nav_social -> {
                    loadFragment(SocialFragment())
                    return@setOnNavigationItemSelectedListener true
                }

            }
            false

        }
    }
    private fun loadFragment(fragment: Fragment) {
        // load fragment
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragment_container, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }

}
