package com.tiendasuplementos.app

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.navOptions
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.onNavDestinationSelected
import androidx.navigation.ui.setupActionBarWithNavController
import com.tiendasuplementos.app.databinding.ActivityMainBinding
import com.tiendasuplementos.app.util.SessionManager

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    private lateinit var sessionManager: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sessionManager = SessionManager(this)

        // Configurar la Toolbar
        setSupportActionBar(binding.toolbar)

        // Configurar NavController
        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController

        // Configurar la ActionBar para que funcione con NavController
        val appBarConfiguration = AppBarConfiguration(navController.graph)
        setupActionBarWithNavController(navController, appBarConfiguration)

        // Listener para mostrar u ocultar la Toolbar y el menú según el rol
        navController.addOnDestinationChangedListener { _, destination, _ ->
            val isLoginScreen = destination.id == R.id.loginFragment
            binding.toolbar.isVisible = !isLoginScreen
            invalidateOptionsMenu() // Vuelve a dibujar el menú
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onPrepareOptionsMenu(menu: Menu): Boolean {
        // Ocultar/mostrar ítems del menú según el rol y la pantalla
        val isClient = sessionManager.fetchUserRole() != "admin"
        menu.findItem(R.id.cartFragment)?.isVisible = isClient
        menu.findItem(R.id.profileFragment)?.isVisible = isClient

        // Ocultar el menú de logout en la pantalla de login
        val isLoginScreen = navController.currentDestination?.id == R.id.loginFragment
        if (isLoginScreen) {
            menu.findItem(R.id.action_logout)?.isVisible = false
            menu.findItem(R.id.cartFragment)?.isVisible = false
            menu.findItem(R.id.profileFragment)?.isVisible = false
        }

        return super.onPrepareOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Manejar clics en los ítems de la action bar
        if (item.itemId == R.id.action_logout) {
            sessionManager.clearAuthToken()
            // Navegar al login y limpiar todo el historial de navegación
            val navOptions = navOptions {
                popUpTo(R.id.nav_graph) { inclusive = true }
            }
            navController.navigate(R.id.loginFragment, null, navOptions)
            return true
        }
        // Dejar que el NavigationUI maneje el resto de clics (carrito, perfil)
        return item.onNavDestinationSelected(navController) || super.onOptionsItemSelected(item)
    }

    override fun onSupportNavigateUp(): Boolean {
        // Manejar el botón de "atrás" en la toolbar
        return navController.navigateUp() || super.onSupportNavigateUp()
    }
}