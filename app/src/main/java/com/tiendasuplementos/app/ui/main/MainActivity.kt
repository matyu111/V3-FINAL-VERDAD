package com.tiendasuplementos.app.ui.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.tiendasuplementos.app.R
import com.tiendasuplementos.app.ui.main.product.AddProductFragment
import com.tiendasuplementos.app.ui.main.product.ProductListFragment
import com.tiendasuplementos.app.ui.main.profile.ProfileFragment

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val bottomNavigation: BottomNavigationView = findViewById(R.id.bottom_navigation)
        bottomNavigation.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener)

        // Cargar el fragment inicial
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, ProductListFragment())
                .commit()
        }
    }

    private val onNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        var selectedFragment: Fragment? = null
        when (item.itemId) {
            R.id.action_products -> {
                selectedFragment = ProductListFragment()
            }
            R.id.action_add_product -> {
                selectedFragment = AddProductFragment()
            }
            R.id.action_user_info -> {
                selectedFragment = ProfileFragment()
            }
        }

        if (selectedFragment != null) {
            supportFragmentManager.beginTransaction() // Corregido aquí
                .replace(R.id.fragment_container, selectedFragment)
                .commit()
        }

        true
    }
}