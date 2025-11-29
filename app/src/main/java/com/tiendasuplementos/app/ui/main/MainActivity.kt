package com.tiendasuplementos.app.ui.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.tiendasuplementos.app.R
import com.tiendasuplementos.app.ui.main.cart.CartFragment
import com.tiendasuplementos.app.ui.main.order.OrderListFragment
import com.tiendasuplementos.app.ui.main.product.ProductListFragment
import com.tiendasuplementos.app.ui.main.profile.ProfileFragment
import com.tiendasuplementos.app.ui.main.user.UserListFragment
import com.tiendasuplementos.app.util.SessionManager

class MainActivity : AppCompatActivity() {

    private lateinit var bottomNavigation: BottomNavigationView
    private lateinit var sessionManager: SessionManager
    private var userRole: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        sessionManager = SessionManager(this)
        userRole = sessionManager.fetchUserRole()
        
        bottomNavigation = findViewById(R.id.bottom_navigation)

        setupBottomNavigation()

        if (savedInstanceState == null) {
            // Carga el fragmento inicial independientemente del rol
            loadFragment(ProductListFragment())
        }
    }

    private fun setupBottomNavigation() {
        bottomNavigation.menu.clear()
        
        if (userRole == "admin") {
            bottomNavigation.inflateMenu(R.menu.admin_bottom_nav_menu)
            bottomNavigation.setOnItemSelectedListener(adminNavigationListener)
        } else {
            bottomNavigation.inflateMenu(R.menu.client_bottom_nav_menu)
            bottomNavigation.setOnItemSelectedListener(clientNavigationListener)
        }
    }

    private val adminNavigationListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.nav_admin_products -> {
                loadFragment(ProductListFragment())
                return@OnNavigationItemSelectedListener true
            }
            R.id.nav_admin_users -> {
                loadFragment(UserListFragment())
                return@OnNavigationItemSelectedListener true
            }
            R.id.nav_admin_orders -> {
                loadFragment(OrderListFragment()) // Carga el nuevo fragmento
                return@OnNavigationItemSelectedListener true
            }
            R.id.nav_admin_profile -> {
                loadFragment(ProfileFragment())
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    private val clientNavigationListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.action_products -> {
                loadFragment(ProductListFragment())
                return@OnNavigationItemSelectedListener true
            }
            R.id.action_cart -> {
                loadFragment(CartFragment())
                return@OnNavigationItemSelectedListener true
            }
            R.id.action_user_info -> {
                loadFragment(ProfileFragment())
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    private fun loadFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .commit()
    }
}